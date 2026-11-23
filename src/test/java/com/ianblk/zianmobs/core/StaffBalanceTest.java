package com.ianblk.zianmobs.core;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class StaffBalanceTest {
 @Test void valuesMatchDecisionD7(){assertEquals(25,StaffBalance.COOLDOWN_SECONDS);assertEquals(5,StaffBalance.MELEE_DAMAGE);assertEquals(10,StaffBalance.abilityDamage("SONIC"));assertEquals(8,StaffBalance.abilityDamage("WITHER"));assertEquals(8,StaffBalance.abilityDamage("DRAGON"));assertEquals(0,StaffBalance.effectSeconds("SONIC"));assertEquals(5,StaffBalance.effectSeconds("WITHER"));assertEquals(4,StaffBalance.effectSeconds("DRAGON"));}
 @Test void unknownKindThrows(){assertThrows(IllegalArgumentException.class,()->StaffBalance.abilityDamage("BLAZE"));assertThrows(IllegalArgumentException.class,()->StaffBalance.effectSeconds("x"));}
}
