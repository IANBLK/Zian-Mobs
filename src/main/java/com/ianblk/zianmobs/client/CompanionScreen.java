package com.ianblk.zianmobs.client;

import com.google.gson.*;

import com.ianblk.zianmobs.MobsNetwork;

import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.network.chat.Component;

import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

/** Live countdowns use a monotonic local clock; access and actions remain server-authoritative. */

public final class CompanionScreen extends ManagerThemedScreen {

 private JsonObject data;private long received;private int ticks;private String selected;private int left,top;private boolean pending;private int page,shownCount;private boolean searching,onlyOwned=true;private String query="";

 public CompanionScreen(JsonObject data){super(Component.literal("Compañeros · Refugio"));this.data=data;selected=data.get("id").getAsString();received=System.nanoTime();}

 public void accept(JsonObject next){data=next;selected=next.get("id").getAsString();received=System.nanoTime();pending=false;rebuildWidgets();}

 private JsonObject pet(){for(var v:data.getAsJsonArray("pets"))if(v.getAsJsonObject().get("id").getAsString().equals(selected))return v.getAsJsonObject();return data.getAsJsonArray("pets").get(0).getAsJsonObject();}

 private void request(String section,String id){PacketDistributor.sendToServer(new MobsNetwork.Request(section,id));}

 private void send(String action){if(pending)return;pending=true;PacketDistributor.sendToServer(new MobsNetwork.PetAction(UUID.fromString(data.get("token").getAsString()),action,"{}"));rebuildWidgets();}

 private net.minecraft.client.gui.components.Button button(int x,int y,int w,String name,Runnable action){return addRenderableWidget(themed(x,y,w,24,Component.literal(name),b->action.run()));}

 @Override protected void init(){fitViewport(620,410);left=(width-580)/2;top=(height-386)/2;var search=new net.minecraft.client.gui.components.EditBox(font,left+12,top+36,112,20,Component.literal("Buscar compañero"));search.setHint(Component.literal("Buscar compañero..."));search.setValue(query);search.setResponder(value->{query=value;page=0;searching=true;rebuildWidgets();});addRenderableWidget(search);button(left+130,top+34,72,onlyOwned?"Ver todos":"Ver míos",()->{onlyOwned=!onlyOwned;page=0;if(onlyOwned&&!pet().get("allowed").getAsBoolean()){for(var entry:data.getAsJsonArray("pets")){var row=entry.getAsJsonObject();if(row.get("allowed").getAsBoolean()){pending=true;request("companions",row.get("id").getAsString());break;}}}rebuildWidgets();});if(searching)setInitialFocus(search);var filtered=new ArrayList<JsonObject>();for(var value:data.getAsJsonArray("pets")){var row=value.getAsJsonObject();if((!onlyOwned||row.get("allowed").getAsBoolean())&&row.get("name").getAsString().toLowerCase(java.util.Locale.ROOT).contains(query.toLowerCase(java.util.Locale.ROOT)))filtered.add(row);}shownCount=filtered.size();page=Math.min(page,Math.max(0,(filtered.size()-1)/6));for(int i=page*6;i<Math.min(filtered.size(),page*6+6);i++){var row=filtered.get(i);String id=row.get("id").getAsString();button(left+12,top+65+(i%6)*29,190,(row.get("allowed").getAsBoolean()?"✦ ":"◇ ")+row.get("name").getAsString(),()->request("companions",id)).active=!pending&&!id.equals(selected);}button(left+12,top+243,88,"Anterior",()->{page--;rebuildWidgets();}).active=page>0;button(left+114,top+243,88,"Siguiente",()->{page++;rebuildWidgets();}).active=(page+1)*6<filtered.size();

  var p=pet();boolean active=p.get("active").getAsBoolean(),allowed=p.get("allowed").getAsBoolean();boolean dead=p.get("remaining").getAsLong()>0;button(left+224,top+249,158,"Invocar",()->send("summon")).active=!pending&&allowed&&!active&&!dead;button(left+396,top+249,168,"Guardar y curar",()->send("store")).active=!pending&&active&&!dead;button(left+224,top+281,340,"Traer a mi lado",()->send("bring")).active=!pending&&active&&allowed&&!dead;

  button(left+224,top+313,340,"Mejorar vínculo",()->request("companion_upgrades",selected)).active=!pending&&allowed&&!active&&!dead;
  if(data.get("admin").getAsBoolean()){button(left+12,top+275,190,"Ajustes del compañero",()->request("companion_settings",selected));button(left+12,top+309,190,"Permisos y accesos",()->request("companion_access",""));}

  button(left+12,top+350,130,"Actualizar",()->request("companions",selected));button(left+434,top+350,130,"Cerrar",this::onClose);

 }

