package com.ianblk.zianmobs;
import com.ianblk.zianmobs.core.*;
import com.ianblk.zianmobs.core.Definitions.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Mob;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.LevelResource;
import java.util.*;
/** Real spawns and lifecycle handlers, confined to the explicitly opted-in isolated world. */
final class BossWaveSmoke {
 private static void check(boolean ok,String label,java.util.function.Consumer<String> passed){if(!ok)throw new IllegalStateException(label);passed.accept(label);}
 static void run(MinecraftServer server,Zones zones,java.util.function.Consumer<String> passed)throws Exception{
  if(!Boolean.getBoolean("zian.ux.smoke")||!server.getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize().getFileName().toString().equals("mobs-smoke-ux"))throw new IllegalStateException("Boss smoke requires isolated opt-in world");
  var world=server.overworld();var at=new BlockPos(64,300,64);world.getChunkAt(at);
  for(int x=-3;x<=3;x++)for(int z=-3;z<=3;z++){world.setBlockAndUpdate(at.offset(x,-1,z),Blocks.STONE.defaultBlockState());for(int y=0;y<5;y++)world.setBlockAndUpdate(at.offset(x,y,z),Blocks.AIR.defaultBlockState());}
  var mobs=new LinkedHashMap<>(zones.config.mobs());String[] ids={"ux_wave_a","ux_wave_b","ux_final_boss"};String[] entities={"minecraft:zombie","minecraft:husk","minecraft:skeleton"};
  for(int i=0;i<3;i++)mobs.put(ids[i],new MobSpec(ids[i],ids[i],entities[i],20,1,0,0,0,List.of(),Map.of(),"",i==2));
  zones.save(new Zones.Config(5,mobs,zones.config.loot(),zones.config.zones()));
  var zone=new ZoneSpec("ux_boss_zone","minecraft:overworld",new Point(60,299,60,0),new Point(68,304,68,0),List.of(new Point(64.5,300,64.5,0)),List.of(ids[0]),2,1,10,"",true).plan(ids[2],List.of(List.of(ids[0]),List.of(ids[1])));zones.putZone(zone);
  var begin=Zones.class.getDeclaredMethod("begin",ZoneSpec.class,Set.class,int.class,UUID.class);begin.setAccessible(true);
  var deathType=Class.forName(Zones.class.getName()+"$Death");var constructor=deathType.getDeclaredConstructor(Mob.class,ServerPlayer.class);constructor.setAccessible(true);var died=Zones.class.getDeclaredMethod("died",deathType);died.setAccessible(true);var runId=UUID.randomUUID();
  for(int stage=1;stage<=3;stage++){
   begin.invoke(zones,zone,Set.of(),stage,runId);var active=zones.ledger.get(zone.id());check(active.phase()==EncounterLedger.Phase.ACTIVE&&active.wave()==stage&&active.spawns().size()==1&&active.spawns().getFirst().template().equals(ids[stage-1]),"native-boss-plan-stage-"+stage,passed);
   var mob=(Mob)world.getEntity(active.spawns().getFirst().uuid());check(mob!=null&&mob.isAlive(),"native-boss-plan-spawn-"+stage,passed);check(mob.hurt(world.damageSources().genericKill(),10000),"native-boss-plan-defeat-"+stage,passed);died.invoke(zones,constructor.newInstance(mob,null));mob.discard();
   var ended=zones.ledger.get(zone.id());long remaining=ended.readyAt()-System.currentTimeMillis();check(stage<3?ended.phase()==EncounterLedger.Phase.WAITING&&remaining>0&&remaining<=1000:ended.phase()==EncounterLedger.Phase.COMPLETE&&remaining>=9000&&remaining<=10000,"native-boss-cooldown-stage-"+stage,passed);
   var loaded=new EncounterLedger(server.getWorldPath(LevelResource.ROOT).resolve("data/zianmobs/encounters.json"));check(loaded.get(zone.id()).equals(ended),"native-boss-stage-persists-"+stage,passed);
  }
 }
}
