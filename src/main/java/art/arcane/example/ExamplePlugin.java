package art.arcane.example;

import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {
    public void onEnable() {
        // PhantomBootstrap is NOT a part of phantom
        // Its copied code from phantom to "install" phantom in the
        // event that its not already
        PhantomBootstrap.ensurePhantom(this);
    }
}
