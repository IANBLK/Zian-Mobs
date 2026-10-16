package com.ianblk.zianmobs.core;
import com.ianblk.zianmobs.core.Definitions.*;
import com.ianblk.zianmobs.core.EncounterLedger.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class AutoZoneTest {
 @TempDir Path dir;
 @Test void populationScalesByExtraPlayerAndCapsEight(){for(int base=1;base<=3;base++){assertEquals(base,ZonePopulation.target(base,1));assertEquals(base+2,ZonePopulation.target(base,2));assertEquals(8,ZonePopulation.target(base,100));}assertThrows(IllegalArgumentException.class,()->ZonePopulation.target(4,1));}
 @Test void waveScaleMetadataSurvivesDeathAndRestart()throws Exception{var path=dir.resolve("runs.json");var j=new EncounterLedger(path);var id=UUID.randomUUID();var mob=UUID.randomUUID();j.put(new Run(id,"room",1,Phase.ACTIVE,0,List.of(new Spawn(mob,"guardian",new Point(1,65,1,0),false)),Set.of(UUID.randomUUID()),1,1));j.defeated("room",id,mob,2,5,60,1000);var old=new EncounterLedger(path).get("room");assertEquals(1,old.baseMobs());assertEquals(1,old.scaledPlayers());assertEquals(Phase.WAITING,old.phase());}
}
