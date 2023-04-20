package art.arcane.example.services;

import art.arcane.phantom.Phantom;
import art.arcane.phantom.api.service.PhantomService;
import art.arcane.phantom.api.tick.TickedObject;

// This service will auto start!
public class ExampleService4 extends TickedObject implements PhantomService.AutoStart {
    public ExampleService4() {
        super("example", "service-name", 100);
        // Tick 10 per second
    }

    @Override
    public void onStart() {
        Phantom.get(ExampleService1.class).someUtilityMethod();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onTick() {

    }
}
