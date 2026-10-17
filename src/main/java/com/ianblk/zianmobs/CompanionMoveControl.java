package com.ianblk.zianmobs;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
/** Smoothed velocity steering, with native collision handling in the entity's travel step. */
final class CompanionMoveControl extends FlyingMoveControl {
 CompanionMoveControl(CompanionEntity pet){super(pet,35,true);}
 @Override public void tick(){mob.setNoGravity(true);mob.setZza(0);mob.setYya(0);if(operation!=Operation.MOVE_TO){mob.setDeltaMovement(mob.getDeltaMovement().scale(.72));return;}operation=Operation.WAIT;var delta=new Vec3(wantedX-mob.getX(),wantedY-mob.getY(),wantedZ-mob.getZ());double distance=delta.length();double speed=Math.min(speedModifier*mob.getAttributeValue(Attributes.FLYING_SPEED)*.65,distance*.3);var desired=distance<.01?Vec3.ZERO:delta.scale(speed/distance);mob.setDeltaMovement(mob.getDeltaMovement().lerp(desired,.2));if(delta.horizontalDistanceSqr()>.0025){float yaw=(float)(Math.atan2(delta.z,delta.x)*180/Math.PI)-90;mob.setYRot(rotlerp(mob.getYRot(),yaw,25));}float pitch=(float)Math.max(-15,Math.min(15,-Math.atan2(delta.y,Math.max(.001,delta.horizontalDistance()))*180/Math.PI));mob.setXRot(rotlerp(mob.getXRot(),pitch,5));}
}
