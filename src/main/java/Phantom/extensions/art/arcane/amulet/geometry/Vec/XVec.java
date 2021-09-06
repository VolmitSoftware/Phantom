package Phantom.extensions.art.arcane.amulet.geometry.Vec;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import art.arcane.amulet.geometry.Vec;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;


@Extension
public class XVec {
  public static Location location(@This Vec self, World world) {
    return new Location(world, self.x(), self.y(), self.z());
  }

  public static Vector vector(@This Vec self) {
    return new Vector(self.x(), self.y(), self.z());
  }

  public static BlockVector blockVector(@This Vec self) {
    return new BlockVector(self.floor().vector());
  }
}