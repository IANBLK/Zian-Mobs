package com.ianblk.zianmobs;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.*;
public final class SpawnEggs {
 private static final DeferredRegister.Items ITEMS=DeferredRegister.createItems("zianmobs");
 public static final java.util.List<DeferredItem<Item>> EGGS=new java.util.ArrayList<>();
 static{EGGS.add(ITEMS.register("combat_npc_spawn_egg",()->new net.neoforged.neoforge.common.DeferredSpawnEggItem(Mobs.NPC,0x123f49,0x78f2ef,new Item.Properties())));for(var entry:java.util.List.of(Mobs.FOREST,Mobs.ICE,Mobs.CRYSTAL))EGGS.add(ITEMS.register(entry.getId().getPath()+"_spawn_egg",()->new net.neoforged.neoforge.common.DeferredSpawnEggItem(entry,0x4a4237,0xb3ae85,new Item.Properties())));EGGS.add(ITEMS.register("oni_brute_spawn_egg",()->new net.neoforged.neoforge.common.DeferredSpawnEggItem(Mobs.ONI,0x873e42,0xdfc496,new Item.Properties())));}
 public static void register(IEventBus bus){ITEMS.register(bus);bus.addListener((net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent e)->{if(e.getTabKey()==CreativeModeTabs.SPAWN_EGGS)for(var egg:EGGS)e.accept(egg);});}
}
