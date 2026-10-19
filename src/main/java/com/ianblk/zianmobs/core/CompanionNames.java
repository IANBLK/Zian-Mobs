package com.ianblk.zianmobs.core;
import java.util.*;

/** Cosmetic preferences, separate from ownership, upgrades and lifecycle state. */
public record CompanionNames(int schema,Map<UUID,Map<String,String>> owners){
 public CompanionNames{
  if(schema!=1||owners==null||owners.size()>100000)throw new IllegalArgumentException("Nombres de compañeros inválidos");
  var copy=new LinkedHashMap<UUID,Map<String,String>>();
  owners.forEach((owner,rows)->{Objects.requireNonNull(owner);if(rows==null||rows.size()>64)throw new IllegalArgumentException("Demasiados nombres");var names=new LinkedHashMap<String,String>();rows.forEach((id,name)->{Definitions.id(id);String valid=normalize(name);if(!valid.isEmpty())names.put(id,valid);});if(!names.isEmpty())copy.put(owner,Map.copyOf(names));});owners=Map.copyOf(copy);
 }
 public static CompanionNames empty(){return new CompanionNames(1,Map.of());}
 public String name(UUID owner,String id){return owners.getOrDefault(owner,Map.of()).getOrDefault(id,"");}
 public CompanionNames rename(UUID owner,String id,String raw){Definitions.id(id);String value=normalize(raw);var all=new LinkedHashMap<>(owners);var names=new LinkedHashMap<>(all.getOrDefault(owner,Map.of()));if(value.isEmpty())names.remove(id);else names.put(id,value);if(names.isEmpty())all.remove(owner);else all.put(owner,names);return new CompanionNames(1,all);}
 public static String normalize(String raw){
  if(raw==null)throw new IllegalArgumentException("Escribe un nombre");
  if(raw.codePoints().anyMatch(c->Character.isISOControl(c)||Character.getType(c)==Character.FORMAT||Character.getType(c)==Character.SURROGATE||Character.getType(c)==Character.LINE_SEPARATOR||Character.getType(c)==Character.PARAGRAPH_SEPARATOR||c=='§'))throw new IllegalArgumentException("El nombre no admite colores, saltos de línea ni caracteres de control");
  String name=raw.strip();if(name.codePointCount(0,name.length())>32)throw new IllegalArgumentException("El nombre admite hasta 32 caracteres");return name;
 }
}
