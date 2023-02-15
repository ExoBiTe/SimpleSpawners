package com.github.exobite.mc.simplespawners.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class GUIManager implements Listener {

    private static GUIManager instance;

    public static GUIManager getInstance() {
        return instance;
    }

    public static void register(JavaPlugin main) {
        if(instance!=null) return;
        instance = new GUIManager(main);
    }


    private final Map<Inventory, GUI> guis = new HashMap<>();


    private GUIManager(JavaPlugin main) {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    public GUI createGUI(String name, InventoryType invType) {
        GUI g = new GUI(this, name, invType);
        guis.put(g.getInv(), g);
        return g;
    }

    public GUI createGUI(String name, int size) {
        GUI g = new GUI(this, name, size);
        guis.put(g.getInv(), g);
        return g;
    }

    protected void removeGUI(GUI g) {
        guis.remove(g.getInv());
    }

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        Inventory clickedInv = e.getClickedInventory();
        Inventory topInv = e.getWhoClicked().getOpenInventory().getTopInventory();
        if(guis.containsKey(clickedInv)) {
            guis.get(clickedInv).clicked(e);
        } else if(clickedInv != topInv && guis.containsKey(topInv)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        if(!guis.containsKey(inv)) return;
        guis.get(inv).closed(e);
    }


}
