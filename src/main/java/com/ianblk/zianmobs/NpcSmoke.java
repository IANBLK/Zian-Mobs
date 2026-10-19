package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.effect.MobEffects;
import com.ianblk.zianmobs.core.AtomicJson;
/** Controlled native combat and impact tests in the isolated development world. */
final class NpcSmoke {
 private static void require(boolean ok,String why){if(!ok)throw new IllegalStateException(why);}
 private static Cow target(ServerPlayer p,double dx){var cow=EntityType.COW.create(p.serverLevel());cow.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100);cow.setHealth(100);cow.moveTo(p.getX()+dx,p.serverLevel().getMaxBuildHeight()-8,p.getZ()+16,0,0);cow.setNoAi(true);p.serverLevel().addFreshEntity(cow);return cow;}
 static void run(ServerPlayer player,Zones zones)throws Exception{
  var world=player.serverLevel();var actor=Mobs.NPC.get().create(world);actor.moveTo(player.getX()+12,world.getMaxBuildHeight()-8,player.getZ()+16,0,0);actor.setNoAi(true);world.addFreshEntity(actor);
  for(var skin:NpcProfiles.SKINS){String id="npc_"+skin.id();actor.configure(id);require(actor.skin().equals(skin.id()),"NPC skin not synchronized: "+id);require(zones.config.mobs().containsKey(id),"Default NPC template missing: "+id);var data=zones.admin.snapshot(player,"npc",id,"");var fields=data.getAsJsonObject("values");fields.addProperty("sonic",true);fields.addProperty("dragon",true);fields.addProperty("wither",true);fields.addProperty("blaze",true);zones.admin.apply(player,"npc",id,"save",fields);require(Tuning.current.npcs().get(id).skills().size()==4,"NPC skill toggles failed");}
  Tuning.start(world.getServer().getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("data/zianmobs"));require(Tuning.current.npcs().size()==7,"NPC profiles not persisted");
  var cow=target(player,16);actor.setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);require(actor.doHurtTarget(cow)&&cow.getHealth()<100,"NPC unarmed attack failed");cow.discard();
  cow=target(player,16);actor.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.IRON_SWORD));require(actor.doHurtTarget(cow)&&cow.getHealth()<100,"NPC sword attack failed");cow.discard();
  cow=target(player,16);actor.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.IRON_AXE));require(actor.doHurtTarget(cow)&&cow.getHealth()<100,"NPC axe attack failed");cow.discard();
  cow=target(player,16);actor.cast("SONIC",cow);require(cow.getHealth()<100,"Sonic attack failed");cow.discard();
  var snapshots=new LinkedHashMap<BlockPos,net.minecraft.world.level.block.state.BlockState>();BlockPos center=BlockPos.containing(actor.getX()+3,actor.getY(),actor.getZ());for(int x=-2;x<=2;x++)for(int z=-2;z<=2;z++){var block=center.offset(x,-1,z);world.setBlockAndUpdate(block,Blocks.OAK_PLANKS.defaultBlockState());snapshots.put(block,world.getBlockState(block));snapshots.put(block.above(),world.getBlockState(block.above()));}
  for(String kind:List.of("DRAGON","WITHER","BLAZE")){
   cow=target(player,16);var projectile=new NpcProjectile(Mobs.PROJECTILE.get(),actor,world,kind,6);projectile.smokeImpact(cow.position(),cow);require(cow.getHealth()<100,"Special projectile did not damage: "+kind);if(kind.equals("WITHER"))require(cow.hasEffect(MobEffects.WITHER),"Wither effect missing");if(kind.equals("BLAZE"))require(cow.isOnFire(),"Blaze did not ignite target entity");cow.discard();
   var wall=new NpcProjectile(Mobs.PROJECTILE.get(),actor,world,kind,6);wall.smokeImpact(Vec3.atCenterOf(center.below()),null);require(wall.isRemoved(),"Projectile survived impact");
  }
  for(var block:snapshots.entrySet())require(world.getBlockState(block.getKey()).equals(block.getValue()),"Special attack changed a block: "+block.getKey());
  actor.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.BOW));cow=target(player,20);actor.setTarget(cow);require(actor.hasLineOfSight(cow),"NPC smoke arena line of sight blocked");for(int tick=0;tick<64;tick++)actor.customServerAiStep();var arrows=world.getEntitiesOfClass(net.minecraft.world.entity.projectile.Arrow.class,actor.getBoundingBox().inflate(8));require(arrows.stream().anyMatch(a->a.getOwner()==actor&&a.pickup==net.minecraft.world.entity.projectile.AbstractArrow.Pickup.DISALLOWED),"NPC bow did not launch a safe arrow");arrows.forEach(Entity::discard);cow.discard();
  var saved=actor.saveWithoutId(new net.minecraft.nbt.CompoundTag());var loaded=Mobs.NPC.get().create(world);loaded.readAdditionalSaveData(saved);require(loaded.skin().equals(actor.skin()),"NPC skin lost after save/read");loaded.discard();actor.discard();
 }
 private NpcSmoke(){}
}
