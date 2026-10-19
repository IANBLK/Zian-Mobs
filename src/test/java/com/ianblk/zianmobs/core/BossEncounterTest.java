package com.ianblk.zianmobs.core;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import com.ianblk.zianmobs.core.Definitions.*;
import com.ianblk.zianmobs.core.EncounterLedger.*;
class BossEncounterTest {
 @TempDir Path temp;
 @Test void finalBossAndWavesMustAllFinishBeforePersistentCooldown()throws Exception{
  var p=new Point(0,64,0,0);
  var z=new ZoneSpec("arena","minecraft:overworld",p,p,List.of(p),List.of("minion"),2,5,60,"",false).plan("boss",List.of(List.of("archer","archer")));
  assertEquals(List.of("archer","archer"),z.teamForStage(1));assertEquals(List.of("minion"),z.teamForStage(2));assertEquals(List.of("boss"),z.teamForStage(3));assertEquals(3,z.totalStages());
  var file=temp.resolve("runs.json");var ledger=new EncounterLedger(file);var runId=UUID.randomUUID();var first=UUID.randomUUID();var second=UUID.randomUUID();
  ledger.put(new Run(runId,"arena",1,Phase.ACTIVE,0,List.of(new Spawn(first,"archer",p,false),new Spawn(second,"archer",p,false)),Set.of()));
  ledger.defeated("arena",runId,first,z.totalStages(),5,60,1000);assertEquals(Phase.ACTIVE,ledger.get("arena").phase());
  ledger.defeated("arena",runId,second,z.totalStages(),5,60,2000);assertEquals(Phase.WAITING,ledger.get("arena").phase());
  var boss=UUID.randomUUID();ledger.put(new Run(runId,"arena",3,Phase.ACTIVE,0,List.of(new Spawn(boss,"boss",p,false)),Set.of()));
  assertEquals(Phase.ACTIVE,new EncounterLedger(file).get("arena").phase());
  ledger.defeated("arena",runId,boss,z.totalStages(),5,60,9000);assertEquals(Phase.COMPLETE,ledger.get("arena").phase());assertEquals(69000,new EncounterLedger(file).get("arena").readyAt());
  assertFalse(ledger.defeated("arena",runId,boss,z.totalStages(),5,60,10000));assertEquals(69000,ledger.get("arena").readyAt());
 }
 @Test void oldZoneRetainsOriginalRepeatedWaves(){var p=new Point(0,64,0,0);var z=new ZoneSpec("old","minecraft:overworld",p,p,List.of(),List.of("minion"),16,5,60,"",false);assertEquals(16,z.totalStages());assertEquals(List.of("minion"),z.teamForStage(16));}
 @Test void allSeventeenStagesAndReferencesAreValid(){var p=new Point(0,64,0,0);var z=new ZoneSpec("new","minecraft:overworld",p,p,List.of(),List.of("minion"),16,5,60,"",false).plan("boss",List.of(List.of("archer")));assertEquals(17,z.totalStages());assertEquals(Set.of("minion","archer","boss"),z.referencedMobs());assertThrows(IllegalArgumentException.class,()->z.teamForStage(18));}
}
