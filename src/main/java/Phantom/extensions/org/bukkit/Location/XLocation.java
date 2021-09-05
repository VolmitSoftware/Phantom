package Phantom.extensions.org.bukkit.Location;

import art.arcane.amulet.geometry.Vec3;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.bukkit.Location;

@Extension
public class XLocation {
  public static Vec3 toVec3(@This Location self) {
    return new Vec3(self.getX(), self.getY(), self.getZ());
  }
}