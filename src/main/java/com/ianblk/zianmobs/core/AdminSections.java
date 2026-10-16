package com.ianblk.zianmobs.core;

import java.util.Set;

/** Presentation groups only: the complete validated form is still submitted together. */
public final class AdminSections {
 public static String field(String section,String key){
  return switch(section){
   case "mob"->Set.of("id","name","entity","boss").contains(key)?"Básico":Set.of("health","damage").contains(key)?"Combate":key.equals("slot")?"Equipo":key.equals("loot")?"Premios":"Avanzado";
   case "npc"->Set.of("skin","slim","hostile").contains(key)?"Básico":Set.of("sonic","dragon","wither","blaze").contains(key)?"Habilidades":"Combate";
   case "zone"->Set.of("id","name","enabled","team").contains(key)?"Básico":Set.of("radiusX","radiusZ","above","below").contains(key)?"Tamaño":Set.of("waves","pause","respawn").contains(key)?"Oleadas":key.equals("loot")?"Premios":"Puntos";
   case "attack"->Set.of("enabled","cooldown","multiplier","radius").contains(key)?"Básico":"Avanzado";
   default->"Básico";
  };
 }
 public static String action(String section,String operation){
  if(section.equals("mob"))return switch(operation){case "appearance"->"Combate";case "equip","unequip"->"Equipo";default->"Avanzado";};
  if(section.equals("zone"))return switch(operation){case "center","preview","preview_stop"->"Tamaño";case "point_add","point_move","point_remove"->"Puntos";default->"Avanzado";};
  return "Básico";
 }
 public static String help(String section,String group){
  if(group.equals("Avanzado"))return "Opciones adicionales. Revisa las confirmaciones antes de eliminar.";
  if(group.equals("Equipo"))return "Sostén el objeto, elige una ranura y pulsa Asignar.";
  if(group.equals("Premios"))return "Selecciona una tabla creada en Recompensas.";
  if(group.equals("Puntos"))return "Colócate en cada lugar de aparición y añade un punto distinto.";
  if(group.equals("Tamaño"))return "El centro es tu referencia; los radios se miden en bloques.";
  if(group.equals("Oleadas"))return "Pausa entre oleadas y enfriamiento final: tiempos en segundos.";
  if(group.equals("Habilidades"))return "Activa los ataques especiales que quieras combinar. No dañan bloques.";
  if(group.equals("Combate"))return section.equals("npc")?"20 ticks = 1 segundo. Skin y habilidades se guardan por plantilla.":"Ajusta la dificultad antes de añadir la criatura a una zona.";
  return "Empieza aquí y continúa por las pestañas. Guardar aplica todo el formulario.";
 }
 private AdminSections(){}
}
