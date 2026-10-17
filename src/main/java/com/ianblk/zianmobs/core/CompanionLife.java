package com.ianblk.zianmobs.core;
/** Persistent wall-clock deadlines: storing never bypasses death or support cooldowns. */
public record CompanionLife(double health,long reviveAt,long supportAt,long attackAt){
 public CompanionLife{if(!Double.isFinite(health)||health<0||health>2000||reviveAt<0||supportAt<0||attackAt<0)throw new IllegalArgumentException("Estado de compañero inválido");}
 public CompanionLife(double health,long reviveAt,long supportAt){this(health,reviveAt,supportAt,0);}
 public boolean dead(long now){return reviveAt>now;}
 public CompanionLife stored(double maximum,boolean heal){return new CompanionLife(reviveAt>0?health:heal?maximum:Math.min(health,maximum),reviveAt,supportAt,attackAt);}
 public CompanionLife revived(double maximum,long now){return reviveAt>0&&now>=reviveAt?new CompanionLife(maximum,0,supportAt,attackAt):this;}
 public CompanionLife died(long now,int seconds){return new CompanionLife(0,Math.addExact(now,seconds*1000L),supportAt,attackAt);}
 public static boolean unlocked(long grantUntil,long now){return grantUntil>now;}
}
