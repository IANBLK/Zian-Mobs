package com.ianblk.zianmobs.client;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
/** Preserve the original arm UVs; resize only chest armor for native slim player skins. */
public final class TitanArmorFit implements IClientItemExtensions {
 private HumanoidModel<LivingEntity> slim;
 static boolean slim(LivingEntity entity){return entity instanceof AbstractClientPlayer p&&p.getSkin().model()==PlayerSkin.Model.SLIM;}
 @Override public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity,ItemStack stack,EquipmentSlot slot,HumanoidModel<?> original){if(slot!=EquipmentSlot.CHEST||!slim(entity))return original;if(slim==null)slim=new HumanoidModel<>(LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1),0),64,32).bakeRoot());return slim;}
 @Override public void setupModelAnimations(LivingEntity entity,ItemStack stack,EquipmentSlot slot,Model model,float limbSwing,float limbSwingAmount,float partialTick,float ageInTicks,float netHeadYaw,float headPitch){if(slot==EquipmentSlot.CHEST&&slim(entity)&&model==slim){slim.rightArm.xScale*=5f/6f;slim.leftArm.xScale*=5f/6f;slim.rightArm.x+=1f/3f;slim.leftArm.x-=1f/3f;}}
}
