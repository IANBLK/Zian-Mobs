package com.ianblk.zianmobs.core;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.ianblk.zianmobs.core.Definitions.*;
import com.ianblk.zianmobs.core.EncounterLedger.*;
import java.util.*;

class ZoneStatusTest {
 private final Point p=new Point(0,64,0,0);
 private ZoneSpec zone(boolean enabled){return new ZoneSpec("test","minecraft:overworld",p,new Point(8,68,8,0),List.of(p),List.of("guard"),3,5,60,"",enabled);}
 private Run run(Phase phase,long ready){return new Run(UUID.randomUUID(),"test",2,phase,ready,List.of(new Spawn(UUID.randomUUID(),"guard",p,true),new Spawn(UUID.randomUUID(),"guard",p,false)),Set.of());}
 @Test void roundsUpWithoutNegativeCountdowns(){assertEquals("0 s",ZoneStatus.countdown(-1));assertEquals("1 s",ZoneStatus.countdown(1));assertEquals("1 min 1 s",ZoneStatus.countdown(60001));assertEquals("1 h 1 min",ZoneStatus.countdown(3660000));}
 @Test void usesSavedDeadlineInsteadOfStartingAnotherCooldown(){var r=run(Phase.COMPLETE,61000);assertEquals("Zona lista en 1 min 0 s",ZoneStatus.text(zone(true),r,1000,true));assertEquals("Zona lista en 1 s",ZoneStatus.text(zone(true),r,60999,true));assertTrue(ZoneStatus.text(zone(true),r,61000,true).startsWith("Zona lista ·"));}
 @Test void distinguishesActiveAndNextWave(){assertEquals("Oleada 2 de 3 · quedan 1 criaturas",ZoneStatus.text(zone(true),run(Phase.ACTIVE,0),1000,true));assertEquals("Oleada 3 de 3 en 5 s",ZoneStatus.text(zone(true),run(Phase.WAITING,6000),1000,true));}
 @Test void doesNotAdvertisePausedOrReviewZonesAsReady(){assertEquals("Zona desactivada",ZoneStatus.text(zone(false),null,0,true));assertEquals("Apariciones pausadas",ZoneStatus.text(zone(true),null,0,false));assertTrue(ZoneStatus.text(zone(true),run(Phase.REVIEW,0),0,true).contains("revisión"));}
}
