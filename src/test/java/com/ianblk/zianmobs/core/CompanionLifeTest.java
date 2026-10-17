package com.ianblk.zianmobs.core;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CompanionLifeTest {
 @Test void storeHealsLivingPetButPreservesSupportWait(){var p=new CompanionLife(4,0,9000).stored(24,true);assertEquals(24,p.health());assertEquals(9000,p.supportAt());}
 @Test void storingCannotResurrectDeadPet(){var p=new CompanionLife(0,10000,9000).stored(24,true);assertEquals(0,p.health());assertEquals(10000,p.reviveAt());}
 @Test void deathClockSurvivesStoreAndResurrectsOnlyAtDeadline(){var p=new CompanionLife(10,0,77).died(1000,300);assertTrue(p.dead(300999));assertEquals(0,p.revived(24,300999).health());assertEquals(new CompanionLife(24,0,77),p.revived(24,301000));}
 @Test void expiredAndRevokedUnlocksAreDenied(){assertFalse(CompanionLife.unlocked(1000,1000));assertFalse(CompanionLife.unlocked(0,1000));assertTrue(CompanionLife.unlocked(1001,1000));}
 @Test void permanentUnlockPersistsAndAttackWaitSurvivesHealing(){assertTrue(CompanionLife.unlocked(Long.MAX_VALUE,1000));var state=new CompanionLife(4,0,77,999);assertEquals(999,state.stored(24,true).attackAt());assertEquals(999,state.died(1000,300).revived(24,301000).attackAt());}
 @Test void invalidStoredStateIsRejected(){assertThrows(IllegalArgumentException.class,()->new CompanionLife(Double.NaN,0,0));assertThrows(IllegalArgumentException.class,()->new CompanionLife(-1,0,0));}
}
