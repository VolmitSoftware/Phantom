package art.arcane.phantom.util;

import art.arcane.chrono.PrecisionStopwatch;
import art.arcane.phantom.core.PhantomAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class is intended to be used for plugins trying to ensure phantom is enabled and installed
 * do not declare phantom as a dependency or soft dependency! Copy this code into your project
 * and use it in your plugin, don't call any phantom classes in your main plugin class!
 */
public class PhantomBootstrap {
    private static final String PHANTOM_JAR = "https://something.com/Phantom.jar";

    public static void ensurePhantom(Plugin plugin) {
        if(!isPhantomInstalled()) {
            new Thread(() -> {
                File f = new File("plugins/Phantom.jar");

                if(f.exists()) {
                    f.delete();
                }

                download(PHANTOM_JAR, f);

                if(f.exists()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        try {
                            Bukkit.getPluginManager().loadPlugin(f);
                        } catch (InvalidPluginException | InvalidDescriptionException e) {
                            throw new RuntimeException(e);
                        }
                    },0);
                }
            }).start();
        }
    }

    private static void download(String url, File file) {
        if (file.exists()) {
            return;
        }

        file.getParentFile().mkdirs();
        PrecisionStopwatch p = PrecisionStopwatch.start();

        try {
            URL at = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(at.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static boolean isPhantomInstalled() {
        if(Bukkit.getPluginManager().getPlugin("Phantom") != null) {
            return true;
        }

        File pluginsFolder = new File("plugins");

        for(File i : pluginsFolder.listFiles()) {
            if(i.getName().endsWith(".jar")) {
                try {
                    PluginDescriptionFile pdf = getPluginDescription(i);

                    if(pdf != null) {
                        if(pdf.getName().equals("Phantom")) {
                            return true;
                        }
                    }
                }

                catch(Throwable e)
                {

                }
            }
        }

        return false;
    }

    private static PluginDescriptionFile getPluginDescription(File file) {
        try {
            return PhantomAPIPlugin.getInstance().getPluginLoader().getPluginDescription(file);
        } catch (Throwable e) {
            try {
                return readPluginDescriptionDirectly(file);
            }

            catch(Throwable ex) {
                return null;
            }
        }
    }

    private static PluginDescriptionFile readPluginDescriptionDirectly(File jarFile) throws InvalidDescriptionException {
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

                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
