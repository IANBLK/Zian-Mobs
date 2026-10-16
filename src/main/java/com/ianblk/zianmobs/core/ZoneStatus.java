package com.ianblk.zianmobs.core;

import com.ianblk.zianmobs.core.Definitions.ZoneSpec;
import com.ianblk.zianmobs.core.EncounterLedger.*;

/** Shared public wording. All countdowns use persisted deadlines, never a reset on startup. */
public final class ZoneStatus {
 public static String countdown(long millis){long seconds=millis<=0?0:1+(millis-1)/1000;return seconds>=3600?seconds/3600+" h "+seconds%3600/60+" min":seconds>=60?seconds/60+" min "+seconds%60+" s":seconds+" s";}
 public static String text(ZoneSpec zone,Run run,long now,boolean enabled){
  if(!zone.enabled())return "Zona desactivada";
  if(!enabled)return "Apariciones pausadas";
  if(run==null)return "Zona lista · entra para comenzar";
  return switch(run.phase()){
   case ACTIVE->"Oleada "+run.wave()+" de "+zone.waves()+" · quedan "+run.spawns().stream().filter(s->!s.defeated()).count()+" criaturas";
   case WAITING->"Oleada "+(run.wave()+1)+" de "+zone.waves()+" en "+countdown(run.readyAt()-now);
   case COMPLETE,CANCELLED->run.readyAt()>now?"Zona lista en "+countdown(run.readyAt()-now):"Zona lista · entra para comenzar";
   case REVIEW->"Zona en revisión · avisa al administrador";
  };
 }
 private ZoneStatus(){}
}
