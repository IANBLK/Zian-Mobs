package com.ianblk.zianmobs;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animation.*;
/** Native adaptation of the supplied 20 HP, 3 damage, 4 second snowball attack. */
public final class Snowbone extends Monster implements RangedAttackMob,GeoEntity {
 private final AnimatableInstanceCache cache=GeckoLibUtil.createInstanceCache(this);
 private int windup;private LivingEntity pending;
 public Snowbone(EntityType<? extends Snowbone> type,Level level){super(type,level);xpReward=5;}
 public static AttributeSupplier.Builder attributes(){return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH,20).add(Attributes.ATTACK_DAMAGE,3).add(Attributes.MOVEMENT_SPEED,.23).add(Attributes.FOLLOW_RANGE,24);}
 @Override protected void registerGoals(){goalSelector.addGoal(0,new FloatGoal(this));goalSelector.addGoal(2,new RangedAttackGoal(this,1,80,9));goalSelector.addGoal(5,new WaterAvoidingRandomStrollGoal(this,.7));goalSelector.addGoal(6,new LookAtPlayerGoal(this,Player.class,12));targetSelector.addGoal(1,new HurtByTargetGoal(this));targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,true));}
 @Override public void performRangedAttack(LivingEntity target,float power){if(windup>0||!target.isAlive())return;pending=target;windup=13;triggerAnim("main","shoot");}
 @Override public void tick(){super.tick();if(level().isClientSide()||!isAlive()||windup<=0)return;getNavigation().stop();if(--windup==0){if(pending!=null&&pending.isAlive()&&distanceToSqr(pending)<=12*12&&hasLineOfSight(pending)){var shot=new FrostBall(level(),this,(float)getAttributeValue(Attributes.ATTACK_DAMAGE));var delta=pending.getEyePosition().subtract(shot.position());shot.shoot(delta.x,delta.y,delta.z,1.2f,.5f);level().addFreshEntity(shot);playSound(net.minecraft.sounds.SoundEvents.SNOW_GOLEM_SHOOT,1,1);}pending=null;}}
 @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers){controllers.add(new AnimationController<>(this,"main",3,state->state.setAndContinue(isDeadOrDying()?RawAnimation.begin().thenPlayAndHold("death"):state.isMoving()?RawAnimation.begin().thenLoop("walk"):getTarget()!=null?RawAnimation.begin().thenLoop("combat_idle"):RawAnimation.begin().thenLoop("idle"))).triggerableAnim("shoot",RawAnimation.begin().thenPlay("shoot")));}
 @Override public AnimatableInstanceCache getAnimatableInstanceCache(){return cache;}
 static final class FrostBall extends Snowball {
  private final float damage;
  FrostBall(Level level,LivingEntity owner,float damage){super(level,owner);this.damage=damage;}
  @Override protected void onHitEntity(EntityHitResult hit){if(!level().isClientSide()&&hit.getEntity() instanceof LivingEntity target&&target!=getOwner())target.hurt(damageSources().thrown(this,getOwner()),damage);}
 }
}
