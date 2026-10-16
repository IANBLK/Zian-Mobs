package com.ianblk.zianmobs;
import java.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.neoforged.neoforge.common.NeoForge;
public final class NpcCombat {
 private static final List<Arrow> ARROWS=new ArrayList<>();
 public static boolean canHurt(LivingEntity owner,LivingEntity target){if(target==null||target==owner||!target.isAlive()||owner.isAlliedTo(target)||target instanceof Player p&&(p.isCreative()||p.isSpectator()))return false;String run=owner.getPersistentData().getString("ZianMobsRun");return run.isEmpty()||!run.equals(target.getPersistentData().getString("ZianMobsRun"));}
 static void track(Arrow arrow){if(!ARROWS.contains(arrow))ARROWS.add(arrow);if(ARROWS.size()>512)ARROWS.removeFirst().discard();}
 static void register(){NeoForge.EVENT_BUS.addListener((net.neoforged.neoforge.event.tick.ServerTickEvent.Post e)->ARROWS.removeIf(a->{if(a.isRemoved())return true;if(a.tickCount>=120){a.discard();return true;}return false;}));NeoForge.EVENT_BUS.addListener((net.neoforged.neoforge.event.server.ServerStoppedEvent e)->ARROWS.clear());NeoForge.EVENT_BUS.addListener((net.neoforged.neoforge.event.entity.EntityJoinLevelEvent e)->{if(!e.getLevel().isClientSide()&&e.getEntity() instanceof Arrow a&&a.getPersistentData().getBoolean("ZianMobsNpcArrow"))track(a);});NeoForge.EVENT_BUS.addListener((net.neoforged.neoforge.event.entity.ProjectileImpactEvent e)->{if(e.getProjectile() instanceof Arrow a&&a.getPersistentData().getBoolean("ZianMobsNpcArrow")&&a.getOwner() instanceof LivingEntity owner&&e.getRayTraceResult() instanceof net.minecraft.world.phys.EntityHitResult hit&&hit.getEntity() instanceof LivingEntity target&&!canHurt(owner,target))e.setCanceled(true);});}
 private NpcCombat(){}
}
