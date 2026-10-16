package com.ianblk.zianmobs.client;
import com.google.gson.*;
import com.ianblk.zianmobs.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import java.nio.file.*;
import java.util.*;
/** Opt-in, isolated development client. No account, world, or production profile is used. */
public final class ClientSmoke {
 private static int ticks,stage=-1;private static final List<Item> items=new ArrayList<>();
 public static void register(){if(Boolean.getBoolean("zianmobs.clientSmoke"))net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(ClientSmoke::tick);}
 private static void require(boolean b,String message){if(!b)throw new IllegalStateException(message);}
 private static void tick(ClientTickEvent.Post event){var mc=Minecraft.getInstance();if(mc.getOverlay()!=null||mc.screen==null)return;try{
  if(stage<0){Class.forName("net.minecraft.client.multiplayer.ClientPacketListener");require(mc.gameDirectory.toPath().toAbsolutePath().normalize().toString().contains("mobs-client-smoke"),"Client smoke requires isolated profile");Gear.ITEMS.values().forEach(i->items.add(i.get()));SpawnEggs.EGGS.forEach(i->items.add(i.get()));for(var item:items){var id=net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);var model=mc.getModelManager().getModel(ModelResourceLocation.inventory(id));require(model!=mc.getModelManager().getMissingModel(),"Missing item model "+id);{var quads=new ArrayList<>(model.getQuads(null,null,RandomSource.create(0)));for(var side:Direction.values())quads.addAll(model.getQuads(null,side,RandomSource.create(0)));require(!quads.isEmpty(),"Empty item geometry "+id);for(var q:quads)require(!q.getSprite().contents().name().getPath().equals("missingno"),"Missing atlas sprite "+id);}}stage=0;ticks=0;mc.setScreen(new Catalog(0));}
  if(++ticks<35)return;ticks=0;if(stage>=3)require((int)mc.getWindow().getGuiScale()==2+(stage-3)%3,"Requested GUI scale was clamped by the window");
  Screenshot.grab(mc.gameDirectory,"smoke-"+stage+".png",mc.getMainRenderTarget(),c->ZianMobs.LOGGER.info("Client smoke screenshot: {}",c.getString()));
  stage++;if(stage<30)openStage(mc);else{Files.writeString(mc.gameDirectory.toPath().resolve("client-smoke-result.txt"),"PASS: "+items.size()+" baked inventory models; actual server dashboard, mob/zone/NPC tabs and read-only gear catalog plus scythe, staff, Master Sword and Hylian Shield ability cards rendered at GUI scales 2, 3, 4; tab edits retained and unsaved-close confirmation verified.\n");ZianMobs.LOGGER.info("ZIAN-MOBS-CLIENT-SMOKE PASS");mc.stop();}

 }catch(Throwable e){ZianMobs.LOGGER.error("ZIAN-MOBS-CLIENT-SMOKE FAIL",e);try{Files.writeString(mc.gameDirectory.toPath().resolve("client-smoke-result.txt"),"FAIL: "+e);}catch(Exception ignored){}mc.stop();}}
 private static void press(MobsScreen screen,String text){var button=screen.children().stream().filter(c->c instanceof net.minecraft.client.gui.components.Button b&&b.getMessage().getString().equals(text)).map(c->(net.minecraft.client.gui.components.Button)c).findFirst().orElseThrow(()->new IllegalStateException("Missing tab "+text));if(button.active)button.onPress();}
 private static void openStage(Minecraft mc)throws Exception{
  if(stage<3){mc.setScreen(new Catalog(stage));return;}
  mc.options.guiScale().set(2+(stage-3)%3);mc.resizeDisplay();String name=stage<6?"home":stage<9?"mob":stage<12?"zone":stage<15?"npc":"item";
  if(stage>=18)name=new String[]{"carnivoret_weapon_set_scythe","valentines_animated_weapon_and_tool_set_v1_staff","toffy_master_sword","toffy_hylian_shield"}[(stage-18)/3];
  var fixture=mc.gameDirectory.toPath().resolve("fixtures/"+name+".json");require(Files.isRegularFile(fixture),"Run server smoke first to generate real admin fixtures");var screen=new MobsScreen(JsonParser.parseString(Files.readString(fixture)).getAsJsonObject());mc.setScreen(screen);
  if(stage==6){var box=screen.children().stream().filter(c->c instanceof net.minecraft.client.gui.components.EditBox e&&e.getValue().equals("Guardián abisal")).map(c->(net.minecraft.client.gui.components.EditBox)c).findFirst().orElseThrow();box.setValue("Guardián abisal · prueba");press(screen,"Equipo");press(screen,"Básico");require(screen.children().stream().anyMatch(c->c instanceof net.minecraft.client.gui.components.EditBox e&&e.getValue().equals("Guardián abisal · prueba")),"Tab navigation lost the edited name");screen.onClose();require(mc.screen instanceof ManagerConfirmScreen,"Unsaved edit closed without confirmation");mc.setScreen(screen);}
  if(stage>=6&&stage<15){String[] tab=stage<9?new String[]{"Básico","Equipo","Avanzado"}:stage<12?new String[]{"Tamaño","Puntos","Avanzado"}:new String[]{"Básico","Combate","Habilidades"};press(screen,tab[(stage-3)%3]);}
 }
 private static final class Catalog extends ManagerThemedScreen{
  private final int page;Catalog(int p){super(Component.literal("Zian Mobs · Catálogo "+(p+1)+" / 3"));page=p;}
  @Override protected void init(){fitViewport(600,360);}
  @Override protected void renderCanvas(GuiGraphics g,int mx,int my,float dt){int left=(width-600)/2,top=(height-360)/2;panel(g,left,top,600,360);for(int i=page*28;i<Math.min(items.size(),(page+1)*28);i++){int n=i%28,x=left+8+(n%7)*84,y=top+42+(n/7)*78;var stack=items.get(i).getDefaultInstance();g.pose().pushPose();g.pose().translate(x+22,y,0);g.pose().scale(2,2,1);g.renderItem(stack,0,0);g.pose().popPose();g.drawString(font,font.plainSubstrByWidth(stack.getHoverName().getString(),79),x,y+40,0xFFE4D8B5);}if(page==2){g.renderTooltip(font,Gear.ITEMS.get("toffy_rupee").get().getDefaultInstance(),left+24,top+70);g.renderTooltip(font,Gear.ITEMS.get("carnivoret_weapon_set_sword").get().getDefaultInstance(),left+300,top+180);}renderWidgets(g,mx,my,dt);}
 }
}
