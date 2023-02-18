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
        setupPrices();
    }

    private void setupPrices() {
        boolean defaultSet = false;
        boolean vaultLoaded = PluginMaster.getInstance().vaultRegistered();
        ConfigurationSection cfg = Config.getInstance().getEntityPrices();
        if(cfg==null) {
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "No 'EconomyPrices' Section was found in your Config.yml found!\nUsing the Default Price.");
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
            IEconomy price = createPrice(key, cfg.getString(key), vaultLoaded);
            if(price==null) continue;
            if(isDefault) {
                defaultSet = true;
                defaultPrice = price;
            }else{
                prices.put(se, price);
            }
        }
        if(!defaultSet) {
            PluginMaster.sendConsoleMessage(Level.WARNING, "No Default Price found, setting it to EMERALD x32!");
            defaultPrice = new ItemPrice(Material.EMERALD, 32);
        }
    }

    private IEconomy createPrice(String key, String d, boolean vaultRegistered) {
        if(d==null) return null;
        String[] data = d.replace(" ", "").split(",");
        IEconomy price = null;
        //ItemPrice
        if(data.length>1) {
            //Item Price
            price = getItemPriceFromString(key, data);
        }else if(data.length==1 && vaultRegistered){
            //Vault Price & Registered
            price = getVaultPriceFromString(key, d);
        }else if(data.length==1){
            //Vault not registered
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "Provided an Economy Price for Entity '"+key+"', but Vault isn't loaded!");
        }else{
            //Error
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "An unknown Error occurred by parsing the Price for '"+key+"'!");
        }
        return price;
    }

    private ItemPrice getItemPriceFromString(String key, String[] str) {
        if(str==null) return null;
        Material m;
        int amount;
        //Error checking
        try {
            m = Material.valueOf(str[0]);
        }catch(IllegalArgumentException e) {
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "Unknown item '"+str[0]+"' found for Entity '"+key+"' in ItemPrices!");
            return null;
        }
        try {
            amount = Integer.parseInt(str[1]);
        }catch(NumberFormatException e) {
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "Unknown amount '"+str[1]+"' found for Entity '"+key+"' in ItemPrices!");
            return null;
        }
        if(amount<0 || amount>64) {
            PluginMaster.sendConsoleMessage(Level.WARNING,
                    "Specify an amount from 0 to 64 for ItemPrice '"+str[0]+"' at Entity '"+key+"'!");
            return null;
        }
        return new ItemPrice(m, amount);
    }

    public IEconomy getPrice(SpawnableEntity en) {
        if(en==null) return defaultPrice;
        IEconomy ie = prices.get(en);
        return ie != null ? ie : defaultPrice;
    }

    private VaultPrice getVaultPriceFromString(String key, String data) {
        if(data==null) return null;
        float cost;
        try {
            cost = Float.parseFloat(data);
        }catch (NumberFormatException e) {
            PluginMaster.sendConsoleMessage(Level.WARNING, "Unknown price for Entity '"+key+"'!");
            return null;
        }
        return new VaultPrice(cost);
    }

}
