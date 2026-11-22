package com.ianblk.zianmobs;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CompanionPublicAccessTest{
 @Test void bridgesZianmobsRootWithoutTouchingCompanionRoots(){assertTrue(CompanionPublicAccess.nodes().contains("minecraft.command.zianmobs"));assertFalse(CompanionPublicAccess.ROOTS.contains("zianmobs"));for(String root:CompanionPublicAccess.ROOTS)assertTrue(CompanionPublicAccess.nodes().contains("minecraft.command."+root));assertEquals(CompanionPublicAccess.ROOTS.size()+CompanionPublicAccess.BRIDGE_ROOTS.size(),CompanionPublicAccess.nodes().size());}
}
