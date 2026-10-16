package com.ianblk.zianmobs;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.*;
/** Native projectile movement, but impact deliberately never calls explosions or block ignition. */
public final class NpcProjectile extends ThrowableItemProjectile {
 private ItemStack weapon=ItemStack.EMPTY;
 void setWeapon(ItemStack stack){weapon=stack.copyWithCount(1);}ItemStack weapon(){return weapon;}
 private String kind="BLAZE";private double damage=6;
 public NpcProjectile(EntityType<? extends NpcProjectile> type,Level level){super(type,level);setNoGravity(true);}
 NpcProjectile(EntityType<? extends NpcProjectile> type,LivingEntity owner,Level level,String kind,double damage){super(type,owner,level);setNoGravity(true);this.kind=kind;this.damage=damage;setItem(new ItemStack(kind.equals("WITHER")?Items.WITHER_SKELETON_SKULL:kind.equals("DRAGON")?Items.DRAGON_BREATH:Items.FIRE_CHARGE));}
 @Override protected Item getDefaultItem(){return Items.FIRE_CHARGE;}
 @Override public void tick(){super.tick();if(tickCount>100&&!level().isClientSide())discard();if(level().isClientSide())level().addParticle(getItem().is(Items.DRAGON_BREATH)?ParticleTypes.DRAGON_BREATH:getItem().is(Items.FIRE_CHARGE)?ParticleTypes.FLAME:ParticleTypes.SMOKE,getX(),getY(),getZ(),0,0,0);}
 @Override protected void onHitBlock(BlockHitResult hit){if(!level().isClientSide())impact(hit.getLocation(),null);}
 @Override protected void onHitEntity(EntityHitResult hit){if(!level().isClientSide())impact(hit.getLocation(),hit.getEntity() instanceof LivingEntity living?living:null);}
 private void impact(Vec3 point,LivingEntity target){if(!(getOwner() instanceof LivingEntity owner)){discard();return;}if(kind.equals("DRAGON")){for(var living:level().getEntitiesOfClass(LivingEntity.class,new AABB(point,point).inflate(3)))if(living.distanceToSqr(point)<=9&&NpcCombat.canHurt(owner,living)&&level().clip(new net.minecraft.world.level.ClipContext(point,living.getEyePosition(),net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,this)).getType()==HitResult.Type.MISS)living.hurt(damageSources().indirectMagic(this,owner),(float)damage);}else if(NpcCombat.canHurt(owner,target)&&target.hurt(damageSources().mobProjectile(this,owner),(float)damage)){if(kind.equals("WITHER"))target.addEffect(new MobEffectInstance(MobEffects.WITHER,weapon.isEmpty()?80:40,0),owner);if(kind.equals("BLAZE"))target.igniteForSeconds(4);}if(level() instanceof ServerLevel server)server.sendParticles(kind.equals("DRAGON")?ParticleTypes.DRAGON_BREATH:kind.equals("BLAZE")?ParticleTypes.FLAME:ParticleTypes.SMOKE,point.x,point.y,point.z,25,.6,.6,.6,.03);discard();}
 @Override public void addAdditionalSaveData(CompoundTag tag){super.addAdditionalSaveData(tag);if(!weapon.isEmpty())tag.put("ZianMobsWeapon",weapon.save(registryAccess()));tag.putString("ZianMobsSkill",kind);tag.putDouble("ZianMobsSkillDamage",damage);}
 @Override public void readAdditionalSaveData(CompoundTag tag){super.readAdditionalSaveData(tag);weapon=ItemStack.parseOptional(registryAccess(),tag.getCompound("ZianMobsWeapon"));String saved=tag.getString("ZianMobsSkill");kind=java.util.Set.of("DRAGON","WITHER","BLAZE").contains(saved)?saved:"BLAZE";double value=tag.getDouble("ZianMobsSkillDamage");damage=Double.isFinite(value)&&value>=0&&value<=64?value:6;setNoGravity(true);}
 void smokeImpact(Vec3 point,LivingEntity target){if(!Boolean.getBoolean("zianmobs.smoke"))throw new IllegalStateException("Test disabled");impact(point,target);}
}
