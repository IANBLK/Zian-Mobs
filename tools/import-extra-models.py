"""Convert user supplied Blockbench artwork. Does not execute plugin scripts."""
import json,base64,hashlib,textwrap,zipfile,subprocess,copy,uuid
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]; OUT=ROOT/'src/main/resources/assets/zianmobs'; DOWNLOAD=(Path.home()/'Downloads')
# Share the already checked coordinate and keyframe conversion, without rerunning its archive loop.
source=(ROOT/'tools/convert-private-packs.py').read_text(encoding='utf-8')
code=textwrap.dedent(source[source.index('   groups='):source.index('   manifest.append')])
manifest=[]
inputs=[('goofy_goblin',(DOWNLOAD/'goofy_goblin.bbmodel').read_bytes()),('armored_grub',(DOWNLOAD/'armored_grub.bbmodel').read_bytes())]
with zipfile.ZipFile(DOWNLOAD/'KraberAPSniper.zip') as z:
 name=next(n for n in z.namelist() if n.endswith('.bbmodel'));inputs.append(('kraber_sniper',z.read(name)))
inputs.append(('toffy_triforce',subprocess.check_output(['tar','-xOf',str(DOWNLOAD/'Toffys-Zelda-Pack-ph2tmh.rar'),'Toffys Zelda Pack/Raw Files/toffy_triforce_generic.bbmodel'])))
for identifier,raw in inputs:
 d=json.loads(raw)
 # Bedrock cubes do not encode UV quarter turns. Split those faces into planar cubes,
 # rotate their geometry about the face centre, and preserve the PNG bytes exactly.
 replacements={};extra=[]
 for e in list(d['elements']):
  rotated={k:v for k,v in e.get('faces',{}).items() if v.get('rotation',0)}
  if not rotated:continue
  gid=str(uuid.uuid5(uuid.NAMESPACE_URL,identifier+e['uuid']));children=[e['uuid']]
  group={'uuid':gid,'name':'uv_'+e['uuid'].replace('-',''),'origin':e.get('origin',[0,0,0]),'rotation':e.pop('rotation',[0,0,0])}
  d.setdefault('groups',[]).append(group);replacements[e['uuid']]={'uuid':gid,'children':children}
  for face,v in rotated.items():
   angle=v.pop('rotation');plane=copy.deepcopy(e);plane['uuid']=str(uuid.uuid5(uuid.NAMESPACE_URL,gid+face));children.append(plane['uuid']);extra.append(plane)
   axis={'north':2,'south':2,'east':0,'west':0,'up':1,'down':1}[face];normal=e['from'][axis] if face in ('north','west','down') else e['to'][axis]
   mid=[(a+b)/2 for a,b in zip(e['from'],e['to'])];mid[axis]=normal
   dims=[e['to'][i]-e['from'][i] for i in range(3)];dims[axis]=0
   if angle%180:
    axes=[i for i in range(3) if i!=axis];dims[axes[0]],dims[axes[1]]=dims[axes[1]],dims[axes[0]]
   plane['from']=[mid[i]-dims[i]/2 for i in range(3)];plane['to']=[mid[i]+dims[i]/2 for i in range(3)];plane['origin']=mid;plane['rotation']=[0,0,0];plane['rotation'][axis]=angle*(1 if face in ('north','east','up') else -1);plane['faces']={face:v};del e['faces'][face]
 def replace(nodes):
  return [replacements.get(n,n) if isinstance(n,str) else dict(n,children=replace(n.get('children',[]))) for n in nodes]
 d['outliner']=replace(d['outliner']);d['elements'].extend(extra)
 # Only one used texture in these models; fail rather than silently render other faces incorrectly.
 used={v.get('texture') for e in d['elements'] for v in e.get('faces',{}).values() if v.get('texture') is not None}
 if len(used)>1:raise ValueError((identifier,'requires multi-texture conversion',used))
 scope={'d':d,'identifier':identifier,'OUT':OUT,'json':json,'base64':base64}
 exec(code,scope)
 geometry=scope['geometry'];animations=scope['animations'];bones=geometry['minecraft:geometry'][0]['bones'];root=next(b['name'] for b in bones if 'parent' not in b)
 if identifier in ('goofy_goblin','armored_grub'):
  def clip(length,loop,b):return {'animation_length':length,'loop':loop,'bones':b}
  def rot(times):return {'rotation':{str(t):v for t,v in times}}
  if identifier=='goofy_goblin':
   animations['idle']=clip(2,True,{root:rot([(0,[0,0,0]),(1,[0,0,1]),(2,[0,0,0])])})
   animations['attack']=clip(1,False,{root:rot([(0,[0,0,0]),(.35,[-18,0,-12]),(.6,[20,0,15]),(1,[0,0,0])])})
  else:
   # Keep the original idle and add a leg/body walk and a forward bite.
   animations['walk']=clip(1,True,{b['name']:rot([(0,[0,0,-12]),(.5,[0,0,12]),(1,[0,0,-12])]) for b in bones if 'leg' in b['name'].lower()})
   animations['attack']=clip(1,False,{root:rot([(0,[0,0,0]),(.35,[-12,0,0]),(.6,[16,0,0]),(1,[0,0,0])])})
  animations['death']=clip(1,False,{root:rot([(0,[0,0,0]),(1,[0,0,85])])})
 else:animations.setdefault('idle',clip(1,True,{}) if 'clip' in locals() else {'animation_length':1,'loop':True,'bones':{}})
 (OUT/'animations'/f'{identifier}.animation.json').write_text(json.dumps({'format_version':'1.8.0','animations':animations}),encoding='utf-8')
 manifest.append({'id':identifier,'sha256':hashlib.sha256(raw).hexdigest(),'cubes':sum(len(b.get('cubes',[])) for b in bones),'animations':list(animations)})
