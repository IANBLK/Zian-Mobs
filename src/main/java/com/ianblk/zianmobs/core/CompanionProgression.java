package com.ianblk.zianmobs.core;

/** Ten existing levels reach absolute defensive caps; offense keeps its original curve. */
public final class CompanionProgression {
 public static final double MAX_HEALTH=100, MAX_ARMOR=75;
 private CompanionProgression(){}
 private static double interpolate(double base,int level,double cap){
  if(!Double.isFinite(base)||base<0||level<0||level>10)throw new IllegalArgumentException("Progreso inválido");
  double start=Math.min(base,cap);
  return Math.min(cap,start+(cap-start)*level/10.0);
 }
 public static double health(double base,int level){return interpolate(base,level,MAX_HEALTH);}
 public static double armor(double base,int level){return interpolate(base,level,MAX_ARMOR);}
}
