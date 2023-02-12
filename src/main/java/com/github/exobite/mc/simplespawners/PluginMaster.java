package com.github.exobite.mc.simplespawners;

import com.github.exobite.mc.simplespawners.command.SimpleSpawnersCmd;
import com.github.exobite.mc.simplespawners.gui.GUIManager;
import com.github.exobite.mc.simplespawners.playerdata.PlayerDataManager;
import com.github.exobite.mc.simplespawners.util.*;
import com.github.exobite.mc.simplespawners.listener.DebugListener;
import com.github.exobite.mc.simplespawners.listener.PlayerInteraction;
import com.github.exobite.mc.simplespawners.vault.VaultHelper;
import com.github.exobite.mc.simplespawners.web.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class PluginMaster extends JavaPlugin {

    private static final int BSTATS_ID = 17666;
    private static final long UPDATE_CHECK_INTERVAL = 20L * 3600 * 12;  //12 Hours

    private static PluginMaster instance;

    public static PluginMaster getInstance() {
        return instance;
    }

    private boolean useVault;
    private boolean usePapi;

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
        Utils.registerUtils(this);
        Msg.registerMessages();
        CustomSound.registerSoundLibrary(this);
        PlayerDataManager.register(this);
        GUIManager.register(this);
        setupExternals();
        Config.setupConfig(this).loadConfig(false);
        SpawnableEntity.init();
        getServer().getPluginManager().registerEvents(new DebugListener(), this);
        interactInst = new PlayerInteraction(this);
        getServer().getPluginManager().registerEvents(interactInst, this);
        getServer().getPluginCommand("simplespawners").setExecutor(new SimpleSpawnersCmd());
        enableMetrics();
        //TODO: Add checkForUpdate when RESOURCE_ID is known!
        checkForUpdate();
        sendConsoleMessage(Level.INFO, "Running (took "+(System.currentTimeMillis()-t1)+"ms)!");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    private void setupExternals() {
        if(getServer().getPluginManager().getPlugin("Vault") != null) {
            useVault = VaultHelper.register(this);
        }
    }

    private void enableMetrics() {
        if(!Config.getInstance().allowMetrics()) return;
        new Metrics(this, BSTATS_ID);
    }

    private void checkForUpdate() {
        if(!Config.getInstance().checkForUpdate()) return;
        final JavaPlugin inst = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(UpdateChecker.getInstance()==null) {
                    UpdateChecker.createUpdateChecker(inst, false);
                }
                UpdateChecker.getInstance().start(false);
            }
        }.runTaskTimerAsynchronously(this, 20L, UPDATE_CHECK_INTERVAL);
    }

    public PlayerInteraction getInteractInst() {
        return interactInst;
    }

    public boolean useVault() {
        return useVault;
    }

    public boolean usePapi() {
        return usePapi;
    }

}
