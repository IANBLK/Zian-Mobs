package com.ianblk.zianmobs.client;

import com.ianblk.zianmobs.core.UiKeys;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;

/** Texto fijo de pantalla: traduce por la clave ui.zianmobs.&lt;hash&gt; y, si falta, usa el español del código. */
final class Ui {
 static String ui(String es,Object... args){
  var text=Language.getInstance().getOrDefault(UiKeys.key(es),es);
  return args.length==0?text:String.format(locale(),text,args);
 }
 private static Locale locale(){return Locale.forLanguageTag(Minecraft.getInstance().getLanguageManager().getSelected().replace('_','-'));}
 private Ui(){}
}
