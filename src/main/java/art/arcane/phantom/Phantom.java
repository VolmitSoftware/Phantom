package art.arcane.phantom;

import art.arcane.phantom.api.service.PhantomService;
import art.arcane.phantom.core.PhantomAPIPlugin;
import art.arcane.phantom.util.PluginUtil;

public class Phantom {
    public static <T extends PhantomService> T get(Class<T> serviceClass) {
        return (T) PhantomAPIPlugin.getInstance().getServiceContainer(serviceClass).get();
    }

    public static void log(Object f)
    {
        String plugin = PluginUtil.whodis().getName();
        System.out.println(plugin + ": " + f);
    }

    public static void warn(Object f)
    {

    }

    public static void error(Object f)
    {

    }

    public static void info(Object f)
    {

    }

    public static void verbose(Object f) {
    }
}
