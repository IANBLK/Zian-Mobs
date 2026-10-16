package com.ianblk.zianmobs;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
public final class EnhancedBow extends BowItem {
 public static final int DRAW_TICKS=16;
 public EnhancedBow(Properties properties){super(properties);}
 @Override public void releaseUsing(ItemStack stack,Level world,LivingEntity entity,int timeLeft){int used=Math.max(0,getUseDuration(stack,entity)-timeLeft);super.releaseUsing(stack,world,entity,getUseDuration(stack,entity)-Math.min(72000,used*20/DRAW_TICKS));}
 @Override public AbstractArrow customArrow(AbstractArrow arrow,ItemStack ammo,ItemStack weapon){arrow.setBaseDamage(arrow.getBaseDamage()+2);return arrow;}
}
