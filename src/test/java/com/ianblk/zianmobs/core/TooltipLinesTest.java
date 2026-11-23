package com.ianblk.zianmobs.core;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.ianblk.zianmobs.core.TooltipLines.Line;
import java.util.List;

class TooltipLinesTest {
 private static List<String> keys(List<Line> lines){return lines.stream().map(Line::key).toList();}
 @Test void titanCollapsedShowsOnlyBasicsAndHint(){assertEquals(List.of("tooltip.zianmobs.titan.name","tooltip.zianmobs.titan.slots","tooltip.zianmobs.shift"),keys(TooltipLines.titan(false,600,900,60,45)));}
 @Test void titanExpandedConvertsTicksToSeconds(){var lines=TooltipLines.titan(true,600,900,60,45);assertEquals(List.of("tooltip.zianmobs.titan.name","tooltip.zianmobs.titan.slots","tooltip.zianmobs.titan.trigger","tooltip.zianmobs.titan.partial","tooltip.zianmobs.titan.full"),keys(lines));assertEquals(List.of(30,60),lines.get(3).args());assertEquals(List.of(45,45),lines.get(4).args());}
 @Test void titanTextFollowsTheValuesPassedIn(){var lines=TooltipLines.titan(true,400,1000,10,20);assertEquals(List.of(20,10),lines.get(3).args());assertEquals(List.of(50,20),lines.get(4).args());}
 @Test void staffCollapsedAndExpandedByKind(){assertEquals(List.of("tooltip.zianmobs.ability","tooltip.zianmobs.cooldown","tooltip.zianmobs.shift"),keys(TooltipLines.staff(false,"WITHER",30)));var lines=TooltipLines.staff(true,"DRAGON",30);assertEquals(List.of("tooltip.zianmobs.ability","tooltip.zianmobs.cooldown","tooltip.zianmobs.staff.dragon"),keys(lines));assertEquals(List.of(30),lines.get(1).args());assertEquals("tooltip.zianmobs.staff.sonic",TooltipLines.staff(true,"SONIC",5).get(2).key());}
}
