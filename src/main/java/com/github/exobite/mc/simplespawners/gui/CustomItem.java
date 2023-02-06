package com.github.exobite.mc.simplespawners.gui;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomItem {

    private String name;
    private List<String> lore;
    private Material mat;
    private final int amount;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();

    //TODO: Refactor this class to not lose ItemMeta Data when the ItemStack Constructor is used.

    public CustomItem(Material Mat) {
        this.name = Mat.name();
        this.lore = null;
        this.mat = Mat;
        this.amount = 1;
    }

    public CustomItem(String Name, List<String> Lore, Material Mat, int Amount) {
        this.name = Name;
        this.lore = Lore;
        this.mat = Mat;
        this.amount = Amount;
    }

    public CustomItem(ItemStack is) {
        amount = is.getAmount();
        mat = is.getType();
        enchantments = is.getEnchantments();
        if(is.getItemMeta()!=null) {
            lore = is.getItemMeta().getLore();
        }
    }

    public CustomItem addEnchantment(Enchantment e, int lv) {
        enchantments.put(e, lv);
        return this;
    }

    public CustomItem setDisplayName(String displayName) {
        this.name = displayName;
        return this;
    }

    public CustomItem setLore(List<String> Lore) {
        this.lore = Lore;
        return this;
    }

    public CustomItem setLore(String ... loreRows) {
        if(loreRows!=null) {
            this.lore = Arrays.asList(loreRows);
        }
        return this;
    }

    public CustomItem setLoreFromString(String lore) {
        String[] splits = lore.split("\n");
        if(splits.length>0) {
            this.lore = Arrays.asList(splits);
        }
        return this;
    }

    public CustomItem setMaterial(Material mat) {
        this.mat = mat;
        return this;
    }

    public ItemStack getItemStack() {
        ItemStack is = new ItemStack(mat, amount/*, data*/);
        ItemMeta im = is.getItemMeta();
        if(im==null) return is;
        if(name !=null) im.setDisplayName(ChatColor.RESET + name);
        if(lore !=null) im.setLore(lore);
        for(Map.Entry<Enchantment, Integer> e: enchantments.entrySet()) {
            im.addEnchant(e.getKey(), e.getValue(), true);
        }
        is.setItemMeta(im);
        return is;
    }
}
