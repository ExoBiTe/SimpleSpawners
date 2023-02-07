package com.github.exobite.mc.simplespawners.playerdata;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.gui.CustomItem;
import com.github.exobite.mc.simplespawners.gui.GUI;
import com.github.exobite.mc.simplespawners.gui.GUIManager;
import com.github.exobite.mc.simplespawners.util.BlockLoc;
import com.github.exobite.mc.simplespawners.util.SpawnableEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerData {

    private static final long SPAWNER_OPEN_MENU_DELAY_MS = 500L;
    private static final int GUI_SIZE = 54;

    private final UUID id;
    private GUI[] spawnerMenu;
    private BlockLoc openMenuLoc;
    private long nextSpawnerInteractAllowed;

    protected PlayerData(UUID id) {
        this.id = id;
    }

    public boolean canOpenSpawnerMenu() {
        return System.currentTimeMillis() >= nextSpawnerInteractAllowed;
    }

    public boolean openSpawnerMenu(CreatureSpawner sp, BlockLoc bl) {
        nextSpawnerInteractAllowed = System.currentTimeMillis() + SPAWNER_OPEN_MENU_DELAY_MS;
        openMenuLoc = bl;
        int pagesNeeded = Math.round(SpawnableEntity.getValidAmount() / (GUI_SIZE-9f) + 0.5f);
        spawnerMenu = new GUI[pagesNeeded];
        int currPage = -1;
        int idx = 0;
        for(SpawnableEntity ent:SpawnableEntity.values()) {
            if(!ent.isValid()) continue;
            if(idx==0) {
                //Create new GUI
                currPage++;
                spawnerMenu[currPage] = GUIManager.getInstance().createGUI("Page "+currPage, GUI_SIZE);
                spawnerMenu[currPage].setOnCloseAction(e -> removeGuis());
                spawnerMenu[currPage].setItemWithAction(GUI_SIZE-5, CustomItem.getGUICloseButton().getItemStack(), e ->  {
                    e.getWhoClicked().closeInventory();
                    removeGuis();
                });
                //Add "Next/Previous Page" Buttons
                if(currPage>0) {
                    int finalCurrPage = currPage;
                    spawnerMenu[currPage-1].setItemWithAction(GUI_SIZE-1,
                            CustomItem.getGUINextPageButton().getItemStack(), e ->
                            openOtherPage(spawnerMenu[finalCurrPage-1], spawnerMenu[finalCurrPage]));
                    spawnerMenu[currPage].setItemWithAction(GUI_SIZE-9,
                            CustomItem.getGUIPrevPageButton().getItemStack(), e ->
                            openOtherPage(spawnerMenu[finalCurrPage], spawnerMenu[finalCurrPage-1]));
                }
            }
            int finalIdx = idx;
            int finalCurrPage = currPage;
            spawnerMenu[currPage].setItemWithAction(idx, ent.getItemStack(), e -> {
                e.getWhoClicked().sendMessage("Clicked slot "+ finalIdx +", page "+ finalCurrPage +" for ent "+ent.toString()+"!");
                sp.setSpawnedType(ent.getType());
            });
            idx++;
            if(idx>=(GUI_SIZE-9)) idx=0;
        }
        this.spawnerMenu[0].openInventory(p());
        return true;
    }

    private void setSpawnerType(CreatureSpawner sp, EntityType et) {
        sp.setSpawnedType(et);
        sp.update();
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
