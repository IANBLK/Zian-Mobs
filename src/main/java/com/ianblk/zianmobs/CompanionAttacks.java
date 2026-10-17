package com.ianblk.zianmobs;
import java.util.*;
import java.nio.charset.StandardCharsets;
/** Source animation release markers, rounded to Minecraft's 20 Hz server clock. */
public final class CompanionAttacks {
 public record Profile(String clip,int duration,List<Integer> impacts){public Profile{impacts=List.copyOf(impacts);if(clip==null||duration<0||impacts.isEmpty()||impacts.stream().anyMatch(t->t<0||t>duration))throw new IllegalArgumentException("Invalid companion attack timeline");}}
 public static final Map<String,Profile> PROFILES=load();
 private static Map<String,Profile> load(){try(var in=CompanionAttacks.class.getResourceAsStream("/assets/zianmobs/companion-attacks.json")){var type=new com.google.gson.reflect.TypeToken<Map<String,Profile>>(){}.getType();return Map.copyOf(new com.google.gson.Gson().fromJson(new java.io.InputStreamReader(in,StandardCharsets.UTF_8),type));}catch(Exception ex){throw new ExceptionInInitializerError(ex);}}
 public static Profile profile(String id){return PROFILES.getOrDefault(id,new Profile("",0,List.of(0)));}
 static final class Pending {final UUID target;final int started;final Profile profile;final float damage;int released;Pending(UUID target,int started,Profile profile,float damage){this.target=target;this.started=started;this.profile=profile;this.damage=damage;}}
 private CompanionAttacks(){}
}
