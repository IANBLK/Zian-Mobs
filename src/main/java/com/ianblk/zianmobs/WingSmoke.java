package com.ianblk.zianmobs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.effect.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;
import java.util.function.Consumer;
/** Exercises the vanilla lethal-damage path, not a simulated rescue helper. */
final class WingSmoke {
 private static void require(boolean b,String s){if(!b)throw new IllegalStateException(s);}
 private static net.minecraft.world.entity.animal.Cow target(ServerPlayer p){var z=EntityType.COW.create(p.serverLevel());z.moveTo(p.getX()+16,p.getY(),p.getZ()+16,0,0);z.setNoAi(true);p.serverLevel().addFreshEntity(z);return z;}
 static void run(ServerPlayer p){
  var wing=Gear.ITEMS.get(WingsTotem.ID).get();require(wing instanceof WingsTotem.WingItem&&wing.getDefaultInstance().getMaxStackSize()==1,"Wing is not a non-stackable totem");
  var z=target(p);z.setItemSlot(EquipmentSlot.OFFHAND,wing.getDefaultInstance());z.hurt(z.damageSources().generic(),1000);require(z.isAlive()&&z.getHealth()==1&&z.getOffhandItem().isEmpty(),"Offhand wing did not rescue or consume exactly one item");require(z.hasEffect(MobEffects.REGENERATION)&&z.hasEffect(MobEffects.ABSORPTION)&&z.hasEffect(MobEffects.FIRE_RESISTANCE),"Vanilla totem effects missing");z.discard();
  z=target(p);z.setItemSlot(EquipmentSlot.MAINHAND,wing.getDefaultInstance());z.setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(Items.TOTEM_OF_UNDYING));z.hurt(z.damageSources().generic(),1000);require(z.isAlive()&&z.getMainHandItem().isEmpty()&&z.getOffhandItem().is(Items.TOTEM_OF_UNDYING),"Mainhand wing or vanilla priority failed");z.discard();
  z=target(p);z.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.TOTEM_OF_UNDYING));z.setItemSlot(EquipmentSlot.OFFHAND,wing.getDefaultInstance());z.hurt(z.damageSources().generic(),1000);require(z.isAlive()&&z.getMainHandItem().isEmpty()&&WingsTotem.isWing(z.getOffhandItem()),"Vanilla mainhand totem no longer has priority");z.discard();
  z=target(p);z.setItemSlot(EquipmentSlot.OFFHAND,wing.getDefaultInstance());z.hurt(z.damageSources().genericKill(),1000);require(!z.isAlive()&&WingsTotem.isWing(z.getOffhandItem()),"Wing bypassed vanilla kill damage exceptions");z.discard();
  z=target(p);var cancelled=z;z.setItemSlot(EquipmentSlot.OFFHAND,wing.getDefaultInstance());Consumer<LivingUseTotemEvent> deny=e->{if(e.getEntity()==cancelled)e.setCanceled(true);};NeoForge.EVENT_BUS.addListener(deny);try{z.hurt(z.damageSources().generic(),1000);require(!z.isAlive()&&WingsTotem.isWing(z.getOffhandItem()),"Wing ignored totem cancellation");}finally{NeoForge.EVENT_BUS.unregister(deny);z.discard();}
  var originalMain=p.getMainHandItem();var originalOff=p.getOffhandItem();p.removeEffect(MobEffects.NIGHT_VISION);
  p.setItemSlot(EquipmentSlot.OFFHAND,wing.getDefaultInstance());WingsTotem.tick(p);require(p.hasEffect(MobEffects.NIGHT_VISION)&&p.getEffect(MobEffects.NIGHT_VISION).getDuration()>200,"Offhand night vision missing or flickering");p.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);WingsTotem.tick(p);require(!p.hasEffect(MobEffects.NIGHT_VISION),"Own night vision persisted after removing wing");
  p.setItemSlot(EquipmentSlot.MAINHAND,wing.getDefaultInstance());WingsTotem.tick(p);require(!p.hasEffect(MobEffects.NIGHT_VISION),"Mainhand incorrectly gives night vision");
  p.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,2000));p.setItemSlot(EquipmentSlot.OFFHAND,wing.getDefaultInstance());WingsTotem.tick(p);p.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);WingsTotem.tick(p);require(p.getEffect(MobEffects.NIGHT_VISION).getDuration()==2000,"Existing potion night vision was changed");p.removeEffect(MobEffects.NIGHT_VISION);
  p.setItemSlot(EquipmentSlot.OFFHAND,wing.getDefaultInstance());WingsTotem.tick(p);p.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,3000));p.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);WingsTotem.tick(p);require(p.getEffect(MobEffects.NIGHT_VISION).getDuration()==3000,"Potion added while holding wing was removed");p.removeEffect(MobEffects.NIGHT_VISION);p.setItemSlot(EquipmentSlot.MAINHAND,originalMain);p.setItemSlot(EquipmentSlot.OFFHAND,originalOff);
  for(var spec:Gear.CATALOG)require(!(Gear.ITEMS.get(spec.id()).get() instanceof ArmorItem),"A mod armor piece remains registered");
 }
 private WingSmoke(){}
}
