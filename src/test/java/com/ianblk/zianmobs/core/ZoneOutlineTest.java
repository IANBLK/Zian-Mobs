package com.ianblk.zianmobs.core;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.ianblk.zianmobs.core.Definitions.Point;
class ZoneOutlineTest {
 @Test void includesExactExclusiveBoundsAndCorners(){var a=new Point(-8,60,-8,0);var b=new Point(8,67,8,0);var points=ZoneOutline.sample(a,b);assertTrue(points.contains(new ZoneOutline.Vertex(-8,60,-8)));assertTrue(points.contains(new ZoneOutline.Vertex(9,69,9)));assertEquals(points,ZoneOutline.sample(b,a));}
 @Test void largestAllowedAreaHasBoundedParticleWork(){var points=ZoneOutline.sample(new Point(-64,0,-64,0),new Point(64,63,64,0));assertTrue(points.size()<=220);assertTrue(points.stream().allMatch(p->p.x()>=-64&&p.x()<=65&&p.y()>=0&&p.y()<=65&&p.z()>=-64&&p.z()<=65));}
}
