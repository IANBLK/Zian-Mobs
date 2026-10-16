"""Native item states, vanilla tags, and deterministic repairs to supplied model references."""
import json
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1];RES=ROOT/'src/main/resources';ASSETS=RES/'assets/zianmobs'
catalog_path=ASSETS/'gear-catalog.json';rows=json.loads(catalog_path.read_text(encoding='utf-8'))
tags={'HELMET':['head_armor'],'CHESTPLATE':['chest_armor'],'LEGGINGS':['leg_armor'],'BOOTS':['foot_armor'],'SWORD':['swords'],'AXE':['axes'],'PICKAXE':['pickaxes'],'SHOVEL':['shovels'],'HOE':['hoes'],'BOW':['enchantable/bow'],'GEOBOW':['enchantable/bow'],'CROSSBOW':['enchantable/crossbow'],'FISHING_ROD':['enchantable/fishing'],'SHIELD':['enchantable/durability']}
for row in rows:
 if row['pack'] not in ('Kraber','Toffy Zelda') and row['pack'].split()[0].lower() not in row['name'].lower():row['name']=row['pack']+' · '+row['name']
 base=row['model'];variants=[]
 if row['kind']=='BOW':variants=[({'pulling':1},base+'_0'),({'pulling':1,'pull':.65},base+'_1'),({'pulling':1,'pull':.9},base+'_2')]
 if row['kind']=='CROSSBOW':variants=[({'pulling':1},base+'_0'),({'pulling':1,'pull':.58},base+'_1'),({'pulling':1,'pull':1},base+'_2'),({'charged':1},base+'_charged'),({'charged':1,'firework':1},base+'_2_charged')]
 if row['kind']=='FISHING_ROD':variants=[({'cast':1},base+'_cast')]
 if row['kind']=='SHIELD':variants=[({'blocking':1},base+'_blocking')]
 if variants:
  p=ASSETS/'models/item'/f"{row['id']}.json";d=json.loads(p.read_text(encoding='utf-8'));d['overrides']=[{'predicate':predicate,'model':model} for predicate,model in variants];p.write_text(json.dumps(d,indent=2),encoding='utf-8')
 for tag in tags.get(row['kind'],[]):
  p=RES/'data/minecraft/tags/item'/f'{tag}.json';p.parent.mkdir(parents=True,exist_ok=True);d=json.loads(p.read_text(encoding='utf-8')) if p.exists() else {'replace':False,'values':[]};d['values']=sorted(set(d['values']+['zianmobs:'+row['id']]));p.write_text(json.dumps(d),encoding='utf-8')
# Original quiver contains six faces referencing a literal '#missing'. Reuse its original
# primary material rather than draw a replacement texture or leave the missing-texture checker.
for name in ('quiver','quiver_self'):
 p=RES/'assets/elitecreatures/models/valentines_animated_weapon_and_tool_set_v1'/f'{name}.json'
 if not p.exists():continue
 d=json.loads(p.read_text(encoding='utf-8'));d['textures']['missing']=d['textures']['0'];p.write_text(json.dumps(d,indent=2),encoding='utf-8')
catalog_path.write_text(json.dumps(rows,ensure_ascii=False,indent=2),encoding='utf-8')
for language in ['en_us','es_es']:
 p=ASSETS/'lang'/f'{language}.json';d=json.loads(p.read_text(encoding='utf-8'));d.update({'item.zianmobs.'+r['id']:r['name'] for r in rows});p.write_text(json.dumps(d,ensure_ascii=False,indent=2),encoding='utf-8')
print('Native item states, tags and model reference repairs applied.')
