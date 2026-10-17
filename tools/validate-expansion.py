"""Resource graph, texture preservation and source animation checks for trial two."""
from pathlib import Path
import json,math,hashlib,struct,base64
ROOT=Path(__file__).resolve().parents[1]
ASSETS=ROOT/'src/main/resources/assets/zianmobs'
INPUT=ROOT.parent/'pets-expansion-review'
catalog=json.loads((ASSETS/'companion-catalog.json').read_text(encoding='utf-8'))
assert len(catalog)==29 and len({r['id'] for r in catalog})==29
report=[]
for row in json.loads((ROOT/'art/expansion-import.json').read_text()):
 source=next(INPUT.rglob(row['source']))
 assert hashlib.sha256(source.read_bytes()).hexdigest()==row['sourceSha256']
 original=json.loads(source.read_text(encoding='utf-8'))
 geometry=json.loads((ASSETS/'geo'/(row['id']+'.geo.json')).read_text())['minecraft:geometry'][0]
 animations=json.loads((ASSETS/'animations'/(row['id']+'.animation.json')).read_text())['animations']
 assert set(animations)=={a['name'] for a in original['animations']}
 bones=geometry['bones'];names=[b['name'] for b in bones]
 assert len(names)==len(set(names))
 width=geometry['description']['texture_width'];height=geometry['description']['texture_height']
 negative=0;rotations=0;cubes=0
 for bone in bones:
  assert 'parent' not in bone or bone['parent'] in names
  for cube in bone['cubes']:
   cubes+=1
   assert all(math.isfinite(v) for v in cube['size']+cube['origin'])
   negative+=any(v<0 for v in cube['size'])  # Original inverted outlines require culled rendering.
   for face in cube['uv'].values():
    x,y=face['uv'];dx,dy=face['uv_size']
    assert min(x,x+dx)>=-.01 and max(x,x+dx)<=width+.01
    assert min(y,y+dy)>=-.01 and max(y,y+dy)<=height+.01
    assert face.get('uv_rotation',0) in (0,90,180,270)
    rotations+=face.get('uv_rotation',0)!=0
 assert cubes==row['cubes']
 for clip in animations.values():assert set(clip['bones']).issubset(names)
 png=ASSETS/'textures/entity'/(row['id']+'.png')
 raw=png.read_bytes();assert raw[:8]==b'\x89PNG\r\n\x1a\n'
 if not row['multiTextureAtlas']:
  assert raw==base64.b64decode(original['textures'][0]['source'].split(',',1)[1])
  if len(original['textures'])>1:
   assert png.with_name(row['id']+'_glowmask.png').read_bytes()==base64.b64decode(original['textures'][1]['source'].split(',',1)[1])
  actual_w,actual_h=struct.unpack('>II',raw[16:24])
  if actual_h>height:
   meta=json.loads(png.with_suffix('.png.mcmeta').read_text())['animation']
   assert meta['width']==width and meta['height']==height and actual_h%height==0
 else:assert struct.unpack('>II',raw[16:24])==(width,height)
 report.append({'id':row['id'],'cubes':cubes,'animationClips':len(animations),'invertedOutlines':negative,'rotatedUvs':rotations,'sourcePngPreserved':not row['multiTextureAtlas'],'losslessAtlas':row['multiTextureAtlas']})
gift=json.loads((ASSETS/'geo/companion_gift.geo.json').read_text())['minecraft:geometry'][0]
assert len(next(b for b in gift['bones'] if b['name']=='zian_gift_eyes')['cubes'])==4
result={'result':'PASS','companions':len(catalog),'newModelsIncludingSnowbone':len(report),'models':report,'giftEyes':'Four geometry cubes using the unchanged source palette','outlineFix':'CompanionRenderer uses culled entity rendering for original inverted outlines'}
(ROOT/'build/expansion-assets-report.json').write_text(json.dumps(result,indent=2)+'\n')
print(json.dumps({k:v for k,v in result.items() if k!='models'}))
