package com.ianblk.zianmobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.*;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
public final class Mobs {
 private static final DeferredRegister<EntityType<?>> TYPES=DeferredRegister.create(Registries.ENTITY_TYPE,"zianmobs");
 public static final DeferredHolder<EntityType<?>,EntityType<CursedOne>> FOREST=type("cursed_one_forest"),ICE=type("cursed_one_ice"),CRYSTAL=type("cursed_one_crystal");
 private static DeferredHolder<EntityType<?>,EntityType<CursedOne>> type(String name){return TYPES.register(name,()->EntityType.Builder.of(CursedOne::new,MobCategory.MONSTER).sized(0.8f,2.0f).clientTrackingRange(10).build("zianmobs:"+name));}
 public static final DeferredHolder<EntityType<?>,EntityType<OniBrute>> ONI=TYPES.register("oni_brute",()->EntityType.Builder.of(OniBrute::new,MobCategory.MONSTER).sized(1.2f,2.8f).clientTrackingRange(10).build("zianmobs:oni_brute"));
 public static final DeferredHolder<EntityType<?>,EntityType<CombatNpc>> NPC=TYPES.register("combat_npc",()->EntityType.Builder.of(CombatNpc::new,MobCategory.MONSTER).sized(.6f,1.8f).clientTrackingRange(10).build("zianmobs:combat_npc"));
 public static final DeferredHolder<EntityType<?>,EntityType<NpcProjectile>> PROJECTILE=TYPES.register("npc_projectile",()->EntityType.Builder.<NpcProjectile>of(NpcProjectile::new,MobCategory.MISC).sized(.3f,.3f).clientTrackingRange(8).updateInterval(1).build("zianmobs:npc_projectile"));
 public static final DeferredHolder<EntityType<?>,EntityType<Snowbone>> SNOWBONE=TYPES.register("snowbone",()->EntityType.Builder.of(Snowbone::new,MobCategory.MONSTER).sized(.65f,2).clientTrackingRange(10).build("zianmobs:snowbone"));
 public static void register(IEventBus bus){TYPES.register(bus);bus.addListener((EntityAttributeCreationEvent e)->{for(var type:java.util.List.of(FOREST,ICE,CRYSTAL))e.put(type.get(),CursedOne.attributes().build());e.put(ONI.get(),OniBrute.attributes().build());e.put(NPC.get(),CombatNpc.attributes().build());e.put(SNOWBONE.get(),Snowbone.attributes().build());});}
}
