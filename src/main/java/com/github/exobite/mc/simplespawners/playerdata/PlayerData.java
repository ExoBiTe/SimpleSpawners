package com.github.exobite.mc.simplespawners.playerdata;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.gui.CustomItem;
import com.github.exobite.mc.simplespawners.gui.GUI;
import com.github.exobite.mc.simplespawners.gui.GUIManager;
import com.github.exobite.mc.simplespawners.util.BlockLoc;
import com.github.exobite.mc.simplespawners.util.Msg;
import com.github.exobite.mc.simplespawners.util.SpawnableEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {

    private static final String IGNORE_BLACKLIST_PERM = "simplespawners.ignoreblacklist";
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
        boolean allowBlacklistedEntities = p().hasPermission(IGNORE_BLACKLIST_PERM);
        for(SpawnableEntity ent:SpawnableEntity.values()) {
            if(!ent.isValid()) continue;
            if(ent.isBlacklisted() && !allowBlacklistedEntities) continue;
            if(idx==0) {
                //Create new GUI
                currPage++;
                spawnerMenu[currPage] = GUIManager.getInstance().createGUI(Msg.SPAWNER_GUI_TITLE.getMessage(), GUI_SIZE);
                spawnerMenu[currPage].setOnCloseAction(e -> removeGuis());
                spawnerMenu[currPage].setItemWithAction(GUI_SIZE-4, CustomItem.getGUICloseButton().getItemStack(), e ->  {
                    e.getWhoClicked().closeInventory();
                    removeGuis();
                });
                spawnerMenu[currPage].setItem(GUI_SIZE-6, SpawnableEntity.valueOf(sp.getSpawnedType().toString()).getItemStack());
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
                if(!setSpawnerType(sp, ent)) {
                    e.getWhoClicked().closeInventory();
                    removeGuis();
                }
            });
            idx++;
            if(idx>=(GUI_SIZE-9)) idx=0;
        }
        this.spawnerMenu[0].openInventory(p());
        return true;
    }

    private boolean setSpawnerType(CreatureSpawner sp, SpawnableEntity ent) {
        //Check if Spawner is still there
        if(new Location(p().getLocation().getWorld(), openMenuLoc.x(), openMenuLoc.y(), openMenuLoc.z()).getBlock().getType()!= Material.SPAWNER)
            return false;
        sp.setSpawnedType(ent.getType());
        sp.update();
        //Refresh GUIs
        for(GUI g : spawnerMenu) {
            g.setItem(GUI_SIZE-6, ent.getItemStack());
        }
        return true;
    }

    private void removeGuis() {
        for(GUI g : spawnerMenu) {
            g.removeGUI();
        }
        PluginMaster.getInstance().getInteractInst().releaseBlockLoc(openMenuLoc);
        openMenuLoc = null;
    }

    private void openOtherPage(GUI currGui, GUI newGui) {
        currGui.ignoreNextClose();
        newGui.openInventory(p());
    }

    public void closeMenu() {
        if(openMenuLoc==null) return;
        p().closeInventory();
        removeGuis();
    }

    protected void quit() {
        removeGuis();
    }

    public Player p() {
        return Bukkit.getPlayer(id);
    }

}
