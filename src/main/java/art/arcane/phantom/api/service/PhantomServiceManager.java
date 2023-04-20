package art.arcane.phantom.api.service;

import art.arcane.phantom.api.registry.Registry;
import art.arcane.phantom.core.PhantomAPIPlugin;
import art.arcane.phantom.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PhantomServiceManager implements Listener {
    private final List<Plugin> plugins;
    private final Registry<Class<?>, PhantomServiceContainer> containerRegistry;

    public PhantomServiceManager() {
        containerRegistry = new Registry<>();
        plugins = new ArrayList<>();

        for(Plugin i : Bukkit.getPluginManager().getPlugins()) {
            setupPlugin(i);
        }
    }

    public void setupPlugin(Plugin p) {
        if(plugins.contains(p)) {
            return;
        }

        plugins.add(p);
        PluginUtil.allClasses(p)
                .filter(i -> !i.type().isInterface())
                .filter((i) -> i.type().isAssignableFrom(PhantomService.class) || PhantomService.class.isAssignableFrom(i.type()))
                .map(i -> new PhantomServiceContainer((PhantomService) i.construct()))
                .forEach(i -> containerRegistry.register(i.getSilent().getClass(), i));

        for(PhantomServiceContainer i : containerRegistry.values()) {
            if(i.getSilent().shouldAutoStart()) {
                i.get();
            }
        }
    }

    @EventHandler
    public void on(PluginEnableEvent e) {
        setupPlugin(e.getPlugin());
    }

    @EventHandler
    public void on(PluginDisableEvent e) {
        plugins.remove(e.getPlugin());
    }

    public void start() {
        Bukkit.getPluginManager().registerEvents(this, PhantomAPIPlugin.getInstance());
    }

    public void stop() {
        HandlerList.unregisterAll(this);
    }

    public PhantomServiceContainer get(Class<? extends PhantomService> s) {
        return containerRegistry.get(s);
    }
}
