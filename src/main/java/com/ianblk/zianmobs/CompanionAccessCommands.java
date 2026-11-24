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
 static UUID target(CommandSourceStack source,String name){var online=source.getServer().getPlayerList().getPlayerByName(name);if(online!=null)return online.getUUID();try{return UUID.fromString(name);}catch(IllegalArgumentException ex){throw new Say("error.zianmobs.access.offline","Jugador no conectado. Usa su UUID para gestionar un acceso sin conexión.");}}
 private static int apply(CommandContext<CommandSourceStack> context,String action,int seconds){var source=context.getSource();try{
  if(!com.ianblk.zianmobs.permission.MobsPermissions.allows(source,"admin.companions"))throw new Say("error.zianmobs.access.admin_only","Solo administradores pueden gestionar accesos");
  var service=Companions.current;if(service==null||!service.ready||service.server!=source.getServer())throw new Say("error.zianmobs.companion.unavailable","Compañeros no disponibles");String id=StringArgumentType.getString(context,"pet");service.config(id);String name=StringArgumentType.getString(context,"player");UUID owner=target(source,name);
  if(action.equals("grant")){if(seconds!=0&&seconds<10)throw new Say("error.zianmobs.access.duration","Duración: 0 permanente, o al menos 10 segundos");service.grant(owner,id,seconds==0?Long.MAX_VALUE:Math.addExact(service.now(),seconds*1000L));}
  else if(action.equals("revoke"))service.revoke(owner,id);
  long until=service.owner(owner).grants().getOrDefault(id,0L);boolean allowed=service.config(id).enabled()&&com.ianblk.zianmobs.core.CompanionLife.unlocked(until,service.now());var message=Component.translatableWithFallback("message.zianmobs.prefix.bonds","✦ Vínculos » ").append(Component.literal(name+" · ")).append(Companions.petName(id)).append(" · ").append(action.equals("revoke")?Component.translatableWithFallback("message.zianmobs.access.revoked","acceso retirado"):allowed?(until==Long.MAX_VALUE?Component.translatableWithFallback("message.zianmobs.access.permanent","acceso permanente"):Component.translatableWithFallback("message.zianmobs.access.timed","acceso por %s s",Math.max(0,(until-service.now()+999)/1000))):Component.translatableWithFallback("message.zianmobs.access.none","sin acceso activo"));
  source.sendSuccess(()->message,false);if(action.equals("status"))source.sendSuccess(()->service.upgrades(owner,id).summaryText().append(Component.translatableWithFallback("message.zianmobs.companion.revive_in"," · Revive en %s s",Math.max(0,(service.life(owner,id).reviveAt()-service.now()+999)/1000))),false);
  else {var online=source.getServer().getPlayerList().getPlayer(owner);if(online!=null){Component personal=action.equals("revoke")?Component.translatableWithFallback("message.zianmobs.companion.access_revoked","Acceso a %s retirado. Tus mejoras se conservan.",Companions.petName(id)):seconds==0?Component.translatableWithFallback("message.zianmobs.companion.access_permanent","%s tiene acceso permanente.",Companions.petName(id)):Component.translatableWithFallback("message.zianmobs.companion.access_timed","%s estará contigo durante %s.",Companions.petName(id),com.ianblk.zianmobs.core.ZoneStatus.countdown(seconds*1000L));online.sendSystemMessage(Component.translatableWithFallback("message.zianmobs.prefix.companions","✦ Compañeros » ").append(personal).withStyle(net.minecraft.ChatFormatting.GOLD));}}
  return 1;
 }catch(RuntimeException ex){source.sendFailure(Say.of(ex,"message.zianmobs.access_failed","No se pudo gestionar el acceso"));return 0;}}
}
