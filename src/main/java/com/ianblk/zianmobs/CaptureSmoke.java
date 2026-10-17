package com.ianblk.zianmobs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
/** Exercises real event hooks and optionally the installed Supplementaries capture entry points. */
final class CaptureSmoke {
 static void run(ServerPlayer owner)throws Exception {
  var service=Companions.current;var hand=owner.getMainHandItem().copy();
  service.grant(owner.getUUID(),"otter",Long.MAX_VALUE);service.summon(owner,"otter");var pet=service.activeEntity(owner.getUUID());
  try{
   owner.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.STICK));
   var right=NeoForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteract(owner,InteractionHand.MAIN_HAND,pet));CompanionSmoke.check(right.isCanceled(),"Capture interaction was not stopped");
   var specific=NeoForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteractSpecific(owner,InteractionHand.MAIN_HAND,pet,Vec3.ZERO));CompanionSmoke.check(specific.isCanceled(),"Specific capture interaction was not stopped");
   var left=NeoForge.EVENT_BUS.post(new AttackEntityEvent(owner,pet));CompanionSmoke.check(left.isCanceled(),"Left click capture was not stopped");
   CompanionSmoke.check(!pet.save(new net.minecraft.nbt.CompoundTag()),"Generic entity save can package a companion");
   var clone=CompanionContent.PETS.get("otter").get().create(owner.serverLevel());clone.readAdditionalSaveData(pet.saveWithoutId(new net.minecraft.nbt.CompoundTag()));var join=NeoForge.EVENT_BUS.post(new EntityJoinLevelEvent(clone,owner.serverLevel()));CompanionSmoke.check(join.isCanceled(),"Captured companion clone can bypass refuge ownership");
   var cow=EntityType.COW.create(owner.serverLevel());var ordinary=NeoForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteract(owner,InteractionHand.MAIN_HAND,cow));CompanionSmoke.check(!ordinary.isCanceled(),"Capture guard interferes with ordinary creatures");
   if(net.neoforged.fml.ModList.get().isLoaded("supplementaries"))supplementaries(owner,cow);
  }finally{service.store(owner.getUUID(),false);owner.setItemInHand(InteractionHand.MAIN_HAND,hand);}
 }
 private static void supplementaries(ServerPlayer owner,Entity ordinary)throws Exception {
  var functional=Class.forName("net.mehvahdjukaar.supplementaries.configs.CommonConfigs$Functional");var option=functional.getField("CAGE_ALL_MOBS").get(null);var supplier=(java.util.function.Supplier<?>)option;Object previous=supplier.get();var config=Class.forName("net.mehvahdjukaar.moonlight.api.platform.configs.IConfigValue");var setter=config.getMethod("setValue",Object.class);
  try{
   setter.invoke(option,true);CompanionSmoke.check(Boolean.TRUE.equals(supplier.get()),"Could not test Supplementaries allow-all capture mode");
   var cage=net.minecraft.core.registries.BuiltInRegistries.ITEM.get(net.minecraft.resources.ResourceLocation.parse("supplementaries:cage"));var type=Class.forName("net.mehvahdjukaar.supplementaries.common.items.AbstractMobContainerItem");var canCatch=type.getDeclaredMethod("canCatch",Entity.class,Player.class);canCatch.setAccessible(true);
   CompanionSmoke.check(Boolean.TRUE.equals(canCatch.invoke(cage,ordinary,owner)),"Supplementaries capture control is not active");
   var containerType=Class.forName("net.mehvahdjukaar.supplementaries.common.misc.mob_container.MobContainer");var capture=containerType.getMethod("captureEntity",Entity.class,ItemStack.class);
   for(String id:CompanionContent.IDS){var pet=CompanionContent.PETS.get(id).get().create(owner.serverLevel());CompanionSmoke.check(Boolean.FALSE.equals(canCatch.invoke(cage,pet,owner)),"Supplementaries can catch companion "+id);var container=containerType.getConstructor(float.class,float.class,boolean.class).newInstance(1f,1f,false);CompanionSmoke.check(Boolean.FALSE.equals(capture.invoke(container,pet,ItemStack.EMPTY)),"Supplementaries direct container capture can store "+id);}
   ZianMobs.LOGGER.info("ZIAN-CAPTURE-COMPAT PASS: Supplementaries allow-all and direct container capture deny all 29 companions");
  }finally{setter.invoke(option,previous);}
 }
}
