package com.ianblk.zianmobs.core;
import java.text.Normalizer;
import java.util.*;
public final class AutomaticIds {
 public static String create(String prefix,String name,Set<String> used){
  String clean=Normalizer.normalize(name==null?"":name,Normalizer.Form.NFD).replaceAll("\\p{M}","").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+","_").replaceAll("^_+|_+$","");
  String stem=prefix+"_"+(clean.isEmpty()?"nuevo":clean);stem=stem.substring(0,Math.min(32,stem.length())).replaceAll("_+$","");
  for(int i=1;i<=10000;i++){String suffix=i==1?"":"_"+i;String candidate=stem.substring(0,Math.min(stem.length(),32-suffix.length()))+suffix;if(!used.contains(candidate)){Definitions.id(candidate);return candidate;}}
  throw new IllegalArgumentException("No se pudo generar un identificador único");
 }
 private AutomaticIds(){}
}
