package com.volmit.phantom;

import com.volmit.phantom.plugin.PhantomAPIPlugin;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public interface Phantom {
    static Phantom api()
    {
        return PhantomAPIPlugin.plugin;
    }

    PhantomAPIPlugin plugin();

    default void register()
    {
        register(this);
    }

    default String name()
    {
        return getClass().getSimpleName().replaceAbs("Service", "");
    }

    default Server server()
    {
        return plugin().getServer();
    }

    default PluginManager pluginManager()
    {
        return plugin().getServer().getPluginManager();
    }

    default void registerListener(Listener l)
    {
        pluginManager().registerEvents(l, plugin());
    }

    default void register(Object registerable)
    {
        if(registerable instanceof Listener l)
        {
            registerListener(l);
        }
    }
}
