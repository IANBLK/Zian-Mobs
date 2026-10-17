package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.server.level.ServerPlayer;
import com.ianblk.zianmobs.core.AtomicJson;
final class CompanionAccessCommandSmoke {
 static void check(boolean value,String message){if(!value)throw new IllegalStateException(message);}
 static void run(ServerPlayer p,Companions service)throws Exception {
  var dispatcher=p.server.getCommands().getDispatcher();var console=p.server.createCommandSourceStack();check(console.getEntity()==null&&console.hasPermission(2),"Test source is not administrative console");String id="cubee_lush",prefix="zianmobs companion ",who=p.getUUID().toString();var previousClock=service.clock;long[] now={service.now()};service.clock=()->now[0];service.store(p.getUUID(),false);var levels=service.upgrades(p.getUUID(),id);
  try {
   check(dispatcher.execute(prefix+"grant "+who+" "+id,console)==1&&service.allowed(p,id),"Console permanent grant failed");service.summon(p,id);var pet=service.activeEntity(p.getUUID());pet.hurt(p.damageSources().generic(),2);float injured=pet.getHealth();var state=service.life(p.getUUID(),id);
   check(dispatcher.execute(prefix+"revoke "+who+" "+id,console)==1&&!service.allowed(p,id)&&service.activeEntity(p.getUUID())==null,"Console revoke did not withdraw active companion");check(service.upgrades(p.getUUID(),id).equals(levels)&&service.life(p.getUUID(),id).health()==injured,"Revocation lost upgrades or healed pet");check(service.life(p.getUUID(),id).supportAt()==state.supportAt()&&service.life(p.getUUID(),id).attackAt()==state.attackAt(),"Revoke reset cooldowns");
   check(dispatcher.execute(prefix+"grant "+who+" "+id+" 30",console)==1&&service.owner(p.getUUID()).grants().get(id)==now[0]+30000,"Console timed grant failed");service.summon(p,id);now[0]+=30001;service.checkAccess(p);check(!service.allowed(p,id)&&service.activeEntity(p.getUUID())==null,"Timed access failed to expire and withdraw companion");
   check(dispatcher.execute(prefix+"grant "+who+" "+id+" 0",console)==1&&service.owner(p.getUUID()).grants().get(id)==Long.MAX_VALUE&&service.upgrades(p.getUUID(),id).equals(levels),"Restoring access lost upgrades");var saved=service.data;check(dispatcher.execute(prefix+"status "+who+" "+id,console)==1&&service.data.equals(saved),"Status changed data");
   check(dispatcher.execute(prefix+"grant "+who+" "+id+" 1",console)==0&&service.data.equals(saved),"Invalid duration changed access");check(dispatcher.execute(prefix+"grant "+who+" invalid_pet",console)==0&&service.data.equals(saved),"Invalid pet changed access");
   var guest=p.createCommandSourceStack().withPermission(0);check(!dispatcher.getRoot().getChild("zianmobs").getChild("companion").canUse(guest),"Non-OP can access administrative commands");boolean rejected=false;try{dispatcher.execute(prefix+"revoke "+who+" "+id,guest);}catch(com.mojang.brigadier.exceptions.CommandSyntaxException expected){rejected=true;}check(rejected&&service.data.equals(saved),"Non-OP command changed grant");
   var disk=AtomicJson.read(service.file,Companions.Data.class,null);check(disk.owners().get(p.getUUID()).grants().get(id)==Long.MAX_VALUE&&disk.owners().get(p.getUUID()).upgrades().get(id).equals(levels),"Console access change not persisted");
  }finally{service.store(p.getUUID(),false);service.clock=previousClock;}
 }
}
