package com.ianblk.zianmobs.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Language keys (and numeric arguments) of the Titan and staff tooltips; Minecraft-free so it can be tested. */
public final class TooltipLines {
 public record Line(String key,List<Integer> args){}
 private static Line line(String key,Integer... args){return new Line(key,List.of(args));}
 public static List<Line> titan(boolean expanded,int partialTicks,int fullTicks,int partialCooldown,int fullCooldown){
  var lines=new ArrayList<>(List.of(line("tooltip.zianmobs.titan.name"),line("tooltip.zianmobs.titan.slots")));
  if(!expanded)lines.add(line("tooltip.zianmobs.shift"));
  else{lines.add(line("tooltip.zianmobs.titan.trigger"));lines.add(line("tooltip.zianmobs.titan.partial",partialTicks/20,partialCooldown));lines.add(line("tooltip.zianmobs.titan.full",fullTicks/20,fullCooldown));}
  return lines;
 }
 public static List<Line> staff(boolean expanded,String kind,int cooldownSeconds,int damage,int effectSeconds){
  var lines=new ArrayList<>(List.of(line("tooltip.zianmobs.ability"),line("tooltip.zianmobs.cooldown",cooldownSeconds)));
  lines.add(expanded?line("tooltip.zianmobs.staff."+kind.toLowerCase(Locale.ROOT)):line("tooltip.zianmobs.shift"));
  if(expanded){lines.add(line("tooltip.zianmobs.staff.damage",damage));if(effectSeconds>0)lines.add(line("tooltip.zianmobs.staff.effect."+kind.toLowerCase(Locale.ROOT),effectSeconds));}
  return lines;
 }
 private TooltipLines(){}
}
