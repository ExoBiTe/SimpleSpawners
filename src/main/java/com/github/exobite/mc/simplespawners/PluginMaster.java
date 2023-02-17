package com.github.exobite.mc.simplespawners;

import com.github.exobite.mc.simplespawners.command.SimpleSpawnersCmd;
import com.github.exobite.mc.simplespawners.economy.EconManager;
import com.github.exobite.mc.simplespawners.gui.GUIManager;
import com.github.exobite.mc.simplespawners.playerdata.PlayerDataManager;
import com.github.exobite.mc.simplespawners.util.*;
import com.github.exobite.mc.simplespawners.listener.DebugListener;
import com.github.exobite.mc.simplespawners.listener.PlayerInteraction;
import com.github.exobite.mc.simplespawners.vault.VaultHelper;
import com.github.exobite.mc.simplespawners.web.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
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
    private boolean writeCommentsToFile;

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
        checkForVersion();
        Utils.registerUtils(this);
        Msg.registerMessages();
        CustomSound.registerSoundLibrary(this);
        PlayerDataManager.register(this);
        GUIManager.register(this);
        setupExternals();
        Config.setupConfig(this).loadConfig(false);
        SpawnableEntity.init();
        EconManager.register();
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
            if(!useVault) sendConsoleMessage(Level.SEVERE, "Found Vault but no Economy Plugin!");
        }
        usePapi = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    private void enableMetrics() {
        if(!Config.getInstance().allowMetrics()) return;
        Metrics bs = new Metrics(this, BSTATS_ID);
        bs.addCustomChart(new SimplePie("uses_vault", () -> String.valueOf(useVault)));
    }

    private void checkForUpdate() {
        if(!Config.getInstance().checkForUpdate()) return;
        final JavaPlugin inst = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(UpdateChecker.getInstance()==null) {
                    UpdateChecker.createUpdateChecker(inst);
                }
                UpdateChecker.getInstance().start(false);
            }
        }.runTaskTimerAsynchronously(this, 20L, UPDATE_CHECK_INTERVAL);
    }

    private void checkForVersion() {
        writeCommentsToFile = VersionHelper.isEqualOrLarger(VersionHelper.getBukkitVersion(), new Version(1, 18, 0));
        if(!writeCommentsToFile) sendConsoleMessage(Level.WARNING,
                """
                Minecraft Version < 1.18 detected:
                This plugin may write to it's config Files, resulting in removal of the Comments
                in the YAML Files. Consider upgrading your Server Version to MC1.18 or newer,
                in order for this not to happen anymore.
                """);
    }

    public PlayerInteraction getInteractInst() {
        return interactInst;
    }

    public boolean vaultRegistered() {
        return useVault;
    }

    public boolean usePapi() {
        return usePapi;
    }

    public boolean writeCommentsToFile() {
        return writeCommentsToFile;
    }

}
