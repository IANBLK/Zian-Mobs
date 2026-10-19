package com.ianblk.zianmobs;
import com.ianblk.zianmobs.core.Definitions.*;
import java.util.*;
import java.util.function.Predicate;

/** Explicit removals requested after beta.2; IDs stay here only for safe world migration. */
public final class ContentRetirement {
 public static final Set<String> ITEMS=Set.of(
  "barrel_knight_helmet","barrel_knight_chestplate","carnivoret_weapon_set_chest","carnivoret_weapon_set_key",
  "valentines_animated_weapon_and_tool_set_v1_chest","kraber_sniper","toffy_triforce",
  "valentines_animated_weapon_and_tool_set_v1_wing","valentines_animated_weapon_and_tool_set_v1_quiver",
  "valentines_animated_weapon_and_tool_set_v1_quiver_self","valentines_animated_weapon_and_tool_set_v1_grenade",
  "valentines_animated_weapon_and_tool_set_v1_key","valentines_animated_weapon_and_tool_set_v1_wing_self",
  "armored_grub_spawn_egg","goofy_goblin_spawn_egg",
  "carnivoret_weapon_set_chestplate",
  "carnivoret_weapon_set_leggings",
  "carnivoret_weapon_set_boots",
  "carnivoret_weapon_set_hat",
  "valentines_animated_weapon_and_tool_set_v1_helmet",
  "valentines_animated_weapon_and_tool_set_v1_helmet1",
  "valentines_animated_weapon_and_tool_set_v1_chestplate",
  "valentines_animated_weapon_and_tool_set_v1_leggings",
  "valentines_animated_weapon_and_tool_set_v1_boots",
  "toffy_link_hat",
  "toffy_link_tunic",
  "toffy_link_pants",
  "toffy_link_boots");
 public static final Set<String> ENTITIES=Set.of("zianmobs:goofy_goblin","zianmobs:armored_grub");
 public static boolean itemId(String id){return id.startsWith("zianmobs:")&&ITEMS.contains(id.substring(9));}
 public static String serializedItemId(String snbt){try{return net.minecraft.nbt.TagParser.parseTag(snbt).getString("id");}catch(Exception e){return "";}}
 public static boolean itemTag(String snbt){return itemId(serializedItemId(snbt));}
 public record Migration(Zones.Config config,Set<String> affectedZones){}
 public static Migration migrate(Zones.Config before,Predicate<String> retired){
  var mobs=new LinkedHashMap<>(before.mobs());var loot=new LinkedHashMap<>(before.loot());var zones=new LinkedHashMap<>(before.zones());var affectedMobs=new HashSet<String>();
  mobs.entrySet().removeIf(e->{boolean remove=ENTITIES.contains(e.getValue().entity());if(remove)affectedMobs.add(e.getKey());return remove;});
  for(var old:before.loot().values()){
   var entries=old.entries().stream().filter(e->!retired.test(e.item())).toList();
   if(entries.isEmpty()&&old.table().isEmpty())loot.remove(old.id());
   else if(!entries.equals(old.entries()))loot.put(old.id(),new LootSpec(old.id(),old.category(),old.rolls(),entries,old.table(),old.independent()));
  }
  for(var old:List.copyOf(mobs.values())){
   var equipment=new LinkedHashMap<>(old.equipment());equipment.values().removeIf(retired);String reward=loot.containsKey(old.loot())?old.loot():"";
   if(!equipment.equals(old.equipment())||!reward.equals(old.loot()))affectedMobs.add(old.id());
   mobs.put(old.id(),new MobSpec(old.id(),old.name(),old.entity(),old.health(),old.damage(),old.armor(),old.toughness(),old.knockback(),old.effects(),equipment,reward,old.boss()));
  }
  var affected=new HashSet<String>();
  for(var old:before.zones().values()){
   var team=old.team().stream().filter(mobs::containsKey).toList();String reward=loot.containsKey(old.completionLoot())?old.completionLoot():"";
   boolean changed=old.referencedMobs().stream().anyMatch(affectedMobs::contains)||!reward.equals(old.completionLoot())||old.referencedMobs().stream().map(before.mobs()::get).anyMatch(m->!Objects.equals(before.loot().get(m.loot()),loot.get(m.loot())))||!Objects.equals(before.loot().get(old.completionLoot()),loot.get(old.completionLoot()));
   if(changed)affected.add(old.id());
   zones.put(old.id(),new ZoneSpec(old.id(),old.dimension(),old.first(),old.second(),old.points(),team,old.waves(),old.pauseSeconds(),old.respawnSeconds(),reward,old.enabled()&&!changed,old.center(),old.radiusX(),old.radiusZ(),old.above(),old.below(),old.name(),mobs.containsKey(old.boss())?old.boss():"",old.waveTeams().stream().map(row->row.stream().filter(mobs::containsKey).toList()).toList(),old.stages()==null?null:old.stages().stream().map(stage->new java.util.AbstractMap.SimpleEntry<>(stage,stage.team().stream().filter(mobs::containsKey).toList())).filter(stage->!stage.getValue().isEmpty()).map(stage->new EncounterStage(stage.getKey().kind(),stage.getValue(),stage.getKey().population())).toList(),old.autoSpawn()));
  }
  return new Migration(new Zones.Config(before.schema(),mobs,loot,zones),Set.copyOf(affected));
 }
 private ContentRetirement(){}
}
