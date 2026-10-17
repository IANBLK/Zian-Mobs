package com.ianblk.zianmobs.client;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
final class TitanVisualSmoke {
 static void check(BakedModel model,boolean active){var quads=new java.util.ArrayList<>(model.getQuads(null,null,RandomSource.create(0)));for(var side:Direction.values())quads.addAll(model.getQuads(null,side,RandomSource.create(0)));int gems=0,body=0;for(var quad:quads){boolean gem=quad.getSprite().contents().name().getPath().equals("item/titan_gems");for(int v=0;v<4;v++){int light=quad.getVertices()[v*net.neoforged.neoforge.client.model.IQuadTransformer.STRIDE+net.neoforged.neoforge.client.model.IQuadTransformer.UV2];if(light!=((gem&&active)?net.minecraft.client.renderer.LightTexture.FULL_BRIGHT:0))throw new IllegalStateException("Gauntlet emission escaped its active gems");}if(gem)gems++;else body++;}if(gems!=36||body==0)throw new IllegalStateException("Gauntlet gem geometry changed");}
}
