package com.ianblk.zianmobs.mixin;
import com.ianblk.zianmobs.WingsTotem;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
/** Expand only the totem identity check; consume, effects, cancellation and bypass remain vanilla. */
@Mixin(LivingEntity.class)
public abstract class WingDeathProtectionMixin {
 @WrapOperation(method="checkTotemDeathProtection",at=@At(value="INVOKE",target="Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"),require=1)
 private boolean zianmobs$wingTotem(ItemStack stack,Item expected,Operation<Boolean> original){return original.call(stack,expected)||expected==Items.TOTEM_OF_UNDYING&&(WingsTotem.isWing(stack)||com.ianblk.zianmobs.TitanAbilities.isGauntlet(stack)&&stack==((LivingEntity)(Object)this).getOffhandItem());}
}
