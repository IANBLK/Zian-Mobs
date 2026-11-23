package com.ianblk.zianmobs.core;
/** Staff balance (decision D7); Minecraft-free so it can be tested. */
public final class StaffBalance {
 public static final int COOLDOWN_SECONDS=25;
 public static final int MELEE_DAMAGE=5;
 public static int abilityDamage(String kind){return switch(kind){case "SONIC"->10;case "WITHER","DRAGON"->8;default->throw new IllegalArgumentException("Unknown staff kind: "+kind);};}
 public static int effectSeconds(String kind){return switch(kind){case "SONIC"->0;case "WITHER"->5;case "DRAGON"->4;default->throw new IllegalArgumentException("Unknown staff kind: "+kind);};}
 private StaffBalance(){}
}
