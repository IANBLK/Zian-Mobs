package com.ianblk.zianmobs;
import com.ianblk.zianmobs.core.Definitions.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import java.util.*;
/** Loaded ground only: no hazards, fluids, overlapping bodies or forced chunk loading. */
final class SafeZoneSpawn {
 private static final Set<net.minecraft.world.level.block.Block> hazards=Set.of(net.minecraft.world.level.block.Blocks.MAGMA_BLOCK,net.minecraft.world.level.block.Blocks.CACTUS,net.minecraft.world.level.block.Blocks.CAMPFIRE,net.minecraft.world.level.block.Blocks.SOUL_CAMPFIRE,net.minecraft.world.level.block.Blocks.FIRE,net.minecraft.world.level.block.Blocks.SOUL_FIRE,net.minecraft.world.level.block.Blocks.SWEET_BERRY_BUSH,net.minecraft.world.level.block.Blocks.POWDER_SNOW);
 static Point find(ServerLevel world,ZoneSpec zone,Mob mob,List<AABB> occupied){
  if(!zone.autoSpawn()){for(var point:zone.points())if(safe(world,zone,mob,point,occupied))return point;return null;}
  int minX=(int)Math.floor(Math.min(zone.first().x(),zone.second().x())),maxX=(int)Math.floor(Math.max(zone.first().x(),zone.second().x())),minZ=(int)Math.floor(Math.min(zone.first().z(),zone.second().z())),maxZ=(int)Math.floor(Math.max(zone.first().z(),zone.second().z())),minY=(int)Math.floor(Math.min(zone.first().y(),zone.second().y())),maxY=(int)Math.floor(Math.max(zone.first().y(),zone.second().y()))+1;
  int preferred=zone.center()!=null?(int)zone.center().y():minY+1;
  for(int attempt=0;attempt<160;attempt++){int x=minX+world.random.nextInt(maxX-minX+1),z=minZ+world.random.nextInt(maxZ-minZ+1);if(!world.getChunkSource().hasChunk(x>>4,z>>4))continue;for(int step=0;step<2*(maxY-minY+2);step++){int y=preferred+(step==0?0:(step%2==1?1:-1)*((step+1)/2));if(y<minY||y>maxY||y< -64||y>320||Math.abs(x+.5)>29999984||Math.abs(z+.5)>29999984)continue;var p=new Point(x+.5,y,z+.5,world.random.nextInt(360));if(safe(world,zone,mob,p,occupied))return p;}}
  return null;
 }
 static boolean validPoint(ServerLevel world,ZoneSpec zone,Mob mob,Point p){return safe(world,zone,mob,p,List.of(),false);}
 private static boolean safe(ServerLevel world,ZoneSpec zone,Mob mob,Point p,List<AABB> occupied){return safe(world,zone,mob,p,occupied,true);}
 private static boolean safe(ServerLevel world,ZoneSpec zone,Mob mob,Point p,List<AABB> occupied,boolean avoidPlayers){var pos=BlockPos.containing(p.x(),p.y(),p.z());if(!world.hasChunkAt(pos))return false;var floor=world.getBlockState(pos.below());if(!world.getFluidState(pos).isEmpty())return false;if(hazards.contains(floor.getBlock())||hazards.contains(world.getBlockState(pos).getBlock()))return false;mob.moveTo(p.x(),p.y(),p.z(),p.yaw(),0);var box=mob.getBoundingBox();if(!zone.contains(zone.dimension(),box.minX,box.minY,box.minZ)||!zone.contains(zone.dimension(),box.maxX-.0001,box.maxY-.0001,box.maxZ-.0001))return false;for(int cx=((int)Math.floor(box.minX))>>4;cx<=((int)Math.floor(box.maxX-.0001))>>4;cx++)for(int cz=((int)Math.floor(box.minZ))>>4;cz<=((int)Math.floor(box.maxZ-.0001))>>4;cz++)if(!world.getChunkSource().hasChunk(cx,cz))return false;if(!world.getBlockCollisions(mob,box.move(0,-.05,0)).iterator().hasNext())return false;return world.getBlockStates(box).noneMatch(state->!state.getFluidState().isEmpty()||hazards.contains(state.getBlock()))&&world.noCollision(mob)&&world.getWorldBorder().isWithinBounds(box)&&occupied.stream().noneMatch(b->b.inflate(.2).intersects(box))&&(!avoidPlayers||world.players().stream().noneMatch(player->player.getBoundingBox().inflate(.5).intersects(box)));}
}
