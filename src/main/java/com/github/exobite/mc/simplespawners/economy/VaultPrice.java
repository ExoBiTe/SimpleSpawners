package com.github.exobite.mc.simplespawners.economy;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.vault.VaultHelper;
import org.bukkit.entity.Player;

public class VaultPrice implements IEconomy {

    private final float cost;

    public VaultPrice(float cost) {
        this.cost = Math.max(cost, 0f);
    }

    @Override
    public boolean canBuy(Player p) {
        if(!PluginMaster.getInstance().vaultRegistered()) return false;
        if(isFree()) return true;
        return VaultHelper.getInstance().playerHasMoney(p, cost);
    }

    @Override
    public boolean buy(Player p) {
        if(!canBuy(p)) return false;
        if(isFree()) return true;
        return VaultHelper.getInstance().withdrawMoney(p, cost);
    }

    @Override
    public String getPrice() {
        if(!PluginMaster.getInstance().vaultRegistered()) return "ERROR";
        return VaultHelper.getInstance().formatCurrency(cost);
    }

    @Override
    public boolean isFree() {
        return cost <= 0;
    }

}