 @Override public void tick(){if(++ticks%40==0&&minecraft.getConnection()!=null&&!pending)request("companions",selected);}

 private long remaining(String key){return Math.max(0,pet().get(key).getAsLong()-(System.nanoTime()-received)/1000000);}

 private static String duration(long millis){long seconds=(millis+999)/1000;return String.format(java.util.Locale.ROOT,"%02d:%02d",seconds/60,seconds%60);}

 @Override protected void renderCanvas(GuiGraphics g,int mx,int my,float partial){panel(g,left,top,580,386);int owned=0;for(var entry:data.getAsJsonArray("pets"))if(entry.getAsJsonObject().get("allowed").getAsBoolean())owned++;g.drawString(font,"✦ Desbloqueados: "+owned+" / "+data.getAsJsonArray("pets").size(),left+12,top+23,0xFFF2C14E);if(shownCount==0)for(var line:font.split(Component.literal(query.isBlank()?"Sin compañeros desbloqueados.":"No hay coincidencias."),184))g.drawString(font,line,left+12,top+80,0xFFBBBBBB);var p=pet();int x=left+224,y=top+62;g.drawString(font,p.get("name").getAsString(),x,y,0xFFF2C14E);y+=22;boolean dead=remaining("remaining")>0;String status=!p.get("allowed").getAsBoolean()?"Bloqueado · un administrador debe desbloquearlo":dead?"Descansando · revive en "+duration(remaining("remaining")):p.get("active").getAsBoolean()?"Invocado · vuela a tu lado":"En el refugio · listo para invocar";for(var line:font.split(Component.literal(status),338)){g.drawString(font,line,x,y,dead?0xFFFFCC77:0xFF95D7BC);y+=12;}y+=10;g.drawString(font,String.format(java.util.Locale.forLanguageTag("es"),"Vida: %.0f / %.0f",p.get("health").getAsDouble(),p.get("maxHealth").getAsDouble())+" · Defensa: "+String.format(java.util.Locale.ROOT,"%.1f",p.get("armor").getAsDouble()),x,y,0xFFDDDDDD);y+=21;for(var line:font.split(Component.literal(p.get("support").getAsString()),338)){g.drawString(font,line,x,y,0xFFDDD6C4);y+=12;}y+=8;g.drawString(font,"Apoyo: "+(remaining("supportWait")>0?duration(remaining("supportWait")):"disponible"),x,y,0xFF99D6E8);y+=18;g.drawString(font,p.get("ranged").getAsBoolean()?"Ataque a distancia: "+String.format(java.util.Locale.ROOT,"%.1f",p.get("attackDamage").getAsDouble())+" de daño":"Ataque a distancia desactivado",x,y,0xFF99D6E8);y+=18;for(var line:font.split(Component.literal("Guardar cura a tu compañero. Si muere, debe cumplir su descanso."),338)){g.drawString(font,line,x,y,0xFFAAAAAA);y+=12;}if(!p.get("allowed").getAsBoolean()){y+=8;for(var line:font.split(Component.literal(p.get("permission").getAsString()),338)){g.drawString(font,line,x,y,0xFFC7AA75);y+=12;}}

  String notice=data.get("notice").getAsString();if(!notice.isBlank())g.drawString(font,font.plainSubstrByWidth(notice,552),left+12,top+340,0xFFFFC777);renderWidgets(g,mx,my,partial);}

}
