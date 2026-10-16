package com.ianblk.zianmobs;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import java.util.*;
public class CursedOne extends Monster implements GeoEntity {
 private final AnimatableInstanceCache cache=GeckoLibUtil.createInstanceCache(this);
 private int attackTicks=-1,cooldown;private LivingEntity victim;
 public CursedOne(EntityType<? extends Monster> type,Level level){super(type,level);xpReward=5;}
 public static AttributeSupplier.Builder attributes(){return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH,25).add(Attributes.ATTACK_DAMAGE,3).add(Attributes.MOVEMENT_SPEED,.28).add(Attributes.FOLLOW_RANGE,32).add(Attributes.KNOCKBACK_RESISTANCE,.7);}
 @Override protected void registerGoals(){goalSelector.addGoal(0,new FloatGoal(this));goalSelector.addGoal(2,new MeleeAttackGoal(this,1,false));goalSelector.addGoal(5,new WaterAvoidingRandomStrollGoal(this,.8));goalSelector.addGoal(6,new LookAtPlayerGoal(this,Player.class,8));targetSelector.addGoal(1,new HurtByTargetGoal(this));targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,true));}
 // Vanilla melee only requests the timed attack; actual damage is applied once at its impact frame.
 @Override public boolean doHurtTarget(Entity target){if(!(target instanceof LivingEntity living)||cooldown>0||attackTicks>=0||!Tuning.attack(asset()).enabled())return false;victim=living;attackTicks=0;cooldown=Tuning.attack(asset()).cooldown();getNavigation().stop();triggerAnim("main","attack");playSound(SoundEvents.PILLAGER_AMBIENT,.5f,.7f);return true;}
 @Override public void tick(){super.tick();if(level().isClientSide || !isAlive())return;if(cooldown>0)cooldown--;if(attackTicks<0)return;getNavigation().stop();if(victim!=null && victim.isAlive())getLookControl().setLookAt(victim,30,30);if(++attackTicks==Tuning.attack(asset()).windup())impact();if(attackTicks>=Math.max(20,Tuning.attack(asset()).windup()+8)){attackTicks=-1;victim=null;}}
 private void impact(){Vec3 forward=getLookAngle().multiply(1,0,1).normalize();setDeltaMovement(forward.scale(Tuning.attack(asset()).leap()).add(0,getDeltaMovement().y,0));Set<UUID> hit=new HashSet<>();double radius=Tuning.attack(asset()).radius();var area=getBoundingBox().inflate(radius,1,radius);for(var entity:level().getEntitiesOfClass(LivingEntity.class,area,e->e!=this && !isAlliedTo(e) && !(e instanceof Player p && (p.isCreative()||p.isSpectator())))){if(hit.add(entity.getUUID()) && distanceToSqr(entity)<=radius*radius && hasLineOfSight(entity) && entity.hurt(damageSources().mobAttack(this),(float)(getAttributeValue(Attributes.ATTACK_DAMAGE)*Tuning.attack(asset()).multiplier())))entity.knockback(.6,getX()-entity.getX(),getZ()-entity.getZ());}}
 public String asset(){return "cursed_one_"+variant();}
 public String variant(){if(getType()==Mobs.ICE.get())return "ice";if(getType()==Mobs.CRYSTAL.get())return "crystal";return "forest";}
 @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers){controllers.add(new AnimationController<>(this,"main",2,state->state.setAndContinue(isDeadOrDying()?RawAnimation.begin().thenPlayAndHold("death"):state.isMoving()?RawAnimation.begin().thenLoop("walk"):RawAnimation.begin().thenLoop("idle"))).triggerableAnim("attack",RawAnimation.begin().thenPlay("attack")));}
 @Override public AnimatableInstanceCache getAnimatableInstanceCache(){return cache;}
}
