package com.ianblk.zianmobs;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;
@Mod("zianmobs")
public final class ZianMobs {
 public static final Logger LOGGER=LogUtils.getLogger();
 public ZianMobs(IEventBus bus){net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener((net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent e)->PersonalRewards.forget(e.getEntity().getUUID()));if(net.neoforged.fml.loading.FMLEnvironment.dist==net.neoforged.api.distmarker.Dist.CLIENT)ItemPresentation.register();Mobs.register(bus);CompanionContent.register(bus);CompanionUpgrades.register(bus);SpawnEggs.register(bus);Gear.register(bus);MobsNetwork.register(bus);WingsTotem.register();NpcCombat.register();GearAbilities.register(bus);TitanAbilities.register();var zones=new Zones();new Companions();CompanionPublicAccess.register();CompanionCaptureGuard.register();new MobsSmoke(zones);new OrderedStageSmoke(zones);new ConsolidationSmoke(zones);new YouerUxSmoke();LOGGER.info("Zian Mobs development runtime initialized");}
}
