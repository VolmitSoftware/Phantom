package art.arcane.example.services;

import art.arcane.phantom.api.service.PhantomService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExampleService1 implements PhantomService {
    private List<String> someData;

    @Override
    public void onStart() {
        // Init in onStart
        // onStart is always called before this service is returned
        // to whoever requested it
        someData = new ArrayList<>();
    }

    @Override
    public void onStop() {
        someData.clear();
    }

    public void someUtilityMethod() {
        Collections.shuffle(someData);
    }
}
