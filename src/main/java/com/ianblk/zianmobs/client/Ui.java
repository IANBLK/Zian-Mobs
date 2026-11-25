package com.ianblk.zianmobs.client;

import com.google.gson.JsonParser;
import com.ianblk.zianmobs.core.UiKeys;
import com.ianblk.zianmobs.core.UiTemplates;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;

/** Texto fijo de pantalla: traduce por la clave ui.zianmobs.&lt;hash&gt; y, si falta, usa el español del código. También traduce los textos en español que manda el servidor. */
final class Ui {
 static String ui(String es,Object... args){
  var text=Language.getInstance().getOrDefault(KEYS.computeIfAbsent(es,UiKeys::key),es);
  if(args.length==0)return text;
  try{return String.format(locale(),text,args);}catch(java.util.IllegalFormatException error){return String.format(locale(),es,args);}
 }
 private static final java.util.Map<String,String> KEYS=new java.util.concurrent.ConcurrentHashMap<>();
 private static Locale locale(){return Locale.forLanguageTag(Minecraft.getInstance().getLanguageManager().getSelected().replace('_','-'));}

 /** Texto del servidor: se traduce entero o, si es "a · b", cada tramo; lo que no es del mod (nombres, números) queda igual. */
 static String text(String text){
  if(text==null||text.isEmpty())return "";
  String lang=Minecraft.getInstance().getLanguageManager().getSelected();
  synchronized(CACHE){if(!lang.equals(cacheLanguage)||CACHE.size()>2000){CACHE.clear();cacheLanguage=lang;}var done=CACHE.get(text);if(done!=null)return done;}
  String result=translate(text);
  synchronized(CACHE){CACHE.put(text,result);}
  return result;
 }
 /** "Criatura · nombre del admin": se traduce el primer tramo y el resto queda tal cual. */
 static String title(String text){
  if(text==null)return "";
  String whole=render(text);if(whole!=null)return whole;
  int cut=text.indexOf(" · ");
  return cut<0?text:text(text.substring(0,cut))+text.substring(cut);
 }
 /** "nombre del admin · estado": el primer tramo queda tal cual y se traduce el resto. */
 static String name(String text){
  if(text==null)return "";
  int cut=text.indexOf(" · ");
  return cut<0?text:text.substring(0,cut+3)+text(text.substring(cut+3));
 }
 /** Etiqueta que puede ser dato del admin ("Ninguno" o el nombre de un loot): solo se traduce si es exactamente un texto fijo del mod. */
 static String fixed(String text){
  if(text==null)return "";
  var key=templates().exactKey(text);
  return key==null?text:Language.getInstance().getOrDefault(key,text);
 }

 /** Opción de una lista del servidor: los materiales de mejora llevan el nombre del objeto en español y se muestran con el del idioma elegido; el resto solo se traduce si es un texto fijo. */
 static String choice(com.google.gson.JsonObject row){
  String name=row.get("name").getAsString(),id=row.get("id").getAsString();
  templates();String es=spanish.get("item.zianmobs."+id);
  if(es!=null&&name.startsWith(es))return Language.getInstance().getOrDefault("item.zianmobs."+id,es)+text(name.substring(es.length()));
  String pet=spanish.get("entity.zianmobs.companion_"+id);
  if(pet!=null&&name.equals(pet))return Language.getInstance().getOrDefault("entity.zianmobs.companion_"+id,pet);
  return fixed(name);
 }
 /** Título de una pantalla de compañero "Nombre · tipo": el tipo se muestra con el nombre del idioma elegido (el apodo del jugador no aparece en estos títulos). */
 static String petTitle(String title,String id){
  String done=title(title);templates();String es=spanish.get("entity.zianmobs.companion_"+id);
  return es!=null&&done.endsWith(" · "+es)?done.substring(0,done.length()-es.length())+Language.getInstance().getOrDefault("entity.zianmobs.companion_"+id,es):done;
 }
 /** Si el texto no cabe, recorta el principio y conserva el final ("… · tienes 8"). */
 static String fit(net.minecraft.client.gui.Font font,String text,int width){
  if(font.width(text)<=width)return text;
  int cut=text.lastIndexOf(" · ");String tail=cut<0?"":text.substring(cut);
  return font.plainSubstrByWidth(text,Math.max(10,width-font.width(tail)-font.width("…")))+"…"+tail;
 }

 private static final java.util.Map<String,String> CACHE=new HashMap<>();
 private static String cacheLanguage="";
 private static UiTemplates templates;
 private static final java.util.Map<String,String> spanish=new HashMap<>();
 private static synchronized UiTemplates templates(){
  if(templates==null){var map=new HashMap<String,String>();
   try(var in=Ui.class.getResourceAsStream("/assets/zianmobs/lang/es_es.json")){
    JsonParser.parseString(new String(in.readAllBytes(),StandardCharsets.UTF_8)).getAsJsonObject().entrySet().forEach(e->{map.put(e.getKey(),e.getValue().getAsString());if(e.getKey().startsWith("item.zianmobs.shadow_")||e.getKey().startsWith("entity.zianmobs.companion_"))spanish.put(e.getKey(),e.getValue().getAsString());});
   }catch(Exception ignored){}
   templates=new UiTemplates(map);}
  return templates;
 }
 private static String translate(String text){
  String whole=render(text);if(whole!=null)return whole;
  if(!text.contains(" · "))return text;
  var out=new StringBuilder();boolean first=true;
  for(String part:text.split(" · ",-1)){if(!first)out.append(" · ");first=false;String done=render(part);if(done==null)done=sentences(part);out.append(done==null?part:done);}
  return out.toString();
 }
 private static String sentences(String part){
  if(!part.contains(". "))return null;
  var out=new StringBuilder();boolean any=false;
  for(String s:part.split("(?<=[.]) ")){String done=render(s);any|=done!=null;if(out.length()>0)out.append(" ");out.append(done==null?s:done);}
  return any?out.toString():null;
 }
 private static String render(String text){
  var hit=templates().match(text);
  if(hit==null)return null;
  var args=hit.args().stream().map(a->(Object)text(a)).toArray();
  String pattern=Language.getInstance().getOrDefault(hit.key(),hit.es());
  try{return args.length==0?pattern:String.format(locale(),pattern,args);}catch(java.util.IllegalFormatException error){return text;}
 }
 private Ui(){}
}
