package com.github.exobite.mc.simplespawners.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemPrice implements IEconomy {

    private final Material m;
    private final int amount;

    public ItemPrice(Material m, int amount) {
        this.m = m;
        this.amount = amount;
    }

    @Override
    public boolean canBuy(Player p) {
        if(isFree()) return true;
        int a = 0;
        for(ItemStack is:p.getInventory().getContents()) {
            if(is==null) continue;
            if(is.getType()==m) a+= is.getAmount();
        }
        return a >= amount;
    }

    @Override
    public boolean buy(Player p) {
        if(!canBuy(p)) return false;
        if(isFree()) return true;
        //Remove Items from Inventory
        int amountLeft = amount;
        Inventory inv = p.getInventory();
        for(ItemStack is:inv.getContents()) {
            if(is==null || is.getType()!=m) continue;
            if(amountLeft<=0) break;
            if((amountLeft-is.getAmount())<0) {
                is.setAmount(is.getAmount() - amountLeft);
                amountLeft = 0;
            }else{
                amountLeft -= is.getAmount();
                inv.remove(is);
            }
        }
        return true;
    }

    @Override
    public String getPrice() {
        return m.toString()+" x"+amount;
    }

    @Override
    public boolean isFree() {
        return amount==0 || m==null;
    }

}
