package com.ianblk.zianmobs.core;
import com.ianblk.zianmobs.core.Definitions.*;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class OrderedStagesTest {
 private ZoneSpec zone(){var p=new Point(0,64,0,0);return new ZoneSpec("arena","minecraft:overworld",p,p,List.of(p),List.of("guard"),2,5,60,"",true);}
 private EncounterStage wave(String... ids){return new EncounterStage(StageKind.WAVE,List.of(ids));}
 private EncounterStage boss(){return new EncounterStage(StageKind.BOSS,List.of("boss"));}
 @Test void bossCanStartAndFollowWaves(){var z=zone().withStages(List.of(boss(),wave("guard","archer"),boss()));assertEquals(3,z.totalStages());assertTrue(z.bossStage(1));assertFalse(z.bossStage(2));assertEquals("Oleada 1 de 1",z.stageLabel(2));assertEquals(List.of("guard","archer"),z.teamForStage(2));assertEquals(Set.of("guard","archer","boss"),z.referencedMobs());}
 @Test void bossOnlyAndWaveOnlyNeedNoDummyStage(){assertEquals(1,zone().withStages(List.of(boss())).totalStages());assertTrue(zone().withStages(List.of(boss())).bossStage(1));assertEquals(1,zone().withStages(List.of(wave("guard"))).totalStages());}
 @Test void allSeventeenSlotsCanBeOrdered(){var z=zone().withStages(Collections.nCopies(17,boss()));assertEquals(17,z.totalStages());assertThrows(IllegalArgumentException.class,()->zone().withStages(Collections.nCopies(18,boss())));assertThrows(IllegalArgumentException.class,()->z.teamForStage(18));assertFalse(z.bossStage(0));}
 @Test void enabledZoneCannotLoseItsWholePlan(){assertThrows(IllegalArgumentException.class,()->zone().withStages(List.of()));}
 @Test void invalidAndOversizedTeamsAreRejected(){assertThrows(IllegalArgumentException.class,()->wave());assertThrows(IllegalArgumentException.class,()->new EncounterStage(StageKind.BOSS,List.of("a","b")));assertThrows(IllegalArgumentException.class,()->wave(Collections.nCopies(9,"guard").toArray(String[]::new)));assertThrows(IllegalArgumentException.class,()->wave("bad id"));assertThrows(IllegalArgumentException.class,()->new EncounterStage(null,List.of("guard")));}
 @Test void legacyOrderAndDuplicatesAreRetained(){var z=zone().plan("boss",List.of(List.of("archer","archer")));var stages=z.encounterStages();assertNull(z.stages());assertEquals(List.of(wave("archer","archer"),wave("guard"),boss()),stages);var converted=z.withStages(stages);for(int n=1;n<=z.totalStages();n++)assertEquals(z.teamForStage(n),converted.teamForStage(n));}
 @Test void jsonRoundTripRetainsOrderAndRejectsUnknownKind(){var gson=new Gson();var z=zone().withStages(List.of(boss(),wave("guard")));assertEquals(z,gson.fromJson(gson.toJson(z),ZoneSpec.class));assertThrows(RuntimeException.class,()->gson.fromJson("{\"kind\":\"INVALID\",\"team\":[\"guard\"]}",EncounterStage.class));}
 @Test void legacyJsonStillLoadsAndCommandsCannotSilentlyDiscardOrderedStages(){var gson=new Gson();var old=zone().plan("boss",List.of());var reloaded=gson.fromJson(gson.toJson(old),ZoneSpec.class);assertNull(reloaded.stages());assertEquals(3,reloaded.totalStages());assertThrows(IllegalArgumentException.class,()->old.withStages(old.encounterStages()).plan("",List.of()));}
}
