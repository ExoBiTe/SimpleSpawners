package com.github.exobite.mc.simplespawners.gui;

import org.bukkit.entity.HumanEntity;
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

    protected void removeGUI(GUI g) {
        guis.remove(g.getInv());
        /*for(HumanEntity he:g.getInv().getViewers()) {
            he.closeInventory();
        }*/
    }

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if(!guis.containsKey(inv)) return;
        guis.get(inv).clicked(e);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        if(!guis.containsKey(inv)) return;
        guis.get(inv).closed(e);
    }


}
