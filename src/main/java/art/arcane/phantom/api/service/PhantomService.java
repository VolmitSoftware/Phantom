package art.arcane.phantom.api.service;

public interface PhantomService {
    default String getServiceId() {
        return getClass().getSimpleName().toLowerCase().replaceAll("service", "");
    }

    void onStart();

    void onStop();

    default boolean shouldAutoStart() {
        return false;
    }

    interface AutoStart extends PhantomService {
        default boolean shouldAutoStart() {
            return true;
        }
    }
}
