package com.ianblk.zianmobs.permission;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
public final class MobsPermissions {
 private MobsPermissions(){}
 public static boolean allows(ServerPlayer p,String action){return allows(p.createCommandSourceStack(),action);}
 public static boolean allows(CommandSourceStack source,String action){
  if(source.getPlayer()==null)return source.hasPermission(2);
  try{
   var specific=LuckPermsLookup.lookup(source.getPlayer(),"zianmobs."+action);
   if(specific==LuckPermsLookup.Decision.FALSE)return false;
   if(action.startsWith("admin.")){
    var parent=LuckPermsLookup.lookup(source.getPlayer(),"zianmobs.admin");
    if(parent==LuckPermsLookup.Decision.FALSE)return false;
    if(specific==LuckPermsLookup.Decision.TRUE||parent==LuckPermsLookup.Decision.TRUE)return true;
   }else if(specific==LuckPermsLookup.Decision.TRUE)return true;
   return publicAction(action)||source.hasPermission(2);
  }catch(Exception|LinkageError unavailable){return false;}
 }
 public static boolean publicAction(String action){return action.equals("companions.open")||action.equals("loot");}
}
