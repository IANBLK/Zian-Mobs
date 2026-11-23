package com.ianblk.zianmobs;
import com.ianblk.zianmobs.core.TooltipLines.Line;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLEnvironment;
/** Shared tooltip plumbing: Shift detection that is safe on a dedicated server, and key-to-style rendering. */
final class Tooltips {
 static boolean shift(){return FMLEnvironment.dist.isClient()&&com.ianblk.zianmobs.client.TooltipKeys.shift();}
 private static ChatFormatting color(String key){return switch(key.substring(key.lastIndexOf('.')+1)){case "name"->ChatFormatting.LIGHT_PURPLE;case "trigger","full","ability","cooldown"->ChatFormatting.GOLD;case "shift"->ChatFormatting.DARK_GRAY;default->ChatFormatting.GRAY;};}
 static void add(List<Component> lines,List<Line> keys){for(var line:keys){var text=Component.translatable(line.key(),line.args().toArray()).withStyle(color(line.key()));lines.add(line.key().endsWith(".shift")?text.withStyle(ChatFormatting.ITALIC):text);}}
 private Tooltips(){}
}
