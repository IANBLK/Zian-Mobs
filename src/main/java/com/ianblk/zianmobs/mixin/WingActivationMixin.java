package com.ianblk.zianmobs.mixin;
import com.ianblk.zianmobs.WingsTotem;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(ClientPacketListener.class)
public abstract class WingActivationMixin {
 @WrapOperation(method="findTotem",at=@At(value="INVOKE",target="Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"),require=1)
 private static boolean zianmobs$wingAnimation(ItemStack stack,Item expected,Operation<Boolean> original){return original.call(stack,expected)||expected==Items.TOTEM_OF_UNDYING&&WingsTotem.isWing(stack);}
}
