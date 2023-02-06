package com.github.exobite.mc.simplespawners.util;

import com.github.exobite.mc.simplespawners.PluginMaster;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public enum CustomSound {

    SPAWNER_DROPPED_INTO_INV(Sound.ENTITY_ITEM_PICKUP, 1, 1,
            "Played when a mined spawner is dropped into the Miners Inventory");

    private static final String FILE_NAME = "sounds.yml";
    private static PluginMaster main;
    private static File soundFile;

    public static void registerSoundLibrary(PluginMaster main) {
        if(CustomSound.main!=null) return; //If main isn't null, SoundLibrary has already been loaded
        CustomSound.main = main;
        soundFile = new File(main.getDataFolder() + File.separator + FILE_NAME);
        loadSoundData();
    }

    private static void loadSoundData() {
        boolean fileIsUpToDate = false;
        if(!soundFile.exists()) {
            createNewSoundFile();
            fileIsUpToDate = true;
        }
        if(!fileIsUpToDate) {
            //Check if all Values exist in Cfg, if not add them.
            readFileDataAndUpdateFile();
        }
    }

    private static void readFileDataAndUpdateFile() {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(soundFile);
        Set<String> existingKeys = conf.getKeys(false);
        boolean changedConfig = false;
        for(CustomSound sound: CustomSound.values()) {
            if(!existingKeys.contains(sound.toString())) {
                //Sound doesn't exist in Cfg, add it
                conf.set(sound + ".sound" , sound.getSound().toString());
                conf.set(sound + ".v1" , sound.getV1());
                conf.set(sound + ".v2" , sound.getV2());
                conf.setComments(sound.toString(), stringToList(sound.comment));
                if(!changedConfig) changedConfig = true;
            }else{
                //Sound exists in Cfg, read Data from it
                sound.readSoundDataFromConfigSection(conf.getConfigurationSection(sound.toString()));
            }
        }
        if(changedConfig) {
            try {
                conf.save(soundFile);
                PluginMaster.sendConsoleMessage(Level.INFO, "Updated '"+FILE_NAME+"!");
            } catch (IOException e) {
                PluginMaster.sendConsoleMessage(Level.SEVERE, "Couldn't update '"+FILE_NAME+"'. Using defaults for now.");
                e.printStackTrace();
            }
        }
    }

    private static void createNewSoundFile() {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(soundFile);
        for(CustomSound sound: CustomSound.values()) {
            conf.set(sound + ".sound" , sound.getSound().toString());
            conf.set(sound + ".v1" , sound.getV1());
            conf.set(sound + ".v2" , sound.getV2());
            conf.setComments(sound.toString(), stringToList(sound.comment));
        }
        try {
            conf.save(soundFile);
            PluginMaster.sendConsoleMessage(Level.INFO, "Created a new '"+FILE_NAME+"' File!");
        } catch (IOException e) {
            PluginMaster.sendConsoleMessage(Level.SEVERE, "Error while trying to write 'sounds.yml'!");
            e.printStackTrace();
        }
    }

    private static List<String> stringToList(String s) {
        List<String> l = new ArrayList<>();
        l.add(null);
        if(s.contains("\n")) {
            String[] split = s.split("\n");
            l.addAll(Arrays.asList(split));
        }else{
            l.add(s);
        }
        return l;
    }

    private Sound sound;
    private float v1;
    private float v2;
    private final String comment;

    CustomSound(Sound sound, float v1, float v2, String comment) {
        this.sound = sound;
        this.v1 = v1;
        this.v2 = v2;
        this.comment = comment;
    }

    private void readSoundDataFromConfigSection(ConfigurationSection conf) {
        if(conf==null) return;
        float newV1 = (float) conf.getDouble("v1", -1);
        float newV2 = (float) conf.getDouble("v2", -1);
        Sound newSound;
        try {
            newSound = Sound.valueOf(conf.getString("sound", "none"));
        }catch(IllegalArgumentException e){
            newSound = null;
        }
        if(newSound==null || newV1<0 || newV2<0) {
            PluginMaster.sendConsoleMessage(Level.WARNING, "Sound "+this+" contains an Error in '"+FILE_NAME+"'!");
            return;
        }
        this.v1 = newV1;
        this.v2 = newV2;
        this.sound = newSound;
    }

    public void playSound(Location loc) {
        if(loc==null || loc.getWorld()==null) return;
        loc.getWorld().playSound(loc, sound, v1, v2);
    }

    public Sound getSound() {
        return sound;
    }

    public float getV1() {
        return v1;
    }

    public float getV2() {
        return v2;
    }



}
