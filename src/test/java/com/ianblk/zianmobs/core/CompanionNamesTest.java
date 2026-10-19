package com.ianblk.zianmobs.core;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class CompanionNamesTest {
 @Test void namesAreScopedToOwnerAndCompanion(){var a=UUID.randomUUID();var b=UUID.randomUUID();var old=CompanionNames.empty();var n=old.rename(a,"kit","Luna").rename(b,"kit","Sol");assertEquals("Luna",n.name(a,"kit"));assertEquals("Sol",n.name(b,"kit"));assertEquals("",n.name(a,"ghost"));assertTrue(old.owners().isEmpty());}
 @Test void restoreOnlyClearsSelectedNickname(){var a=UUID.randomUUID();var n=CompanionNames.empty().rename(a,"kit","Luna").rename(a,"ghost","Nube").rename(a,"kit","  ");assertEquals("",n.name(a,"kit"));assertEquals("Nube",n.name(a,"ghost"));}
 @Test void spanishAndUnicodeNamesRoundTrip(){var a=UUID.randomUUID();var n=CompanionNames.empty().rename(a,"kit","  Pequeña Estrella ✨  ");assertEquals("Pequeña Estrella ✨",n.name(a,"kit"));assertEquals(n,new Gson().fromJson(new Gson().toJson(n),CompanionNames.class));}
 @Test void lengthCountsUnicodeCharacters(){assertEquals(32,CompanionNames.normalize("🐾".repeat(32)).codePointCount(0,64));assertThrows(IllegalArgumentException.class,()->CompanionNames.normalize("a".repeat(33)));}
 @Test void formattingControlsAndBrokenUnicodeAreRejected(){for(String s:List.of("§aAdmin","A\nB","A\u202eB","A\u0000B","A\ud800B"))assertThrows(IllegalArgumentException.class,()->CompanionNames.normalize(s));}
 @Test void lastResetRemovesEmptyOwnerAndEmptyFileHasNoGameState(){var a=UUID.randomUUID();assertTrue(CompanionNames.empty().rename(a,"kit","Luna").rename(a,"kit","").owners().isEmpty());assertThrows(IllegalArgumentException.class,()->new CompanionNames(2,Map.of()));}
}
