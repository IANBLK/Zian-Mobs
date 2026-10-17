package com.ianblk.zianmobs;
import java.util.List;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.effect.*;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
/** Pair-only accepted melee procs; vanilla owns death protection and consumption. */
public final class TitanAbilities {
 public static final String SWORD="titan_double_sword",GAUNTLET="titan_gauntlet";
 public static boolean isGauntlet(ItemStack stack){var item=CompanionContent.EXTRAS.get(GAUNTLET);return item!=null&&stack.is(item.get());}
 static boolean paired(Player player){var sword=CompanionContent.EXTRAS.get(SWORD);return sword!=null&&player.getMainHandItem().is(sword.get())&&isGauntlet(player.getOffhandItem());}
 static boolean fullArmor(Player player){return player.getItemBySlot(EquipmentSlot.HEAD).is(CompanionContent.ARMOR.get("titan_helmet").get())&&player.getItemBySlot(EquipmentSlot.CHEST).is(CompanionContent.ARMOR.get("titan_chestplate").get())&&player.getItemBySlot(EquipmentSlot.LEGS).is(CompanionContent.ARMOR.get("titan_leggings").get())&&player.getItemBySlot(EquipmentSlot.FEET).is(CompanionContent.ARMOR.get("titan_boots").get());}
 public static void register(){NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,TitanAbilities::killed);}
 private static void killed(LivingDeathEvent event){if(!event.isCanceled()&&!event.getEntity().level().isClientSide()&&event.getEntity() instanceof Mob&&!(event.getEntity() instanceof CompanionEntity)&&event.getSource().getEntity() instanceof Player player&&event.getSource().getDirectEntity()==player&&event.getSource().is(net.minecraft.world.damagesource.DamageTypes.PLAYER_ATTACK))activate(player);}
 static void critical(LivingEntity owner,LivingEntity target,String weapon,boolean critical){if(critical&&weapon.equals(SWORD)&&target instanceof Mob&&!(target instanceof CompanionEntity)&&owner instanceof Player player)activate(player);}
 private static void activate(Player player){if(player.level().isClientSide()||!paired(player))return;boolean full=fullArmor(player);int duration=full?900:600;if(!GearAbilities.acquire(player,"titan",full?45:60))return;for(var entry:List.of(new MobEffectInstance(MobEffects.REGENERATION,duration,1),new MobEffectInstance(MobEffects.ABSORPTION,duration,3),new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,duration,0),new MobEffectInstance(MobEffects.FIRE_RESISTANCE,duration,0),new MobEffectInstance(MobEffects.NIGHT_VISION,duration,0),new MobEffectInstance(GearAbilities.TITAN_AWAKENING,duration,0,false,false,false)))player.addEffect(entry);player.displayClientMessage(Component.literal("✦ ").withStyle(ChatFormatting.GOLD).append(Component.literal("Concordia del Titán").withStyle(ChatFormatting.LIGHT_PURPLE)).append(Component.literal(" · Bendición de Notch y visión nocturna durante "+(full?45:30)+" s.").withStyle(ChatFormatting.YELLOW)),false);}
 public static boolean awakened(LivingEntity wearer){return wearer!=null&&wearer.hasEffect(GearAbilities.TITAN_AWAKENING)&&wearer.hasEffect(MobEffects.NIGHT_VISION)&&wearer.hasEffect(MobEffects.REGENERATION)&&wearer.hasEffect(MobEffects.ABSORPTION)&&wearer.hasEffect(MobEffects.DAMAGE_RESISTANCE)&&wearer.hasEffect(MobEffects.FIRE_RESISTANCE);}
 private static void tooltip(List<Component> lines){lines.add(Component.literal("✦ Concordia del Titán").withStyle(ChatFormatting.LIGHT_PURPLE));lines.add(Component.literal("Espada principal y guantelete secundario.").withStyle(ChatFormatting.GRAY));lines.add(Component.literal("Crítico o baja: bendición de Notch y visión nocturna.").withStyle(ChatFormatting.GOLD));lines.add(Component.literal("Duración 30 s · Reutilización 60 s.").withStyle(ChatFormatting.GRAY));lines.add(Component.literal("Armadura completa: duración 45 s · espera 45 s.").withStyle(ChatFormatting.GOLD));}
 public static final class Blade extends SwordItem {public Blade(Properties properties){super(Tiers.NETHERITE,properties);}@Override public void appendHoverText(ItemStack stack,TooltipContext context,List<Component> lines,TooltipFlag flag){super.appendHoverText(stack,context,lines,flag);tooltip(lines);}}
 public static final class Gauntlet extends ShieldItem {public Gauntlet(Properties properties){super(properties);}@Override public int getEnchantmentValue(){return 15;}@Override public void appendHoverText(ItemStack stack,TooltipContext context,List<Component> lines,TooltipFlag flag){super.appendHoverText(stack,context,lines,flag);tooltip(lines);lines.add(Component.literal("Salva de la muerte en la mano secundaria.").withStyle(ChatFormatting.YELLOW));lines.add(Component.literal("Se consume al activarse como tótem.").withStyle(ChatFormatting.RED));}}
 private TitanAbilities(){}
}
