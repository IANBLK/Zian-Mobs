package com.ianblk.zianmobs.core;
import com.ianblk.zianmobs.core.Definitions.*;
import com.ianblk.zianmobs.core.EncounterLedger.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class ZoneRefreshTest {
 @TempDir Path dir;
 @Test void editingCooldownKeepsCompletionTimeAndAppliesImmediately()throws Exception{var j=new EncounterLedger(dir.resolve("runs.json"));var run=new Run(UUID.randomUUID(),"crypt",1,Phase.COMPLETE,700000,List.of(),Set.of(),2,1);j.put(run);j.updateWait("crypt",5,5,600,60);assertEquals(160000,j.get("crypt").readyAt());j.updateWait("crypt",5,5,60,120);assertEquals(220000,new EncounterLedger(dir.resolve("runs.json")).get("crypt").readyAt());assertEquals(run.uuid(),j.get("crypt").uuid());}
 @Test void pauseEditsRebaseWaitingWaveButDoNotRestartActiveWave()throws Exception{var j=new EncounterLedger(dir.resolve("runs.json"));var run=new Run(UUID.randomUUID(),"crypt",1,Phase.WAITING,15000,List.of(),Set.of(),2,1);j.put(run);j.updateWait("crypt",5,10,600,60);assertEquals(20000,j.get("crypt").readyAt());var active=new Run(run.uuid(),"crypt",1,Phase.ACTIVE,0,List.of(),Set.of(),2,1);j.put(active);j.updateWait("crypt",10,20,60,120);assertEquals(active,j.get("crypt"));}
}
