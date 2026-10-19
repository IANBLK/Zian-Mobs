package com.ianblk.zianmobs.core;
import com.google.gson.*;
import com.ianblk.zianmobs.core.Definitions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
class ManagerCombatTransferTest {
 private final Gson gson=new Gson();
 private MobSpec mob(String id,String loot){return new MobSpec(id,"Guardián","minecraft:zombie",40,4,2,0,0,List.of(),Map.of(),loot,false);}
 private LootSpec loot(String id,String category){return new LootSpec(id,category,1,List.of(new LootEntry("{id:'minecraft:diamond',count:1}",10,1,2)),"");}
 private ZoneSpec zone(String id){var p=new Point(0,64,0,0);return new ZoneSpec(id,"minecraft:overworld",p,new Point(8,68,8,0),List.of(),List.of("guard"),2,5,60,"prize",true);}
 private JsonObject source(){return gson.toJsonTree(Map.of("schema",1,"mobs",Map.of("guard",mob("guard","prize")),"loot",Map.of("prize",loot("prize","MOB"),"chest_only",loot("chest_only","CHEST")),"zones",Map.of("arena",zone("arena")),"chests",Map.of(),"npcs",Map.of())).getAsJsonObject();}
 @Test void importsCombatWithoutChestOnlyTablesOrSourceChanges(){var before=source();var copy=before.deepCopy();var plan=ManagerCombatTransfer.plan(before,Map.of(),Map.of(),Map.of());assertEquals(copy,before);assertEquals(1,plan.addedMobs());assertEquals(Set.of("prize"),plan.loot().keySet());assertFalse(plan.zones().get("arena").enabled());assertTrue(plan.zones().get("arena").autoSpawn());assertEquals(2,plan.zones().get("arena").totalStages());assertEquals(Population.POOL,plan.zones().get("arena").stages().getFirst().population());assertEquals(60,plan.zones().get("arena").respawnSeconds());}
 @Test void collisionsRemapEveryReferenceWithoutOverwritingTarget(){var targetMob=mob("guard","");var targetLoot=loot("prize","BOSS");var targetZone=zone("arena");var plan=ManagerCombatTransfer.plan(source(),Map.of("guard",targetMob),Map.of("prize",targetLoot),Map.of("arena",targetZone));assertEquals(targetMob,plan.mobs().get("guard"));assertEquals(targetLoot,plan.loot().get("prize"));assertEquals(targetZone,plan.zones().get("arena"));String mob=plan.identifiers().get("mob:guard"),loot=plan.identifiers().get("loot:prize"),zone=plan.identifiers().get("zone:arena");assertNotEquals("guard",mob);assertEquals(loot,plan.mobs().get(mob).loot());assertEquals(List.of(mob),plan.zones().get(zone).teamForStage(1));assertEquals(loot,plan.zones().get(zone).completionLoot());}
 @Test void missingRewardsAndMissingMobsRejectTheWholePlan(){var bad=source();bad.getAsJsonObject("loot").remove("prize");assertThrows(RuntimeException.class,()->ManagerCombatTransfer.plan(bad,Map.of(),Map.of(),Map.of()));var badMob=source();badMob.getAsJsonObject("mobs").remove("guard");assertThrows(RuntimeException.class,()->ManagerCombatTransfer.plan(badMob,Map.of(),Map.of(),Map.of()));}
 @Test void unknownVersionIsNotSilentlyImported(){var bad=source();bad.addProperty("schema",99);assertThrows(IllegalArgumentException.class,()->ManagerCombatTransfer.plan(bad,Map.of(),Map.of(),Map.of()));}
 @Test void bossCannotUseScaledPopulation(){assertThrows(IllegalArgumentException.class,()->new EncounterStage(StageKind.BOSS,List.of("boss"),Population.POOL));}
 @Test void explicitAndScaledPopulationPersistThroughJson(){var z=zone("arena").withStages(List.of(new EncounterStage(StageKind.WAVE,List.of("guard"),Population.POOL)));assertEquals(z,gson.fromJson(gson.toJson(z),ZoneSpec.class));assertEquals(Population.POOL,z.stages().getFirst().population());}
}
