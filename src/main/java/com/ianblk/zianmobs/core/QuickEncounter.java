package com.ianblk.zianmobs.core;
import com.ianblk.zianmobs.core.Definitions.*;
import java.util.*;

/** Simple authoring produces the same ordered plan as the advanced editor. */
public final class QuickEncounter {
 public static List<EncounterStage> waves(String mob,int count,boolean scalable){
  if(count<1||count>16)throw new IllegalArgumentException("Elige entre 1 y 16 oleadas");
  return Collections.nCopies(count,new EncounterStage(StageKind.WAVE,List.of(mob),scalable?Population.POOL:Population.EXACT));
 }
 public static List<EncounterStage> boss(String mob){return List.of(new EncounterStage(StageKind.BOSS,List.of(mob)));}
 public static List<EncounterStage> repeat(List<String> team,int count,boolean scalable,String boss){
  if(count<1||count>16)throw new IllegalArgumentException("Elige entre 1 y 16 oleadas");
  if(team.isEmpty())throw new IllegalArgumentException("Elige el mob de las oleadas en Básico");
  var result=new ArrayList<>(Collections.nCopies(count,new EncounterStage(StageKind.WAVE,team,scalable?Population.POOL:Population.EXACT)));
  if(!boss.isEmpty())result.add(new EncounterStage(StageKind.BOSS,List.of(boss)));
  return List.copyOf(result);
 }
 public static boolean simple(List<EncounterStage> plan){
  if(plan.isEmpty())return true;
  int waves=plan.size()-(plan.getLast().kind()==StageKind.BOSS?1:0);
  if(waves<1||waves>16)return false;
  var first=plan.getFirst();
  return first.kind()==StageKind.WAVE&&plan.subList(0,waves).stream().allMatch(first::equals);
 }
 private QuickEncounter(){}
}
