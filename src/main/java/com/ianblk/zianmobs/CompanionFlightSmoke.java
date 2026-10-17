package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
final class CompanionFlightSmoke {
 static void check(boolean value,String message){if(!value)throw new IllegalStateException(message);}
 static void step(CompanionEntity pet,int ticks){for(int n=0;n<ticks;n++){pet.tick();check(pet.level().noCollision(pet),"Flying companion entered a block: "+pet.id());}}
 static void run(ServerPlayer original,Companions s)throws Exception {
  var level=original.serverLevel();var p=FakePlayerFactory.get(level,new com.mojang.authlib.GameProfile(UUID.fromString("92b89667-6538-43cd-8652-a0f611f0fd59"),"FlightSmoke"));p.moveTo(original.getX(),original.getY(),original.getZ(),0,0);p.setHealth(p.getMaxHealth());boolean added=!level.players().contains(p);if(added)level.players().add(p);var blocks=new LinkedHashMap<BlockPos,net.minecraft.world.level.block.state.BlockState>();
  try {
   for(String id:CompanionContent.IDS){s.grant(p.getUUID(),id,Long.MAX_VALUE);s.summon(p,id);var pet=s.activeEntity(p.getUUID());check(pet.isNoGravity()&&pet.getNavigation() instanceof FlyingPathNavigation&&pet.getMoveControl() instanceof FlyingMoveControl,"Companion still uses ground movement: "+id);check(pet.getY()>p.getY()+.8,"Companion spawned on ground: "+id);pet.moveTo(p.getX()+4,p.getY(),p.getZ());step(pet,100);check(pet.getY()>p.getY()+.35&&pet.getY()<p.getY()+3&&pet.distanceToSqr(p)<=64,"Companion did not rise and hover: "+id+" dy="+(pet.getY()-p.getY()));s.store(p.getUUID(),false);}
   s.summon(p,"cubee_fire");var pet=s.activeEntity(p.getUUID());double floor=p.getY();p.moveTo(p.getX(),floor+3,p.getZ());step(pet,100);check(pet.getY()>floor+3.3,"Companion did not follow upward into air");p.moveTo(p.getX(),floor,p.getZ());step(pet,100);check(pet.getY()<floor+2.5,"Companion did not descend with owner");s.store(p.getUUID(),false);
   // A normal two-block-high room must permit a lower safe hover target.
   var center=p.blockPosition();for(int dx=-5;dx<=5;dx++)for(int dz=-5;dz<=5;dz++){var pos=center.offset(dx,2,dz);blocks.put(pos,level.getBlockState(pos));level.setBlockAndUpdate(pos,Blocks.STONE.defaultBlockState());}
   for(String id:List.of("otter","clank","cubee_fire","cubee_swag_pillager")){s.summon(p,id);pet=s.activeEntity(p.getUUID());step(pet,60);check(pet.getBoundingBox().maxY<=center.getY()+2.001,"Companion flew through ceiling");check(!pet.causeFallDamage(30,1,p.damageSources().fall()),"Flying companion takes fall damage");s.store(p.getUUID(),false);}
   blocks.forEach((pos,state)->level.setBlockAndUpdate(pos,state));blocks.clear();
   s.summon(p,"cubee_fire");pet=s.activeEntity(p.getUUID());pet.moveTo(p.getX()+9,p.getY()+1,p.getZ());s.tetherTick(pet,p);check(pet.distanceToSqr(p)>64&&s.activeEntity(p.getUUID())==pet,"Flying companion teleported at 9 blocks");pet.moveTo(p.getX()+17,p.getY()+1,p.getZ());for(int n=0;n<20;n++){pet.tickCount++;s.tetherTick(pet,p);}check(pet.distanceToSqr(p)<=16*16,"Flying 16 block recovery failed");pet.moveTo(p.getX()+33,p.getY()+1,p.getZ());s.tetherTick(pet,p);check(s.activeEntity(p.getUUID())==null,"Flying 32 block auto store failed");
  }finally {s.store(p.getUUID(),false);blocks.forEach((pos,state)->level.setBlockAndUpdate(pos,state));if(added)level.players().remove(p);}
 }
}
