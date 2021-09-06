package com.volmit.phantom.api.service;

import com.volmit.phantom.Phantom;
import com.volmit.phantom.plugin.PhantomAPIPlugin;
import org.bukkit.event.Listener;

public abstract class PhantomService implements Phantom, Listener
{
    public PhantomService()
    {
        register();
        start();
    }

    public abstract void start();

    public abstract void stop();

    @Override
    public PhantomAPIPlugin plugin() {
        return PhantomAPIPlugin.plugin;
    }
}
