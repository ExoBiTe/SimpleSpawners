package com.github.exobite.mc.simplespawners.listener;

import com.github.exobite.mc.simplespawners.playerdata.PlayerData;
import com.github.exobite.mc.simplespawners.playerdata.PlayerDataManager;
import com.github.exobite.mc.simplespawners.util.*;
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
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PlayerInteraction implements Listener {

    private static final String SPAWNER_MENU_PERM = "simplespawners.interact.changespawner";
    private static final String BREAK_NATURAL_SPAWNER = "simplespawners.mine.natural";
    private static final String BREAK_CUSTOM_SPAWNER = "simplespawners.mine.custom";

    private final NamespacedKey itemKey;
    private final NamespacedKey blockKey;
    private final Map<BlockLoc, PlayerData> inEdit = new HashMap<>();

    public PlayerInteraction(JavaPlugin mainInstance) {
        itemKey = new NamespacedKey(mainInstance, "spawnerType");
        blockKey = new NamespacedKey(mainInstance, "isCustomSpawner");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock()==null || e.getAction()!=Action.RIGHT_CLICK_BLOCK || !e.getPlayer().hasPermission(SPAWNER_MENU_PERM)) return;
        Block b = e.getClickedBlock();
        if(b.getType()!= Material.SPAWNER) return;
        Player p = e.getPlayer();
        BlockLoc bl = new BlockLoc(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ());
        PlayerData pd = PlayerDataManager.getInstance().getPlayerData(p.getUniqueId());
        if(!pd.canOpenSpawnerMenu()) return;
        if(inEdit.containsKey(bl)){
            p.sendMessage(Msg.SPAWNER_GUI_ALREADY_IN_EDIT.getMessage(p));
            return;
        }
        if(!pd.openSpawnerMenu((CreatureSpawner) e.getClickedBlock().getState(), bl)) {
            return;
        }
        e.setCancelled(true);
        inEdit.put(bl, pd);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if(e.getBlock().getType()!=Material.SPAWNER) return;
        Player p = e.getPlayer();
        ItemStack mh = p.getInventory().getItemInMainHand();
        BlockLoc bl = new BlockLoc(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ());
        if(!mh.getType().toString().toLowerCase(Locale.ROOT).contains("pickaxe")
                || mh.getEnchantmentLevel(Enchantment.SILK_TOUCH) <= 0) {
            forceClose(bl);
            return;
        }
        CreatureSpawner sp = (CreatureSpawner)e.getBlock().getState();
        if(!isMineable(p, sp)) {
            forceClose(bl);
            return;
        }
        EntityType et = sp.getSpawnedType();
        ItemStack spawner = new ItemStack(Material.SPAWNER, 1);
        ItemMeta im = spawner.getItemMeta();
        assert im != null;
        im.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, et.toString());
        im.setLore(List.of(Msg.SPAWNER_ITEM_LORE.getMessage(p, et.toString())));
        spawner.setItemMeta(im);
        if(!Config.getInstance().dropIntoInventory()) {
            dropItem(spawner, e.getBlock().getLocation());
        }else{
            Map<Integer, ItemStack> items = p.getInventory().addItem(spawner);
            if(!items.isEmpty()) {
                //Player Inventory is full, drop Item
                dropItem(spawner, e.getBlock().getLocation());
            }else{
                CustomSound.SPAWNER_DROPPED_INTO_INV.playSound(p);
            }
        }
    }

    private void dropItem(ItemStack is, Location l) {
        assert l.getWorld() != null;
        l.getWorld().dropItemNaturally(l.add(0.5, 0.5, 0.5), is);
    }

    @EventHandler
    private void onExplosion(BlockExplodeEvent e) {
        handleExplosion(e.blockList());
    }

    @EventHandler
    private void onExplosion(EntityExplodeEvent e) {
        handleExplosion(e.blockList());
    }

    private void handleExplosion(List<Block> blocks) {
        for(Block b:blocks) {
            BlockLoc bl = new BlockLoc(b.getX(), b.getY(), b.getZ());
            if(inEdit.containsKey(bl)) forceClose(bl);
        }
    }

    private void forceClose(BlockLoc bl) {
        PlayerData pd = inEdit.get(bl);
        if(pd==null) return;
        pd.closeMenu();
    }


    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        ItemStack is = e.getItemInHand();
        if(is.getType()!=Material.SPAWNER) return;
        ItemMeta im = is.getItemMeta();
        if(im==null) return;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        String spawnType = pdc.get(itemKey, PersistentDataType.STRING);
        if(spawnType==null) return;
        EntityType et;
        try {
            et = EntityType.valueOf(spawnType);
        }catch(IllegalArgumentException ignored) {
            return;
        }
        CreatureSpawner cs = (CreatureSpawner) e.getBlock().getState();
        cs.setSpawnedType(et);
        cs.getPersistentDataContainer().set(blockKey, PersistentDataType.BYTE, (byte) 0xFF); //Value is irrelevant
        cs.update();
    }

    private boolean isMineable(Player p, CreatureSpawner sp) {
        boolean hasBlockPdcKey = sp.getPersistentDataContainer().has(blockKey, PersistentDataType.BYTE);
        return (hasBlockPdcKey && p.hasPermission(BREAK_CUSTOM_SPAWNER)) || (!hasBlockPdcKey && p.hasPermission(BREAK_NATURAL_SPAWNER));
    }

    public void releaseBlockLoc(BlockLoc bl) {
        inEdit.remove(bl);
    }

}
