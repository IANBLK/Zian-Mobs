package com.ianblk.zianmobs;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class NpcProfilesTest {
 @Test void skinsAndDefaultsAreValid(){assertEquals(7,NpcProfiles.SKINS.size());for(var skin:NpcProfiles.SKINS)assertEquals(skin.id(),NpcProfiles.defaults("npc_"+skin.id()).skin());}
 @Test void invalidCombatParametersAreRejected(){assertThrows(IllegalArgumentException.class,()->new NpcProfiles.Spec("missing",false,true,30,160,20,6,true,true,true,true));assertThrows(IllegalArgumentException.class,()->new NpcProfiles.Spec("abyss_guard",false,true,0,160,20,6,true,true,true,true));assertThrows(IllegalArgumentException.class,()->new NpcProfiles.Spec("abyss_guard",false,true,30,160,Double.NaN,6,true,true,true,true));assertThrows(IllegalArgumentException.class,()->new NpcProfiles.Spec("abyss_guard",false,true,30,160,20,100,true,true,true,true));}
 @Test void selectedSkillsAreDeterministicAndOptional(){assertTrue(NpcProfiles.defaults("").skills().isEmpty());assertEquals(java.util.List.of("SONIC","WITHER"),new NpcProfiles.Spec("abyss_guard",false,true,30,160,20,6,true,false,true,false).skills());}
}
