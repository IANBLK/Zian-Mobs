package com.ianblk.zianmobs;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TuningTest{
 @Test void attacksRejectNonFiniteAndExtremeValues(){assertThrows(IllegalArgumentException.class,()->new Tuning.Attack(true,20,12,Double.NaN,1,0,0));assertThrows(IllegalArgumentException.class,()->new Tuning.Attack(true,0,12,2,1,0,0));assertThrows(IllegalArgumentException.class,()->new Tuning.Attack(true,20,12,2,1,31,0));assertDoesNotThrow(()->new Tuning.Attack(false,20,100,8,5,30,1.5));}
 @Test void itemStatsStayInSupportedRanges(){assertThrows(IllegalArgumentException.class,()->new Tuning.Stats(Double.NaN,1,1,0,0,0,1));assertThrows(IllegalArgumentException.class,()->new Tuning.Stats(1,4,0,0,0,0,1));assertThrows(IllegalArgumentException.class,()->new Tuning.Stats(1,4,100,31,0,0,1));assertDoesNotThrow(()->new Tuning.Stats(7,1.6,2031,8,3,.1,2));}
}
