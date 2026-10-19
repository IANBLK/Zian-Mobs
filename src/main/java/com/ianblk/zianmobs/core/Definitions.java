package com.ianblk.zianmobs.core;
import java.util.*;

public final class Definitions {
    private Definitions(){}
    public static void id(String id){if(id==null || !id.matches("[a-z0-9_]{1,32}"))throw new IllegalArgumentException("ID: usa 1–32 letras minúsculas, números o _");}
    public static void resource(String id){if(id==null || !id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))throw new IllegalArgumentException("Identificador inválido: "+id);}
    public record Point(double x,double y,double z,float yaw){public Point{if(!Double.isFinite(x+y+z+yaw) || Math.abs(x)>29999984 || Math.abs(z)>29999984 || y< -64 || y>320)throw new IllegalArgumentException("Posición fuera de límites");}}
    public record Effect(String id,int amplifier,int seconds){public Effect{resource(id);if(amplifier<0 || amplifier>10 || seconds<1 || seconds>86400)throw new IllegalArgumentException("Efecto inválido");}}
    public record MobSpec(String id,String name,String entity,double health,double damage,double armor,double toughness,double knockback,List<Effect> effects,Map<String,String> equipment,String loot,boolean boss){
        public MobSpec{Definitions.id(id);resource(entity);if(name==null || name.isBlank() || name.length()>80)throw new IllegalArgumentException("Nombre inválido");
            if(!Double.isFinite(health+damage+armor+toughness+knockback) || health<1 || health>1024 || damage<0 || damage>128 || armor<0 || armor>30 || toughness<0 || toughness>20 || knockback<0 || knockback>1)throw new IllegalArgumentException("Atributos fuera de rango");
            effects=List.copyOf(effects);equipment=Map.copyOf(equipment);if(effects.size()>8 || equipment.size()>6 || equipment.keySet().stream().anyMatch(s->!Set.of("mainhand","offhand","head","chest","legs","feet").contains(s)))throw new IllegalArgumentException("Equipo o efectos inválidos");
            if(loot==null)loot="";if(!loot.isEmpty())Definitions.id(loot);
        }
        public MobSpec equip(String slot,String item){var next=new LinkedHashMap<>(equipment);next.put(slot,item);return new MobSpec(id,name,entity,health,damage,armor,toughness,knockback,effects,next,loot,boss);}
    }
    public record LootEntry(String item,int weight,int min,int max){public LootEntry{if(item==null || item.isBlank() || item.length()>65536 || weight<1 || weight>10000 || min<1 || max<min || max>64)throw new IllegalArgumentException("Entrada de loot inválida");}}
    public record LootSpec(String id,String category,int rolls,List<LootEntry> entries,String table,boolean independent){public LootSpec(String id,String category,int rolls,List<LootEntry> entries,String table){this(id,category,rolls,entries,table,false);}public LootSpec{Definitions.id(id);if(!Set.of("MOB","BOSS","CHEST").contains(category))throw new IllegalArgumentException("Categoría inválida");entries=List.copyOf(entries);table=table==null?"":table;if(!table.isEmpty())resource(table);if(rolls<1 || rolls>32 || entries.size()>64)throw new IllegalArgumentException("Loot fuera de límites");if(independent && (!category.equals("MOB") || !table.isEmpty() || entries.size()>32 || entries.stream().anyMatch(e->e.weight()>100)))throw new IllegalArgumentException("Probabilidad real: solo mobs, hasta 32 objetos y porcentajes de 1 a 100; sin tabla externa");if(entries.isEmpty() && table.isEmpty())throw new IllegalArgumentException("Añade objetos o selecciona una loot table");}}
    public enum StageKind { WAVE, BOSS }
    public enum Population { EXACT, POOL }
    public record EncounterStage(StageKind kind,List<String> team,Population population){
        public EncounterStage(StageKind kind,List<String> team){this(kind,team,Population.EXACT);}
        public EncounterStage{population=population==null?Population.EXACT:population;if(kind==StageKind.BOSS&&population!=Population.EXACT)throw new IllegalArgumentException("El jefe se invoca una vez");if(kind==null||team==null||team.isEmpty()||team.size()>8||kind==StageKind.BOSS&&team.size()!=1)throw new IllegalArgumentException("Oleada: 1–8 criaturas. Jefe: exactamente una plantilla.");team=List.copyOf(team);team.forEach(Definitions::id);}
    }
    public record ZoneSpec(String id,String dimension,Point first,Point second,List<Point> points,List<String> team,int waves,int pauseSeconds,int respawnSeconds,String completionLoot,boolean enabled,Point center,int radiusX,int radiusZ,int above,int below,String name,String boss,List<List<String>> waveTeams,List<EncounterStage> stages,boolean autoSpawn){
        public ZoneSpec(String id,String dimension,Point first,Point second,List<Point> points,List<String> team,int waves,int pauseSeconds,int respawnSeconds,String completionLoot,boolean enabled,Point center,int radiusX,int radiusZ,int above,int below,String name,String boss,List<List<String>> waveTeams,List<EncounterStage> stages){this(id,dimension,first,second,points,team,waves,pauseSeconds,respawnSeconds,completionLoot,enabled,center,radiusX,radiusZ,above,below,name,boss,waveTeams,stages,false);}
        public ZoneSpec(String id,String dimension,Point first,Point second,List<Point> points,List<String> team,int waves,int pauseSeconds,int respawnSeconds,String completionLoot,boolean enabled,Point center,int radiusX,int radiusZ,int above,int below,String name,String boss,List<List<String>> waveTeams){this(id,dimension,first,second,points,team,waves,pauseSeconds,respawnSeconds,completionLoot,enabled,center,radiusX,radiusZ,above,below,name,boss,waveTeams,null);}
        public ZoneSpec(String id,String dimension,Point first,Point second,List<Point> points,List<String> team,int waves,int pauseSeconds,int respawnSeconds,String completionLoot,boolean enabled,Point center,int radiusX,int radiusZ,int above,int below,String name){this(id,dimension,first,second,points,team,waves,pauseSeconds,respawnSeconds,completionLoot,enabled,center,radiusX,radiusZ,above,below,name,"",List.of());}
        public ZoneSpec(String id,String dimension,Point first,Point second,List<Point> points,List<String> team,int waves,int pauseSeconds,int respawnSeconds,String completionLoot,boolean enabled,Point center,int radiusX,int radiusZ,int above,int below){this(id,dimension,first,second,points,team,waves,pauseSeconds,respawnSeconds,completionLoot,enabled,center,radiusX,radiusZ,above,below,id);}
        public ZoneSpec(String id,String dimension,Point first,Point second,List<Point> points,List<String> team,int waves,int pauseSeconds,int respawnSeconds,String completionLoot,boolean enabled){this(id,dimension,first,second,points,team,waves,pauseSeconds,respawnSeconds,completionLoot,enabled,null,0,0,0,0);}
        public ZoneSpec{
            if(stages!=null){stages=List.copyOf(stages);if(stages.size()>17)throw new IllegalArgumentException("Máximo 17 etapas por encuentro");}
            boss=boss==null?"":boss;if(!boss.isEmpty())Definitions.id(boss);
            waveTeams=waveTeams==null?List.of():waveTeams.stream().map(List::copyOf).toList();
            if(waveTeams.size()>16)throw new IllegalArgumentException("Máximo 16 composiciones de oleada");
            for(var row:waveTeams){if(row.size()>8)throw new IllegalArgumentException("Máximo 8 criaturas por oleada");row.forEach(Definitions::id);}
            name=name==null || name.isBlank()?id:name.trim();if(name.length()>64 || name.chars().anyMatch(Character::isISOControl))throw new IllegalArgumentException("Nombre de dungeon: máximo 64 caracteres, sin saltos de línea");if(center!=null){var expected=CenteredZone.bounds(center,radiusX,radiusZ,above,below);if(!expected.first().equals(first) || !expected.second().equals(second))throw new IllegalArgumentException("Los límites no coinciden con el centro y tamaño");}Definitions.id(id);resource(dimension);points=List.copyOf(points);team=List.copyOf(team);team.forEach(Definitions::id);completionLoot=completionLoot==null?"":completionLoot;if(!completionLoot.isEmpty())Definitions.id(completionLoot);
            if(waves<1 || waves>16 || pauseSeconds<1 || pauseSeconds>3600 || respawnSeconds<10 || respawnSeconds>2592000 || points.size()>16 || team.size()>8)throw new IllegalArgumentException("Zona fuera de límites");
            if(first!=null && second!=null && (Math.abs(first.x-second.x)>128 || Math.abs(first.z-second.z)>128 || Math.abs(first.y-second.y)>64))throw new IllegalArgumentException("Zona máxima: 128×64×128");
            var unique=new HashSet<String>();for(var p:points)if(!unique.add(((int)Math.floor(p.x))+":"+((int)Math.floor(p.y))+":"+((int)Math.floor(p.z))))throw new IllegalArgumentException("Cada punto debe estar en un bloque distinto");
            if(enabled && (first==null || second==null || (stages==null?team.isEmpty():stages.isEmpty())))throw new IllegalArgumentException("Crea la zona y selecciona al menos un tipo de mob");
        }
        /** null stages is the legacy plan; resolving it never changes its order or saved counters. */
        public List<EncounterStage> encounterStages(){
            if(stages!=null)return stages;
            var plan=new ArrayList<EncounterStage>();
            for(int n=0;n<waves;n++){var members=n<waveTeams.size()&&!waveTeams.get(n).isEmpty()?waveTeams.get(n):team;if(!members.isEmpty())plan.add(new EncounterStage(StageKind.WAVE,members));}
            if(!boss.isEmpty())plan.add(new EncounterStage(StageKind.BOSS,List.of(boss)));
            return List.copyOf(plan);
        }
        public int totalStages(){return stages==null?waves+(boss.isEmpty()?0:1):stages.size();}
        public boolean bossStage(int stage){return stage>=1&&stage<=totalStages()&&(stages==null?!boss.isEmpty()&&stage==totalStages():stages.get(stage-1).kind()==StageKind.BOSS);}
        public List<String> teamForStage(int stage){
            if(stage<1||stage>totalStages())throw new IllegalArgumentException("Etapa fuera de rango");
            return stages!=null?stages.get(stage-1).team():bossStage(stage)?List.of(boss):stage<=waveTeams.size()&&!waveTeams.get(stage-1).isEmpty()?waveTeams.get(stage-1):team;
        }
        public int waveCount(){return stages==null?waves:(int)stages.stream().filter(s->s.kind()==StageKind.WAVE).count();}
        public String stageLabel(int stage){if(stage<1||stage>totalStages())return "Etapa no disponible";if(bossStage(stage))return stage==totalStages()?"Jefe final":stage==1?"Jefe inicial":"Jefe · etapa "+stage;int n=stages==null?stage:(int)stages.subList(0,stage).stream().filter(s->s.kind()==StageKind.WAVE).count();return "Oleada "+n+" de "+waveCount();}
        public Set<String> referencedMobs(){var ids=new LinkedHashSet<String>();if(stages!=null)stages.forEach(s->ids.addAll(s.team()));else{ids.addAll(team);waveTeams.forEach(ids::addAll);if(!boss.isEmpty())ids.add(boss);}return Set.copyOf(ids);}
        public ZoneSpec withStages(List<EncounterStage> plan){return new ZoneSpec(id,dimension,first,second,points,team,waves,pauseSeconds,respawnSeconds,completionLoot,enabled,center,radiusX,radiusZ,above,below,name,boss,waveTeams,plan,autoSpawn);}
        public ZoneSpec plan(String boss,List<List<String>> teams){if(stages!=null)throw new IllegalArgumentException("Esta zona usa etapas ordenadas: edítalas en Zonas → Encuentro.");return new ZoneSpec(id,dimension,first,second,points,team,waves,pauseSeconds,respawnSeconds,completionLoot,enabled,center,radiusX,radiusZ,above,below,name,boss,teams);}
        public boolean contains(String dimension,double x,double y,double z){return this.dimension.equals(dimension) && first!=null && second!=null && x>=Math.min(first.x,second.x) && x<Math.max(first.x,second.x)+1 && y>=Math.min(first.y,second.y) && y<Math.max(first.y,second.y)+2 && z>=Math.min(first.z,second.z) && z<Math.max(first.z,second.z)+1;}
    }
}
