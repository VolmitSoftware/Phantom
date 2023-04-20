package art.arcane.phantom.api.service;

import art.arcane.phantom.api.service.PhantomService;
import art.arcane.phantom.api.service.ServiceState;
import art.arcane.phantom.util.PluginTools;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PhantomServiceContainer {
    private final PhantomService service;
    private final Plugin plugin;
    private ServiceState state;

    public PhantomServiceContainer(PhantomService service) {
        this.service = service;
        this.state = ServiceState.OFFLINE;
        this.plugin = PluginTools.getOwningPlugin(service.getClass());
    }

    public PhantomService get() {
        if(state == ServiceState.OFFLINE) {
            start();
        }

        return getSilent();
    }

    public PhantomService getSilent() {
        return service;
    }

    public void restart() {
        if(state == ServiceState.FAULT) {
            start();
            return;
        }

        stop();
        start();
    }

    public void start() {
        if (state == ServiceState.OFFLINE) {
            state = ServiceState.STARTING;
            try {
                service.onStart();

                if(service instanceof Listener l) {
                    Bukkit.getPluginManager().registerEvents(l, plugin);
                }

                state = ServiceState.ONLINE;
            } catch (Throwable e) {
                state = ServiceState.FAULT;
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (state == ServiceState.ONLINE) {
            state = ServiceState.STOPPING;
            try {
                if(service instanceof Listener l) {
                    HandlerList.unregisterAll(l);
                }

                service.onStop();
                state = ServiceState.OFFLINE;
            } catch (Throwable e) {
                state = ServiceState.FAULT;
                e.printStackTrace();
            }
        }
    }
}
