package com.github.exobite.mc.simplespawners.playerdata;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager implements Listener {

    private static PlayerDataManager instance;

    public static PlayerDataManager getInstance() {
        return instance;
    }

    public static void register(JavaPlugin mainInst) {
        if(instance!=null) return;
        instance = new PlayerDataManager(mainInst);
    }

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    private PlayerDataManager(JavaPlugin mainInst) {
        mainInst.getServer().getPluginManager().registerEvents(this, mainInst);
    }

    public PlayerData getPlayerData(UUID id) {
        PlayerData pd = playerDataMap.get(id);
        if(pd==null) {
            pd = new PlayerData(id);
            playerDataMap.put(id, pd);
        }
        return pd;
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        PlayerData pd = playerDataMap.get(e.getPlayer().getUniqueId());
        if(pd!=null) {
            pd.quit();
            playerDataMap.remove(e.getPlayer().getUniqueId());
        }
    }

}
