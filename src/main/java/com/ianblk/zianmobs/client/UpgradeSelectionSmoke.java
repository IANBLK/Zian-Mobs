package com.ianblk.zianmobs.client;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import java.nio.file.*;
/** Exercise the real selector and response router without sending packets from the isolated client. */
final class UpgradeSelectionSmoke {
 static void check(boolean value,String message){if(!value)throw new IllegalStateException(message);}
 static Button button(net.minecraft.client.gui.screens.Screen screen,java.util.function.Predicate<String> label){return screen.children().stream().filter(c->c instanceof Button b&&label.test(b.getMessage().getString())).map(c->(Button)c).findFirst().orElseThrow();}
 static void run(Minecraft mc,String tab)throws Exception {
  var path=mc.gameDirectory.toPath().resolve("fixtures");var before=JsonParser.parseString(Files.readString(path.resolve("companion_upgrades.json"))).getAsJsonObject();var response=JsonParser.parseString(Files.readString(path.resolve("upgrade_response.json"))).getAsJsonObject();var screen=new MobsScreen(before);mc.setScreen(screen);
  button(screen,s->s.startsWith("Diamante del Vínculo")).onPress();check(mc.screen instanceof ChoiceScreen,"Material selector did not open");var search=mc.screen.children().stream().filter(c->c instanceof EditBox).map(c->(EditBox)c).findFirst().orElseThrow();search.setValue("Huevo");button(mc.screen,s->s.startsWith("Huevo")&&s.contains("Despierto")).onPress();check(mc.screen==screen,"Selector lost parent form");button(screen,s->s.equals(tab)).onPress();
  String material="shadow_infested_egg_1a";var carried=screen.preserveUpgradeContext(response.deepCopy());check(carried.getAsJsonObject("values").get("material").getAsString().equals(material),"Chosen material reset before response");check(carried.get("selectedGroup").getAsString().equals(tab),"Chosen tab reset");check(carried.getAsJsonObject("values").get("health").equals(response.getAsJsonObject("values").get("health")),"Preserved context replaced refreshed server statistics");check(carried.get("token").equals(response.get("token")),"Old token was retained");
  MobsScreens.accept(response.toString());check(mc.screen instanceof MobsScreen,"Response did not reopen upgrade form");screen=(MobsScreen)mc.screen;var state=screen.preserveUpgradeContext(response.deepCopy());check(state.getAsJsonObject("values").get("material").getAsString().equals(material),"Response router reset material");check(!button(screen,s->s.equals(tab)).active,"Response router reset selected tab");
  response.addProperty("notice","Esta mejora ya alcanzó el máximo");MobsScreens.accept(response.toString());screen=(MobsScreen)mc.screen;check(screen.preserveUpgradeContext(response.deepCopy()).getAsJsonObject("values").get("material").getAsString().equals(material),"Rejected upgrade reset material");screen.onClose();check(!(mc.screen instanceof ManagerConfirmScreen),"Material selection incorrectly reported unsaved changes");mc.setScreen(screen);
  var other=response.deepCopy();other.addProperty("id","kit");other.getAsJsonObject("values").addProperty("material","shadow_dark_geode_0a");check(screen.preserveUpgradeContext(other).getAsJsonObject("values").get("material").getAsString().equals("shadow_dark_geode_0a"),"Material leaked into another companion form");
 }
}
