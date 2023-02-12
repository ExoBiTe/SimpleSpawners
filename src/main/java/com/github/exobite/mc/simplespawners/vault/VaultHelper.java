package com.github.exobite.mc.simplespawners.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultHelper {

    private static VaultHelper instance;

    public static VaultHelper getInstance() {
        return instance;
    }

    public static boolean register(JavaPlugin main) {
        if(instance!=null) return false;
        instance = new VaultHelper(main);
        return instance.eco != null;
    }

    private Economy eco;

    private VaultHelper(JavaPlugin main) {
        RegisteredServiceProvider<Economy> rsp = main.getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) return;
        eco = rsp.getProvider();
    }

    public boolean playerHasMoney(Player p, float amount) {
        if(eco==null) return false;
        return amount >= eco.getBalance(p);
    }

    public boolean withdrawMoney(Player p, float amount) {
        if(eco==null) return false;
        return eco.withdrawPlayer(p, amount).transactionSuccess();
    }


}
