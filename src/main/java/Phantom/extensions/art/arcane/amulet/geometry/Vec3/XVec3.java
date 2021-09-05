package Phantom.extensions.art.arcane.amulet.geometry.Vec3;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import art.arcane.amulet.geometry.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

@Extension
public class XVec3 {
  public static Location location(@This Vec3 self, World world) {
    return new Location(world, self.getX(), self.getY(), self.getZ());
  }

  public static Vector vector(@This Vec3 self) {
    return new Vector(self.getX(), self.getY(), self.getZ());
  }

  public static BlockVector blockVector(@This Vec3 self) {
    return new BlockVector(self.getX(), self.getY(), self.getZ());
  }
}