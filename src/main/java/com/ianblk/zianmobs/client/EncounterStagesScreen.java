package com.ianblk.zianmobs.client;
import com.google.gson.*;
import com.ianblk.zianmobs.core.Definitions.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import java.util.*;
import java.util.function.Consumer;

/** Local draft only. The zone form submits the complete plan through its protected server session. */
public final class EncounterStagesScreen extends ManagerThemedScreen {
 private final Screen parent;private final JsonArray mobs,bosses;private final Consumer<String> accept;
 private final List<EncounterStage> stages=new ArrayList<>();private final List<EncounterStage> original;
 private int left,top,page;private String notice="";
 public EncounterStagesScreen(Screen parent,JsonArray mobs,JsonArray bosses,String json,Consumer<String> accept){super(Component.literal("Encuentro · orden de aparición"));this.parent=parent;this.mobs=mobs;this.bosses=bosses;this.accept=accept;var rows=JsonParser.parseString(json).getAsJsonArray();for(var row:rows)stages.add(new Gson().fromJson(row,EncounterStage.class));original=List.copyOf(stages);}
 private Button button(int x,int y,int w,int h,String text,Runnable action){return addRenderableWidget(themed(x,y,w,h,Component.literal(text),b->action.run()));}
 @Override protected void init(){fitViewport(600,400);left=(width-560)/2;top=(height-376)/2;page=Math.max(0,Math.min(page,Math.max(0,(stages.size()-1)/4)));
  for(int i=page*4;i<Math.min(stages.size(),page*4+4);i++){final int at=i;var stage=stages.get(i);int y=top+80+(i%4)*48;String text=(i+1)+". "+(stage.kind()==StageKind.BOSS?"Jefe · "+name(stage.team().getFirst()):"Oleada · "+stage.team().size()+(stage.population()==Population.POOL?" tipos":" criaturas"));var edit=button(left+12,y,238,34,text,()->edit(at,stage.kind()));edit.setTooltip(Tooltip.create(Component.literal(text+"\n"+members(stage)+"\nPulsa para cambiar las criaturas")));
   var mode=button(left+256,y+7,98,20,stage.kind()==StageKind.BOSS?"Único":stage.population()==Population.POOL?"Escalable":"Fija",()->{stages.set(at,new EncounterStage(stage.kind(),stage.team(),stage.population()==Population.EXACT?Population.POOL:Population.EXACT));notice=stages.get(at).population()==Population.POOL?"Escalable: 1–3 criaturas + 2 por jugador adicional, máximo 8":"Fija: aparece una criatura por cada casilla de la oleada";rebuildWidgets();});mode.active=stage.kind()==StageKind.WAVE;
   button(left+362,y+7,30,20,"↑",()->move(at,-1)).active=i>0;button(left+398,y+7,30,20,"↓",()->move(at,1)).active=i<stages.size()-1;
   button(left+436,y+7,112,20,"Quitar",()->minecraft.setScreen(new ManagerConfirmScreen(ok->{if(ok){stages.remove(at);notice="Etapa quitada del borrador";}minecraft.setScreen(this);},Component.literal("Quitar etapa "+(at+1)),Component.literal("Las siguientes etapas conservarán su orden. Guarda después la zona para aplicar el cambio."))));
  }
  var wave=button(left+12,top+280,262,22,"Añadir oleada",()->edit(-1,StageKind.WAVE));wave.active=stages.size()<17&&!mobs.isEmpty();
  var boss=button(left+286,top+280,262,22,"Añadir jefe",()->edit(-1,StageKind.BOSS));boss.active=stages.size()<17&&!bosses.isEmpty();boss.setTooltip(Tooltip.create(Component.literal(bosses.isEmpty()?"Crea primero una plantilla en Jefes, dentro de Zian Mobs":"Elige un jefe y muévelo a la posición que quieras")));
  button(left+12,top+314,32,20,"<",()->{page--;rebuildWidgets();}).active=page>0;button(left+48,top+314,32,20,">",()->{page++;rebuildWidgets();}).active=(page+1)*4<stages.size();
  button(left+12,top+348,262,20,"Usar estas etapas",()->accept.accept(new Gson().toJson(stages)));
  button(left+286,top+348,262,20,"Cancelar",this::onClose);
 }
 private String name(String id){for(var e:mobs)if(e.getAsJsonObject().get("id").getAsString().equals(id))return e.getAsJsonObject().get("name").getAsString();return id;}
 private String members(EncounterStage stage){return String.join(", ",stage.team().stream().map(this::name).toList());}
 private void move(int at,int delta){Collections.swap(stages,at,at+delta);page=(at+delta)/4;notice="Orden actualizado en el borrador";rebuildWidgets();}
 private void edit(int at,StageKind kind){if(kind==StageKind.BOSS)minecraft.setScreen(new ChoiceScreen(this,"Elegir jefe para la etapa",bosses,false,id->set(at,new EncounterStage(kind,List.of(id)))));else minecraft.setScreen(new TeamScreen(this,mobs,at<0?"":String.join(",",stages.get(at).team()),value->{var team=Arrays.stream(value.split(",")).filter(v->!v.isBlank()).toList();if(team.isEmpty()){notice="La oleada necesita al menos una criatura";minecraft.setScreen(this);}else set(at,new EncounterStage(kind,team,at<0?Population.EXACT:stages.get(at).population()));}));}
 private void set(int at,EncounterStage stage){if(at<0){stages.add(stage);page=(stages.size()-1)/4;}else stages.set(at,stage);notice="Borrador actualizado. Usa estas etapas y guarda la zona.";minecraft.setScreen(this);}
 @Override public void onClose(){if(!stages.equals(original))minecraft.setScreen(new ManagerConfirmScreen(ok->minecraft.setScreen(ok?parent:this),Component.literal("Etapas sin aplicar"),Component.literal("¿Descartar los cambios de este borrador?")));else minecraft.setScreen(parent);}
 @Override protected void renderCanvas(GuiGraphics g,int mx,int my,float dt){panel(g,left,top,560,376);g.drawString(font,"Se ejecutan de arriba abajo. Puedes comenzar con una oleada o un jefe.",left+12,top+36,0xFFDDDDDD);g.drawString(font,font.plainSubstrByWidth(notice.isEmpty()?"Hasta 17 etapas · la zona se reutiliza después de completarlas todas.":notice,536),left+12,top+53,0xFFF2C14E);if(stages.isEmpty())g.drawCenteredString(font,"Añade la primera oleada o el primer jefe.",left+280,top+150,0xFFBBBBBB);else g.drawString(font,"Página "+(page+1)+"/"+Math.max(1,(stages.size()+3)/4)+" · "+stages.size()+" etapas",left+100,top+320,0xFFBBBBBB);renderWidgets(g,mx,my,dt);}
}
