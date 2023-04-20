package art.arcane.phantom.core.service;

import art.arcane.phantom.api.service.PhantomService;
import art.arcane.phantom.api.tick.Ticker;

public class PhantomTickService implements PhantomService {
    private Ticker ticker;

    @Override
    public void onStart() {
        ticker = new Ticker();
    }

    @Override
    public void onStop() {
        ticker.clear();
    }

    public Ticker getTicker() {
        return ticker;
    }
}
