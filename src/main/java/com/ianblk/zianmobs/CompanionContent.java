package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.registries.*;
import net.neoforged.bus.api.IEventBus;
public final class CompanionContent {
 public record Definition(String id,String name,boolean flying,boolean glow,boolean death){}
 public static final List<Definition> CATALOG=load();
 public static final List<String> IDS=CATALOG.stream().map(Definition::id).toList();
 private static List<Definition> load(){try(var in=CompanionContent.class.getResourceAsStream("/assets/zianmobs/companion-catalog.json")){return List.of(new com.google.gson.Gson().fromJson(new java.io.InputStreamReader(in,java.nio.charset.StandardCharsets.UTF_8),Definition[].class));}catch(Exception ex){throw new ExceptionInInitializerError(ex);}}
 public static Definition definition(String id){return CATALOG.stream().filter(d->d.id().equals(id)).findFirst().orElseThrow();}
 private static final DeferredRegister<EntityType<?>> TYPES=DeferredRegister.create(Registries.ENTITY_TYPE,"zianmobs");
 public static final Map<String,DeferredHolder<EntityType<?>,EntityType<CompanionEntity>>> PETS=new LinkedHashMap<>();
 private static final DeferredRegister<ArmorMaterial> MATERIALS=DeferredRegister.create(Registries.ARMOR_MATERIAL,"zianmobs");
 private static final DeferredRegister.Items ITEMS=DeferredRegister.createItems("zianmobs");
 public static final Map<String,DeferredItem<Item>> EXTRAS=new LinkedHashMap<>();
 public static final Map<String,DeferredItem<Item>> ARMOR=new LinkedHashMap<>();
 private static final Holder<ArmorMaterial> TITAN=MATERIALS.register("titan",()->new ArmorMaterial(Map.of(ArmorItem.Type.HELMET,3,ArmorItem.Type.CHESTPLATE,8,ArmorItem.Type.LEGGINGS,6,ArmorItem.Type.BOOTS,3,ArmorItem.Type.BODY,11),15,SoundEvents.ARMOR_EQUIP_NETHERITE,()->Ingredient.of(Items.NETHERITE_INGOT),List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath("zianmobs","thanos"))),3,.1f));
 static{for(String id:IDS)PETS.put(id,TYPES.register("companion_"+id,()->EntityType.Builder.<CompanionEntity>of(CompanionEntity::new,MobCategory.CREATURE).sized(id.equals("otter")?.65f:.5f,id.equals("otter")?.6f:.85f).clientTrackingRange(10).build("zianmobs:companion_"+id)));
  for(var type:List.of(ArmorItem.Type.HELMET,ArmorItem.Type.CHESTPLATE,ArmorItem.Type.LEGGINGS,ArmorItem.Type.BOOTS)){String id="titan_"+type.getName();ARMOR.put(id,ITEMS.register(id,()->new ArmorItem(TITAN,type,new Item.Properties().fireResistant().durability(type.getDurability(37)).component(net.minecraft.core.component.DataComponents.ITEM_NAME,net.minecraft.network.chat.Component.translatable("item.zianmobs."+id).withStyle(net.minecraft.ChatFormatting.GOLD)).component(net.minecraft.core.component.DataComponents.LORE,new net.minecraft.world.item.component.ItemLore(List.of(net.minecraft.network.chat.Component.literal("✧ Legado del Titán").withStyle(net.minecraft.ChatFormatting.GOLD),net.minecraft.network.chat.Component.literal("Forjado para soportar el destino.").withStyle(net.minecraft.ChatFormatting.GRAY)))))));}
  EXTRAS.put("titan_double_sword",ITEMS.register("titan_double_sword",()->new TitanAbilities.Blade(titanProperties("titan_double_sword","Dos filos, una voluntad inquebrantable.","Su sombra anuncia la caída de los reinos.").attributes(SwordItem.createAttributes(Tiers.NETHERITE,5,-2.4f)))));
  EXTRAS.put("titan_gauntlet",ITEMS.register("titan_gauntlet",()->new TitanAbilities.Gauntlet(titanProperties("titan_gauntlet","Las estrellas reposan en su puño dorado.","Alza tu mano y desafía al destino.").durability(736))));

 }
 private static Item.Properties titanProperties(String id,String first,String second){return new Item.Properties().fireResistant().component(net.minecraft.core.component.DataComponents.ITEM_NAME,net.minecraft.network.chat.Component.translatable("item.zianmobs."+id).withStyle(net.minecraft.ChatFormatting.GOLD)).component(net.minecraft.core.component.DataComponents.LORE,new net.minecraft.world.item.component.ItemLore(List.of(net.minecraft.network.chat.Component.literal("✧ Legado del Titán").withStyle(net.minecraft.ChatFormatting.LIGHT_PURPLE),net.minecraft.network.chat.Component.literal(first).withStyle(net.minecraft.ChatFormatting.GRAY),net.minecraft.network.chat.Component.literal(second).withStyle(net.minecraft.ChatFormatting.GRAY))));}
 public static void register(IEventBus bus){TYPES.register(bus);MATERIALS.register(bus);ITEMS.register(bus);bus.addListener((net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent e)->PETS.values().forEach(t->e.put(t.get(),CompanionEntity.attributes().build())));}
 public static String name(String id){return definition(id).name();}
 public static String support(String id){return "Apoyo aleatorio: 1 o 2 beneficios durante 30 s";}
}
