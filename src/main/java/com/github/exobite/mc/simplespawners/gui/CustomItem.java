package com.github.exobite.mc.simplespawners.gui;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class CustomItem {

    public static CustomItem getGUICloseButton() {
        return new CustomItem(Material.BARRIER).setDisplayName(ChatColor.RED + "Exit");
    }

    public static CustomItem getGUINextPageButton() {
        return new CustomItem(Material.EMERALD).setDisplayName(ChatColor.GOLD + "Next Page");
    }

    public static CustomItem getGUIPrevPageButton() {
        return new CustomItem(Material.REDSTONE).setDisplayName(ChatColor.GOLD + "Previous Page");
    }

    private ItemStack is;

    public CustomItem(ItemStack is) {
        this.is = is;
    }

    public CustomItem(Material m) {
        this.is = new ItemStack(m, 1);
    }

    public CustomItem(Material m, int amount) {
        this.is = new ItemStack(m, amount);
    }

    public CustomItem setLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        if(im!=null) {
            im.setLore(lore);
            is.setItemMeta(im);
        }
        return this;
    }

    public CustomItem setLore(String lore) {
        return setLore(Collections.singletonList(lore));
    }

    public CustomItem setDisplayName(String name) {
        ItemMeta im = is.getItemMeta();
        if(im!=null) {
            im.setDisplayName(name);
            is.setItemMeta(im);
        }
        return this;
    }

    public ItemStack getItemStack() {
        return is;
    }



}
