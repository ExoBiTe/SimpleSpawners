package com.github.exobite.mc.simplespawners.economy;

import org.bukkit.entity.Player;

public interface IEconomy {

    boolean canBuy(Player p);

    String getPrice();

    boolean buy(Player p);

    boolean isFree();

}
