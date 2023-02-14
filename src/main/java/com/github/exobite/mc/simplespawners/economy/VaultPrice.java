package com.github.exobite.mc.simplespawners.economy;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.vault.VaultHelper;
import org.bukkit.entity.Player;

public class VaultPrice implements IEconomy {

    private final float cost;

    public VaultPrice(float cost) {
        this.cost = cost;
    }

    @Override
    public boolean canBuy(Player p) {
        if(!PluginMaster.getInstance().useVault()) return false;
        return VaultHelper.getInstance().playerHasMoney(p, cost);
    }

    @Override
    public boolean buy(Player p) {
        if(!canBuy(p)) return false;
        return VaultHelper.getInstance().withdrawMoney(p, cost);
    }

    @Override
    public String getPrice() {
        if(!PluginMaster.getInstance().useVault()) return "ERROR";
        return VaultHelper.getInstance().formatCurrency(cost);
    }

}
