package com.ianblk.zianmobs;

import com.ianblk.zianmobs.core.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.nbt.CompoundTag;
import java.util.*;

/** Manager's local text displays, owned by Mobs; no chunk tickets or saved duplicate entities. */
public final class ZoneHolograms {
 public static final String TAG="ZianMobsCountdown";
 private static final class Label extends Display.TextDisplay {
  Label(net.minecraft.world.level.Level level){super(EntityType.TEXT_DISPLAY,level);}
  void text(CompoundTag tag){super.readAdditionalSaveData(tag);}
  @Override public boolean shouldBeSaved(){return false;}
 }
 private final Map<String,Label> displays=new HashMap<>();
 public boolean owns(UUID uuid){return displays.values().stream().anyMatch(e->e.getUUID().equals(uuid));}
 public void clear(){displays.values().forEach(Entity::discard);displays.clear();}
 public void update(MinecraftServer server,Zones.Config config,EncounterLedger ledger,long now,boolean enabled){
  var active=new HashSet<String>();
  for(var zone:config.zones().values()){
   if(zone.first()==null||zone.second()==null)continue;
   var level=server.getLevel(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,net.minecraft.resources.ResourceLocation.parse(zone.dimension())));if(level==null)continue;
   double x=zone.center()!=null?zone.center().x()+.5:(zone.first().x()+zone.second().x()+1)/2;
   double z=zone.center()!=null?zone.center().z()+.5:(zone.first().z()+zone.second().z()+1)/2;
   double y=(zone.center()!=null?zone.center().y():Math.min(zone.first().y(),zone.second().y()))+3;
   var block=BlockPos.containing(x,y,z);if(!level.getChunkSource().hasChunk(block.getX()>>4,block.getZ()>>4))continue;
   active.add(zone.id());var display=displays.get(zone.id());boolean fresh=display==null||display.isRemoved()||display.level()!=level;
   if(fresh){if(display!=null)display.discard();display=new Label(level);display.setInvulnerable(true);display.setNoGravity(true);displays.put(zone.id(),display);}
   String text=zone.name()+"\n"+ZoneStatus.text(zone,ledger.get(zone.id()),now,enabled);
   var tag=new CompoundTag();tag.putString("text",AtomicJson.GSON.toJson(Map.of("text",text,"color","gold")));tag.putString("billboard","center");tag.putInt("line_width",260);tag.putString("alignment","center");tag.putFloat("view_range",1);var brightness=new CompoundTag();brightness.putInt("block",15);brightness.putInt("sky",15);tag.put("brightness",brightness);tag.putBoolean("shadow",true);tag.putBoolean("see_through",false);
   display.text(tag);display.moveTo(x,y,z,0,0);display.getPersistentData().putBoolean(TAG,true);
   if(fresh&&!level.addFreshEntity(display)){displays.remove(zone.id());display.discard();}
  }
  displays.entrySet().removeIf(e->{if(active.contains(e.getKey()))return false;e.getValue().discard();return true;});
 }
}
