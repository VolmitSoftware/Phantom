package art.arcane.example.services;

import art.arcane.phantom.api.service.PhantomService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// Make sure to implement listener
public class ExampleService2 implements PhantomService, Listener {
    @Override
    public void onStart() {
        // Events auto-registered
    }

    @Override
    public void onStop() {
        // Events auto-unregistered
    }

    @EventHandler
    public void on(PlayerJoinEvent e)
    {
        // Events are registered and unregistered
        // But only when the service is started or stopped
    }
}
