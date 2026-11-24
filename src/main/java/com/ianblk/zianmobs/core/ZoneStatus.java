package com.ianblk.zianmobs.core;

import com.ianblk.zianmobs.core.Definitions.ZoneSpec;
import com.ianblk.zianmobs.core.EncounterLedger.*;
import java.util.List;

/** Shared public wording. All countdowns use persisted deadlines, never a reset on startup. */
public final class ZoneStatus {
 public static String countdown(long millis){long seconds=millis<=0?0:1+(millis-1)/1000;return seconds>=3600?seconds/3600+" h "+seconds%3600/60+" min":seconds>=60?seconds/60+" min "+seconds%60+" s":seconds+" s";}
 /** Estado con clave de lang, español de respaldo (formato %s) y argumentos; text() lo da ya formateado en español. */
 public record Line(String key,String es,List<String> args){public String text(){return String.format(es,args.toArray());}}
 private static Line line(String key,String es,String... args){return new Line("zone.zianmobs.status."+key,es,List.of(args));}
 public static Line line(ZoneSpec zone,Run run,long now,boolean enabled){
  if(!zone.enabled())return line("disabled","Zona desactivada");
  if(!enabled)return line("paused","Apariciones pausadas");
  if(run==null)return line("ready","Zona lista · entra para comenzar");
  return switch(run.phase()){
   case ACTIVE->line("active","%s · quedan %s criaturas",zone.stageLabel(run.wave()),""+run.spawns().stream().filter(s->!s.defeated()).count());
   case WAITING->line("waiting","%s en %s",zone.stageLabel(run.wave()+1),countdown(run.readyAt()-now));
   case COMPLETE,CANCELLED->run.readyAt()>now?line("cooldown","Zona lista en %s",countdown(run.readyAt()-now)):line("ready","Zona lista · entra para comenzar");
   case REVIEW->line("review","Zona en revisión · avisa al administrador");
  };
 }
 public static String text(ZoneSpec zone,Run run,long now,boolean enabled){return line(zone,run,now,enabled).text();}
 private ZoneStatus(){}
}
