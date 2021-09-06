package Phantom.extensions.org.bukkit.Location;

import art.arcane.amulet.geometry.Vec;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.bukkit.Location;

@Extension
public abstract class XLocation implements Vec{
  public static double x(@This Location l) {
    return l.getX();
  }

  public static Location x(@This Location l, double v) {
    l.setX(v);
    return l;
  }

  public static double y(@This Location l) {
    return l.getY();
  }

  public static Location y(@This Location l, double v) {
    l.setY(v);
    return l;
  }

  public static double z(@This Location l) {
    return l.getZ();
  }

  public static Location z(@This Location l, double v) {
    l.setZ(v);
    return l;
  }
}