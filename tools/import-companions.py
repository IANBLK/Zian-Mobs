from pathlib import Path
import json,base64,hashlib,struct,shutil
ROOT=Path(__file__).resolve().parents[1];OUT=ROOT/'src/main/resources/assets/zianmobs';INPUT=ROOT.parent/'pets-trial-review'
for part in ['geo','animations','textures/entity','textures/models/armor','textures/item','models/item']:(OUT/part).mkdir(parents=True,exist_ok=True)
def convert(d,identifier):
 groups={g['uuid']:g for g in d.get('groups',[])};elements={e['uuid']:e for e in d['elements']};bones=[];names={}
 def walk(node,parent=None,skip=False):
  if isinstance(node,str):return
  group=groups.get(node['uuid'],node);bn=group['name'];names[node['uuid']]=bn
  hidden=skip or bn in ('hitbox','g_damage','tag_name','tag_false')
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
 return geometry,animations
manifest=[]
for name,id in [('nocsy_otter_v2.bbmodel','companion_otter'),('Kit.bbmodel','companion_kit'),('Clank.bbmodel','companion_clank'),('ghost.bbmodel','companion_ghost')]:
 raw=(INPUT/name).read_bytes();d=json.loads(raw);geometry,animations=convert(d,id)
 (OUT/'geo'/f'{id}.geo.json').write_text(json.dumps(geometry),encoding='utf-8')
 (OUT/'animations'/f'{id}.animation.json').write_text(json.dumps({'format_version':'1.8.0','animations':animations}),encoding='utf-8')
 for i,t in enumerate(d['textures']):
  data=base64.b64decode(t['source'].split(',',1)[1],validate=True);assert struct.unpack('>II',data[16:24])==(d['resolution']['width'],d['resolution']['height'])
  (OUT/'textures/entity'/(id+('_glowmask' if i else '')+'.png')).write_bytes(data)
 manifest.append({'id':id,'source':name,'sourceSha256':hashlib.sha256(raw).hexdigest(),'animations':list(animations),'textureSha256':hashlib.sha256(base64.b64decode(d['textures'][0]['source'].split(',',1)[1])).hexdigest(),'cubes':sum(len(b['cubes']) for b in geometry['minecraft:geometry'][0]['bones'])})
armor=INPUT/'thanos/files/textures/armor'
for source,target in [('thanos_armor_layer_1.png','thanos_layer_1.png'),('thanos_layer_2.png','thanos_layer_2.png')]:shutil.copy2(armor/source,OUT/'textures/models/armor'/target)
for part in ['helmet','chestplate','leggings','boots']:
 id='titan_'+part;shutil.copy2(armor/('thanos_'+part+'.png'),OUT/'textures/item'/(id+'.png'))
 (OUT/'models/item'/(id+'.json')).write_text(json.dumps({'parent':'minecraft:item/generated','textures':{'layer0':'zianmobs:item/'+id}}))
(ROOT/'art/companions-import.json').write_text(json.dumps(manifest,indent=2))
print(json.dumps(manifest,indent=2))
