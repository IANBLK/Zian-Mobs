package com.ianblk.zianmobs.core;
import java.util.*;
import com.ianblk.zianmobs.core.Definitions.Point;
/** Bounded samples of the exact half-open region used by ZoneSpec.contains. */
public final class ZoneOutline {
 public record Vertex(double x,double y,double z){}
 public static List<Vertex> sample(Point first,Point second){
  double x0=Math.min(first.x(),second.x()),x1=Math.max(first.x(),second.x())+1,y0=Math.min(first.y(),second.y()),y1=Math.max(first.y(),second.y())+2,z0=Math.min(first.z(),second.z()),z1=Math.max(first.z(),second.z())+1;
  double step=Math.max(1,4*((x1-x0)+(y1-y0)+(z1-z0))/180);var points=new LinkedHashSet<Vertex>();
  for(double y:new double[]{y0,y1})for(double z:new double[]{z0,z1})edge(points,new Vertex(x0,y,z),new Vertex(x1,y,z),step);
  for(double x:new double[]{x0,x1})for(double z:new double[]{z0,z1})edge(points,new Vertex(x,y0,z),new Vertex(x,y1,z),step);
  for(double x:new double[]{x0,x1})for(double y:new double[]{y0,y1})edge(points,new Vertex(x,y,z0),new Vertex(x,y,z1),step);
  return List.copyOf(points);
 }
 private static void edge(Set<Vertex> out,Vertex a,Vertex b,double step){int count=Math.max(1,(int)Math.ceil((Math.abs(b.x-a.x)+Math.abs(b.y-a.y)+Math.abs(b.z-a.z))/step));for(int i=0;i<=count;i++){double f=(double)i/count;out.add(new Vertex(a.x+(b.x-a.x)*f,a.y+(b.y-a.y)*f,a.z+(b.z-a.z)*f));}}
 private ZoneOutline(){}
}
