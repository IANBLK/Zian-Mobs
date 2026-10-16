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
   var text=entity.saveWithoutId(new CompoundTag()).getString("text");if(text.contains("smoke_zone")){count++;if(!text.contains(expected))throw new IllegalStateException("Unexpected zone label: "+text);if(entity.shouldBeSaved())throw new IllegalStateException("Temporary label would persist into chunks");}
  }
  if(count!=1)throw new IllegalStateException("Zone label missing or duplicated: "+count);
 }
 private ZonePresentationSmoke(){}
}
