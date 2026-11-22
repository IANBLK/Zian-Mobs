package com.ianblk.zianmobs.permission;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
public final class MobsPermissions {
 private MobsPermissions(){}
 public static boolean allows(ServerPlayer p,String action){return allows(p.createCommandSourceStack(),action);}
 public static boolean allows(CommandSourceStack source,String action){
  if(source.getPlayer()==null)return source.hasPermission(2);
  return decide(action,node->LuckPermsLookup.lookup(source.getPlayer(),node),()->source.hasPermission(2));
 }
 interface Lookup{LuckPermsLookup.Decision get(String node)throws Exception;}
 static boolean decide(String action,Lookup lookup,java.util.function.BooleanSupplier op){
  try{
   var specific=lookup.get("zianmobs."+action);
   if(specific==LuckPermsLookup.Decision.FALSE)return false;
   if(action.startsWith("admin.")){
    var parent=lookup.get("zianmobs.admin");
    if(parent==LuckPermsLookup.Decision.FALSE)return false;
    if(specific==LuckPermsLookup.Decision.TRUE||parent==LuckPermsLookup.Decision.TRUE)return true;
   }else if(specific==LuckPermsLookup.Decision.TRUE)return true;
   return publicAction(action)||op.getAsBoolean();
  }catch(Exception|LinkageError unavailable){return false;}
 }
 public static boolean publicAction(String action){return action.equals("companions.open")||action.equals("loot");}
}
