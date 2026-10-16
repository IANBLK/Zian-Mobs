package com.ianblk.zianmobs;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.network.syncher.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.*;

/** Player-shaped enemy adapted from Manager's local-skin NPC. No dependency on Manager. */
public final class CombatNpc extends Monster {
 private static final EntityDataAccessor<String> SKIN=SynchedEntityData.defineId(CombatNpc.class,EntityDataSerializers.STRING);
 private static final EntityDataAccessor<Boolean> SLIM=SynchedEntityData.defineId(CombatNpc.class,EntityDataSerializers.BOOLEAN);
 private String profileKey="npc_abyss_guard",pending="";private int attackWait,skillWait=80,preparation,skillIndex;private LivingEntity victim;
 public CombatNpc(EntityType<? extends Monster> type,Level level){super(type,level);setPersistenceRequired();xpReward=5;for(var slot:java.util.List.of(EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND,EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET))setDropChance(slot,0);}
 public static AttributeSupplier.Builder attributes(){return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH,40).add(Attributes.ATTACK_DAMAGE,6).add(Attributes.MOVEMENT_SPEED,.28).add(Attributes.FOLLOW_RANGE,32);}
 @Override protected void defineSynchedData(SynchedEntityData.Builder b){super.defineSynchedData(b);b.define(SKIN,"abyss_guard");b.define(SLIM,false);}
 public String skin(){return entityData.get(SKIN);}public boolean slim(){return entityData.get(SLIM);}
 public NpcProfiles.Spec profile(){return Tuning.current.npcs().getOrDefault(profileKey,NpcProfiles.defaults(profileKey));}
 public void configure(String id){com.ianblk.zianmobs.core.Definitions.id(id);profileKey=id;appearance();}
 private void appearance(){var p=profile();entityData.set(SKIN,p.skin());entityData.set(SLIM,p.slim());}
 @Override public void addAdditionalSaveData(CompoundTag n){super.addAdditionalSaveData(n);n.putString("ZianMobsNpcProfile",profileKey);}
 @Override public void readAdditionalSaveData(CompoundTag n){super.readAdditionalSaveData(n);if(n.contains("ZianMobsNpcProfile"))try{configure(n.getString("ZianMobsNpcProfile"));}catch(IllegalArgumentException ignored){configure("npc_abyss_guard");}}
 @Override protected void registerGoals(){goalSelector.addGoal(0,new FloatGoal(this));goalSelector.addGoal(6,new WaterAvoidingRandomStrollGoal(this,.7));goalSelector.addGoal(7,new LookAtPlayerGoal(this,Player.class,10));targetSelector.addGoal(1,new HurtByTargetGoal(this));targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,10,true,false,p->profile().hostile()&&NpcCombat.canHurt(this,p)));}
 @Override protected void customServerAiStep(){super.customServerAiStep();appearance();if(attackWait>0)attackWait--;if(skillWait>0)skillWait--;var p=profile();var target=getTarget();if(!NpcCombat.canHurt(this,target)){stopUsingItem();pending="";preparation=0;setTarget(null);return;}getLookControl().setLookAt(target,30,30);
  if(preparation>0){getNavigation().stop();if(--preparation==0){if(NpcCombat.canHurt(this,victim)&&distanceToSqr(victim)<=p.range()*p.range()&&hasLineOfSight(victim))cast(pending,victim);pending="";stopUsingItem();}return;}
  double distance=distanceToSqr(target);if(distance>p.range()*p.range()||!hasLineOfSight(target)){getNavigation().moveTo(target,1);return;}
  var skills=p.skills();if(skillWait==0&&!skills.isEmpty()){pending=skills.get(skillIndex++%skills.size());victim=target;preparation=20;skillWait=p.skillCooldown();getNavigation().stop();playSound(pending.equals("SONIC")?SoundEvents.WARDEN_SONIC_CHARGE:SoundEvents.EVOKER_PREPARE_ATTACK,.7f,1);return;}
  if(getMainHandItem().getItem() instanceof BowItem||getMainHandItem().getItem() instanceof CrossbowItem){getNavigation().stop();if(attackWait==0){pending="ARROW";victim=target;preparation=getMainHandItem().getItem() instanceof EnhancedBow?EnhancedBow.DRAW_TICKS:20;attackWait=p.attackCooldown();startUsingItem(InteractionHand.MAIN_HAND);}return;}
  getNavigation().moveTo(target,1);double reach=2+target.getBbWidth()/2;if(distance<=reach*reach&&attackWait==0){attackWait=p.attackCooldown();swing(InteractionHand.MAIN_HAND);super.doHurtTarget(target);}
 }
 void cast(String kind,LivingEntity target){if(!(level() instanceof ServerLevel world)||!NpcCombat.canHurt(this,target))return;var delta=target.getEyePosition().subtract(getEyePosition());
  if(kind.equals("SONIC")){world.sendParticles(ParticleTypes.SONIC_BOOM,getX(),getEyeY(),getZ(),0,0,0,0,0);for(double d=1;d<delta.length();d++)world.sendParticles(ParticleTypes.SONIC_BOOM,getX()+delta.normalize().x*d,getEyeY()+delta.normalize().y*d,getZ()+delta.normalize().z*d,1,0,0,0,0);playSound(SoundEvents.WARDEN_SONIC_BOOM,1,1);target.hurt(damageSources().sonicBoom(this),(float)profile().skillDamage());return;}
  if(kind.equals("ARROW")){var arrow=new Arrow(world,this,new ItemStack(Items.ARROW),getMainHandItem().isEmpty()?null:getMainHandItem());arrow.setBaseDamage(getAttributeValue(Attributes.ATTACK_DAMAGE)/1.6);if(getMainHandItem().getItem() instanceof ProjectileWeaponItem weapon)weapon.customArrow(arrow,new ItemStack(Items.ARROW),getMainHandItem());arrow.pickup=AbstractArrow.Pickup.DISALLOWED;arrow.shoot(delta.x,delta.y+delta.horizontalDistance()*.2,delta.z,1.6f,1);arrow.getPersistentData().putBoolean("ZianMobsNpcArrow",true);world.addFreshEntity(arrow);NpcCombat.track(arrow);playSound(SoundEvents.SKELETON_SHOOT,1,1);return;}
  var projectile=new NpcProjectile(Mobs.PROJECTILE.get(),this,world,kind,profile().skillDamage());projectile.shoot(delta.x,delta.y,delta.z,1.2f,0);world.addFreshEntity(projectile);playSound(SoundEvents.BLAZE_SHOOT,.7f,1);
 }
}
