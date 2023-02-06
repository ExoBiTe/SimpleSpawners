package com.github.exobite.mc.simplespawners.playerdata;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.gui.GUI;
import com.github.exobite.mc.simplespawners.gui.GUIManager;
import com.github.exobite.mc.simplespawners.util.BlockLoc;
import com.github.exobite.mc.simplespawners.util.SpawnableEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerData {

    private static final long SPAWNER_OPEN_MENU_DELAY_MS = 500L;

    private final UUID id;
    private GUI[] spawnerMenu;
    private BlockLoc openMenuLoc;
    private long nextSpawnerInteractAllowed;

    protected PlayerData(UUID id) {
        this.id = id;
    }

    public boolean openSpawnerMenu(CreatureSpawner sp, BlockLoc bl) {
        if(System.currentTimeMillis()<nextSpawnerInteractAllowed) return false;
        nextSpawnerInteractAllowed = System.currentTimeMillis() + SPAWNER_OPEN_MENU_DELAY_MS;
        //Chest Size is 27, last Row is reserved, so 18
        //Valid Entities / 27 + 0.5 are the Inventory Pages needed
        openMenuLoc = bl;
        int pagesNeeded = Math.round(SpawnableEntity.getValidAmount() / 18f + 0.5f);
        spawnerMenu = new GUI[pagesNeeded];
        int currPage = -1;
        int idx = 0;
        for(SpawnableEntity ent:SpawnableEntity.values()) {
            if(!ent.isValid()) continue;
            if(idx==0) {
                //Create new GUI
                currPage++;
                spawnerMenu[currPage] = GUIManager.getInstance().createGUI("Page"+currPage, InventoryType.CHEST);
                spawnerMenu[currPage].setOnCloseAction(e -> removeGuis());
                spawnerMenu[currPage].setItemWithAction(22, new ItemStack(Material.BARRIER), e ->  {
                    e.getWhoClicked().closeInventory();
                    removeGuis();
                });
                //Add "Next/Previous Page" Buttons
                if(currPage>0) {
                    int finalCurrPage = currPage;
                    spawnerMenu[currPage-1].setItemWithAction(26, new ItemStack(Material.EMERALD), e ->
                            openOtherPage(spawnerMenu[finalCurrPage-1], spawnerMenu[finalCurrPage]));
                    spawnerMenu[currPage].setItemWithAction(18, new ItemStack(Material.REDSTONE), e ->
                            openOtherPage(spawnerMenu[finalCurrPage], spawnerMenu[finalCurrPage-1]));
                }
            }
            int finalIdx = idx;
            int finalCurrPage = currPage;
            spawnerMenu[currPage].setItemWithAction(idx, ent.getItemStack(), e -> {
                e.getWhoClicked().sendMessage("Clicked slot "+ finalIdx +", page "+ finalCurrPage +" for ent "+ent.toString()+"!");
            });
            idx++;
            if(idx>=18) idx=0;
        }
        this.spawnerMenu[0].openInventory(p());
        return true;
    }

    private void removeGuis() {
        for(GUI g : spawnerMenu) {
            g.removeGUI();
        }
        PluginMaster.getInstance().getInteractInst().releaseBlockLoc(openMenuLoc);
    }

    private void openOtherPage(GUI currGui, GUI newGui) {
        currGui.ignoreNextClose();
        newGui.openInventory(p());
    }

    protected void quit() {
        removeGuis();
    }

    public Player p() {
        return Bukkit.getPlayer(id);
    }

}
