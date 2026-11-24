package com.ianblk.zianmobs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Display;
import net.minecraft.nbt.CompoundTag;
import com.ianblk.zianmobs.core.EncounterLedger;
import net.minecraft.world.level.storage.LevelResource;

/** Native display lifecycle against the persisted encounter, in the opt-in test world. */
final class ZonePresentationSmoke {
 static void check(MinecraftServer server,Zones zones,String expected)throws Exception{
  var saved=new EncounterLedger(server.getWorldPath(LevelResource.ROOT).resolve("data/zianmobs/encounters.json"));
  zones.holograms.update(server,zones.config,saved,System.currentTimeMillis(),true);
  int count=0;for(var entity:server.overworld().getAllEntities())if(!entity.isRemoved()&&entity instanceof Display.TextDisplay&&entity.getPersistentData().getBoolean(ZoneHolograms.TAG)){
   var text=rendered(entity.saveWithoutId(new CompoundTag()).getString("text"));if(text.contains("smoke_zone")){count++;if(!text.contains(expected))throw new IllegalStateException("Unexpected zone label: "+text);if(entity.shouldBeSaved())throw new IllegalStateException("Temporary label would persist into chunks");}
  }
  if(count!=1)throw new IllegalStateException("Zone label missing or duplicated: "+count);
 }
 /* El cartel viaja como componente traducible; se reconstruye su español de respaldo para comparar el mismo texto de siempre. */
 private static String rendered(String json){
  var root=com.google.gson.JsonParser.parseString(json).getAsJsonObject();var out=new StringBuilder(root.get("text").getAsString());
  for(var element:root.getAsJsonArray("extra")){var part=element.getAsJsonObject();if(!part.get("translate").getAsString().startsWith("zone.zianmobs.status."))throw new IllegalStateException("Unexpected zone label key: "+json);out.append(fallback(part,json));}
  return out.toString();
 }
 private static String fallback(com.google.gson.JsonObject part,String json){
  if(!part.get("translate").getAsString().startsWith("zone.zianmobs."))throw new IllegalStateException("Unexpected zone label key: "+json);
  var args=new java.util.ArrayList<String>();for(var arg:part.getAsJsonArray("with"))args.add(arg.isJsonObject()?fallback(arg.getAsJsonObject(),json):arg.getAsString());
  return String.format(part.get("fallback").getAsString(),args.toArray());
 }
 private ZonePresentationSmoke(){}
}
