package com.github.exobite.mc.simplespawners.economy;

import com.github.exobite.mc.simplespawners.util.Config;
import com.github.exobite.mc.simplespawners.util.SpawnableEntity;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class EconManager {

    private static EconManager instance;

    public static EconManager getInstance() {
        return instance;
    }

    private final Map<SpawnableEntity, IEconomy> prices = new HashMap<>();
    private boolean useVault;

    private EconManager() {
        useVault = Config.getInstance().useVault();
        if(useVault) setupItemPrices();
        else setupVaultPrices();

    }

    private void setupItemPrices() {
        boolean defaultSet = false;
        ConfigurationSection cfg = Config.getInstance().getItemPrices();
        for(String key:cfg.getKeys(false)) {
            String[] d = cfg.getString(key).replace(" ", "").split(",");
            if(key.equalsIgnoreCase("default")) {
            }
        }
    }

    private void setupVaultPrices() {

    }

}
