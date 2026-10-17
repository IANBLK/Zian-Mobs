"""Verify original Thanos geometry, UVs, PNGs and selective gem lighting survive import."""
import json,hashlib
from pathlib import Path
root=Path(__file__).resolve().parents[1]
source=root.parent/'pets-trial-review/thanos/files'
assets=root/'src/main/resources/assets/zianmobs'
models=[]
for original,target in [('doubleedgesword','titan_double_sword'),('gauntlet','titan_gauntlet'),('gauntlet_blocking','titan_gauntlet_blocking')]:
 before=json.loads((source/(original+'.json')).read_text())
 after=json.loads((assets/'models/item'/(target+'.json')).read_text(encoding='utf-8'))
 assert len(before['elements'])==len(after['elements'])
 count=0
 for a,b in zip(before['elements'],after['elements']):
  for key in ['from','to','rotation','faces']:assert a.get(key)==b.get(key),(target,key)
  if a.get('light_emission',0):
   assert all(f['texture']=='#3' for f in a['faces'].values())
   assert 'neoforge_data' not in b
   lit=json.loads((assets/'models/item'/(target+'_active.json')).read_text(encoding='utf-8'))['elements'][before['elements'].index(a)]
   assert lit['neoforge_data']==dict(block_light=15,sky_light=15,ambient_occlusion=False) and lit['shade']==False
   count+=1
  else:assert 'neoforge_data' not in b
 assert count==(0 if original=='doubleedgesword' else 6)
 models.append(dict(id=target,elements=len(after['elements']),emissiveGemElements=count))
images=[]
for name in ['thanossword','gauntlet','gems']:
 raw=(assets/'textures/item'/('titan_'+name+'.png')).read_bytes()
 assert raw==(source/'textures'/(name+'.png')).read_bytes()
 images.append(dict(id=name,sha256=hashlib.sha256(raw).hexdigest()))
report=dict(result='PASS',models=models,originalPNGs=images)
(root/'build/titan-assets-report.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8')
print(json.dumps(report))
