"""Import model data and configuration only. No plugin scripts or vanilla overrides are executed/copied."""
from pathlib import Path,PurePosixPath
import sys,json,zipfile,re,subprocess,collections,struct
sys.path.insert(0,str(Path(__file__).parent/'python-libs'))
import yaml
ROOT=Path(__file__).resolve().parents[1];RES=ROOT/'src/main/resources';DOWNLOAD=(Path.home()/'Downloads')
catalog=[];sources=[];atlases=set(['zianmobs:barrel_knight/barrel','zianmobs:barrel_knight/wooden_bucket'])
def write(relative,data):
    path=RES/PurePosixPath(relative)
    assert '..' not in PurePosixPath(relative).parts and path.resolve().is_relative_to(RES.resolve())
    path.parent.mkdir(parents=True,exist_ok=True)
    path.write_bytes(data if isinstance(data,bytes) else json.dumps(data,ensure_ascii=False,indent=2).encode())
def kind(material,entry):
    material=material.upper()
    slot=entry.get('specific_properties',{}).get('armor',{}).get('slot','').upper()
    if slot:return {'HEAD':'HELMET','CHEST':'CHESTPLATE','LEGS':'LEGGINGS','FEET':'BOOTS'}[slot]
    for suffix in ['CHESTPLATE','LEGGINGS','HELMET','BOOTS','PICKAXE','SHOVEL','SWORD','AXE','HOE','CROSSBOW','BOW','FISHING_ROD','SHIELD']:
        if material==suffix or material.endswith('_'+suffix):return suffix
    if entry.get('behaviours',{}).get('hat'):return 'HELMET'
    return 'ITEM'
def add(id,name,type,model,pack,material='',armor=None,armorModel=False):
    id=id.split(':')[-1].lower();assert re.fullmatch('[a-z0-9_]+',id),id
    assert id not in [v['id'] for v in catalog],id
    row={'id':id,'name':re.sub('<[^>]+>|&[0-9a-fklmnor]','',name),'kind':type,'model':model,'pack':pack,'material':'NETHERITE' if type in ('HELMET','CHESTPLATE','LEGGINGS','BOOTS') else 'DIAMOND' if material.upper().startswith('DIAMOND') else 'IRON'}
    if armor:row.update(armorTexture1=armor[0],armorTexture2=armor[1])
    if armorModel:row['armorModel']=True
    catalog.append(row);write('assets/zianmobs/models/item/'+id+'.json',{'parent':model})
def import_zip(file,pack,preferred):
    with zipfile.ZipFile(DOWNLOAD/file) as z:
        paths=[n for n in z.namelist() if not n.endswith('/')]
        base=next(n.split('/assets/')[0]+'/assets/' for n in paths if preferred in n and '/assets/' in n and '/models/' in n)
        copied=0
        for n in paths:
            if not n.startswith(base):continue
            relative=n[len(base):];parts=PurePosixPath(relative).parts
            if len(parts)<3 or parts[0]=='minecraft' or parts[1] not in ('textures','models'):continue
            write('assets/'+relative,z.read(n));copied+=1
            if parts[1]=='textures' and relative.endswith('.png'):atlases.add(parts[0]+':'+('/'.join(parts[2:])[:-4]))
        configs=[n for n in paths if 'ItemsAdder/' in n and n.endswith(('.yml','.yaml'))]
        count=0
        for n in configs:
            data=yaml.safe_load(z.read(n).decode('utf-8-sig')) or {}
            if not isinstance(data,dict) or not isinstance(data.get('items'),dict):continue
            namespace=data.get('info',{}).get('namespace','elitecreatures')
            armors=data.get('armors_rendering',{})
            for id,entry in data['items'].items():
                resource=entry.get('resource',{});type=kind(str(resource.get('material','PAPER')),entry);armor=None
                armorId=entry.get('specific_properties',{}).get('armor',{}).get('custom_armor')
                if armorId in armors:
                    layer=armors[armorId];armor=[namespace+':textures/'+layer[key].removesuffix('.png')+'.png' for key in ('layer_1','layer_2')]
                model=resource.get('model_path')
                if model:
                    model=(namespace+':'+model if ':' not in model else model).removesuffix('.json')
                elif resource.get('generate') and resource.get('textures'):
                    model='zianmobs:generated/'+id;texture=resource['textures'][0].removesuffix('.png');texture=namespace+':'+texture if ':' not in texture else texture
                    write('assets/zianmobs/models/generated/'+id+'.json',{'parent':'minecraft:item/generated','textures':{'layer0':texture}})
                else:raise ValueError('No item model: '+id)
                ns,mp=model.split(':',1)
                if not (RES/'assets'/ns/'models'/(mp+'.json')).exists():raise ValueError('Missing source model '+model)
                add(id,entry.get('display_name',id),type,model,pack,str(resource.get('material','')),armor,type=='HELMET' and not armor);count+=1
        sources.append({'archive':file,'pack':pack,'assetsCopied':copied,'registeredItems':count})

