package com.github.exobite.mc.simplespawners.listener;

import com.github.exobite.mc.simplespawners.gui.GUI;
import com.github.exobite.mc.simplespawners.gui.GUIClickAction;
import com.github.exobite.mc.simplespawners.gui.GUICloseAction;
import com.github.exobite.mc.simplespawners.gui.GUIManager;
import com.github.exobite.mc.simplespawners.util.Config;
import com.github.exobite.mc.simplespawners.util.CustomSound;
import com.github.exobite.mc.simplespawners.util.Msg;
import com.github.exobite.mc.simplespawners.util.SpawnableEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlayerInteraction implements Listener {

    private record BlockLoc(int x, int y, int z){}

    private static final String SPAWNER_MENU_PERM = "";
    private static final String BREAK_SPAWNER_PERM = "";

    private final NamespacedKey key;
    private final Map<BlockLoc, GUI[]> inEdit = new HashMap<>();

    public PlayerInteraction(JavaPlugin mainInstance) {
        key = new NamespacedKey(mainInstance, "spawnerType");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock()==null || e.getAction()!=Action.RIGHT_CLICK_BLOCK || !e.getPlayer().hasPermission(SPAWNER_MENU_PERM)) return;
        Block b = e.getClickedBlock();
        if(b.getType()!= Material.SPAWNER) return;
        BlockLoc bl = new BlockLoc(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ());
        if(inEdit.containsKey(bl)){
            e.getPlayer().sendMessage(Msg.SPAWNER_ALREADY_IN_EDIT.getMessage());
            return;
        }
        e.setCancelled(true);
        GUI[] toOpen = createGuis(e.getPlayer(), (CreatureSpawner) e.getClickedBlock().getState(), bl);
        toOpen[0].openInventory(e.getPlayer());
        inEdit.put(bl, toOpen);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if(e.getBlock().getType()!=Material.SPAWNER) return;
        ItemStack mh = e.getPlayer().getInventory().getItemInMainHand();
        if(!mh.getType().toString().toLowerCase(Locale.ROOT).contains("pickaxe")) return;
        if(mh.getEnchantmentLevel(Enchantment.SILK_TOUCH) <= 0) return;
        if(!e.getPlayer().hasPermission(BREAK_SPAWNER_PERM)) return;
        EntityType et = ((CreatureSpawner)e.getBlock().getState()).getSpawnedType();
        ItemStack spawner = new ItemStack(Material.SPAWNER, 1);
        ItemMeta im = spawner.getItemMeta();
        assert im != null;
        im.getPersistentDataContainer().set(key, PersistentDataType.STRING, et.toString());
        im.setLore(List.of(Msg.SPAWN_ITEM_LORE.getMessage(et.toString())));
        spawner.setItemMeta(im);
        if(!Config.getInstance().dropIntoInventory()) {
            dropItem(spawner, e.getBlock().getLocation());
        }else{
            Map<Integer, ItemStack> items = e.getPlayer().getInventory().addItem(spawner);
            if(!items.isEmpty()) {
                //Player Inventory is full, drop Item
                dropItem(spawner, e.getBlock().getLocation());
            }else{
                CustomSound.SPAWNER_DROPPED_INTO_INV.playSound(e.getPlayer().getLocation());
            }
        }
    }

    private void dropItem(ItemStack is, Location l) {
        assert l.getWorld() != null;
        l.getWorld().dropItemNaturally(l.add(0.5, 0.5, 0.5), is);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        ItemStack is = e.getItemInHand();
        if(is.getType()!=Material.SPAWNER) return;
        ItemMeta im = is.getItemMeta();
        if(im==null) return;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        String spawnType = pdc.get(key, PersistentDataType.STRING);
        if(spawnType==null) return;
        EntityType et;
        try {
            et = EntityType.valueOf(spawnType);
        }catch(IllegalArgumentException ignored) {
            return;
        }
        CreatureSpawner cs = (CreatureSpawner) e.getBlock().getState();
        cs.setSpawnedType(et);
    }

    private GUI[] createGuis(Player p, CreatureSpawner sp, BlockLoc bl) {
        //Chest Size is 27, last Row is reserved, so 18
        //Valid Entities / 27 + 0.5 are the Inventory Pages needed
        int pagesNeeded = Math.round(SpawnableEntity.getValidAmount() / 18f + 0.5f);
        GUI[] guis = new GUI[pagesNeeded];
        System.out.println("Needs "+pagesNeeded+"pages!");
        int currPage = -1;
        int idx = 0;
        for(SpawnableEntity ent:SpawnableEntity.values()) {
            if(!ent.isValid()) continue;
            if(idx==0) {
                //Create new GUI
                currPage++;
                guis[currPage] = GUIManager.getInstance().createGUI("Page"+currPage, InventoryType.CHEST);
                guis[currPage].setOnCloseAction(e -> removeGuis(bl));
                guis[currPage].setItemWithAction(22, new ItemStack(Material.BARRIER), e -> removeGuis(bl));
                //Add "Next/Previous Page" Buttons
                if(currPage>0) {
                    int finalCurrPage = currPage;
                    guis[currPage-1].setItemWithAction(26, new ItemStack(Material.EMERALD), e -> guis[finalCurrPage].openInventory(p));
                    guis[currPage].setItemWithAction(18, new ItemStack(Material.REDSTONE), e -> guis[finalCurrPage-1].openInventory(p));
                }
                System.out.println("Created GUI Page "+currPage+"!");
            }
            int finalIdx = idx;
            int finalCurrPage = currPage;
            guis[currPage].setSlotAction(idx, e -> {
                e.getWhoClicked().sendMessage("Clicked slot "+ finalIdx +", page "+ finalCurrPage +" for ent "+ent.toString()+"!");
            });
            idx++;
            if(idx>=18) idx=0;
        }
        return guis;
    }

    private void removeGuis(BlockLoc bl) {
        if(!inEdit.containsKey(bl)) return;
        GUI[] guis = inEdit.get(bl);
        for(GUI g : guis) {
            g.removeGUI();
        }
        inEdit.remove(bl);
    }

}
