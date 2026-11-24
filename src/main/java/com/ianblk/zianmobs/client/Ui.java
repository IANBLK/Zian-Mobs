package com.ianblk.zianmobs.client;

import com.ianblk.zianmobs.core.UiKeys;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;

/** Texto fijo de pantalla: traduce por la clave ui.zianmobs.&lt;hash&gt; y, si falta, usa el español del código. */
final class Ui {
 static String ui(String es,Object... args){
  var text=Language.getInstance().getOrDefault(KEYS.computeIfAbsent(es,UiKeys::key),es);
  if(args.length==0)return text;
  try{return String.format(locale(),text,args);}catch(java.util.IllegalFormatException error){return String.format(locale(),es,args);}
 }
 private static final java.util.Map<String,String> KEYS=new java.util.concurrent.ConcurrentHashMap<>();
 private static Locale locale(){return Locale.forLanguageTag(Minecraft.getInstance().getLanguageManager().getSelected().replace('_','-'));}
 private Ui(){}
}
