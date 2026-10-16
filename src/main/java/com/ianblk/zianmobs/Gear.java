package com.ianblk.zianmobs;
import com.google.gson.*;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.*;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/** Native items; plugin models are artwork only. No plugin commands or scripts are imported. */
public final class Gear {
 public record Spec(String id,String name,String kind,String model,String pack,String material,String armorTexture1,String armorTexture2,boolean armorModel){}
 public static final List<Spec> CATALOG;
 public static final Map<String,DeferredItem<Item>> ITEMS=new LinkedHashMap<>();
 private static final DeferredRegister.Items REGISTER=DeferredRegister.createItems("zianmobs");
 private static final DeferredRegister<CreativeModeTab> TABS=DeferredRegister.create(Registries.CREATIVE_MODE_TAB,"zianmobs");
 static{
  try(var stream=Gear.class.getResourceAsStream("/assets/zianmobs/gear-catalog.json")){if(stream==null)throw new IOException("Missing catalog");CATALOG=List.of(new Gson().fromJson(new InputStreamReader(stream,StandardCharsets.UTF_8),Spec[].class));}catch(IOException e){throw new ExceptionInInitializerError(e);}
  for(var s:CATALOG){if(!s.id().matches("[a-z0-9_]{1,128}"))throw new IllegalStateException("Invalid gear ID");if(ITEMS.put(s.id(),REGISTER.register(s.id(),()->create(s)))!=null)throw new IllegalStateException("Duplicate gear");}
  TABS.register("equipment",()->CreativeModeTab.builder().title(net.minecraft.network.chat.Component.translatable("itemGroup.zianmobs")).icon(()->ITEMS.values().iterator().next().get().getDefaultInstance()).displayItems((p,out)->{for(var item:ITEMS.values())out.accept(item);for(var egg:SpawnEggs.EGGS)out.accept(egg);}).build());
 }
 public static void register(IEventBus bus){REGISTER.register(bus);TABS.register(bus);}
 private static Item create(Spec s){var t=Tiers.NETHERITE;var p=GearStyle.decorate(new Item.Properties().fireResistant(),s);if(s.id().equals("toffy_rupee"))return new Item(p.food(rupeeFood()).rarity(Rarity.EPIC));if(s.id().endsWith("_staff"))return new StaffItem(t,p.attributes(SwordItem.createAttributes(t,5,-2.4f)),s.pack());if(s.id().endsWith("_scythe"))return new SwordItem(t,p.attributes(SwordItem.createAttributes(t,9,-2.4f)));if(s.id().endsWith("_hammer"))return s.kind().equals("AXE")?new AxeItem(t,p.attributes(AxeItem.createAttributes(t,8,-3))):new SwordItem(t,p.attributes(SwordItem.createAttributes(t,6,-2.4f)));return switch(s.kind()){
  case "SWORD"->new SwordItem(t,p.attributes(SwordItem.createAttributes(t,5,-2.4f)));
  case "AXE"->new AxeItem(t,p.attributes(AxeItem.createAttributes(t,7,-3)));
  case "PICKAXE"->new PickaxeItem(t,p.attributes(PickaxeItem.createAttributes(t,3,-2.8f)));
  case "SHOVEL"->new ShovelItem(t,p.attributes(ShovelItem.createAttributes(t,3.5f,-3)));
  case "HOE"->new HoeItem(t,p.attributes(HoeItem.createAttributes(t,-2,0)));
  case "BOW"->new EnhancedBow(p.durability(2031));
  case "CROSSBOW"->new EnhancedCrossbow(p.durability(2031));case "FISHING_ROD"->new FishingRodItem(p.durability(2031));case "SHIELD"->new ShieldItem(p.durability(2031));
  case "WING_TOTEM"->new WingsTotem.WingItem(p);
  default->new Item(p);
 };}
 static net.minecraft.world.food.FoodProperties rupeeFood(){var base=net.minecraft.world.food.Foods.ENCHANTED_GOLDEN_APPLE;var effects=base.effects().stream().map(possible->new net.minecraft.world.food.FoodProperties.PossibleEffect(()->{var e=possible.effect();return new net.minecraft.world.effect.MobEffectInstance(e.getEffect(),Math.round(e.getDuration()*1.3f),e.getAmplifier(),e.isAmbient(),e.isVisible(),e.showIcon());},possible.probability())).toList();return new net.minecraft.world.food.FoodProperties(base.nutrition(),base.saturation(),true,base.eatSeconds()*1.25f,base.usingConvertsTo(),effects);}
}
