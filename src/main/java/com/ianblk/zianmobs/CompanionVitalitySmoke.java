package com.ianblk.zianmobs;
import com.google.gson.Gson;
import com.ianblk.zianmobs.core.AtomicJson;
import java.nio.file.Files;
/** Checks both fresh balance and one-time migration of existing customized data. */
final class CompanionVitalitySmoke {
 static void check(boolean ok,String message){if(!ok)throw new IllegalStateException(message);}
 static void run(Companions service)throws Exception {
  var defaults=Companions.defaults();
  for(String id:CompanionContent.IDS){var c=defaults.settings().get(id);check(c.maxHealth()==(id.equals("kit")?45:id.equals("ghost")?24:36)&&c.armor()==6&&c.attackDamage()==4,"Fresh vitality baseline: "+id);}
  var gson=new Gson();var old=gson.toJsonTree(service.data).getAsJsonObject();old.addProperty("schema",3);
  for(var entry:old.getAsJsonObject("settings").entrySet()){var row=entry.getValue().getAsJsonObject();row.addProperty("maxHealth",row.get("maxHealth").getAsDouble()/1.5);row.addProperty("armor",row.get("armor").getAsDouble()/1.5);}
  var custom=old.getAsJsonObject("settings").getAsJsonObject("kit");custom.addProperty("maxHealth",40);custom.addProperty("armor",8);custom.addProperty("attackDamage",7);
  var missing=old.getAsJsonObject("settings").getAsJsonObject("ghost");missing.remove("maxHealth");missing.remove("armor");
  var path=service.file.resolveSibling("vitality-migration-smoke.json");AtomicJson.write(path,old);byte[] original=Files.readAllBytes(path);
  var upgraded=Companions.readData(path);check(upgraded.schema()==4,"Vitality schema");
  check(upgraded.owners().equals(service.data.owners()),"Vitality migration changed grants, levels or deadlines");
  check(upgraded.settings().get("kit").maxHealth()==60&&upgraded.settings().get("kit").armor()==12&&upgraded.settings().get("kit").attackDamage()==7,"Customized baseline not preserved and increased once");
  check(upgraded.settings().get("ghost").maxHealth()==24&&upgraded.settings().get("ghost").armor()==6,"Missing legacy settings doubled boost");
  for(String id:CompanionContent.IDS){var before=service.config(id);var after=upgraded.settings().get(id);check(after.attackCooldown()==before.attackCooldown()&&after.supportDuration()==before.supportDuration()&&after.supportCooldown()==before.supportCooldown()&&after.reviveSeconds()==before.reviveSeconds(),"Vitality changed waits: "+id);if(!id.equals("kit"))check(after.attackDamage()==before.attackDamage(),"Vitality changed damage: "+id);}
  var backup=path.resolveSibling(path.getFileName()+".before-companion-vitality.json");check(java.util.Arrays.equals(original,Files.readAllBytes(backup)),"Vitality backup not byte exact");
  AtomicJson.write(path,upgraded);check(Companions.readData(path).equals(upgraded),"Vitality boost repeated on reload");
 }
}
