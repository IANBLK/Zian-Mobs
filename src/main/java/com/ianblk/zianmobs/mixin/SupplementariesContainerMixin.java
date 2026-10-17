package com.ianblk.zianmobs.mixin;
import com.ianblk.zianmobs.CompanionEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Pseudo
@Mixin(targets="net.mehvahdjukaar.supplementaries.common.misc.mob_container.MobContainer",remap=false)
public abstract class SupplementariesContainerMixin {
 @Inject(method="captureEntity",at=@At("HEAD"),cancellable=true,remap=false,require=0)
 private void zianmobs$denyStoredCompanions(Entity entity,ItemStack bucket,CallbackInfoReturnable<Boolean> result){if(entity instanceof CompanionEntity)result.setReturnValue(false);}
}
