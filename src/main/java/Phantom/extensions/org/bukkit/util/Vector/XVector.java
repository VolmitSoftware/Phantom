package Phantom.extensions.org.bukkit.util.Vector;

import art.arcane.amulet.geometry.Vec;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.bukkit.util.Vector;

@Extension
public abstract class XVector implements Vec {
  public static double x(@This Vector l) {
    return l.getX();
  }

  public static Vector x(@This Vector l, double v) {
    l.setX(v);
    return l;
  }

  public static double y(@This Vector l) {
    return l.getY();
  }

  public static Vector y(@This Vector l, double v) {
    l.setY(v);
    return l;
  }

  public static double z(@This Vector l) {
    return l.getZ();
  }

  public static Vector z(@This Vector l, double v) {
    l.setZ(v);
    return l;
  }
}