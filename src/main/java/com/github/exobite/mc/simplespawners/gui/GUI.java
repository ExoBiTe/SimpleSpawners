package com.github.exobite.mc.simplespawners.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GUI {

    private final Inventory inv;
    private final GUIManager manager;
    private final Map<Integer, GUIClickAction> actions = new HashMap<>();
    private GUICloseAction onCloseAction;
    private boolean ignoreNextClose;

    protected GUI(GUIManager manager, String name, InventoryType invType) {
        this.manager = manager;
        inv = Bukkit.createInventory(null, invType, name);
    }

    protected GUI(GUIManager manager, String name, int size) {
        this.manager = manager;
        inv = Bukkit.createInventory(null, size, name);
    }

    protected void clicked(InventoryClickEvent e) {
        int slot = e.getSlot();
        if(!actions.containsKey(slot)) return;
        e.setCancelled(true);
        actions.get(slot).clicked(e);
    }

    protected void closed(InventoryCloseEvent e) {
        if(onCloseAction==null) return;
        if(ignoreNextClose) {
            ignoreNextClose = false;
            return;
        }
        onCloseAction.closed(e);
    }

    public GUI setSlotAction(int slot, GUIClickAction action) {
        actions.put(slot, action);
        return this;
    }

    public GUI setItem(int slot, ItemStack is) {
        inv.setItem(slot, is);
        return this;
    }

    public GUI setItemWithAction(int slot, ItemStack is, GUIClickAction action) {
        setSlotAction(slot, action);
        setItem(slot, is);
        return this;
    }

    public GUI setOnCloseAction(GUICloseAction action) {
        this.onCloseAction = action;
        return this;
    }

    public void removeGUI() {
        inv.clear();
        manager.removeGUI(this);
    }

    public void ignoreNextClose() {
        ignoreNextClose = true;
    }

    protected Inventory getInv() {
        return inv;
    }

    public void openInventory(Player p) {
        p.openInventory(inv);
    }



}
