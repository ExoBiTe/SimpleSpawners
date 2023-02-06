package com.github.exobite.mc.simplespawners;

import com.github.exobite.mc.simplespawners.gui.GUIManager;
import com.github.exobite.mc.simplespawners.playerdata.PlayerDataManager;
import com.github.exobite.mc.simplespawners.util.*;
import com.github.exobite.mc.simplespawners.listener.DebugListener;
import com.github.exobite.mc.simplespawners.listener.PlayerInteraction;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class PluginMaster extends JavaPlugin {

    private static PluginMaster instance;

    public static PluginMaster getInstance() {
        return instance;
    }

    private PlayerInteraction interactInst;

    public static void sendConsoleMessage(Level level, String msg){
        String[] parts = msg.split("\n");
        for (String part : parts) {
            instance.getLogger().log(level, part);
        }
    }

    @Override
    public void onEnable() {
        long t1 = System.currentTimeMillis();
        instance = this;
        SpawnableEntity.init();
        Utils.registerUtils(this);
        Msg.registerMessages();
        CustomSound.registerSoundLibrary(this);
        PlayerDataManager.register(this);
        GUIManager.register(this);
        Config.setupConfig(this).loadConfig(false);
        getServer().getPluginManager().registerEvents(new DebugListener(), this);
        interactInst = new PlayerInteraction(this);
        getServer().getPluginManager().registerEvents(interactInst, this);
        sendConsoleMessage(Level.INFO, "Running (took "+(System.currentTimeMillis()-t1)+"ms)!");
    }

    @Override
    public void onDisable() {
    }

    public PlayerInteraction getInteractInst() {
        return interactInst;
    }

}
