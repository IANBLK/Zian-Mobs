package com.ianblk.zianmobs;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
/** Prevent item interaction capture and reject entities reconstructed outside the refuge. */
public final class CompanionCaptureGuard {
 public static void register(){
  NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,CompanionCaptureGuard::specific);
  NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,CompanionCaptureGuard::interact);
  NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,CompanionCaptureGuard::attack);
  NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,CompanionCaptureGuard::join);
 }
 static void specific(PlayerInteractEvent.EntityInteractSpecific e){if(e.getTarget() instanceof CompanionEntity pet){e.setCanceled(true);e.setCancellationResult(InteractionResult.sidedSuccess(e.getLevel().isClientSide()));gesture(pet,e.getEntity(),e.getHand());}}
 static void interact(PlayerInteractEvent.EntityInteract e){if(e.getTarget() instanceof CompanionEntity pet){e.setCanceled(true);e.setCancellationResult(InteractionResult.sidedSuccess(e.getLevel().isClientSide()));gesture(pet,e.getEntity(),e.getHand());}}
 static void attack(AttackEntityEvent e){if(e.getTarget() instanceof CompanionEntity)e.setCanceled(true);}
 static void join(EntityJoinLevelEvent e){if(!e.getLevel().isClientSide()&&e.getEntity() instanceof CompanionEntity pet&&!pet.managed)e.setCanceled(true);}
 private static void gesture(CompanionEntity pet,Player player,InteractionHand hand){if(player.level().isClientSide())return;if(player.getItemInHand(hand).isEmpty()){if(hand==InteractionHand.MAIN_HAND)pet.mobInteract(player,hand);}else player.displayClientMessage(net.minecraft.network.chat.Component.literal("✦ Compañeros » ").withStyle(net.minecraft.ChatFormatting.GOLD).append(net.minecraft.network.chat.Component.literal("No se pueden capturar. Usa el refugio para guardarlos.").withStyle(net.minecraft.ChatFormatting.AQUA)),true);}
}
