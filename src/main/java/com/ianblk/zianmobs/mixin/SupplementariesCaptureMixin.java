package com.ianblk.zianmobs.mixin;
import com.ianblk.zianmobs.CompanionEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/** Optional: also denies Supplementaries' allow-all mode before its own blacklist bypass. */
@Pseudo
@Mixin(targets="net.mehvahdjukaar.supplementaries.common.items.AbstractMobContainerItem",remap=false)
public abstract class SupplementariesCaptureMixin {
 @Inject(method="canCatch",at=@At("HEAD"),cancellable=true,remap=false,require=0)
 private void zianmobs$denyCompanions(Entity entity,Player player,CallbackInfoReturnable<Boolean> result){if(entity instanceof CompanionEntity)result.setReturnValue(false);}
}
