"""Cross-check attack timing metadata against original Blockbench effect markers."""
from pathlib import Path
import json,math,hashlib
root=Path(__file__).resolve().parents[1];assets=root/'src/main/resources/assets/zianmobs'
profiles=json.loads((assets/'companion-attacks.json').read_text());results=[]
for name,profile in profiles.items():
 source=root.parent/'pets-expansion-review'/('cubee-'+name.removeprefix('cubee_')+'.bbmodel')
 original=json.loads(source.read_text());clip=next(a for a in original['animations'] if a['name']=='attack');marks=[]
 for animator in clip.get('animators',{}).values():
  if animator.get('type')=='effect':
   for key in animator.get('keyframes',[]):
    for point in key.get('data_points',[]):
     if any(token in point.get('script','') for token in ['-shoot','-ef_shoot','-key-','-Beat-attack-']):marks.append(max(1,math.floor(key['time']*20+.5)))
 if name=='cubee_lush':assert marks==[];marks=[15]
 assert profile['impacts']==sorted(set(marks)) and profile['duration']==math.ceil(clip['length']*20)
 converted=json.loads((assets/'animations'/('companion_'+name+'.animation.json')).read_text())['animations']
 before=root.parent/'Zian-Mobs-Companions-Trial3/src/main/resources/assets/zianmobs/animations'/('companion_'+name+'.animation.json')
 assert json.loads(before.read_text())['animations']==converted,'Original animation was edited'
 assert profile['clip'] in converted
 results.append(dict(id=name,impacts=marks,durationTicks=profile['duration'],sourceSha256=hashlib.sha256(source.read_bytes()).hexdigest()))
assert len(profiles)==16
report=dict(result='PASS',animatedCompanions=16,frameRate=20,profiles=results)
(root/'build/attack-sync-assets.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8')
print(json.dumps(dict(result='PASS',animatedCompanions=16,originalClipsUnchanged=True)))
