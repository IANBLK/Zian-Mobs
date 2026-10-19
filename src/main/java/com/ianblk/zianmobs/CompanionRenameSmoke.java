package com.ianblk.zianmobs;
import com.ianblk.zianmobs.core.*;
import com.google.gson.*;
import net.minecraft.server.level.ServerPlayer;
import java.nio.file.*;
import java.util.*;

/** Called only by the opt-in disposable localhost encounter harness. */
final class CompanionRenameSmoke {
 static int run(ServerPlayer p,Path root)throws Exception{
  var s=Companions.current;String id="kit";int checks=0;
  if(Files.exists(root.resolve("companion-rename-passed.json"))){require(s.nickname(p.getUUID(),id).equals("Luna ✨"),"nickname survives server restart");checks++;}
  s.store(p.getUUID(),false);p.moveTo(72.5,300,72.5,0,0);s.grant(p.getUUID(),id,Long.MAX_VALUE);p.server.getPlayerList().deop(p.getGameProfile());
  try{
   require(!com.ianblk.zianmobs.permission.MobsPermissions.allows(p,"admin"),"ordinary player remains non-admin");checks++;
   var before=s.data;var state=s.life(p.getUUID(),id);var stats=s.effective(p.getUUID(),id);
   var view=s.snapshot(p,"companion_name",id,"");var values=view.getAsJsonObject("values");values.addProperty("name","Luna ✨");UUID token=UUID.fromString(view.get("token").getAsString());s.action(p,new MobsNetwork.PetAction(token,"save",values.toString()));
   require(s.nickname(p.getUUID(),id).equals("Luna ✨")&&s.data.equals(before)&&s.life(p.getUUID(),id).equals(state)&&s.effective(p.getUUID(),id).equals(stats),"public rename preserves ownership upgrades health and timers");checks++;
   values.addProperty("name","Replay");s.action(p,new MobsNetwork.PetAction(token,"save",values.toString()));require(s.nickname(p.getUUID(),id).equals("Luna ✨"),"consumed rename token cannot replay");checks++;
   var other=net.neoforged.neoforge.common.util.FakePlayerFactory.get(p.serverLevel(),new com.mojang.authlib.GameProfile(UUID.fromString("00000000-0000-0000-0000-000000000626"),"RenameOther"));s.revoke(other.getUUID(),id);
   boolean denied=false;try{s.rename(other,id,"Steal");}catch(IllegalArgumentException expected){denied=true;}require(denied&&s.nickname(p.getUUID(),id).equals("Luna ✨"),"locked companion cannot be renamed");checks++;
   s.grant(other.getUUID(),id,Long.MAX_VALUE);s.rename(other,id,"Sol");require(s.nickname(p.getUUID(),id).equals("Luna ✨")&&s.nickname(other.getUUID(),id).equals("Sol"),"each owner has independent name");checks++;
   var disk=AtomicJson.read(root.resolve("data/zianmobs/companion-names.json"),CompanionNames.class,null);require(disk.name(p.getUUID(),id).equals("Luna ✨")&&disk.name(other.getUUID(),id).equals("Sol"),"names persist separately");checks++;
   s.summon(p,id);var pet=s.activeEntity(p.getUUID());require(pet!=null&&pet.getCustomName().getString().equals("Luna ✨"),"summon applies saved name");checks++;
   pet.setHealth(4);UUID entity=pet.getUUID();s.rename(p,id,"Estrella");require(entity.equals(pet.getUUID())&&pet.getHealth()==4&&pet.getCustomName().getString().equals("Estrella"),"active rename does not heal or replace pet");checks++;
   s.rename(p,id,"");require(pet.getCustomName()==null&&s.displayName(p.getUUID(),id).equals(CompanionContent.name(id)),"reset restores original display name");checks++;
   s.rename(p,id,"Luna ✨");Path fixtures=root.resolve("build-stage-fixtures");Files.createDirectories(fixtures);Files.writeString(fixtures.resolve("companions-named.json"),s.snapshot(p,"companions",id,"").toString());s.store(p.getUUID(),false);Files.writeString(fixtures.resolve("companion-name.json"),s.snapshot(p,"companion_name",id,"").toString());
   var saved=Companions.readData(s.file);require(saved.schema()==4&&saved.owners().get(p.getUUID()).grants().equals(s.owner(p.getUUID()).grants()),"existing companion schema and grants remain valid");checks++;
   AtomicJson.write(root.resolve("companion-rename-passed.json"),Map.of("status","PASS","checks",checks,"version","0.3.2"));return checks;
  }finally{p.server.getPlayerList().op(p.getGameProfile());}
 }
 private static void require(boolean ok,String message){if(!ok)throw new IllegalStateException(message);}
}
