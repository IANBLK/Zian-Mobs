"""One-time conversion of the two supplied private packs; never executes pack instructions."""
import zipfile,json,base64,hashlib
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1];OUT=ROOT/'src/main/resources/assets/zianmobs'
for part in ['geo','animations','textures/entity']:(OUT/part).mkdir(parents=True,exist_ok=True)
manifest=[]
for archive in [(Path.home()/'Downloads/Q_Cursed_One.zip'),(Path.home()/'Downloads/amonde-oni_free.zip')]:
 with zipfile.ZipFile(archive) as z:
  for name in z.namelist():
   if not name.endswith('.bbmodel'):continue
   raw=z.read(name);d=json.loads(raw);identifier=Path(name).stem.removeprefix('q_')
   groups={g['uuid']:g for g in d.get('groups',[])};elements={e['uuid']:e for e in d['elements']};bones=[];names={}
   def walk(node,parent=None,skip=False):
    if isinstance(node,str):return
    group=groups.get(node['uuid'],node);bn=group['name'];names[node['uuid']]=bn
    hidden=skip or bn in ('hitbox','g_damage')
    bone={'name':bn,'pivot':[-group.get('origin',[0,0,0])[0],*group.get('origin',[0,0,0])[1:]],'cubes':[]}
    if parent:bone['parent']=parent
    rotation=group.get('rotation',[0,0,0]);bone['rotation']=[-rotation[0],-rotation[1],rotation[2]]
    for child in node.get('children',[]):
     if isinstance(child,dict):continue
     e=elements[child]
     if hidden or e.get('export') is False or e.get('visibility') is False:continue
     size=[e['to'][i]-e['from'][i] for i in range(3)];origin=[-e['to'][0],e['from'][1],e['from'][2]]
     cube={'origin':origin,'size':size,'uv':{}}
     for face,v in e['faces'].items():
      if v.get('texture') is None:continue
      uv=list(v['uv']);uvsize=[uv[2]-uv[0],uv[3]-uv[1]]
      if face in ('up','down'):uv=[uv[2],uv[3],uv[0],uv[1]];uvsize=[uv[2]-uv[0],uv[3]-uv[1]]
      cube['uv'][face]={'uv':uv[:2],'uv_size':uvsize}
      if v.get('rotation'):raise ValueError('UV rotation requires reviewed conversion')
     r=e.get('rotation',[0,0,0]);p=e.get('origin',[0,0,0]);cube.update(pivot=[-p[0],p[1],p[2]],rotation=[-r[0],-r[1],r[2]])
     if e.get('inflate'):cube['inflate']=e['inflate']
     bone['cubes'].append(cube)
    bones.append(bone)
    for child in node.get('children',[]):
     if isinstance(child,dict):walk(child,bn,hidden)
   for node in d['outliner']:walk(node)
   geometry={'format_version':'1.12.0','minecraft:geometry':[{'description':{'identifier':'geometry.'+identifier,'texture_width':d['resolution']['width'],'texture_height':d['resolution']['height'],'visible_bounds_width':8,'visible_bounds_height':8,'visible_bounds_offset':[0,2,0]},'bones':bones}]}
   animations={}
   for a in d.get('animations',[]):
    channels={}
    for uid,animator in a.get('animators',{}).items():
     if uid not in names:continue
     bone={}
     for keyframe in animator.get('keyframes',[]):
      channel=keyframe['channel']
      if channel not in ('position','rotation','scale'):raise ValueError('Unsupported animation channel')
      points=keyframe['data_points'];values=[]
      for dp in points:
       v=[float(dp[axis]) for axis in ('x','y','z')]
       if channel=='position':v[0]=-v[0]
       if channel=='rotation':v[0]=-v[0];v[1]=-v[1]
       values.append(v)
      result={'post':values[-1],'lerp_mode':keyframe.get('interpolation','linear')}
      if len(values)>1:result['pre']=values[0]
      bone.setdefault(channel,{})[str(keyframe['time'])]=result
     if bone:channels[names[uid]]={channel:dict(sorted(frames.items(),key=lambda pair:float(pair[0]))) for channel,frames in bone.items()}
    animations[a['name']]={'loop':True if a['loop']=='loop' else 'hold_on_last_frame' if a['loop']=='hold' else False,'animation_length':a['length'],'bones':channels}
   (OUT/'geo'/f'{identifier}.geo.json').write_text(json.dumps(geometry),encoding='utf-8')
   (OUT/'animations'/f'{identifier}.animation.json').write_text(json.dumps({'format_version':'1.8.0','animations':animations}),encoding='utf-8')
   texture=base64.b64decode(d['textures'][0]['source'].split(',',1)[1],validate=True);assert texture.startswith(b'\x89PNG\r\n\x1a\n')
   (OUT/'textures/entity'/f'{identifier}.png').write_bytes(texture)
   manifest.append({'id':identifier,'sourceArchive':archive.name,'sourceModel':name,'sourceSha256':hashlib.sha256(raw).hexdigest(),'sourceCubes':len(elements),'renderedCubes':sum(len(b['cubes']) for b in bones),'animations':list(animations)})
(ROOT/'artwork-development.json').write_text(json.dumps({'privateDevelopmentOnly':True,'redistributionPermissionNotEstablished':True,'models':manifest},indent=2),encoding='utf-8')
print(json.dumps(manifest,indent=2))
