package Phantom.extensions.org.bukkit.util.Vector;

import art.arcane.amulet.geometry.Vec3;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.bukkit.util.Vector;

@Extension
public class XVector {
  public static Vec3 toVec3(@This Vector self) {
    return new Vec3(self.getX(), self.getY(), self.getZ());
  }
}