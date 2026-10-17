package com.ianblk.zianmobs.client;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.platform.NativeImage;
/** Read actual uploaded atlas pixels on the render thread, not just source metadata. */
final class ShadowAnimationProbe {
 private static final Map<String,Set<Integer>> frames=new LinkedHashMap<>();
 static void sample(){var mc=Minecraft.getInstance();mc.getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).bind();int width=GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0,GL11.GL_TEXTURE_WIDTH),height=GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D,0,GL11.GL_TEXTURE_HEIGHT);try(var pixels=new NativeImage(width,height,false)){pixels.downloadTexture(0,false);for(var m:com.ianblk.zianmobs.CompanionUpgrades.CATALOG){var sprite=mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ResourceLocation.fromNamespaceAndPath("zianmobs","item/"+m.id()));int x=Math.round(sprite.getU0()*width),y=Math.round(sprite.getV0()*height),hash=1;for(int dy=0;dy<16;dy++)for(int dx=0;dx<16;dx++)hash=31*hash+pixels.getPixelRGBA(x+dx,y+dy);frames.computeIfAbsent(m.id(),k->new HashSet<>()).add(hash);}}}
 static void verify(){for(var m:com.ianblk.zianmobs.CompanionUpgrades.CATALOG){int count=frames.getOrDefault(m.id(),Set.of()).size();if(m.id().endsWith("1a")?count<2:count!=1)throw new IllegalStateException("Atlas animation/static upload mismatch: "+m.id()+" unique frames="+count);}}
}
