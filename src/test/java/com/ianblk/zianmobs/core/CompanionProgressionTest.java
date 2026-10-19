package com.ianblk.zianmobs.core;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CompanionProgressionTest {
 @Test void eachSpeciesKeepsBaseAndReachesExactCap(){for(double base:new double[]{24,36,45}){assertEquals(base,CompanionProgression.health(base,0));assertEquals(100,CompanionProgression.health(base,10));double last=base;for(int n=1;n<=10;n++){double next=CompanionProgression.health(base,n);assertTrue(next>last&&next<=100);last=next;}}assertEquals(6,CompanionProgression.armor(6,0));assertEquals(75,CompanionProgression.armor(6,10));}
 @Test void oldCustomValuesCannotExceedNewCaps(){assertEquals(100,CompanionProgression.health(4000,2));assertEquals(75,CompanionProgression.armor(90,9));assertThrows(IllegalArgumentException.class,()->CompanionProgression.health(Double.NaN,1));}
}
