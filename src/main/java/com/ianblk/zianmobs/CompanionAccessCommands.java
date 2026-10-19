package com.ianblk.zianmobs;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.Component;
/** Administrative access management only: ordinary companion collection stays public. */
final class CompanionAccessCommands {
 static void register(CommandDispatcher<CommandSourceStack> dispatcher){
  var root=Commands.literal("zianmobs").requires(s->true).then(Commands.literal("companion").requires(source->com.ianblk.zianmobs.permission.MobsPermissions.allows(source,"admin.companions"))
   .then(Commands.literal("grant").then(player().then(pet().executes(c->apply(c,"grant",0)).then(Commands.argument("seconds",IntegerArgumentType.integer(0,31536000)).executes(c->apply(c,"grant",IntegerArgumentType.getInteger(c,"seconds")))))))
   .then(Commands.literal("revoke").then(player().then(pet().executes(c->apply(c,"revoke",0)))))
   .then(Commands.literal("status").then(player().then(pet().executes(c->apply(c,"status",0))))));
  dispatcher.register(root);
 }
 private static RequiredArgumentBuilder<CommandSourceStack,String> player(){return Commands.argument("player",StringArgumentType.word()).suggests((c,b)->SharedSuggestionProvider.suggest(c.getSource().getServer().getPlayerNames(),b));}
 private static RequiredArgumentBuilder<CommandSourceStack,String> pet(){return Commands.argument("pet",StringArgumentType.word()).suggests((c,b)->SharedSuggestionProvider.suggest(CompanionContent.IDS,b));}
 static UUID target(CommandSourceStack source,String name){var online=source.getServer().getPlayerList().getPlayerByName(name);if(online!=null)return online.getUUID();try{return UUID.fromString(name);}catch(IllegalArgumentException ex){throw new IllegalArgumentException("Jugador no conectado. Usa su UUID para gestionar un acceso sin conexión.");}}
 private static int apply(CommandContext<CommandSourceStack> context,String action,int seconds){var source=context.getSource();try{
  if(!com.ianblk.zianmobs.permission.MobsPermissions.allows(source,"admin.companions"))throw new IllegalArgumentException("Solo administradores pueden gestionar accesos");
  var service=Companions.current;if(service==null||!service.ready||service.server!=source.getServer())throw new IllegalArgumentException("Compañeros no disponibles");String id=StringArgumentType.getString(context,"pet");service.config(id);String name=StringArgumentType.getString(context,"player");UUID owner=target(source,name);
  if(action.equals("grant")){if(seconds!=0&&seconds<10)throw new IllegalArgumentException("Duración: 0 permanente, o al menos 10 segundos");service.grant(owner,id,seconds==0?Long.MAX_VALUE:Math.addExact(service.now(),seconds*1000L));}
  else if(action.equals("revoke"))service.revoke(owner,id);
  long until=service.owner(owner).grants().getOrDefault(id,0L);boolean allowed=service.config(id).enabled()&&com.ianblk.zianmobs.core.CompanionLife.unlocked(until,service.now());String message="✦ Vínculos » "+name+" · "+CompanionContent.name(id)+" · "+(action.equals("revoke")?"acceso retirado":allowed?(until==Long.MAX_VALUE?"acceso permanente":"acceso por "+Math.max(0,(until-service.now()+999)/1000)+" s") :"sin acceso activo");
  source.sendSuccess(()->Component.literal(message),false);if(action.equals("status"))source.sendSuccess(()->Component.literal(service.upgrades(owner,id).summary()+" · Revive en "+Math.max(0,(service.life(owner,id).reviveAt()-service.now()+999)/1000)+" s"),false);
  else {var online=source.getServer().getPlayerList().getPlayer(owner);if(online!=null){String personal=action.equals("revoke")?"Acceso a "+CompanionContent.name(id)+" retirado. Tus mejoras se conservan.":CompanionContent.name(id)+(seconds==0?" tiene acceso permanente.":" estará contigo durante "+com.ianblk.zianmobs.core.ZoneStatus.countdown(seconds*1000L)+".");online.sendSystemMessage(Component.literal("✦ Compañeros » "+personal).withStyle(net.minecraft.ChatFormatting.GOLD));}}
  return 1;
 }catch(RuntimeException ex){source.sendFailure(Component.literal(ex.getMessage()==null?"No se pudo gestionar el acceso":ex.getMessage()));return 0;}}
}
