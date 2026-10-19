package com.ianblk.zianmobs.client;
import com.google.gson.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Consumer;
import java.util.*;
/** Server-approved Mob templates only; never projectiles, items or display entities. */
public final class EntitySelectorScreen extends ManagerThemedScreen {
 private final Screen parent;private final Consumer<String> choose;private final JsonArray source;
 private String filter="";private int page,left,top;private boolean others;
 public EntitySelectorScreen(Screen parent,JsonArray source,Consumer<String> choose){super(Component.literal("Elegir mob o modelo de dungeon"));this.parent=parent;this.source=source;this.choose=choose;}
 private Button button(int x,int y,int width,String text,Runnable action){return addRenderableWidget(themed(x,y,width,20,Component.literal(text),b->action.run()));}
 private String name(JsonObject row){var id=ResourceLocation.parse(row.get("id").getAsString());return Component.translatable(BuiltInRegistries.ENTITY_TYPE.get(id).getDescriptionId()).getString()+(id.getNamespace().equals("minecraft")?"":" · "+id.getNamespace());}
 @Override protected void init(){fitViewport(600,400);left=(width-560)/2;top=(height-376)/2;
  var search=new EditBox(font,left+12,top+32,536,20,Component.literal("Buscar mob o modelo"));search.setValue(filter);search.setResponder(v->{filter=v;page=0;rebuildWidgets();});addRenderableWidget(search);setInitialFocus(search);
  button(left+12,top+62,262,"Hostiles y modelos de dungeon",()->{others=false;page=0;rebuildWidgets();}).active=others;
  button(left+286,top+62,262,"Otros mobs compatibles",()->{others=true;page=0;rebuildWidgets();}).active=!others;
  var rows=source.asList().stream().map(JsonElement::getAsJsonObject).filter(r->r.get("hostile").getAsBoolean()!=others).filter(r->(r.get("id").getAsString()+" "+name(r)).toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT))).sorted(Comparator.comparing((JsonObject r)->!r.get("id").getAsString().startsWith("zianmobs:")).thenComparing(this::name)).toList();
  int per=9,pages=Math.max(1,(rows.size()+per-1)/per);page=Math.min(page,pages-1);
  for(int i=page*per;i<Math.min(rows.size(),(page+1)*per);i++){var row=rows.get(i);String label=name(row);var b=button(left+12,top+94+(i-page*per)*25,536,font.plainSubstrByWidth(label,522),()->choose.accept(row.get("id").getAsString()));b.setTooltip(Tooltip.create(Component.literal(label+"\n"+row.get("id").getAsString())));}
  button(left+12,top+344,32,"<",()->{page--;rebuildWidgets();}).active=page>0;
  button(left+48,top+344,32,">",()->{page++;rebuildWidgets();}).active=page+1<pages;
  button(left+428,top+344,120,"Volver",this::onClose);
 }
 @Override public void onClose(){minecraft.setScreen(parent);}
 @Override protected void renderCanvas(GuiGraphics g,int mx,int my,float dt){panel(g,left,top,560,376);g.drawCenteredString(font,"Página "+(page+1)+" · Solo criaturas compatibles con las plantillas",left+280,top+325,0xFFBBBBBB);renderWidgets(g,mx,my,dt);}
}