catalog_path=OUT/'gear-catalog.json';catalog=json.loads(catalog_path.read_text(encoding='utf-8'))
for id,name,kind,pack in [('kraber_sniper','Kraber · Arco de precisión','GEOBOW','Kraber'),('toffy_triforce','Trifuerza · Trofeo','GEOITEM','Toffy Zelda')]:
 catalog=[e for e in catalog if e['id']!=id];catalog.append({'id':id,'name':name,'kind':kind,'model':'builtin/entity','pack':pack,'material':'IRON'})
 (OUT/'models/item'/f'{id}.json').write_text(json.dumps({'parent':'builtin/entity','display':{'gui':{'rotation':[15,45,0],'scale':[.35,.35,.35]},'firstperson_righthand':{'rotation':[0,-90,0],'translation':[1,1,-2],'scale':[.6,.6,.6]},'thirdperson_righthand':{'rotation':[0,-90,0],'scale':[.6,.6,.6]},'ground':{'scale':[.4,.4,.4]},'head':{'scale':[.7,.7,.7]}}}),encoding='utf-8')
catalog_path.write_text(json.dumps(catalog,ensure_ascii=False,indent=2),encoding='utf-8')
for language in ['en_us','es_es']:
 p=OUT/'lang'/f'{language}.json';lang=json.loads(p.read_text(encoding='utf-8'))
 for id,name in [('goofy_goblin','Duende travieso'),('armored_grub','Larva acorazada')]:
  lang['entity.zianmobs.'+id]=name;lang['item.zianmobs.'+id+'_spawn_egg']='Invocar '+name
  (OUT/'models/item'/f'{id}_spawn_egg.json').write_text('{"parent":"minecraft:item/template_spawn_egg"}',encoding='utf-8')
 lang.update({'item.zianmobs.'+e['id']:e['name'] for e in catalog});lang['itemGroup.zianmobs']='Zian Mobs'
 p.write_text(json.dumps(lang,ensure_ascii=False,indent=2),encoding='utf-8')
(ROOT/'art/extra-models-report.json').write_text(json.dumps(manifest,indent=2),encoding='utf-8');print(json.dumps(manifest))
