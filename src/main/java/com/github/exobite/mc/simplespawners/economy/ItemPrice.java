package com.github.exobite.mc.simplespawners.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemPrice implements IEconomy {

    private final Material m;
    private final int amount;

    public ItemPrice(Material m, int amount) {
        this.m = m;
        this.amount = amount;
    }

    public boolean canBuy(Player p) {
        int a = 0;
        for(ItemStack is:p.getInventory().getContents()) {
            if(is==null) continue;
            if(is.getType()==m) a+= is.getAmount();
        }
        return a >= amount;
    }

}
