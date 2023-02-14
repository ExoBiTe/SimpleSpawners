package com.github.exobite.mc.simplespawners.playerdata;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.economy.EconManager;
import com.github.exobite.mc.simplespawners.economy.IEconomy;
import com.github.exobite.mc.simplespawners.gui.GUI;
import com.github.exobite.mc.simplespawners.gui.GUIManager;
import com.github.exobite.mc.simplespawners.gui.CustomItem;
import com.github.exobite.mc.simplespawners.util.BlockLoc;
import com.github.exobite.mc.simplespawners.util.Msg;
import com.github.exobite.mc.simplespawners.util.SpawnableEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
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
        createGuis(bl);
        fillGuisWithItems(sp);
        this.spawnerMenu[0].openInventory(p());
        return true;
    }

    private void createGuis(BlockLoc bl) {
        if(spawnerMenu!=null) removeGuis();
        openMenuLoc = bl;
        int pagesNeeded = Math.round(SpawnableEntity.getValidAmount() / (GUI_SIZE-9f) + 0.5f);
        spawnerMenu = new GUI[pagesNeeded];
        for(int i=0;i<pagesNeeded;i++) {
            GUI g = GUIManager.getInstance().createGUI(Msg.SPAWNER_GUI_TITLE.getMessage(), GUI_SIZE);
            g.setOnCloseAction(e -> removeGuis());
            g.setItemWithAction(GUI_SIZE-4, CustomItem.getGUICloseButton().getItemStack(), e ->  {
                e.getWhoClicked().closeInventory();
                removeGuis();
            });
            spawnerMenu[i] = g;
            //Set next/prev page Buttons
            if(i>0) {
                int finalCurrPage = i;
                spawnerMenu[i-1].setItemWithAction(GUI_SIZE-1, CustomItem.getGUINextPageButton().getItemStack(),
                        e -> openOtherPage(spawnerMenu[finalCurrPage-1], spawnerMenu[finalCurrPage]));
                spawnerMenu[i].setItemWithAction(GUI_SIZE-9,
                        CustomItem.getGUIPrevPageButton().getItemStack(),
                        e -> openOtherPage(spawnerMenu[finalCurrPage], spawnerMenu[finalCurrPage-1]));
            }
        }
    }

    private void fillGuisWithItems(CreatureSpawner sp) {
        final Player p = p();
        int currPage = -1;
        int idx = 0;
        final boolean allowBlacklistedEntities = p().hasPermission(IGNORE_BLACKLIST_PERM);
        for(SpawnableEntity en:SpawnableEntity.values()) {
            if(!en.isValid() || (en.isBlacklisted() && !allowBlacklistedEntities)) continue;
            if(idx==0) {
                currPage++;
                spawnerMenu[currPage].setItem(GUI_SIZE-6,
                        SpawnableEntity.valueOf(sp.getSpawnedType().toString()).getItemStack());
            }
            spawnerMenu[currPage].setItemWithAction(idx,
                    createGuiItem(en, p),
                    e -> buyAttempt(e.getWhoClicked(), en));
            idx++;
            if(idx>=(GUI_SIZE-9)) idx = 0;
        }
    }

    private void buyAttempt(HumanEntity clicker, SpawnableEntity ent) {
        if(!(clicker instanceof Player p)) return;
        Location l = openMenuLoc.toLocation(clicker.getWorld());
        if(l.getBlock().getType()!=Material.SPAWNER) {
            p.closeInventory();
            removeGuis();
            return;
        }
        IEconomy price = EconManager.getInstance().getPrice(ent);
        boolean canAfford = price.canBuy(p);
        if(!canAfford) {
            p.sendMessage(Msg.ECO_TRANSACTION_ERR_INSUFFICIENT_FUNDS.getMessage());
            return;
        }
        if(!price.buy(p)) {
            p.sendMessage(Msg.ECO_TRANSACTION_UNKNOWN_ERR.getMessage());
        }else{
            CreatureSpawner sp = (CreatureSpawner) l.getBlock().getState();
            sp.setSpawnedType(ent.getType());
            sp.update();
            fillGuisWithItems(sp);
            p.sendMessage(Msg.ECO_TRANSACTION_SUCCESS.getMessage());
        }
    }

    private ItemStack createGuiItem(SpawnableEntity ent, Player p) {
        CustomItem ci = new CustomItem(ent.getItemStack());
        ci.setDisplayName(Msg.SPAWNER_GUI_ITEM_NAME.getMessage(ent.name()));
        IEconomy price = EconManager.getInstance().getPrice(ent);
        List<String> l = new ArrayList<>();
        l.add(Msg.SPAWNER_GUI_COST.getMessage(price.getPrice()));
        l.add("");
        l.add(price.canBuy(p) ? Msg.SPAWNER_GUI_CAN_BUY.getMessage() : Msg.SPAWNER_GUI_CANNOT_BUY.getMessage());
        ci.setLore(l);
        return ci.getItemStack();
    }

    private void removeGuis() {
        if(spawnerMenu==null) return;
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
