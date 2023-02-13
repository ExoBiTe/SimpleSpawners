package com.github.exobite.mc.simplespawners.economy;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.util.Config;
import com.github.exobite.mc.simplespawners.util.SpawnableEntity;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class EconManager {

    private static EconManager instance;

    public static EconManager getInstance() {
        return instance;
    }

    public static void register() {
        if(instance!=null) return;
        instance = new EconManager();
    }

    private final EnumMap<SpawnableEntity, IEconomy> prices = new EnumMap<>(SpawnableEntity.class);
    private IEconomy defaultPrice;
    private final boolean useVault;

    private EconManager() {
        useVault = Config.getInstance().useVault();
        if(useVault) setupVaultPrices();
        else setupItemPrices();

    }

    private void setupItemPrices() {
        boolean defaultSet = false;
        ConfigurationSection cfg = Config.getInstance().getItemPrices();
        for(String key:cfg.getKeys(false)) {
            boolean isDefault = key.equalsIgnoreCase("default");
            if(!entityExists(key) && !isDefault) {
                PluginMaster.sendConsoleMessage(Level.WARNING, "Unknown Entity '"+key+"' in ItemPrices found!");
                continue;
            }
            ItemPrice ip = getItemPriceFromString(cfg.getString(key));
            if(ip==null) continue;
            if(isDefault) {
                defaultSet = true;
                defaultPrice = ip;
            }else{
                prices.put(SpawnableEntity.valueOf(key), ip);
            }
        }
        if(!defaultSet) {
            PluginMaster.sendConsoleMessage(Level.WARNING, "No Default Price found, setting it to EMERALD x32!");
            defaultPrice = new ItemPrice(Material.EMERALD, 32);
        }
    }

    private void setupVaultPrices() {

    }

    private boolean entityExists(String str) {
        try {
            SpawnableEntity.valueOf(str);
            return true;
        }catch(IllegalArgumentException e) {
            return false;
        }
    }

    private ItemPrice getItemPriceFromString(String str) {
        if(str==null) return null;
        String[] d = str.replace(" ", "").split(",");
        if(d.length<2) return null;
        Material m;
        int amount;
        //Error checking
        try {
            m = Material.valueOf(d[0]);
        }catch(IllegalArgumentException e) {
            PluginMaster.sendConsoleMessage(Level.WARNING, "Unknown item '"+d[0]+"' found in ItemPrices!");
            return null;
        }
        try {
            amount = Integer.parseInt(d[1]);
        }catch(NumberFormatException e) {
            PluginMaster.sendConsoleMessage(Level.WARNING, "Unknown number '"+d[1]+"' found in ItemPrices!");
            return null;
        }
        if(amount<0 || amount>64) {
            PluginMaster.sendConsoleMessage(Level.WARNING, "Specify an amount from 0 to 64 for ItemPrice '"+d[0]+"'!");
            return null;
        }
        return new ItemPrice(m, amount);
    }

    public IEconomy getPrice(SpawnableEntity en) {
        if(en==null) return defaultPrice;
        IEconomy ie = prices.get(en);
        return ie != null ? ie : defaultPrice;
    }

}
