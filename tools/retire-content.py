"""Remove exactly the retired content and its now-unreferenced assets, within this project only."""
from pathlib import Path
import json,re
ROOT=Path(__file__).resolve().parents[1];RES=ROOT/'src/main/resources';ASSETS=RES/'assets'
source=(ROOT/'src/main/java/com/ianblk/zianmobs/ContentRetirement.java').read_text(encoding='utf-8')
removed=set(re.findall(r'"([a-z0-9_]+)"',source.split('ITEMS=Set.of(')[1].split(');')[0]))
catalog=ASSETS/'zianmobs/gear-catalog.json';before=json.loads(catalog.read_text(encoding='utf-8'));rows=[r for r in before if r['id'] not in removed];catalog.write_text(json.dumps(rows,ensure_ascii=False,indent=2),encoding='utf-8')
deleted=[]
def discard(p):
 p=p.resolve();assert p.is_relative_to(RES.resolve()),p
 if p.is_file():p.unlink();deleted.append(p.relative_to(RES).as_posix())
for id in removed:discard(ASSETS/'zianmobs/models/item'/f'{id}.json')
for id in ['goofy_goblin','armored_grub','kraber_sniper','toffy_triforce']:
 for directory,suffix in [('geo','.geo.json'),('animations','.animation.json'),('textures/entity','.png')]:discard(ASSETS/'zianmobs'/directory/(id+suffix))
for p in RES.glob('data/*/tags/item/**/*.json'):
 data=json.loads(p.read_text(encoding='utf-8'));data['values']=[v for v in data['values'] if not isinstance(v,str) or v.removeprefix('zianmobs:') not in removed];p.write_text(json.dumps(data),encoding='utf-8')
for p in (ASSETS/'zianmobs/lang').glob('*.json'):
 data=json.loads(p.read_text(encoding='utf-8'));data={k:v for k,v in data.items() if k.removeprefix('item.zianmobs.') not in removed and k not in ['entity.zianmobs.goofy_goblin','entity.zianmobs.armored_grub']};p.write_text(json.dumps(data,ensure_ascii=False,indent=2),encoding='utf-8')
# Traverse model dependencies before pruning. Shared draw states, textures and armor skins remain.
keep_models=set();keep_textures=set()
def visit(id):
 if id.startswith(('minecraft:','builtin/')):return
 if id in keep_models:return
 keep_models.add(id);ns,path=id.split(':',1);p=ASSETS/ns/'models'/(path+'.json');data=json.loads(p.read_text(encoding='utf-8'))
 visit(data.get('parent','minecraft:item/generated'))
 for entry in data.get('overrides',[]):visit(entry['model'])
 for ref in data.get('textures',{}).values():
  if not ref.startswith('#') and not ref.startswith('minecraft:'):keep_textures.add(ref)
for row in rows:
 visit('zianmobs:item/'+row['id'])
 for key in ['armorTexture1','armorTexture2']:
  if row.get(key):keep_textures.add(row[key].replace(':textures/',':').removesuffix('.png'))
visit('zianmobs:item/combat_npc_spawn_egg')
for p in (ASSETS/'zianmobs/textures/entity/npcs').glob('*.png'):keep_textures.add('zianmobs:entity/npcs/'+p.stem)
for id in ['cursed_one_forest','cursed_one_ice','cursed_one_crystal','oni_brute']:
 visit('zianmobs:item/'+id+'_spawn_egg');keep_textures.add('zianmobs:entity/'+id)
for p in list(ASSETS.glob('*/models/**/*.json')):
 relative=p.relative_to(ASSETS);id=relative.parts[0]+':'+relative.as_posix().split('/models/')[1][:-5]
 if id not in keep_models:discard(p)
for p in list(ASSETS.glob('*/textures/**/*.png')):
 relative=p.relative_to(ASSETS);id=relative.parts[0]+':'+relative.as_posix().split('/textures/')[1][:-4]
 if id not in keep_textures:discard(p);discard(Path(str(p)+'.mcmeta'))
atlas=ASSETS/'minecraft/atlases/blocks.json';data=json.loads(atlas.read_text(encoding='utf-8'));data['sources']=[s for s in data['sources'] if s.get('resource') in keep_textures];atlas.write_text(json.dumps(data,indent=2),encoding='utf-8')
report={'removedItems':sorted(removed),'removedEntities':['zianmobs:goofy_goblin','zianmobs:armored_grub'],'remainingGear':len(rows),'remainingRegisteredItems':len(rows)+5,'deletedResourceFiles':deleted};(ROOT/'art/retired-content-beta4.json').write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8');print(json.dumps({'removedItems':len(removed),'remainingGear':len(rows),'deletedResources':len(deleted)}))
