package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animation.*;
/** No drops, vanilla melee or natural spawn. The service owns ranged combat and lifecycle. */
public final class CompanionEntity extends PathfinderMob implements GeoEntity {
 private final AnimatableInstanceCache cache=GeckoLibUtil.createInstanceCache(this);
 CompanionAttacks.Pending pendingAttack;
 UUID ownerId;boolean managed;private int interactCooldown;int outsideFollowTicks,lastRescueTick=-20;private double flightMultiplier=1.6;
 public CompanionEntity(EntityType<? extends CompanionEntity> type,Level level){super(type,level);xpReward=0;setNoGravity(true);moveControl=new CompanionMoveControl(this);}
 public static AttributeSupplier.Builder attributes(){return Mob.createMobAttributes().add(Attributes.MAX_HEALTH,24).add(Attributes.MOVEMENT_SPEED,.26).add(Attributes.FLYING_SPEED,.26).add(Attributes.FOLLOW_RANGE,32).add(Attributes.KNOCKBACK_RESISTANCE,.7);}
 public String id(){return net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(getType()).getPath().replace("companion_","");}
 void cancelAttack(){if(pendingAttack!=null){pendingAttack=null;if(!level().isClientSide())stopTriggeredAnim("combat","ranged");}}
 public String asset(){return "companion_"+id();}
 @Override protected PathNavigation createNavigation(Level level){var navigation=new FlyingPathNavigation(this,level);navigation.setCanOpenDoors(false);navigation.setCanPassDoors(true);return navigation;}
 @Override protected void registerGoals(){goalSelector.addGoal(0,new FloatGoal(this));goalSelector.addGoal(2,new Goal(){ {setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK));}public boolean canUse(){var p=owner();return pendingAttack==null&&p!=null&&p.isAlive()&&!p.isSpectator();}public boolean canContinueToUse(){return canUse();}public void tick(){if(owner() instanceof net.minecraft.server.level.ServerPlayer p)hoverFollow(p);}public void stop(){getNavigation().stop();}});}
 void hoverFollow(net.minecraft.server.level.ServerPlayer owner){var target=hoverTarget(owner);if(target==null){getNavigation().stop();return;}double distance=distanceTo(owner);double desired=distance<3?1.6:Math.min(3.6,1.6+(distance-3)*.22);flightMultiplier+=Math.max(-.14,Math.min(.14,desired-flightMultiplier));if(distance<4)getLookControl().setLookAt(owner,25,25);else getLookControl().setLookAt(target.x,target.y+getBbHeight()/2,target.z,30,30);var hit=level().clip(new net.minecraft.world.level.ClipContext(getEyePosition(),target.add(0,getBbHeight()/2,0),net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,this));if(hit.getType()==net.minecraft.world.phys.HitResult.Type.MISS){getNavigation().stop();getMoveControl().setWantedPosition(target.x,target.y,target.z,flightMultiplier);}else if(tickCount%10==0)getNavigation().moveTo(target.x,target.y,target.z,flightMultiplier);}
 net.minecraft.world.phys.Vec3 hoverTarget(net.minecraft.server.level.ServerPlayer owner){return CompanionFlightTargets.find(this,owner,true);}

 @Override public void travel(net.minecraft.world.phys.Vec3 input){if(isControlledByLocalInstance()){move(MoverType.SELF,getDeltaMovement());setDeltaMovement(getDeltaMovement().scale(.93));}calculateEntityAnimation(false);}
 @Override public boolean causeFallDamage(float distance,float multiplier,DamageSource source){return false;}

 private void safely(Runnable action){try{action.run();}catch(RuntimeException ex){ZianMobs.LOGGER.error("Companion lifecycle action failed; persistent service has stopped safely",ex);}}
 public Player owner(){return ownerId==null?null:level().getPlayerByUUID(ownerId);}
 @Override public boolean doHurtTarget(Entity target){return false;}
 @Override public boolean canBeLeashed(){return false;}
 @Override public boolean hurt(DamageSource source,float amount){if(level().isClientSide()||source.getEntity() instanceof Player||source.getEntity() instanceof CompanionEntity)return false;boolean result=super.hurt(source,amount);if(result&&Companions.current!=null)safely(()->Companions.current.capture(this));return result;}
 @Override public void die(DamageSource source){cancelAttack();if(isDeadOrDying()&&getHealth()>0)return;if(!level().isClientSide()&&Companions.current!=null)safely(()->Companions.current.died(this));super.die(source);}
 @Override protected void tickDeath(){if(++deathTime>=(id().equals("otter")?40:CompanionContent.definition(id()).flying()?20:10)&&!level().isClientSide())remove(RemovalReason.KILLED);}
 @Override protected void dropAllDeathLoot(net.minecraft.server.level.ServerLevel level,DamageSource source){}
 @Override public boolean saveAsPassenger(CompoundTag tag){return false;}
 @Override public boolean shouldBeSaved(){return false;}
 @Override public boolean removeWhenFarAway(double distance){return false;}
 @Override public void tick(){super.tick();if(level().isClientSide())return;if(interactCooldown>0)interactCooldown--;if(!managed&&tickCount>5)discard();if(!isAlive())return;var service=Companions.current;if(service==null){discard();return;}safely(()->service.entityTick(this));}
 @Override protected net.minecraft.world.InteractionResult mobInteract(Player player,net.minecraft.world.InteractionHand hand){if(ownerId!=null&&ownerId.equals(player.getUUID())&&hand==net.minecraft.world.InteractionHand.MAIN_HAND){if(!level().isClientSide()&&interactCooldown==0){interactCooldown=120;if(id().equals("otter"))triggerAnim("main",random.nextBoolean()?"pet1":"pet2");player.displayClientMessage(net.minecraft.network.chat.Component.literal(CompanionContent.name(id())+" · "+Math.round(getHealth())+" / "+Math.round(getMaxHealth())+" de vida"),true);}return net.minecraft.world.InteractionResult.sidedSuccess(level().isClientSide());}return net.minecraft.world.InteractionResult.PASS;}
 @Override public void addAdditionalSaveData(CompoundTag tag){super.addAdditionalSaveData(tag);if(ownerId!=null)tag.putUUID("Owner",ownerId);}
 @Override public void readAdditionalSaveData(CompoundTag tag){super.readAdditionalSaveData(tag);ownerId=tag.hasUUID("Owner")?tag.getUUID("Owner"):null;managed=false;}
 @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers){controllers.add(new AnimationController<>(this,"main",3,state->state.setAndContinue(isDeadOrDying()&&CompanionContent.definition(id()).death()?RawAnimation.begin().thenPlayAndHold("death"):isInWater()&&id().equals("otter")?RawAnimation.begin().thenLoop("swim"):state.isMoving()&&CompanionContent.definition(id()).flying()?RawAnimation.begin().thenLoop("walk"):RawAnimation.begin().thenLoop("idle"))).triggerableAnim("scene_walk",RawAnimation.begin().thenLoop("walk")).triggerableAnim("pet1",RawAnimation.begin().thenPlay("pet1")).triggerableAnim("pet2",RawAnimation.begin().thenPlay("pet2")));var profile=CompanionAttacks.profile(id());if(!profile.clip().isEmpty())controllers.add(new AnimationController<>(this,"combat",0,state->PlayState.STOP).triggerableAnim("ranged",RawAnimation.begin().thenPlay(profile.clip())));}
 @Override public AnimatableInstanceCache getAnimatableInstanceCache(){return cache;}
}
