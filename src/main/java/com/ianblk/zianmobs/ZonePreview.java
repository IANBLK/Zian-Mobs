package com.ianblk.zianmobs;
import java.util.*;
import com.google.gson.*;
import com.ianblk.zianmobs.core.*;
import com.ianblk.zianmobs.core.Definitions.Point;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
/** Admin-only line preview. No blocks, entities, chunk tickets or persisted preview state. */
public final class ZonePreview {
 private record View(String dimension,int expires){}
 private final Map<UUID,View> views=new HashMap<>();
 public void show(ServerPlayer player,String dimension,Point first,Point second,List<Point> spawns){show(player,dimension,first,second,spawns,null);}
 public void show(ServerPlayer player,String dimension,Point first,Point second,List<Point> spawns,Point center){
  if(!com.ianblk.zianmobs.permission.MobsPermissions.allows(player,"admin"))throw new IllegalArgumentException("Necesitas permiso de administrador");
  if(!player.serverLevel().dimension().location().toString().equals(dimension))throw new IllegalArgumentException("Debes estar en el mundo de esta zona");
  double x=(first.x()+second.x())/2,z=(first.z()+second.z())/2;if(player.distanceToSqr(x,player.getY(),z)>256*256)throw new IllegalArgumentException("Acércate a menos de 256 bloques de la zona");
  var payload=new JsonObject();var data=new JsonObject();data.addProperty("dimension",dimension);data.addProperty("id","Zona de aparición");var gson=new Gson();data.add("first",gson.toJsonTree(first));data.add("second",gson.toJsonTree(second));data.add("points",gson.toJsonTree(spawns));if(center!=null)data.add("center",gson.toJsonTree(center));payload.add("zonePreview",data);MobsNetwork.send(player,new MobsNetwork.JsonView(payload.toString()));
  views.put(player.getUUID(),new View(dimension,player.server.getTickCount()+1200));
  player.sendSystemMessage(Component.literal("Vista de zona: 60 s · contorno azul · apariciones doradas. Puedes cerrar el panel para construir.").withStyle(net.minecraft.ChatFormatting.GOLD));
 }
 public void hide(UUID player){views.remove(player);var server=net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();if(server!=null){var p=server.getPlayerList().getPlayer(player);if(p!=null)MobsNetwork.send(p,new MobsNetwork.JsonView("{\"zonePreview\":null}"));}}
 public void clear(){for(var id:List.copyOf(views.keySet()))hide(id);views.clear();}boolean active(UUID player){return views.containsKey(player);}
 public void tick(MinecraftServer server){for(var entry:List.copyOf(views.entrySet())){var p=server.getPlayerList().getPlayer(entry.getKey());var v=entry.getValue();if(p==null||!com.ianblk.zianmobs.permission.MobsPermissions.allows(p,"admin")||!p.isAlive()||server.getTickCount()>=v.expires||!p.serverLevel().dimension().location().toString().equals(v.dimension))hide(entry.getKey());}}
}
