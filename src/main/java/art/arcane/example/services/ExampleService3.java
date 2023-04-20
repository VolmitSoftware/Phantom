package art.arcane.example.services;

import art.arcane.phantom.api.service.PhantomService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// Implements auto start
// This service is started the second your plugin enables
// Unlike other services, which only start when accessed (lazily)
public class ExampleService3 implements PhantomService.AutoStart {
    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
