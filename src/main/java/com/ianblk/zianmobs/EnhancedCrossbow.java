package com.ianblk.zianmobs;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.projectile.AbstractArrow;
public final class EnhancedCrossbow extends CrossbowItem {
 public EnhancedCrossbow(Properties properties){super(properties);}
 @Override public AbstractArrow customArrow(AbstractArrow arrow,ItemStack ammo,ItemStack weapon){arrow.setBaseDamage(arrow.getBaseDamage()+2);return arrow;}
}
