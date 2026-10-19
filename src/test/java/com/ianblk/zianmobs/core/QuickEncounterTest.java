package com.ianblk.zianmobs.core;
import com.ianblk.zianmobs.core.Definitions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class QuickEncounterTest {
 @Test void alphaRepeatsSelectedMobWithAutomaticPopulation(){var plan=QuickEncounter.repeat(List.of("guard"),5,true,"");assertEquals(5,plan.size());assertTrue(plan.stream().allMatch(s->s.kind()==StageKind.WAVE&&s.population()==Population.POOL&&s.team().equals(List.of("guard"))));for(int base=1;base<=3;base++){assertEquals(base,ZonePopulation.target(base,1));assertEquals(base+2,ZonePopulation.target(base,2));assertEquals(8,ZonePopulation.target(base,10));}}
 @Test void optionalBossComesAfterAllWavesAndNeverScales(){var plan=QuickEncounter.repeat(List.of("guard"),16,true,"alpha");assertEquals(17,plan.size());assertEquals(StageKind.BOSS,plan.getLast().kind());assertEquals(Population.EXACT,plan.getLast().population());assertEquals(List.of("alpha"),plan.getLast().team());assertTrue(QuickEncounter.simple(plan));}
 @Test void mixedAndBossFirstPlansAreNeverFlattenedAutomatically(){var boss=new EncounterStage(StageKind.BOSS,List.of("boss"));var wave=new EncounterStage(StageKind.WAVE,List.of("guard"),Population.POOL);assertFalse(QuickEncounter.simple(List.of(boss)));assertFalse(QuickEncounter.simple(List.of(boss,wave)));assertFalse(QuickEncounter.simple(List.of(wave,boss,wave)));assertFalse(QuickEncounter.simple(List.of(wave,new EncounterStage(StageKind.WAVE,List.of("other"),Population.POOL))));}
 @Test void fixedAndPoolModesStayDistinct(){assertEquals(Population.EXACT,QuickEncounter.repeat(List.of("guard"),2,false,"").getFirst().population());assertFalse(QuickEncounter.simple(List.of(new EncounterStage(StageKind.WAVE,List.of("guard")),new EncounterStage(StageKind.WAVE,List.of("guard"),Population.POOL))));}
 @Test void invalidCountsAndMissingMobCannotActivateAnEncounter(){for(int n:new int[]{0,17})assertThrows(IllegalArgumentException.class,()->QuickEncounter.repeat(List.of("guard"),n,true,""));assertThrows(IllegalArgumentException.class,()->QuickEncounter.repeat(List.of(),3,true,"boss"));}
}
