"""Validate the private trial against its original models and byte-identical PNGs."""
from pathlib import Path
import base64
import hashlib
import json
import math
import struct

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / 'src/main/resources/assets/zianmobs'
SOURCE = ROOT.parent / 'pets-trial-review'

def digest(data):
    return hashlib.sha256(data).hexdigest()

def png(path, dimensions):
    raw = path.read_bytes()
    assert raw[:8] == b'\x89PNG\r\n\x1a\n', path
    assert struct.unpack('>II', raw[16:24]) == dimensions, path
    return raw

report = []
for entry in json.loads((ROOT / 'art/companions-import.json').read_text()):
    original = (SOURCE / entry['source']).read_bytes()
    assert digest(original) == entry['sourceSha256']
    source = json.loads(original)
    model = json.loads((ASSETS / 'geo' / (entry['id'] + '.geo.json')).read_text())
    geometry = model['minecraft:geometry'][0]
    bones = geometry['bones']
    names = [b['name'] for b in bones]
    assert len(names) == len(set(names)), 'Duplicate bones'
    cubes = 0
    for bone in bones:
        assert 'parent' not in bone or bone['parent'] in names
        for cube in bone['cubes']:
            cubes += 1
            assert all(math.isfinite(x) and x >= 0 for x in cube['size'])
            assert cube['uv'], 'Untextured cube'
            for face in cube['uv'].values():
                assert all(math.isfinite(x) for x in face['uv'] + face['uv_size'])
    assert cubes == entry['cubes']
    animation = json.loads((ASSETS / 'animations' / (entry['id'] + '.animation.json')).read_text())['animations']
    assert set(animation) == set(entry['animations'])
    for clip in animation.values():
        assert set(clip['bones']).issubset(names)
    for index, texture in enumerate(source['textures']):
        original_png = base64.b64decode(texture['source'].split(',', 1)[1])
        path = ASSETS / 'textures/entity' / (entry['id'] + ('_glowmask' if index else '') + '.png')
        raw = png(path, (source['resolution']['width'], source['resolution']['height']))
        assert raw == original_png, 'Source texture changed: ' + str(path)
    report.append({'id': entry['id'], 'cubes': cubes, 'animations': len(animation), 'texturesPreserved': len(source['textures'])})

armor = SOURCE / 'thanos/files/textures/armor'
for source, target in [('thanos_armor_layer_1.png', 'thanos_layer_1.png'), ('thanos_layer_2.png', 'thanos_layer_2.png')]:
    assert png(ASSETS / 'textures/models/armor' / target, (64, 32)) == (armor / source).read_bytes()
for part in ['helmet', 'chestplate', 'leggings', 'boots']:
    item = 'titan_' + part
    assert png(ASSETS / 'textures/item' / (item + '.png'), (16, 16)) == (armor / ('thanos_' + part + '.png')).read_bytes()
    model = json.loads((ASSETS / 'models/item' / (item + '.json')).read_text())
    assert model['textures']['layer0'] == 'zianmobs:item/' + item
result = {'result': 'PASS', 'companions': report, 'armor': {'pieces': 4, 'nativeLayers': 2, 'sourcePngsPreserved': True}}
output = ROOT / 'build/companions-assets-report.json'
output.parent.mkdir(exist_ok=True)
output.write_text(json.dumps(result, indent=2) + '\n')
print(json.dumps(result, indent=2))
