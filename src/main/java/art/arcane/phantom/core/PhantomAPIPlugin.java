package art.arcane.phantom.core;

import art.arcane.phantom.api.service.PhantomService;
import art.arcane.phantom.api.service.PhantomServiceContainer;
import art.arcane.phantom.api.service.PhantomServiceManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class PhantomAPIPlugin extends JavaPlugin {
    private static PhantomAPIPlugin instance;
    private PhantomServiceManager serviceManager;

    public static PhantomAPIPlugin getInstance() {
        return instance;
    }

    public PhantomServiceContainer getServiceContainer(Class<? extends PhantomService> s) {
        return serviceManager.get(s);
    }

    public void onLoad() {
        serviceManager = new PhantomServiceManager();
    }

    public void onEnable() {
        serviceManager.start();
    }

    public void onDisable() {
        serviceManager.stop();
    }
}
