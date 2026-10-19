package com.ianblk.zianmobs;

import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import java.nio.file.*;
import java.util.*;

/** Explicitly opt-in integration receipt. Refuses every world except the isolated smoke world. */
final class YouerUxSmoke {
 private MinecraftServer server;private int ticks;private final List<String> passed=new ArrayList<>();
 YouerUxSmoke(){if(!Boolean.getBoolean("zian.ux.smoke"))return;NeoForge.EVENT_BUS.addListener((ServerStartedEvent e)->server=e.getServer());NeoForge.EVENT_BUS.addListener(this::tick);}
 private void check(boolean condition,String label){if(!condition)throw new IllegalStateException(label);passed.add(label);}
 private boolean javaPermission(String type,CommandSourceStack source,String node,boolean admin)throws Exception{return (Boolean)Class.forName(type).getMethod("allows",CommandSourceStack.class,String.class,boolean.class).invoke(null,source,node,admin);}
 private boolean gui(ServerPlayer p,String node)throws Exception{return (Boolean)Class.forName("com.ianblk.ziangui.server.PermissionService").getMethod("allows",ServerPlayer.class,String.class).invoke(null,p,node);}
 private Object plugin()throws Exception{var bukkit=Class.forName("org.bukkit.Bukkit");var manager=bukkit.getMethod("getPluginManager").invoke(null);return Class.forName("org.bukkit.plugin.PluginManager").getMethod("getPlugin",String.class).invoke(manager,"LuckPerms");}
 private Object loadUser(Object plugin,UUID id)throws Exception{var loader=plugin.getClass().getClassLoader();var api=Class.forName("net.luckperms.api.LuckPermsProvider",true,loader).getMethod("get").invoke(null);var users=Class.forName("net.luckperms.api.LuckPerms",true,loader).getMethod("getUserManager").invoke(api);var future=(java.util.concurrent.CompletableFuture<?>)Class.forName("net.luckperms.api.model.user.UserManager",true,loader).getMethod("loadUser",UUID.class).invoke(users,id);return future.get(30,java.util.concurrent.TimeUnit.SECONDS);}
 private void deny(Object plugin,Object user,String node)throws Exception{var loader=plugin.getClass().getClassLoader();var holder=Class.forName("net.luckperms.api.model.PermissionHolder",true,loader);var data=holder.getMethod("data").invoke(user);var nodeType=Class.forName("net.luckperms.api.node.Node",true,loader);var builder=nodeType.getMethod("builder",String.class).invoke(null,node);var builderType=Class.forName("net.luckperms.api.node.NodeBuilder",true,loader);builderType.getMethod("value",boolean.class).invoke(builder,false);var built=builderType.getMethod("build").invoke(builder);Class.forName("net.luckperms.api.model.data.NodeMap",true,loader).getMethod("add",nodeType).invoke(data,built);}
 private void tick(ServerTickEvent.Post event){if(server==null||event.getServer()!=server||++ticks!=160)return;var result=new LinkedHashMap<String,Object>();GameProfile profile=new GameProfile(UUID.fromString("21d7a361-c4d2-4c3b-bdae-32ffb4ad0994"),"UXJugador");
  try{
   Path root=server.getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize();
   check(root.getFileName().toString().equals("mobs-smoke-ux"),"isolated-world-guard");
   String version=(String)Class.forName("org.bukkit.Bukkit").getMethod("getVersion").invoke(null);Class.forName("com.mohistmc.youer.Youer");check(true,"real-youer-runtime");result.put("runtime",version);result.put("youerClass","com.mohistmc.youer.Youer");
   Object lp=plugin(),user=lp==null?null:loadUser(lp,profile.getId());result.put("luckPerms",lp!=null);
   var p=new ServerPlayer(server,server.overworld(),profile,net.minecraft.server.level.ClientInformation.createDefault());var publicSource=p.createCommandSourceStack().withPermission(0);var dispatcher=server.getCommands().getDispatcher();
   check(gui(p,"zian.gui.open"),"public-gui-without-op");check(gui(p,"zian.gui.menu.principal"),"public-main-menu");check(!gui(p,"zian.gui.edit"),"player-cannot-edit-gui");check(!com.ianblk.zianmobs.permission.MobsPermissions.allows(publicSource,"admin"),"player-cannot-admin-mobs");
   check(javaPermission("com.ianblk.zianmanager.permission.ManagerPermissions",publicSource,"loot",false),"public-manager-rewards");check(!javaPermission("com.ianblk.zianmanager.permission.ManagerPermissions",publicSource,"admin",true),"player-cannot-admin-manager");
   check(javaPermission("com.ianblk.zianrct.permission.RctPermissions",publicSource,"medals",false),"public-medals");check(!javaPermission("com.ianblk.zianrct.permission.RctPermissions",publicSource,"admin.reload",true),"player-cannot-admin-rct");
   var gts=Class.forName("com.zianblk.ziangts.v2.runtime.GtsPermissions");var gtsInstance=gts.getField("INSTANCE").get(null);check((Boolean)gts.getMethod("allows",CommandSourceStack.class,String.class,boolean.class).invoke(gtsInstance,publicSource,"open",false),"public-gts");check(!(Boolean)gts.getMethod("allows",CommandSourceStack.class,String.class,boolean.class).invoke(gtsInstance,publicSource,"admin.recovery",true),"player-cannot-recover-gts");
   for(String command:List.of("ZianGui","zgui","ZianGTS","gts","companions","companeros","ZianUtilities","crianza","medals","medallas","zianmanager","tiempos")){var node=dispatcher.getRoot().getChild(command);check(node!=null&&node.canUse(publicSource),"public-root-"+command);}
   check(javaPermission("com.zianblk.zianutilities.neoforge.UtilitiesPermissions",publicSource,"gacha.use",false),"public-utilities-gacha");check(!javaPermission("com.zianblk.zianutilities.neoforge.UtilitiesPermissions",publicSource,"admin.gacha",true),"player-cannot-admin-utilities");
   Object bukkitPlayer=Class.forName("com.ianblk.ziangui.server.PermissionService").getMethod("bukkitPlayer",ServerPlayer.class).invoke(null,p);
   for(String command:List.of("ziangui","zgui","ziangts","gts","companions","companeros","zianutilities","crianza","medals","medallas","zianmanager","tiempos"))check((Boolean)Class.forName("org.bukkit.permissions.Permissible").getMethod("hasPermission",String.class).invoke(bukkitPlayer,"minecraft.command."+command),"bukkit-public-wrapper-"+command);
   for(String[] path:new String[][]{{"ZianGui","edit"},{"ZianGui","reload"},{"ZianGTS","recovery"},{"zianmobs","zone"},{"zianmobs","companion"}}){var node=dispatcher.getRoot().getChild(path[0]).getChild(path[1]);check(node!=null&&!node.canUse(publicSource),"hidden-admin-"+path[0]+"-"+path[1]);}
   server.getPlayerList().op(profile);check(gui(p,"zian.gui.edit"),"operator-gui-edit");check(com.ianblk.zianmobs.permission.MobsPermissions.allows(p,"admin"),"operator-mobs-admin");
   if(lp!=null){deny(lp,user,"zian.gui.open");deny(lp,user,"zianmobs.admin");deny(lp,user,"zianmanager.admin");deny(lp,user,"zianrct.medals");deny(lp,user,"ziangts.buy");deny(lp,user,"zianutilities.gacha.use");
    check(!javaPermission("com.zianblk.zianutilities.neoforge.UtilitiesPermissions",p.createCommandSourceStack(),"gacha.use",false),"explicit-utilities-denial-wins-over-op");check(!gui(p,"zian.gui.open"),"explicit-gui-denial-wins-over-op");check(!com.ianblk.zianmobs.permission.MobsPermissions.allows(p,"admin"),"explicit-mobs-denial-wins-over-op");check(!javaPermission("com.ianblk.zianmanager.permission.ManagerPermissions",p.createCommandSourceStack(),"admin",true),"explicit-manager-denial-wins-over-op");check(!javaPermission("com.ianblk.zianrct.permission.RctPermissions",p.createCommandSourceStack(),"medals",false),"explicit-rct-denial-wins-over-op");check(!(Boolean)gts.getMethod("allows",CommandSourceStack.class,String.class,boolean.class).invoke(gtsInstance,p.createCommandSourceStack(),"buy",false),"explicit-gts-denial-wins-over-op");
   }
   server.getPlayerList().deop(profile);
   var pet=CompanionContent.PETS.get("otter").get().create(server.overworld());pet.managed=true;pet.setCompanionArmor(75);pet.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(100);pet.setHealth(100);check(pet.getArmorValue()==75,"companion-75-armor-without-global-attribute-change");var attacker=net.minecraft.world.entity.EntityType.ZOMBIE.create(server.overworld());result.put("companionHealthBefore",pet.getHealth());check(pet.hurt(server.overworld().damageSources().mobAttack(attacker),10),"companion-still-takes-damage");result.put("companionHealthAfter",pet.getHealth());attacker.discard();check(pet.getHealth()>90&&pet.getHealth()<100,"companion-armor-mitigates-without-invulnerability");pet.discard();
   var guiLayout=Class.forName("com.ianblk.ziangui.client.ButtonHitArea");check((Boolean)guiLayout.getMethod("contains",double.class,double.class,int.class,int.class,int.class,int.class).invoke(null,12d,48d,12,48,132,30),"gui-border-hit-rule");
   BossWaveSmoke.run(server,MobsNetwork.admin.zones(),passed::add);
   result.put("status","PASS");result.put("checks",passed);
  }catch(Throwable error){result.put("status","FAIL");result.put("error",error.toString());result.put("checks",passed);ZianMobs.LOGGER.error("YOUER-UX-SMOKE FAILED",error);}
  finally{try{server.getPlayerList().deop(profile);}catch(Exception ignored){}try{Files.writeString(server.getWorldPath(LevelResource.ROOT).resolve("youer-ux-result.json"),new GsonBuilder().setPrettyPrinting().create().toJson(result));}catch(Exception error){ZianMobs.LOGGER.error("Cannot write UX receipt",error);}server.halt(false);}
 }
}
