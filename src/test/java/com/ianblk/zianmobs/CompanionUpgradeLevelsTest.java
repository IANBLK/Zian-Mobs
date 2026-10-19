package com.ianblk.zianmobs;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CompanionUpgradeLevelsTest {
 @Test void normalAndAwakenedItemsStillCapAtTenWithoutErasingOtherTracks(){var base=new CompanionUpgrades.Levels(9,4,10,2,3,4);var next=base.add("health",2);assertEquals(10,next.health());assertEquals(4,next.damage());assertEquals(10,next.armor());assertEquals(next,next.add("health",1));assertEquals(CompanionUpgrades.Levels.ZERO.add("health",1).add("health",1),CompanionUpgrades.Levels.ZERO.add("health",2));}
}
