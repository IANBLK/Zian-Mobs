package com.ianblk.zianmobs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
/** Bounded, loaded-space aerial positions. Water is crossed by seeking free air above it. */
final class CompanionFlightTargets {
 static Vec3 find(CompanionEntity pet,ServerPlayer owner,boolean follow){double yaw=Math.toRadians(owner.getYRot());double[] heights={1.15,.65,.15,1.65,-.35,2.15,3.15,4.15,5.15,6.15,8.15,10.15,12.15,14.15};var lead=follow?owner.getDeltaMovement().multiply(3,1,3):Vec3.ZERO;if(lead.lengthSqr()>1)lead=lead.normalize();double side=(pet.getUUID().getLeastSignificantBits()&1)==0?0:Math.PI;double[] radii=follow?new double[]{1.8,2.5}:new double[]{1.8,2.5,3.5};for(double height:heights)for(double radius:radii)for(double angle:new double[]{side,side+Math.PI,side+Math.PI/2,side-Math.PI/2}){var at=new Vec3(owner.getX()+Math.cos(yaw+angle)*radius+lead.x,owner.getY()+height+(follow?Math.sin((pet.tickCount+pet.getId())*.07)*.05:0),owner.getZ()+Math.sin(yaw+angle)*radius+lead.z);if(at.distanceToSqr(owner.position())<=16*16&&safe(pet,at))return at;}return null;}
 static boolean safe(CompanionEntity pet,Vec3 at){var level=pet.level();var box=pet.getBoundingBox().move(at.subtract(pet.position()));if(box.minY<level.getMinBuildHeight()||box.maxY>level.getMaxBuildHeight())return false;var min=BlockPos.containing(box.minX,box.minY,box.minZ);var max=BlockPos.containing(box.maxX,box.maxY,box.maxZ);return level.hasChunkAt(min)&&level.hasChunkAt(max)&&level.getWorldBorder().isWithinBounds(min)&&level.getWorldBorder().isWithinBounds(max)&&level.noCollision(pet,box)&&!level.containsAnyLiquid(box);}
}
