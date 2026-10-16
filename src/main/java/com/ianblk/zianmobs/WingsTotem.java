package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.effect.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

/** Vanilla death protection is reused by the mixin; this class owns only the offhand effect. */
public final class WingsTotem {
 public static final String ID="carnivoret_weapon_set_wing";
 private static final Map<UUID,MobEffectInstance> OWNED=new HashMap<>();
 private static final Set<UUID> ADDING=new HashSet<>();
 public static boolean isWing(ItemStack stack){var item=Gear.ITEMS.get(ID);return item!=null&&stack.is(item.get());}
 public static void register(){
  NeoForge.EVENT_BUS.addListener((PlayerTickEvent.Post e)->{if(e.getEntity() instanceof ServerPlayer p)tick(p);});
  NeoForge.EVENT_BUS.addListener((MobEffectEvent.Added e)->{if(e.getEntity() instanceof ServerPlayer p&&e.getEffectInstance().is(MobEffects.NIGHT_VISION)&&!ADDING.contains(p.getUUID()))OWNED.remove(p.getUUID());});
  NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedOutEvent e)->{if(e.getEntity() instanceof ServerPlayer p)clear(p);});
  NeoForge.EVENT_BUS.addListener((ServerStoppingEvent e)->{for(var p:e.getServer().getPlayerList().getPlayers())clear(p);OWNED.clear();ADDING.clear();});
 }
 static void tick(ServerPlayer player){
  var id=player.getUUID();var effect=player.getEffect(MobEffects.NIGHT_VISION);var ours=OWNED.get(id);
  if(ours!=null&&effect!=ours){OWNED.remove(id);ours=null;}
  if(!player.isAlive()||player.isSpectator()||!isWing(player.getOffhandItem())){clear(player);return;}
  // Existing potion/beacon effects are left untouched. Our duration stays above the fade threshold.
  if(effect!=null&&ours==null)return;
  if(effect==null||effect.getDuration()<=220){
   var next=new MobEffectInstance(MobEffects.NIGHT_VISION,240,0,true,false,true);
   ADDING.add(id);try{player.addEffect(next);if(player.getEffect(MobEffects.NIGHT_VISION)!=null)OWNED.put(id,player.getEffect(MobEffects.NIGHT_VISION));}finally{ADDING.remove(id);}
  }
 }
 static void clear(ServerPlayer p){var ours=OWNED.remove(p.getUUID());if(ours!=null&&p.getEffect(MobEffects.NIGHT_VISION)==ours)p.removeEffect(MobEffects.NIGHT_VISION);}
 public static final class WingItem extends Item{
  public WingItem(){this(new Properties());}public WingItem(Properties properties){super(properties.stacksTo(1));}
  @Override public void appendHoverText(ItemStack stack,TooltipContext context,List<Component> lines,TooltipFlag flag){lines.add(Component.translatable("tooltip.zianmobs.wing_totem"));lines.add(Component.translatable("tooltip.zianmobs.wing_offhand"));}
 }
 private WingsTotem(){}
}
