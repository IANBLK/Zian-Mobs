package com.ianblk.zianmobs.client;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
public final class MobsScreens{
 public static void accept(String json){var data=JsonParser.parseString(json).getAsJsonObject();Minecraft.getInstance().setScreen(data.get("section").getAsString().equals("loot")?new LootEditorScreen(data):new MobsScreen(data));}
}
