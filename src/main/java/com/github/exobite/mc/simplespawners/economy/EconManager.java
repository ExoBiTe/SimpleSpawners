package com.github.exobite.mc.simplespawners.economy;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.util.Config;
import com.github.exobite.mc.simplespawners.util.SpawnableEntity;
import com.github.exobite.mc.simplespawners.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.EnumMap;
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

    private EconManager() {
        setupItemPrices();
        ConfigurationSection cfgVault = Config.getInstance().getEconPrices();
        if(PluginMaster.getInstance().vaultRegistered()) {
            setupVaultPrices(cfgVault);
        }else if(!cfgVault.getKeys(false).isEmpty()){
            PluginMaster.sendConsoleMessage(Level.WARNING, "Didn't find an Vault and an Economy Plugin, disabling the Economy Prices.");
        }
    }

    private void setupItemPrices() {
        boolean defaultSet = false;
        ConfigurationSection cfg = Config.getInstance().getItemPrices();
        if(cfg==null) {
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "No 'ItemPrices' Section was found in your Config.yml found!\nUsing the Default Price.");
            defaultPrice = new ItemPrice(Material.EMERALD, 32);
            return;
        }
        for(String key:cfg.getKeys(false)) {
            boolean isDefault = key.equalsIgnoreCase("default");
            SpawnableEntity se = Utils.getEntityFromString(key);
            if(se==null && !isDefault) {
                PluginMaster.sendConsoleMessage(Level.WARNING, "Unknown Entity '"+key+"' in ItemPrices found!");
                continue;
            }
            ItemPrice ip = getItemPriceFromString(key, cfg.getString(key));
            if(ip==null) continue;
            if(isDefault) {
                defaultSet = true;
                defaultPrice = ip;
            }else{
                prices.put(se, ip);
            }
        }
        if(!defaultSet) {
            PluginMaster.sendConsoleMessage(Level.WARNING, "No Default Price found, setting it to EMERALD x32!");
            defaultPrice = new ItemPrice(Material.EMERALD, 32);
        }
    }

    private void setupVaultPrices(ConfigurationSection cfg) {
        if(cfg==null) {
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "No 'EconomyPrices' Section was found in your Config.yml found!");
            return;
        }
        for(String key:cfg.getKeys(false)) {
            boolean isDefault = key.equalsIgnoreCase("default");
            SpawnableEntity se = Utils.getEntityFromString(key);
            if(se==null && !isDefault) {
                PluginMaster.sendConsoleMessage(Level.WARNING, "Unknown Entity '"+key+"' in EconomyPrices found!");
                continue;
            }
            if(prices.containsKey(se)) PluginMaster.sendConsoleMessage(Level.WARNING,
                    "Duplicate Entry for Entity '"+key+"' found in EconomyPrices!\n" +
                            "Overwriting the Item Price with an Economy Price!");
            float price;
            try {
                price = Float.parseFloat(cfg.getString(key, "0"));
            }catch(NumberFormatException e) {
                PluginMaster.sendConsoleMessage(Level.WARNING,
                        "Unknown Number '"+cfg.getString(key)+"' for Entity '"+key+"' found in EconomyPrices!");
                continue;
            }
            VaultPrice vp = new VaultPrice(price);
            if(isDefault) {
                if(defaultPrice!=null) PluginMaster.sendConsoleMessage(Level.INFO,
                        "Duplicate default entry found.\nUsing the Economy Price.");
                defaultPrice = vp;
            }else{
                prices.put(se, vp);
            }
        }
    }

    private ItemPrice getItemPriceFromString(String key, String str) {
        if(str==null) return null;
        String[] d = str.replace(" ", "").split(",");
        if(d.length<2) return null;
        Material m;
        int amount;
        //Error checking
        try {
            m = Material.valueOf(d[0]);
        }catch(IllegalArgumentException e) {
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "Unknown item '"+d[0]+"' found for Entity '"+key+"' in ItemPrices!");
            return null;
        }
        try {
            amount = Integer.parseInt(d[1]);
        }catch(NumberFormatException e) {
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "Unknown amount '"+d[1]+"' found for Entity '"+key+"' in ItemPrices!");
            return null;
        }
        if(amount<0 || amount>64) {
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "Specify an amount from 0 to 64 for ItemPrice '"+d[0]+"' at Entity '"+key+"'!");
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
