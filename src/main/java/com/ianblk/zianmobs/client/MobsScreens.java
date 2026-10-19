package com.ianblk.zianmobs.client;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
public final class MobsScreens{
 public static void accept(String json){var data=JsonParser.parseString(json).getAsJsonObject();if(data.get("section").getAsString().equals("personal_rewards")){Minecraft.getInstance().setScreen(new PersonalRewardsScreen(data,Minecraft.getInstance().screen instanceof PersonalRewardsScreen previous?previous.parent():Minecraft.getInstance().screen));return;}if(data.get("section").getAsString().equals("companions")){if(Minecraft.getInstance().screen instanceof CompanionScreen screen)screen.accept(data);else Minecraft.getInstance().setScreen(new CompanionScreen(data));}else {if(Minecraft.getInstance().screen instanceof MobsScreen previous)data=previous.preserveUpgradeContext(data);Minecraft.getInstance().setScreen(data.get("section").getAsString().equals("loot")?new LootEditorScreen(data):new MobsScreen(data));}}
}