import_zip('Carnivoret Weapon Set Volume 1.zip','Carnivoret','ItemsAdder/')
import_zip('Ruby_Vol.1-r1ohjq.zip','Ruby','ItemsAdder/')
import_zip('Dark_Queen-hvcmxs.zip','Dark Queen','ItemsAdder/')
import_zip('Purple_World_Set-djyvi2.zip','Purple World','ItemsAdder/')
import_zip('elitecreatures-valentines_animated_weapon_and_tool_set_v1-dsw24t.zip','Valentines','ItemsAdder/')

# RAR is read entry-by-entry. Never extract its paths into the filesystem.
rar=DOWNLOAD/'Toffys-Zelda-Pack-ph2tmh.rar'
paths=subprocess.check_output(['tar','-tf',str(rar)],text=True).splitlines()
rawprefix='Toffys Zelda Pack/Raw Files/'
def rarread(name):return subprocess.check_output(['tar','-xOf',str(rar),name])
raw=[n for n in paths if n.startswith(rawprefix) and n.endswith(('.json','.png','.mcmeta'))]
models={PurePosixPath(n).stem:'zianmobs:toffy/'+PurePosixPath(n).stem for n in raw if n.endswith('.json')}
textures={PurePosixPath(n).stem:'zianmobs:imports/toffy/'+PurePosixPath(n).stem for n in raw if n.endswith('.png')}
def rewrite(data):
    if isinstance(data,dict):return {k:rewrite(v) for k,v in data.items()}
    if isinstance(data,list):return [rewrite(v) for v in data]
    if isinstance(data,str) and not data.startswith('#'):
        stem=data.split(':')[-1].split('/')[-1]
        return textures.get(stem,data)
    return data
for n in raw:
    name=PurePosixPath(n).name
    if n.endswith('.json'):
        data=json.loads(rarread(n));data['textures']=rewrite(data.get('textures',{}))
        def modelrefs(v):
            if isinstance(v,list):return [modelrefs(x) for x in v]
            if isinstance(v,dict):return {k:(models.get(x.split(':')[-1].split('/')[-1],x) if k in ('model','parent') and isinstance(x,str) else modelrefs(x)) for k,x in v.items()}
            return v
        write('assets/zianmobs/models/toffy/'+name,modelrefs(data))
    else:
        write('assets/zianmobs/textures/imports/toffy/'+name,rarread(n))
        if n.endswith('.png'):atlases.add(textures[PurePosixPath(n).stem])
for suffix in ['1','2']:
    src=next(n for n in paths if 'Oraxen plugin files' in n and n.endswith('toffy_link_layer_'+suffix+'.png'))
    write('assets/zianmobs/textures/imports/toffy/toffy_link_layer_'+suffix+'.png',rarread(src))
armor=['zianmobs:textures/imports/toffy/toffy_link_layer_'+n+'.png' for n in ['1','2']]
for id,type in [('toffy_master_sword','SWORD'),('toffy_hylian_shield','SHIELD'),('toffy_link_hat','HELMET'),('toffy_rupee','ITEM')]:add(id,id.replace('_',' ').title(),type,models[id],'Toffy Zelda','DIAMOND',armor if type=='HELMET' else None,type=='HELMET')
for id,type in [('toffy_link_tunic','CHESTPLATE'),('toffy_link_pants','LEGGINGS'),('toffy_link_boots','BOOTS')]:
    model='zianmobs:toffy/'+id;write('assets/zianmobs/models/toffy/'+id+'.json',{'parent':'minecraft:item/generated','textures':{'layer0':textures[id+'_icon']}});add(id,id.replace('_',' ').title(),type,model,'Toffy Zelda','NETHERITE',armor)
sources.append({'archive':rar.name,'pack':'Toffy Zelda','registeredItems':7})
write('assets/zianmobs/gear-catalog.json',catalog)
write('assets/minecraft/atlases/blocks.json',{'sources':[{'type':'minecraft:single','resource':texture} for texture in sorted(atlases)]})
lang=RES/'assets/zianmobs/lang'
for language in ['en_us','es_es']:
    path=lang/(language+'.json');data=json.loads(path.read_text(encoding='utf-8'));data.update({'item.zianmobs.'+r['id']:r['name'] for r in catalog});path.write_text(json.dumps(data,ensure_ascii=False,indent=2),encoding='utf-8')
(ROOT/'art/gear-import-report.json').write_text(json.dumps(sources,indent=2),encoding='utf-8')
print(json.dumps({'items':len(catalog),'byPack':dict(collections.Counter(r['pack'] for r in catalog)),'sprites':len(atlases)}))
