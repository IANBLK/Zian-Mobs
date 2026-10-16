package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemLore;

/** Native item-name and lore components keep anvil custom names and enchantments independent. */
public final class GearStyle {
 private static Component line(String text,ChatFormatting color){return Component.literal(text).withStyle(s->s.withColor(color).withItalic(false));}
 public static String name(Gear.Spec spec){return spec.name();}
 public static Item.Properties decorate(Item.Properties properties,Gear.Spec spec){
  ChatFormatting color=switch(spec.pack()){case "Ruby"->ChatFormatting.RED;case "Dark Queen"->ChatFormatting.DARK_PURPLE;case "Purple World"->ChatFormatting.LIGHT_PURPLE;case "Valentines"->ChatFormatting.LIGHT_PURPLE;case "Toffy Zelda"->ChatFormatting.AQUA;default->ChatFormatting.GREEN;};
  String collection=switch(spec.pack()){case "Ruby"->"Pacto Carmesí";case "Dark Queen"->"Corte de la Reina Umbría";case "Purple World"->"Vestigios del Velo Astral";case "Valentines"->"Juramento de los Corazones";case "Toffy Zelda"->"Legado de Hyrule";default->"Reliquias de la Arboleda";};
  String first=switch(spec.pack()){case "Ruby"->"Nacido de un juramento de sangre.";case "Dark Queen"->"La noche se inclina ante su reina.";case "Purple World"->"Un fragmento de un cielo olvidado.";case "Valentines"->"Ninguna sombra apaga este latido.";case "Toffy Zelda"->"La luz antigua reconoce tu valor.";default->"El bosque aún recuerda su origen.";};
  String second=switch(spec.kind()){case "BOW","CROSSBOW"->"Su canto alcanza al destino.";case "SHIELD"->"Aquí resiste la última esperanza.";case "PICKAXE","SHOVEL","HOE"->"La tierra revela secretos antiguos.";case "FISHING_ROD"->"Los ríos guardan sueños sin nombre.";case "WING_TOTEM"->"Un último vuelo desafía al ocaso.";default->"Su filo custodia un viejo juramento.";};
  if(spec.id().endsWith("_staff"))second="Su voz despierta fuerzas dormidas.";
  if(spec.id().endsWith("_hammer"))second="Cada golpe retumba como un trueno.";
  if(spec.id().endsWith("_scythe")){first="Las almas susurran entre sus filos.";second="Del ocaso nace una nueva vida.";}
  if(spec.id().equals("toffy_master_sword")){first="Nacida para quebrar la noche.";second="Solo el valor despierta su luz.";}
  if(spec.id().equals("toffy_hylian_shield")){first="Bajo su emblema perdura el valor.";second="Ni la noche doblega al guardián.";}
  if(spec.id().equals("toffy_rupee")){first="Una plegaria cristalizada en jade.";second="Las diosas velan por quien la porta.";}
  var lore=List.of(line("✧ "+collection,color),line(first,ChatFormatting.GRAY),line(second,ChatFormatting.GOLD));
  return properties.component(DataComponents.ITEM_NAME,Component.translatable("item.zianmobs."+spec.id()).withStyle(s->s.withColor(color).withItalic(false).withBold(true))).component(DataComponents.LORE,new ItemLore(lore));
 }
 private GearStyle(){}
}
