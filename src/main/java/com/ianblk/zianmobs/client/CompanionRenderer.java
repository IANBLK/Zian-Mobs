package com.ianblk.zianmobs.client;
import com.ianblk.zianmobs.CompanionEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.model.GeoModel;
public final class CompanionRenderer extends GeoEntityRenderer<CompanionEntity>{
 public CompanionRenderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context){super(context,new GeoModel<>(){public ResourceLocation getModelResource(CompanionEntity pet){return ResourceLocation.fromNamespaceAndPath("zianmobs","geo/"+pet.asset()+".geo.json");}public ResourceLocation getTextureResource(CompanionEntity pet){return ResourceLocation.fromNamespaceAndPath("zianmobs","textures/entity/"+pet.asset()+".png");}public ResourceLocation getAnimationResource(CompanionEntity pet){return ResourceLocation.fromNamespaceAndPath("zianmobs","animations/"+pet.asset()+".animation.json");}});shadowRadius=.35f;addRenderLayer(new software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer<CompanionEntity>(this){@Override protected net.minecraft.client.renderer.RenderType getRenderType(CompanionEntity pet,net.minecraft.client.renderer.MultiBufferSource buffers){return hasSelectiveGlow(pet.id())?super.getRenderType(pet,buffers):null;}});}
 // AutoGlowingGeoLayer removes mask pixels from the base texture. CUBEE masks copy
 // the complete body and are not selective; keep those bodies on the normal pass.
 static boolean hasSelectiveGlow(String id){return id.equals("kit")||id.equals("clank");}
 @Override public net.minecraft.client.renderer.RenderType getRenderType(CompanionEntity pet,ResourceLocation texture,net.minecraft.client.renderer.MultiBufferSource buffers,float partial){return net.minecraft.client.renderer.RenderType.entityCutout(texture);}
}
