package com.ianblk.zianmobs;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
/** No explosions, fire placement or block modification. */
public final class StaffItem extends SwordItem {
 private final String kind;
 StaffItem(Tier tier,Properties properties,String pack){super(tier,properties);kind=switch(pack){case "Ruby","Dark Queen"->"WITHER";case "Valentines"->"DRAGON";default->"SONIC";};}
 @Override public InteractionResultHolder<ItemStack> use(Level level,Player player,InteractionHand hand){var stack=player.getItemInHand(hand);if(level.isClientSide())return InteractionResultHolder.success(stack);if(!GearAbilities.acquire(player,"staff",30)){player.displayClientMessage(net.minecraft.network.chat.Component.literal("El báculo aún se está recargando."),true);return InteractionResultHolder.fail(stack);}
  if(kind.equals("SONIC")){var start=player.getEyePosition();var end=start.add(player.getLookAngle().scale(24));var block=level.clip(new ClipContext(start,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,player));end=block.getLocation();LivingEntity nearest=null;double distance=start.distanceToSqr(end);for(var living:level.getEntitiesOfClass(LivingEntity.class,new AABB(start,end).inflate(1))){if(!NpcCombat.canHurt(player,living))continue;var hit=living.getBoundingBox().inflate(.25).clip(start,end);if(hit.isPresent()&&start.distanceToSqr(hit.get())<distance){distance=start.distanceToSqr(hit.get());nearest=living;}}var direction=player.getLookAngle();for(int n=1;n<=Math.sqrt(distance);n++)((ServerLevel)level).sendParticles(ParticleTypes.SONIC_BOOM,start.x+direction.x*n,start.y+direction.y*n,start.z+direction.z*n,1,0,0,0,0);if(nearest!=null){float before=nearest.getHealth();nearest.hurt(player.damageSources().sonicBoom(player),10);if(nearest.getHealth()<before)GearAbilities.hit(player,nearest,GearAbilities.id(stack),false);}
  }else{var projectile=new NpcProjectile(Mobs.PROJECTILE.get(),player,level,kind,10);projectile.setWeapon(stack);projectile.shootFromRotation(player,player.getXRot(),player.getYRot(),0,1.3f,0);level.addFreshEntity(projectile);}
  for(var spec:Gear.CATALOG)if(spec.id().endsWith("_staff"))player.getCooldowns().addCooldown(Gear.ITEMS.get(spec.id()).get(),600);
  stack.hurtAndBreak(1,player,LivingEntity.getSlotForHand(hand));return InteractionResultHolder.consume(stack);
 }
}
