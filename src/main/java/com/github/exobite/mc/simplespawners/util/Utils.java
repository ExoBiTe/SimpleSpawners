package com.github.exobite.mc.simplespawners.util;

import com.github.exobite.mc.simplespawners.PluginMaster;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Utils {

    private static PluginMaster main;

    public static void registerUtils(PluginMaster mainInstance) {
        if(main != null) return;
        main = mainInstance;
    }

    private Utils() {}

    public static boolean updateConfigurationFile(String filename) {
        boolean setComments = VersionHelper.isEqualOrLarger(VersionHelper.getBukkitVersion(), new Version(1, 18, 0));
        File f = new File(main.getDataFolder()+File.separator+filename);
        boolean changedFile = false;
        if(!f.exists()) {
            main.saveResource(filename, true);
            changedFile = true;
        }else{
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(f);
            YamlConfiguration defaultConf = getDefaultConfiguration(filename);
            //Iterate through all visible keys
            for(String key:defaultConf.getKeys(true)) {
                if(conf.get(key)==null) {
                    conf.set(key, defaultConf.get(key));
                    if(!setComments) continue;
                    conf.setComments(key, defaultConf.getComments(key));
                    conf.setInlineComments(key, defaultConf.getInlineComments(key));
                    changedFile = true;
                }
            }
            if(changedFile) {
                try {
                    conf.save(f);
                } catch (IOException e) {
                    PluginMaster.sendConsoleMessage(Level.SEVERE, "Couldn't update the File "+filename+"!");
                    e.printStackTrace();
                }
            }
        }
        return changedFile;
    }

    private static YamlConfiguration getDefaultConfiguration(String filename) {
        InputStream is = main.getResource(filename);
        if(is==null) {
            //Is handled with a runnable, as it is unknown in which Thread we are.
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().disablePlugin(main);
                }
            }.runTask(main);
            throw new IllegalArgumentException("Embedded File "+filename+" not found!\nIs the Jar Modified?");
        }
        return YamlConfiguration.loadConfiguration(new InputStreamReader(is));
    }

    public static int countMatches(String toSearch, String match) {
        //Example: "abc.abc.abc.def", "def"
        // length = 15, newLength = 12, diff 3 division by length of match = 1
        return (toSearch.length() - toSearch.replace(match, "").length()) / match.length();
    }

    public static SpawnableEntity getEntityFromString(String str) {
        try {
            return SpawnableEntity.valueOf(str);
        }catch(IllegalArgumentException e) {
            return null;
        }
    }

}
