package com.github.exobite.mc.simplespawners.listener;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.util.SpawnableEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Locale;

public class DebugListener implements Listener {

    private abstract class SyncTask {

        abstract void run();

    }

    private void runSync(SyncTask task) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTask(PluginMaster.getInstance());
    }

    private void sendMessage(Player p, String message) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(message);
            }
        }.runTask(PluginMaster.getInstance());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(!e.getPlayer().isOp()) return;
        Player p = e.getPlayer();
        String[] msg = e.getMessage().split(" ");
        if(msg.length==0) {
            msg = new String[]{e.getMessage()};
        }
        boolean cancel = true;
        switch (msg[0].toLowerCase(Locale.ROOT)) {
            case "skull" -> getCustomSkull(p, msg);
            default -> cancel = false;
        }
        e.setCancelled(cancel);
    }

    private void getCustomSkull(final Player p, String[] msg) {
        if(msg.length==1) {
            StringBuilder sb = new StringBuilder("Specify a Skull! Possible Types:\n");
            for(SpawnableEntity sd : SpawnableEntity.values()) {
                sb.append(sd.toString()).append(", ");
            }
            sendMessage(p, sb.toString());
            return;
        }
        SpawnableEntity sd;
        try {
            sd = SpawnableEntity.valueOf(msg[1]);
        }catch (IllegalArgumentException e){
            sendMessage(p, "No Skull '"+msg[1]+"' found!");
            return;
        }
        runSync(new SyncTask() {
            @Override
            void run() {
                p.getInventory().addItem(sd.getItemStack());
            }
        });
    }


}
