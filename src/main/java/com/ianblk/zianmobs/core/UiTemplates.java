package com.ianblk.zianmobs.core;

import java.util.*;
import java.util.regex.Pattern;

/** Reconoce un texto en español que mandó el servidor (con datos ya sustituidos) contra las plantillas del es_es: texto exacto o con huecos %s. Sin Minecraft para poder probarlo. */
public final class UiTemplates {
 public record Hit(String key,String es,List<String> args){}
 private record Template(String key,String es,Pattern pattern,int literal,boolean open){}
 private static final Pattern HOLE=Pattern.compile("%(?:\\d+\\$)?[-#+ 0,(]*\\d*(?:\\.\\d+)?[sdf]");
 private final Map<String,String> exact=new HashMap<>();
 private final List<Template> templates=new ArrayList<>();
 /** @param es clave de lang -> texto español; solo cuentan las claves ui.zianmobs.* y zone.zianmobs.* */
 public UiTemplates(Map<String,String> es){
  es.forEach((key,text)->{
   if(!key.startsWith("ui.zianmobs.")&&!key.startsWith("zone.zianmobs."))return;
   var m=HOLE.matcher(text);boolean m0=m.find()&&m.start()==0;m.reset();var regex=new StringBuilder("^");int last=0,literal=0;boolean holes=false;
   while(m.find()){holes=true;String part=text.substring(last,m.start());literal+=part.length();regex.append(Pattern.quote(part)).append("(.+?)");last=m.end();}
   if(!holes){exact.putIfAbsent(text,key);return;}
   String tail=text.substring(last);literal+=tail.length();regex.append(Pattern.quote(tail)).append("$");
   if(literal>0)templates.add(new Template(key,text,Pattern.compile(regex.toString(),Pattern.DOTALL),literal,m0));
  });
  templates.sort((a,b)->Integer.compare(b.literal,a.literal));
 }
 /** Solo coincidencia de texto completo sin huecos. */
 public String exactKey(String text){return exact.get(text);}
 /** Las plantillas que empiezan por un hueco ("%s en %s") solo valen cuando sus huecos son números o textos reconocidos; las demás encajan siempre. Primero busca una plantilla cuyos huecos sean números o textos reconocibles (así "Oleada 2 de 3 en 5 s" se lee como estado + cuenta atrás); si no, la primera que encaje. */
 public Hit match(String text){var hit=find(text,true);return hit!=null?hit:find(text,false);}
 private static final Pattern NUMBER=Pattern.compile("\\d[\\d.,/]*( ?(h|min|s|%))?( \\d+ ?(min|s))*");
 private Hit find(String text,boolean strict){
  String key=exact.get(text);if(key!=null)return new Hit(key,text,List.of());
  for(var t:templates){if(!strict&&t.open)continue;var m=t.pattern.matcher(text);if(!m.matches())continue;var args=new ArrayList<String>();boolean ok=true;for(int i=1;i<=m.groupCount();i++){String a=m.group(i);args.add(a);if(strict&&!NUMBER.matcher(a).matches()&&(Character.isDigit(a.charAt(0))||find(a,true)==null))ok=false;}if(ok)return new Hit(t.key,t.es,args);}
  return null;
 }
}
