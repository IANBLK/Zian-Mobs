package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.*;
import net.neoforged.bus.api.IEventBus;
/** Permanent consumable improvements, scoped to one owner and companion. */
public final class CompanionUpgrades {
 public record Levels(int health,int damage,int armor,int duration,int cooldown,int revive) {
  public static final Levels ZERO=new Levels(0,0,0,0,0,0);
  public Levels {for(int n:new int[]{health,damage,armor,duration,cooldown,revive})if(n<0||n>10)throw new IllegalArgumentException("Nivel de mejora inválido");}
  int get(String key){return switch(key){case "health"->health;case "damage"->damage;case "armor"->armor;case "duration"->duration;case "cooldown"->cooldown;case "revive"->revive;default->throw new IllegalArgumentException("Mejora desconocida");};}
  Levels add(String key,int points){return new Levels(next("health",key,health,points),next("damage",key,damage,points),next("armor",key,armor,points),next("duration",key,duration,points),next("cooldown",key,cooldown,points),next("revive",key,revive,points));}
  private static int next(String stat,String key,int n,int points){return key.equals(stat)||key.equals("all")?Math.min(10,n+points):n;}
  public String summary(){return "Vida "+health+"/10 · Daño "+damage+"/10 · Armadura "+armor+"/10 · Duración "+duration+"/10 · Espera "+cooldown+"/10 · Resurrección "+revive+"/10";}
 }
 public record Material(String id,String name,String stat,int points){}
 public static final List<Material> CATALOG=new ArrayList<>();
 public static final Map<String,DeferredItem<Item>> ITEMS=new LinkedHashMap<>();
 private static final DeferredRegister.Items REGISTER=DeferredRegister.createItems("zianmobs");
 static {String[][] rows={{"dark_geode","Geoda del Umbral","armor","Fortalece la coraza de tu vínculo."},{"hallow_skull","Cráneo del Retorno","revive","Acorta el viaje de regreso del alma."},{"infested_egg","Huevo del Renacer","health","En su interior palpita una vida ancestral."},{"magical_core","Núcleo del Eclipse","damage","Despierta el poder que duerme en tu aliado."},{"magical_substance","Esencia Crepuscular","duration","Prolonga los dones de tu compañero."},{"nano_nest","Nido del Instante","cooldown","Devuelve el aliento a la magia del vínculo."},{"shadow_diamond","Diamante del Vínculo","all","Eleva todos los dones que aún puedan crecer."}};
  for(var row:rows)for(int variant=0;variant<2;variant++){String id="shadow_"+row[0]+"_"+variant+"a";int points=variant+1;String name=row[1]+(variant==1?" · Despierto":"");CATALOG.add(new Material(id,name,row[2],points));ITEMS.put(id,REGISTER.register(id,()->new Item(new Item.Properties().stacksTo(64).component(net.minecraft.core.component.DataComponents.ITEM_NAME,Component.literal("✧ "+name).withStyle(ChatFormatting.LIGHT_PURPLE)).component(net.minecraft.core.component.DataComponents.LORE,new net.minecraft.world.item.component.ItemLore(List.of(Component.literal(row[3]).withStyle(ChatFormatting.GRAY),Component.literal(label(row[2])+" · +"+points+" nivel"+(points==1?"":"es")).withStyle(ChatFormatting.GOLD),Component.literal("Úsalo desde el refugio de compañeros.").withStyle(ChatFormatting.DARK_PURPLE)))))));}
 }
 public static String label(String stat){return switch(stat){case "health"->"Vida";case "damage"->"Daño";case "armor"->"Armadura";case "duration"->"Duración del apoyo";case "cooldown"->"Espera del apoyo";case "revive"->"Resurrección";case "all"->"Todos los dones";default->throw new IllegalArgumentException("Mejora desconocida");};}
 public static Material material(String id){return CATALOG.stream().filter(m->m.id().equals(id)).findFirst().orElseThrow(()->new IllegalArgumentException("Material desconocido"));}
 public static void register(IEventBus bus){REGISTER.register(bus);}
}
