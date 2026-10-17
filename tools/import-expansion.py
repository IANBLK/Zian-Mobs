from pathlib import Path
import json,base64,hashlib,struct,copy
from model_conversion import convert
ROOT=Path(__file__).resolve().parents[1]
INPUT=ROOT.parent/'pets-expansion-review'
ASSETS=ROOT/'src/main/resources/assets/zianmobs'
STAGING=ROOT/'build/expansion-textures';STAGING.mkdir(parents=True,exist_ok=True)
catalog=[{'id':id,'name':name,'flying':id=='ghost','glow':id in ('kit','clank'),'death':True} for id,name in [('otter','Sleepy · Nutria de las Mareas'),('kit','Kit · Guardián del Vínculo'),('clank','Clank · Corazón de Acero'),('ghost','Espíritu del Lucero')]]
names={'gift':'Regalo del Invierno','evil':'Cubee · Sombra','fire':'Cubee · Ascua','good':'Cubee · Luz','grass':'Cubee · Brote','skeleton':'Cubee · Huesitos','stone':'Cubee · Roca','tnt':'Cubee · Dinamita','water':'Cubee · Marea','desert':'Cubee · Desierto','jungle':'Cubee · Jungla','lush':'Cubee · Cueva florida','mushroom':'Cubee · Hongo','plains':'Cubee · Pradera','savanna':'Cubee · Sabana','snow':'Cubee · Nieve','swamp':'Cubee · Pantano','baby_ghast':'Cubee · Pequeño Ghast','bombardier_creeper':'Cubee · Bombardero','cannon':'Cubee · Cañón','hoody_fox':'Cubee · Zorro arquero','llama':'Cubee · Llama','slingshot':'Cubee · Tirachinas','swag_pillager':'Cubee · Saqueador','twinkle_witch':'Cubee · Brujita'}
manifest=[];plans=[]
inputs=[(INPUT/'gift_pet.bbmodel','gift')]+[(p,'cubee_'+p.stem.removeprefix('cubee-')) for p in sorted(INPUT.glob('cubee-*.bbmodel'))]+[(next(INPUT.glob('snowbone/**/*.bbmodel')),'snowbone')]
for path,id in inputs:
 original=path.read_bytes();d=json.loads(original);model=copy.deepcopy(d)
 textures=d['textures'];used=sorted({int(f['texture']) for e in d['elements'] for f in e.get('faces',{}).values() if f.get('texture') is not None})
 bank={};x=0;texrows=[]
 for i,t in enumerate(textures):
  raw=base64.b64decode(t['source'].split(',',1)[1]);w,h=struct.unpack('>II',raw[16:24]);frame_h=min(w,h)
  source=STAGING/(id+'_'+str(i)+'.png');source.write_bytes(raw)
  texrows.append({'path':str(source.relative_to(ROOT)),'width':w,'height':h,'frameHeight':frame_h,'hash':hashlib.sha256(raw).hexdigest()})
  if i in used:bank[i]={'x':x,'width':w,'height':frame_h,'sx':w/t.get('uv_width',d['resolution']['width']),'sy':frame_h/t.get('uv_height',d['resolution']['height'])};x+=w
 width=x;height=max(v['height'] for v in bank.values());multi=len(used)>1
 if not multi:width=texrows[0]['width'];height=texrows[0]['frameHeight']
 for e in model['elements']:
  for face in e.get('faces',{}).values():
   if face.get('texture') is None:continue
   b=bank[int(face['texture'])];u=face['uv'];face['uv']=[u[0]*b['sx']+b['x'],u[1]*b['sy'],u[2]*b['sx']+b['x'],u[3]*b['sy']]
 model['resolution']={'width':width,'height':height}
 asset='snowbone' if id=='snowbone' else 'companion_'+id
 geometry,animations=convert(model,asset)
 if id=='gift':
  eyes={'name':'zian_gift_eyes','parent':'body','pivot':[0,7,-5],'cubes':[]}
  for x in [-3.3,1.6]:
   eyes['cubes'].append({'origin':[x,7.3,-5.10],'size':[1.7,1.8,.04],'uv':{f:{'uv':[0,0],'uv_size':[1,1]} for f in ['north','south','east','west','up','down']}})
   eyes['cubes'].append({'origin':[x+.5,7.6,-5.16],'size':[.8,1.2,.03],'uv':{f:{'uv':[48,3],'uv_size':[1,1]} for f in ['north','south','east','west','up','down']}})
  geometry['minecraft:geometry'][0]['bones'].append(eyes)
 (ASSETS/'geo'/f'{asset}.geo.json').write_text(json.dumps(geometry),encoding='utf-8')
 (ASSETS/'animations'/f'{asset}.animation.json').write_text(json.dumps({'format_version':'1.8.0','animations':animations}),encoding='utf-8')
 glow=len(textures)>1 and '_e' in textures[1].get('name','')
 plans.append({'id':asset,'width':width,'height':height,'multi':multi,'bank':bank,'textures':texrows,'glow':glow,'fps':textures[0].get('fps',7)})
 if id!='snowbone':catalog.append({'id':id,'name':names.get(id,names.get(id.removeprefix('cubee_'),id)),'flying':id in ('cubee_baby_ghast','cubee_good','cubee_evil'),'glow':glow,'death':'death' in animations})
 manifest.append({'id':asset,'source':path.name,'sourceSha256':hashlib.sha256(original).hexdigest(),'textures':texrows,'multiTextureAtlas':multi,'animations':list(animations),'cubes':sum(len(b['cubes']) for b in geometry['minecraft:geometry'][0]['bones'])})
(STAGING/'plans.json').write_text(json.dumps(plans))
(ASSETS/'companion-catalog.json').write_text(json.dumps(catalog,ensure_ascii=False,indent=2),encoding='utf-8')
(ROOT/'art/expansion-import.json').write_text(json.dumps(manifest,indent=2))
for lang in ['en_us','es_es']:
 path=ASSETS/'lang'/f'{lang}.json';translations=json.loads(path.read_text(encoding='utf-8'))
 translations.update({'entity.zianmobs.companion_'+r['id']:r['name'] for r in catalog})
 translations['entity.zianmobs.snowbone']='Snowbone · Vigía de la Escarcha';translations['item.zianmobs.snowbone_spawn_egg']='Invocar Snowbone'
 path.write_text(json.dumps(translations,ensure_ascii=False,indent=2),encoding='utf-8')
print('Companions:',len(catalog),'New model assets:',len(manifest))
