package com.ianblk.zianmobs.client;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ResponsiveLayoutTest{
 @Test void scaleTwoThreeFourKeepPanelsAndClicksInsideViewport(){for(int[] size:new int[][]{{1920,1080},{1366,768},{1280,720},{854,480}})for(int gui:new int[]{2,3,4}){int w=(size[0]+gui-1)/gui,h=(size[1]+gui-1)/gui;var layout=ResponsiveLayout.fit(w,h,600,360);int left=(layout.width()-520)/2,top=(layout.height()-336)/2;for(double[] point:new double[][]{{left,top},{left+520,top+336},{left+12,top+308},{left+508,top+328}}){double x=layout.x()+point[0]*layout.scale(),y=layout.y()+point[1]*layout.scale();assertTrue(x>=-.01&&x<=w+.01);assertTrue(y>=-.01&&y<=h+.01);assertEquals(point[0],layout.mouseX(x),1e-8);assertEquals(point[1],layout.mouseY(y),1e-8);}assertTrue(layout.scale()>0&&layout.scale()<=1);}}
 @Test void invalidViewportRejected(){assertThrows(IllegalArgumentException.class,()->ResponsiveLayout.fit(0,270,600,360));}
}
