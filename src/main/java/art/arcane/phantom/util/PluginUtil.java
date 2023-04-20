package art.arcane.phantom.util;

import art.arcane.curse.Curse;
import art.arcane.curse.model.CursedComponent;
import art.arcane.phantom.Phantom;
import art.arcane.phantom.core.PhantomAPIPlugin;
import com.google.common.graph.MutableGraph;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class PluginUtil {
    public static Stream<CursedComponent> allClasses() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .map(PluginUtil::allClasses).reduce(Stream::concat)
                .orElseGet(() -> allClasses(PhantomAPIPlugin.getInstance()));
    }

    public static Stream<CursedComponent> allClasses(Plugin p) {
        return Curse.all(p.getClass());
    }

    public static Plugin getOwningPlugin(Class<?> of) {
        return Curse.implemented(of, JavaPlugin.class).map(i -> {
                    for(Plugin k : Bukkit.getPluginManager().getPlugins()) {
                        if(k.getClass().equals(i.type())) {
                            return k;
                        }
                    }

                    return null;
                }).filter(Objects::nonNull)
                .filter(i -> !i.equals(PhantomAPIPlugin.getInstance()))
                .findFirst()
                .orElse(PhantomAPIPlugin.getInstance());
    }

    public static File getPluginFile(Plugin plugin) {
        try {
            return Curse.on(plugin).get("file");
        } catch(Throwable e) {

        }

        return getPluginFile(plugin.getName());
    }

    public static Plugin getLoadedPluginFromFile(File file) {
        return getPlugin(getPluginDescription(file).getName());
    }

    public static PluginDescriptionFile getPluginDescription(File file) {
        try {
            return PhantomAPIPlugin.getInstance().getPluginLoader().getPluginDescription(file);
        } catch (Throwable e) {
            e.printStackTrace();
            Phantom.warn("Failed to get plugin description for " + file.getName() + " (" + file.getAbsolutePath() + ")");
            Phantom.warn("Assuming its because it's a dummy plugin loader. Attempting to read the jar for a yaml directly.");
            try {
                return readPluginDescriptionDirectly(file);
            }

            catch(Throwable ex) {
                Phantom.error("Failed to load plugin description for " + file.getName() + " (" + file.getAbsolutePath() + ")");
                return null;
            }
        }
    }

    public static PluginDescriptionFile readPluginDescriptionDirectly(File jarFile) throws InvalidDescriptionException {
        JarFile jar = null;
        InputStream stream = null;

        try {
            jar = new JarFile(jarFile);
            JarEntry entry = jar.getJarEntry("plugin.yml");

            if (entry == null) {
                throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            stream = jar.getInputStream(entry);

            return new PluginDescriptionFile(stream);

        } catch (Throwable ex) {
            throw new InvalidDescriptionException(ex);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Plugin getPlugin(String query) {
        for (Plugin i : Bukkit.getPluginManager().getPlugins()) {
            if (i.getName().equalsIgnoreCase(query)) {
                return i;
            }
        }

        for (Plugin i : Bukkit.getPluginManager().getPlugins()) {
            if (i.getName().toLowerCase().startsWith(query.toLowerCase())) {
                return i;
            }
        }

        for (Plugin i : Bukkit.getPluginManager().getPlugins()) {
            if (i.getName().toLowerCase().contains(query.toLowerCase())) {
                return i;
            }
        }

        return null;
    }

    public static File getPluginFile(String query) {
        File f = new File("plugins/" + query + ".jar");

        if (f.exists()) {
            return f;
        }

        Map<File, PluginDescriptionFile> pdfs = new HashMap<>();

        for (File i : new File("plugins").listFiles()) {
            try {
                PluginDescriptionFile pdf = pdfs.computeIfAbsent(i, PluginUtil::getPluginDescription);

                if (pdf.getName().equalsIgnoreCase(query)) {
                    return i;
                }
            } catch (Throwable e) {

            }
        }

        for (File i : new File("plugins").listFiles()) {
            try {
                PluginDescriptionFile pdf = pdfs.computeIfAbsent(i, PluginUtil::getPluginDescription);

                if (pdf.getName().toLowerCase().startsWith(query.toLowerCase())) {
                    return i;
                }
            } catch (Throwable e) {

            }
        }

        for (File i : new File("plugins").listFiles()) {
            try {
                PluginDescriptionFile pdf = pdfs.computeIfAbsent(i, PluginUtil::getPluginDescription);

                if (pdf.getName().toLowerCase().contains(query.toLowerCase())) {
                    return i;
                }
            } catch (Throwable e) {

            }
        }

        return null;
    }

    public static void delete(Plugin pp) {
        String n = pp.getName();
        File f = getPluginFile(n);
        unload(pp);
        f.delete();
    }

    public static void reload(Plugin pp) {
        String n = pp.getName();
        File f = getPluginFile(n);
        unload(pp);
        load(f);
    }

    public static Plugin load(File file) {
        try {
            Plugin p = Bukkit.getPluginManager().loadPlugin(file);
            p.onLoad();
            Bukkit.getPluginManager().enablePlugin(p);
            return p;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void unload(Plugin plugin) {
        Phantom.info("Unloading " + plugin.getName() + " v" + plugin.getDescription().getVersion());
        PluginLoader loader = plugin.getPluginLoader();
        SimplePluginManager manager = (SimplePluginManager) Bukkit.getServer().getPluginManager();
        Phantom.info("Disabling " + plugin.getName() + " v" + plugin.getDescription().getVersion());
        manager.disablePlugin(plugin);
        Phantom.info("Disabled " + plugin.getName() + " v" + plugin.getDescription().getVersion());

        synchronized (manager) {
            Phantom.info("Unregistering Listeners for " + plugin.getName() + " v" + plugin.getDescription().getVersion());
            HandlerList.unregisterAll(plugin);
            Phantom.info("Cancelling Tasks for " + plugin.getName() + " v" + plugin.getDescription().getVersion());
            Bukkit.getScheduler().cancelTasks(plugin);
            List<Plugin> plugins = Curse.on(manager).get("plugins");
            Map<String, Plugin> lookupNames = Curse.on(manager).get("lookupNames");
            MutableGraph<String> dependencyGraph = Curse.on(manager).get("dependencyGraph");
            Map<String, Permission> permissions = Curse.on(manager).get("permissions");
            Map<Boolean, Set<Permission>> defaultPerms = Curse.on(manager).get("defaultPerms");

            if (plugins.remove(plugin)) {
                Phantom.info("Removed " + plugin.getName() + " from plugin list");
            } else {
                Phantom.warn("Couldn't find " + plugin.getName() + " in plugin list");
            }

            if (plugins.removeIf(i -> i.getClass().equals(plugin.getClass()))) {
                Phantom.info("Removed refs by class " + plugin.getName() + " from plugin list");
            } else {
                Phantom.warn("Couldn't find refs by class " + plugin.getName() + " in plugin list");
            }

            if (lookupNames.remove(plugin.getDescription().getName()) != null) {
                Phantom.info("Removed " + plugin.getName() + " from lookup names");
            } else {
                Phantom.warn("Couldn't find " + plugin.getName() + " in lookup names");
            }

            for (String i : new HashMap<>(lookupNames).keySet()) {
                if (lookupNames.get(i).getClass().equals(plugin.getClass())) {
                    lookupNames.remove(i);
                    Phantom.info("Removed '" + i + "' from lookup names");
                }
            }

            if (dependencyGraph.removeNode(plugin.getDescription().getName())) {
                Phantom.info("Removed " + plugin.getName() + " from dependency graph");
            } else {
                Phantom.warn("Couldn't find " + plugin.getName() + " in dependency graph");
            }
            dependencyGraph.edges().stream().filter(i ->
                            i.nodeU().equals(plugin.getDescription().getName())
                                    || i.nodeV().equals(plugin.getDescription().getName()))
                    .forEach(i -> {
                        if (dependencyGraph.removeEdge(i)) {
                            Phantom.info("Removed " + plugin.getName() + " from dependency graph edge " + i);
                        } else {
                            Phantom.warn("Couldn't find " + plugin.getName() + " in dependency graph edge " + i);
                        }
                    });
            Set<Permission> p = new HashSet<>(plugin.getDescription().getPermissions());

            for (String i : new HashSet<>(permissions.keySet())) {
                if (p.contains(permissions.get(i))) {
                    permissions.remove(i);
                    Phantom.info("Removed " + plugin.getName() + ":" + i + " from permissions");
                }
            }

            if (defaultPerms.get(true).removeAll(p)) {
                Phantom.info("Removed " + plugin.getName() + " from default perms TRUE");
            } else {
                Phantom.warn("Couldn't find " + plugin.getName() + " in default perms TRUE");
            }
            if (defaultPerms.get(false).removeAll(p)) {
                Phantom.info("Removed " + plugin.getName() + " from default perms FALSE");
            } else {
                Phantom.warn("Couldn't find " + plugin.getName() + " in default perms FALSE");
            }
        }

        synchronized (loader) {
            if (loader.getClass().getCanonicalName().equalsIgnoreCase("io.papermc.paper.plugin.manager.DummyBukkitPluginLoader")) {
                Phantom.warn("Paper detected, unload of " + plugin.getName() + "'s loader may partially fail");
            }

            try {
                List<?> loaders = Curse.on(loader).get("loaders");

                for (Object i : new ArrayList<>(loaders)) {
                    JavaPlugin p = Curse.on(i).get("plugin");
                    if (p != null) {
                        if (p.getClass().equals(plugin.getClass())) {
                            if (loaders.remove(i)) {
                                Phantom.info("Removed " + plugin.getName() + " from loaders");
                            } else {
                                Phantom.warn("Couldn't remove " + plugin.getName() + " in loaders?");
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                Phantom.error("Yup, looks like Paper is in use, and we can't unload " + plugin.getName() + "'s loader. This may cause issues with deleting the jar file or reloading it.");
            }
        }

        Phantom.info("Calling GC to unlock dangling file locks");
        System.gc();
        Phantom.info("Unloaded " + plugin.getName() + "!");
    }
}
