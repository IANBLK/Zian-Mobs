package com.ianblk.zianmobs;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;
@Mod("zianmobs")
public final class ZianMobs {
 public static final Logger LOGGER=LogUtils.getLogger();
 public ZianMobs(IEventBus bus){Mobs.register(bus);SpawnEggs.register(bus);Gear.register(bus);MobsNetwork.register(bus);WingsTotem.register();NpcCombat.register();GearAbilities.register(bus);var zones=new Zones();new MobsSmoke(zones);LOGGER.info("Zian Mobs development runtime initialized");}
}
