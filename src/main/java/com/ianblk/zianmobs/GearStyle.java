package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemLore;

/** Native item-name and lore components keep anvil custom names and enchantments independent. */
public final class GearStyle {
 private static Component line(net.minecraft.network.chat.MutableComponent text,ChatFormatting color){return text.withStyle(s->s.withColor(color).withItalic(false));}
 private static net.minecraft.network.chat.MutableComponent t(String key,String es){return Component.translatableWithFallback("lore.zianmobs."+key,es);}
 public static String name(Gear.Spec spec){return spec.name();}
 public static Item.Properties decorate(Item.Properties properties,Gear.Spec spec){
  ChatFormatting color=switch(spec.pack()){case "Ruby"->ChatFormatting.RED;case "Dark Queen"->ChatFormatting.DARK_PURPLE;case "Purple World"->ChatFormatting.LIGHT_PURPLE;case "Valentines"->ChatFormatting.LIGHT_PURPLE;case "Toffy Zelda"->ChatFormatting.AQUA;default->ChatFormatting.GREEN;};
  var collection=switch(spec.pack()){case "Ruby"->t("collection.ruby","✧ Pacto Carmesí");case "Dark Queen"->t("collection.dark_queen","✧ Corte de la Reina Umbría");case "Purple World"->t("collection.purple_world","✧ Vestigios del Velo Astral");case "Valentines"->t("collection.valentines","✧ Juramento de los Corazones");case "Toffy Zelda"->t("collection.toffy_zelda","✧ Legado de Hyrule");default->t("collection.carnivoret","✧ Reliquias de la Arboleda");};
  var first=switch(spec.pack()){case "Ruby"->t("first.ruby","Nacido de un juramento de sangre.");case "Dark Queen"->t("first.dark_queen","La noche se inclina ante su reina.");case "Purple World"->t("first.purple_world","Un fragmento de un cielo olvidado.");case "Valentines"->t("first.valentines","Ninguna sombra apaga este latido.");case "Toffy Zelda"->t("first.toffy_zelda","La luz antigua reconoce tu valor.");default->t("first.carnivoret","El bosque aún recuerda su origen.");};
  var second=switch(spec.kind()){case "BOW","CROSSBOW"->t("second.ranged","Su canto alcanza al destino.");case "SHIELD"->t("second.shield","Aquí resiste la última esperanza.");case "PICKAXE","SHOVEL","HOE"->t("second.tool","La tierra revela secretos antiguos.");case "FISHING_ROD"->t("second.rod","Los ríos guardan sueños sin nombre.");case "WING_TOTEM"->t("second.wing","Un último vuelo desafía al ocaso.");default->t("second.blade","Su filo custodia un viejo juramento.");};
  if(spec.id().endsWith("_staff"))second=t("second.staff","Su voz despierta fuerzas dormidas.");
  if(spec.id().endsWith("_hammer"))second=t("second.hammer","Cada golpe retumba como un trueno.");
  if(spec.id().endsWith("_scythe")){first=t("first.scythe","Las almas susurran entre sus filos.");second=t("second.scythe","Del ocaso nace una nueva vida.");}
  if(spec.id().equals("toffy_master_sword")){first=t("first.master_sword","Nacida para quebrar la noche.");second=t("second.master_sword","Solo el valor despierta su luz.");}
  if(spec.id().equals("toffy_hylian_shield")){first=t("first.hylian_shield","Bajo su emblema perdura el valor.");second=t("second.hylian_shield","Ni la noche doblega al guardián.");}
  if(spec.id().equals("toffy_rupee")){first=t("first.rupee","Una plegaria cristalizada en jade.");second=t("second.rupee","Las diosas velan por quien la porta.");}
  var lore=List.of(line(collection,color),line(first,ChatFormatting.GRAY),line(second,ChatFormatting.GOLD));
  return properties.component(DataComponents.ITEM_NAME,Component.translatableWithFallback("item.zianmobs."+spec.id(),spec.name()).withStyle(s->s.withColor(color).withItalic(false).withBold(true))).component(DataComponents.LORE,new ItemLore(lore));
 }
 private GearStyle(){}
}
