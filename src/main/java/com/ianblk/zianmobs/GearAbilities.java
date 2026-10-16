package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.effect.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.*;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.entity.living.*;
/** Abilities use accepted damage, projectile weapon snapshots and persistent player deadlines. */
public final class GearAbilities {
 private static final DeferredRegister<MobEffect> EFFECTS=DeferredRegister.create(Registries.MOB_EFFECT,"zianmobs");
 public static final DeferredHolder<MobEffect,MobEffect> BLEEDING=EFFECTS.register("bleeding",()->new MobEffect(MobEffectCategory.HARMFUL,0xAA2030){@Override public boolean shouldApplyEffectTickThisTick(int duration,int amplifier){return duration%20==0;}@Override public boolean applyEffectTick(LivingEntity target,int amplifier){target.hurt(target.damageSources().magic(),1);return true;}});
 private record Crit(UUID target,boolean critical,long tick){}
 private static final Map<Player,Crit> CRITS=new WeakHashMap<>();
 public static void register(IEventBus bus){EFFECTS.register(bus);NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,GearAbilities::critical);NeoForge.EVENT_BUS.addListener(GearAbilities::damage);NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,GearAbilities::shield);}
 private static void critical(CriticalHitEvent e){if(!e.getEntity().level().isClientSide())CRITS.put(e.getEntity(),new Crit(e.getTarget().getUUID(),e.isCriticalHit(),e.getEntity().level().getGameTime()));}
 static String id(ItemStack stack){var key=BuiltInRegistries.ITEM.getKey(stack.getItem());return key.getNamespace().equals("zianmobs")?key.getPath():"";}
 static boolean acquire(Player p,String key,int seconds){var saved=p.getPersistentData();if(!saved.contains(Player.PERSISTED_NBT_TAG))saved.put(Player.PERSISTED_NBT_TAG,new net.minecraft.nbt.CompoundTag());var root=saved.getCompound(Player.PERSISTED_NBT_TAG);String tag="ZianMobsCooldown_"+key;long now=System.currentTimeMillis();if(key.equals("shield")&&!root.getBoolean("ZianMobsShield30")){if(root.contains(tag))root.putLong(tag,Math.max(0,root.getLong(tag)-60000));root.putBoolean("ZianMobsShield30",true);}if(root.getLong(tag)>now)return false;root.putLong(tag,now+seconds*1000L);return true;}
 private static void damage(LivingDamageEvent.Post e){if(e.getNewDamage()<=0||e.getEntity().level().isClientSide()||!(e.getSource().getEntity() instanceof LivingEntity owner))return;var direct=e.getSource().getDirectEntity();ItemStack stack;boolean crit=false;
  if(direct instanceof AbstractArrow arrow){stack=arrow.getWeaponItem();crit=arrow.isCritArrow();}
  else if(direct instanceof NpcProjectile projectile){stack=projectile.weapon();}
  else if(direct==owner&&(e.getSource().is(net.minecraft.world.damagesource.DamageTypes.PLAYER_ATTACK)||e.getSource().is(net.minecraft.world.damagesource.DamageTypes.MOB_ATTACK))){stack=owner.getMainHandItem();if(owner instanceof Player p){var c=CRITS.remove(p);crit=c!=null&&c.tick==p.level().getGameTime()&&c.target.equals(e.getEntity().getUUID())&&c.critical;}}
  else return;
  if(stack==null||stack.isEmpty())return;hit(owner,e.getEntity(),id(stack),crit);
 }
 static void hit(LivingEntity owner,LivingEntity target,String id,boolean critical){if(id.isEmpty())return;var spec=Gear.CATALOG.stream().filter(s->s.id().equals(id)).findFirst().orElse(null);if(spec==null)return;
  if(spec.pack().equals("Carnivoret")&&!id.endsWith("_scythe"))target.addEffect(new MobEffectInstance(MobEffects.POISON,40,0),owner);
  if(spec.pack().equals("Ruby"))target.addEffect(new MobEffectInstance(BLEEDING,40,0),owner);
  if(spec.pack().equals("Dark Queen"))target.addEffect(new MobEffectInstance(MobEffects.WITHER,40,0),owner);
  if(spec.pack().equals("Purple World"))target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,40,0),owner);
  if(critical&&owner instanceof Player p&&(id.endsWith("_scythe")||spec.pack().equals("Valentines")||id.equals("toffy_master_sword"))&&acquire(p,"healing",30)){boolean master=id.equals("toffy_master_sword");p.heal(p.getMaxHealth()*(master?.2f:.1f));p.addEffect(new MobEffectInstance(MobEffects.REGENERATION,master?200:100,0));}
 }
 private static void shield(LivingShieldBlockEvent e){if(e.isCanceled()||!e.getBlocked()||e.getBlockedDamage()<=0||!(e.getEntity() instanceof Player p)||p.level().isClientSide()||!id(p.getUseItem()).equals("toffy_hylian_shield"))return;if(acquire(p,"shield",30)){p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,200,0));p.addEffect(new MobEffectInstance(MobEffects.REGENERATION,200,0));}}
 static String description(String id){String skill=id.endsWith("_staff")?(id.startsWith("carnivoret")?"Eco del Warden: 10 de daño, alcance 24 bloques; ":id.startsWith("valentines")?"Aliento de dragón: 10 de daño, radio 3 bloques; ":"Calavera del Wither: 10 de daño; ")+"clic derecho, espera 30 s. ":"";if(id.equals("toffy_hylian_shield"))return "Al bloquear: resistencia I y regeneración I durante 10 s; espera 30 s.";if(id.equals("toffy_master_sword"))return "Crítico: cura 20% de vida máxima y regeneración I 10 s; espera 30 s.";if(id.endsWith("_scythe"))return "14 de daño. Crítico: cura 10% y regeneración I 5 s; espera 30 s.";if(id.startsWith("valentines"))return skill+"Crítico: cura 10% y regeneración I 5 s; espera 30 s.";if(id.startsWith("carnivoret"))return skill+"Al dañar: veneno I durante 2 s.";if(id.startsWith("ruby"))return skill+"Al dañar: sangrado durante 2 s (1 de daño por segundo).";if(id.startsWith("dark_queen"))return skill+"Al dañar: Wither I durante 2 s.";if(id.startsWith("purpleworld"))return skill+"Al dañar: debilidad I durante 2 s.";return "";}
 private GearAbilities(){}
}
