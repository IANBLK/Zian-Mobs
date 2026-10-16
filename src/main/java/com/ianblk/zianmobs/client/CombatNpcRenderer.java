package com.ianblk.zianmobs.client;
import com.ianblk.zianmobs.CombatNpc;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
/** Local-skin renderer reused from Manager, with held-weapon and attack poses. */
public final class CombatNpcRenderer extends MobRenderer<CombatNpc,PlayerModel<CombatNpc>>{
 private final PlayerModel<CombatNpc> slim,classic;
 public CombatNpcRenderer(EntityRendererProvider.Context context){super(context,new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER),false),.5f);classic=model;slim=new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM),true);addLayer(new ItemInHandLayer<>(this,context.getItemInHandRenderer()));}
 @Override public void render(CombatNpc npc,float yaw,float partial,PoseStack pose,MultiBufferSource buffers,int light){model=npc.slim()?slim:classic;model.rightArmPose=npc.isUsingItem()?HumanoidModel.ArmPose.BOW_AND_ARROW:npc.getMainHandItem().isEmpty()?HumanoidModel.ArmPose.EMPTY:HumanoidModel.ArmPose.ITEM;model.leftArmPose=npc.getOffhandItem().isEmpty()?HumanoidModel.ArmPose.EMPTY:HumanoidModel.ArmPose.ITEM;super.render(npc,yaw,partial,pose,buffers,light);}
 @Override public ResourceLocation getTextureLocation(CombatNpc npc){return ResourceLocation.fromNamespaceAndPath("zianmobs","textures/entity/npcs/"+npc.skin()+".png");}
}
