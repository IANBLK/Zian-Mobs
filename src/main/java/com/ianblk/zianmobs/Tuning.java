package com.ianblk.zianmobs;
import com.ianblk.zianmobs.core.AtomicJson;
import java.nio.file.*;
import java.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

/** Per-world tuning, applied server-side; item components carry the results to vanilla clients. */
public final class Tuning {
 public record Attack(boolean enabled,int cooldown,int windup,double radius,double multiplier,int poisonSeconds,double leap,int roarCooldown,int roarSeconds,double roarRadius){public Attack(boolean enabled,int cooldown,int windup,double radius,double multiplier,int poisonSeconds,double leap){this(enabled,cooldown,windup,radius,multiplier,poisonSeconds,leap,300,8,15);}public Attack{if(cooldown<20||cooldown>1200||windup<1||windup>100||radius<1||radius>8||multiplier<0||multiplier>5||poisonSeconds<0||poisonSeconds>30||leap<0||leap>1.5||roarCooldown<20||roarCooldown>12000||roarSeconds<1||roarSeconds>60||roarRadius<1||roarRadius>24||!Double.isFinite(radius+multiplier+leap+roarRadius))throw new IllegalArgumentException("Ataque fuera de límites: espera 20–1200 ticks, preparación 1–100, radio 1–8, daño ×0–5, veneno 0–30 s, impulso 0–1.5");}}
 public record Stats(double damage,double speed,int durability,double armor,double toughness,double knockback,double projectile){public Stats{if(!Double.isFinite(damage+speed+armor+toughness+knockback+projectile)||damage<0||damage>128||speed<.1||speed>8||durability<1||durability>10000||armor<0||armor>30||toughness<0||toughness>20||knockback<0||knockback>1||projectile<.1||projectile>20)throw new IllegalArgumentException("Estadísticas fuera de límites");}}
 public record Settings(boolean zones,boolean bossbars,boolean leash){}
 public record Data(int schema,Map<String,Attack> attacks,Map<String,Stats> gear,Settings settings,Map<String,NpcProfiles.Spec> npcs){public Data(int schema,Map<String,Attack> attacks,Map<String,Stats> gear,Settings settings){this(schema,attacks,gear,settings,current==null?Map.of():current.npcs());}public Data{npcs=npcs==null?Map.of():Map.copyOf(npcs);if(npcs.size()>256)throw new IllegalArgumentException("Demasiados NPC");for(String id:npcs.keySet())com.ianblk.zianmobs.core.Definitions.id(id);if(schema!=1||attacks.size()>256||gear.size()>128||settings==null)throw new IllegalArgumentException("Configuración inválida");attacks=Map.copyOf(attacks);gear=Map.copyOf(gear);for(String id:attacks.keySet())com.ianblk.zianmobs.core.Definitions.id(id);for(String id:gear.keySet())if(!id.matches("[a-z0-9_]{1,128}"))throw new IllegalArgumentException("Equipo inválido");}}
 static Data current=defaults();private static Path file;
 static Data defaults(){return new Data(1,Map.of(),Map.of(),new Settings(true,true,true),Map.of());}
 static void start(Path root)throws Exception{file=root.resolve("tuning.json");current=AtomicJson.read(file,Data.class,defaults());var attacks=new LinkedHashMap<>(current.attacks());var gear=new LinkedHashMap<>(current.gear());attacks.keySet().removeIf(id->ContentRetirement.ENTITIES.contains("zianmobs:"+id));gear.keySet().removeIf(ContentRetirement.ITEMS::contains);if(!gear.isEmpty()){var fixedBackup=root.resolve("tuning-before-beta6.json");if(!com.ianblk.zianmobs.storage.StorageFiles.exists(fixedBackup))AtomicJson.write(fixedBackup,current);gear.clear();}var next=new Data(1,attacks,gear,current.settings());if(!next.equals(current)){var backup=root.resolve("tuning-before-beta4.json");if(!com.ianblk.zianmobs.storage.StorageFiles.exists(backup))AtomicJson.write(backup,current);}current=next;AtomicJson.write(file,current);}
 static void stop(){current=defaults();file=null;}
 static void save(Data next)throws Exception{if(file==null)throw new IllegalStateException("Servidor no disponible");AtomicJson.write(file,next);current=next;}
 public static Attack attack(String id){return current.attacks().getOrDefault(id,new Attack(true,id.equals("oni_brute")?50:25,12,id.equals("oni_brute")?5:2.5,1,0,id.startsWith("cursed_one_")?.8:.0));}
 public static Stats gear(String id){var item=BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("zianmobs",id));return defaults(item);}
 static Stats defaults(Item item){double damage=1,speed=4,armor=0,toughness=0,kb=0;for(var e:item.getDefaultInstance().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,ItemAttributeModifiers.EMPTY).modifiers()){double a=e.modifier().amount();if(e.attribute().equals(Attributes.ATTACK_DAMAGE))damage+=a;if(e.attribute().equals(Attributes.ATTACK_SPEED))speed+=a;if(e.attribute().equals(Attributes.ARMOR))armor+=a;if(e.attribute().equals(Attributes.ARMOR_TOUGHNESS))toughness+=a;if(e.attribute().equals(Attributes.KNOCKBACK_RESISTANCE))kb+=a;}return new Stats(Math.max(0,damage),Math.max(.1,speed),Math.max(1,item.getDefaultInstance().getMaxDamage()),armor,toughness,kb,2);}
 static void update(ItemStack stack){if(stack.isEmpty())return;var id=BuiltInRegistries.ITEM.getKey(stack.getItem());if(!id.getNamespace().equals("zianmobs")||!Gear.ITEMS.containsKey(id.getPath()))return;var defaults=stack.getItem().getDefaultInstance();var modifiers=defaults.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,ItemAttributeModifiers.EMPTY);if(!modifiers.equals(stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,ItemAttributeModifiers.EMPTY)))stack.set(DataComponents.ATTRIBUTE_MODIFIERS,modifiers);
  if(defaults.getMaxDamage()>0){if(stack.getMaxDamage()!=defaults.getMaxDamage())stack.set(DataComponents.MAX_DAMAGE,defaults.getMaxDamage());if(stack.getDamageValue()>=defaults.getMaxDamage())stack.setDamageValue(defaults.getMaxDamage()-1);}
  if(!java.util.Objects.equals(stack.get(DataComponents.LORE),defaults.get(DataComponents.LORE)))stack.set(DataComponents.LORE,defaults.get(DataComponents.LORE));
  if(!java.util.Objects.equals(stack.get(DataComponents.ITEM_NAME),defaults.get(DataComponents.ITEM_NAME)))stack.set(DataComponents.ITEM_NAME,defaults.get(DataComponents.ITEM_NAME));
  var tool=defaults.get(DataComponents.TOOL);if(tool!=null&&!tool.equals(stack.get(DataComponents.TOOL)))stack.set(DataComponents.TOOL,tool);
 }
}
