package com.ianblk.zianmobs;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import software.bernie.geckolib.animation.*;
/** Timed native equivalents of the three skills included in the supplied Oni pack. */
public final class OniBrute extends CursedOne {
 private int skillTicks=-1,skill,warcryCooldown,skillCooldown;
 public OniBrute(EntityType<? extends Monster> type,Level level){super(type,level);}
 public static AttributeSupplier.Builder attributes(){return CursedOne.attributes().add(Attributes.MAX_HEALTH,50).add(Attributes.ATTACK_DAMAGE,10).add(Attributes.MOVEMENT_SPEED,.25);}
 @Override protected void tickDeath(){deathTime++;if(deathTime>=60&&!level().isClientSide&&!isRemoved()){level().broadcastEntityEvent(this,(byte)60);remove(Entity.RemovalReason.KILLED);}}
 @Override public String asset(){return "oni_brute";}
 @Override public boolean doHurtTarget(Entity target){return false;}
 @Override public void tick(){super.tick();if(level().isClientSide||!isAlive())return;if(skillCooldown>0)skillCooldown--;if(!Tuning.attack(asset()).enabled())return;if(warcryCooldown>0)warcryCooldown--;if(skillTicks<0){if(skillCooldown==0 && tickCount%5==0 && getTarget()!=null && getTarget().isAlive() && hasLineOfSight(getTarget()) && distanceToSqr(getTarget())<=Math.pow(Tuning.attack(asset()).roarRadius(),2)){int choice=random.nextInt(3);if(choice==2 && warcryCooldown>0)return;if(choice<2 && distanceToSqr(getTarget())>(choice==0?20.25:25))return;skill=choice;skillTicks=0;skillCooldown=Tuning.attack(asset()).cooldown();triggerAnim("main",choice==0?"atk":choice==1?"heavy":"warcry");if(choice==2)warcryCooldown=Tuning.attack(asset()).roarCooldown();}return;}getNavigation().stop();skillTicks++;if(skill==0 && (skillTicks==20||skillTicks==40))hit(Tuning.attack(asset()).radius(),220,false);if(skill==1 && skillTicks==14)hit(Tuning.attack(asset()).radius(),360,true);if(skill==2 && skillTicks==10){addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,Tuning.attack(asset()).roarSeconds()*20,1));for(Player player:level().getEntitiesOfClass(Player.class,getBoundingBox().inflate(Tuning.attack(asset()).roarRadius()),p->!p.isCreative()&&!p.isSpectator()))if(distanceToSqr(player)<=Math.pow(Tuning.attack(asset()).roarRadius(),2))player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,Tuning.attack(asset()).roarSeconds()*20,1));playSound(SoundEvents.ILLUSIONER_CAST_SPELL,.5f,1.5f);if(level() instanceof ServerLevel server)server.sendParticles(ParticleTypes.CHERRY_LEAVES,getX(),getY()+1,getZ(),30,1,1,1,.1);}if(skillTicks>=(skill==0?50:skill==1?30:20))skillTicks=-1;}
 private void hit(double radius,double angle,boolean launch){var direction=getLookAngle().multiply(1,0,1).normalize();for(LivingEntity entity:level().getEntitiesOfClass(LivingEntity.class,getBoundingBox().inflate(radius),e->e!=this&&!isAlliedTo(e)&&!(e instanceof OniBrute)&&!(e instanceof Player p&&(p.isCreative()||p.isSpectator())))){var delta=entity.position().subtract(position()).multiply(1,0,1);if(delta.lengthSqr()>radius*radius || Math.abs(entity.getY()-getY())>3 || !hasLineOfSight(entity))continue;if(angle<360 && delta.lengthSqr()>.001 && direction.dot(delta.normalize())<Math.cos(Math.toRadians(angle/2)))continue;if(entity.hurt(damageSources().mobAttack(this),(float)(getAttributeValue(Attributes.ATTACK_DAMAGE)*Tuning.attack(asset()).multiplier())) && launch)entity.knockback(1,getX()-entity.getX(),getZ()-entity.getZ());}}
 @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers){controllers.add(new AnimationController<>(this,"main",2,state->state.setAndContinue(isDeadOrDying()?RawAnimation.begin().thenPlayAndHold("death"):state.isMoving()?RawAnimation.begin().thenLoop("walk"):RawAnimation.begin().thenLoop("idle"))).triggerableAnim("atk",RawAnimation.begin().thenPlay("atk")).triggerableAnim("heavy",RawAnimation.begin().thenPlay("heavy")).triggerableAnim("warcry",RawAnimation.begin().thenPlay("warcry")));controllers.add(new AnimationController<>(this,"blink",0,state->state.setAndContinue(RawAnimation.begin().thenLoop("blink"))));}
}
