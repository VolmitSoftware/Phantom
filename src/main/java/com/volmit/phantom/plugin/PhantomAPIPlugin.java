package com.volmit.phantom.plugin;

import com.volmit.phantom.Phantom;
import com.volmit.phantom.api.service.PhantomService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PhantomAPIPlugin extends JavaPlugin implements Phantom {
    public static PhantomAPIPlugin plugin;
    private static final List<PhantomService> activeServices = new CopyOnWriteArrayList<>();

    public PhantomAPIPlugin()
    {
        super();
        plugin = this;
    }

    public void onEnable()
    {

    }

    public void onDisable()
    {
        activeServices.forEach((i) -> {
            try {
                i.stop();
            }

            catch(Throwable e)
            {
                e.printStackTrace();
            }
        });
        activeServices.clear();
    }

    @Override
    public PhantomAPIPlugin plugin() {
        return this;
    }
}
