package com.ianblk.zianmobs;
import java.util.*;
public final class NpcProfiles {
 public record Skin(String id,String name,boolean slim){}
 public static final List<Skin> SKINS=List.of(new Skin("abyss_guard","Guardián abisal",false),new Skin("eclipse_sentinel","Centinela del eclipse",false),new Skin("glacial_duelist","Espadachín glacial",false),new Skin("solar_knight","Caballero solar",false),new Skin("frost_specter","Espectro de escarcha",false),new Skin("emerald_guard","Guardián esmeralda",false),new Skin("obsidian_knight","Caballero de obsidiana",false));
 public record Spec(String skin,boolean slim,boolean hostile,int attackCooldown,int skillCooldown,double range,double skillDamage,boolean sonic,boolean dragon,boolean wither,boolean blaze){
  public Spec{if(SKINS.stream().noneMatch(s->s.id().equals(skin))||attackCooldown<20||attackCooldown>400||skillCooldown<40||skillCooldown>2400||!Double.isFinite(range+skillDamage)||range<4||range>32||skillDamage<0||skillDamage>64)throw new IllegalArgumentException("NPC: skin válida; golpe 20–400 ticks; habilidad 40–2400 ticks; alcance 4–32; daño especial 0–64");}
  public List<String> skills(){var result=new ArrayList<String>();if(sonic)result.add("SONIC");if(dragon)result.add("DRAGON");if(wither)result.add("WITHER");if(blaze)result.add("BLAZE");return result;}
 }
 public static Spec defaults(String template){String skin=template.startsWith("npc_")?template.substring(4):"abyss_guard";if(!SKINS.stream().map(Skin::id).toList().contains(skin))skin="abyss_guard";return new Spec(skin,false,true,30,160,20,6,false,false,false,false);}
 private NpcProfiles(){}
}
