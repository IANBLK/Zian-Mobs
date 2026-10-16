"""Strict resource graph validation; native client smoke additionally checks the baked atlas."""
import json,struct,hashlib
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1];RES=ROOT/'src/main/resources';issues=[];models={};sprites=set()
for p in RES.glob('assets/*/models/**/*.json'):
 key=p.relative_to(RES).parts[1]+':'+p.as_posix().split('/models/')[1][:-5];models[key]=json.loads(p.read_text(encoding='utf-8'))
for p in RES.glob('assets/minecraft/atlases/*.json'):
 for s in json.loads(p.read_text(encoding='utf-8')).get('sources',[]):
  if s['type']=='minecraft:single':sprites.add(s['resource'])
def textures(key,seen=None):
 seen=set() if seen is None else seen
 if key.startswith(('minecraft:','builtin/')):return {}
 if key not in models:issues.append([key,'missing model']);return {}
 if key in seen:issues.append([key,'parent cycle']);return {}
 seen.add(key);d=models[key];result=textures(d.get('parent','minecraft:item/generated'),seen);result.update(d.get('textures',{}));return result
def texture(ref,table,seen=None):
 seen=set() if seen is None else seen
 if ref.startswith('#'):
  if ref in seen:raise ValueError('texture cycle '+ref)
  if ref[1:] not in table:raise ValueError('unresolved '+ref)
  seen.add(ref);return texture(table[ref[1:]],table,seen)
 if ':' not in ref:ref='minecraft:'+ref
 if ref.startswith('minecraft:'):return
 ns,path=ref.split(':',1)
 if not (RES/'assets'/ns/'textures'/(path+'.png')).is_file():raise ValueError('missing PNG '+ref)
 if not path.startswith(('item/','block/')) and ref not in sprites:raise ValueError('missing atlas sprite '+ref)
for key,d in models.items():
 table=textures(key)
 try:
  for ref in table.values():texture(ref,table)
  for override in d.get('overrides',[]):textures(override['model'])
  for e in d.get('elements',[]):
   if any(v< -16 or v>32 for k in ['from','to'] for v in e[k]):raise ValueError('cube outside native JSON limits')
   if 'rotation' in e and e['rotation']['angle'] not in (-45,-22.5,0,22.5,45):raise ValueError('non-native cube rotation')
   for face in e.get('faces',{}).values():texture(face['texture'],table)
 except ValueError as e:issues.append([key,str(e)])
catalog=json.loads((RES/'assets/zianmobs/gear-catalog.json').read_text(encoding='utf-8'));required=[r['id'] for r in catalog]+[id+'_spawn_egg' for id in ['cursed_one_forest','cursed_one_ice','cursed_one_crystal','oni_brute','combat_npc']]
for id in required:
 if 'zianmobs:item/'+id not in models:issues.append([id,'missing registered item model'])
animations=0
for p in RES.glob('assets/zianmobs/animations/*.json'):animations+=len(json.loads(p.read_text(encoding='utf-8'))['animations'])
for p in RES.glob('assets/*/textures/**/*.png.mcmeta'):
 png=p.with_suffix('');w,h=struct.unpack('>II',png.read_bytes()[16:24]);a=json.loads(p.read_text(encoding='utf-8'))['animation'];fw=a.get('width',min(w,h));fh=a.get('height',fw);count=(w//fw)*(h//fh)
 for frame in a.get('frames',[]):
  index=frame if isinstance(frame,int) else frame['index']
  if index<0 or index>=count:issues.append([p.name,'invalid animation frame'])
skins=json.loads((ROOT/'art/npc-skins.json').read_text(encoding='utf-8'))
for skin in skins:
 p=RES/'assets/zianmobs/textures/entity/npcs'/(skin['id']+'.png')
 if not p.is_file():issues.append([skin['id'],'missing NPC skin']);continue
 raw=p.read_bytes()
 if struct.unpack('>II',raw[16:24])!=(64,64):issues.append([skin['id'],'invalid skin dimensions'])
 if hashlib.sha256(raw).hexdigest()!=skin['sha256']:issues.append([skin['id'],'skin source hash differs'])
report={'itemModels':len(models),'registeredItems':len(required),'atlasSprites':len(sprites),'animationClips':animations,'npcSkins':len(skins),'issues':issues};(ROOT/'art/asset-validation.json').write_text(json.dumps(report,indent=2),encoding='utf-8');print(json.dumps(report));raise SystemExit(1 if issues else 0)
