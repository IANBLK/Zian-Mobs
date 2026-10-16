package com.ianblk.zianmobs.core;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class AutomaticIdsTest {
 @Test void namesWithAccentsAndSpacesAreSafe(){assertEquals("zone_rey_del_hielo",AutomaticIds.create("zone","Rey del hielo",Set.of()));assertEquals("mob_senor_del_vacio",AutomaticIds.create("mob","Señor del Vacío",Set.of()));}
 @Test void duplicateVisibleNamesDoNotOverwriteDefinitions(){assertEquals("zone_prueba_3",AutomaticIds.create("zone","Prueba",Set.of("zone_prueba","zone_prueba_2")));}
 @Test void longNamesRemainWithinLimitsWhenSuffixNeeded(){String name="a".repeat(80);String first=AutomaticIds.create("mob",name,Set.of());String second=AutomaticIds.create("mob",name,Set.of(first));assertEquals(32,second.length());assertNotEquals(first,second);assertDoesNotThrow(()->Definitions.id(second));}
 @Test void symbolsOnlyStillProduceAValidIdentifier(){assertEquals("zone_nuevo",AutomaticIds.create("zone","✨!!!",Set.of()));}
}
