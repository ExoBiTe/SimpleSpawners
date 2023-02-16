package com.github.exobite.mc.simplespawners.command;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.util.Config;
import com.github.exobite.mc.simplespawners.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SimpleSpawnersCmd implements CommandExecutor, TabCompleter {

    private static final ChatColor USAGE_COLOR = ChatColor.DARK_AQUA;
    private static final ChatColor DESC_COLOR = ChatColor.GOLD;
    private static final String USAGE_DESC_SEPARATOR = ChatColor.GRAY + " -- ";


    private static final List<CommandInfo> knownCommands = new ArrayList<>();

    private record CommandInfo(String usage, String description, String permission) {
        private CommandInfo(String usage, String description, String permission) {
            this.usage = USAGE_COLOR + usage;
            this.description = DESC_COLOR + description;
            this.permission = permission;
            knownCommands.add(this);
        }

        public String getUsageAndDesc() {
            return usage + USAGE_DESC_SEPARATOR + description;
        }
    }

    private static final CommandInfo GIVE_CMD = new CommandInfo(
            "/SimpleSpawners"+ChatColor.AQUA+" give "+ChatColor.GRAY+
                    "["+ChatColor.DARK_AQUA+"player"+ChatColor.GRAY+"]",
            "Gives the specified Player a Customizable Spawner",
            "simplespawners.cmd.give"
    );

    private static final CommandInfo RELOAD_CMD = new CommandInfo(
            "/SimpleSpawners"+ChatColor.AQUA+" reload",
            "Reloads the config.yml File",
            "simplespawners.cmd.reload"
    );


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        executeCommand(sender, args==null ? new String[]{} : args);
        return true;
    }

    private void executeCommand(CommandSender s, String[] args) {
        if(args.length==0) {
            s.sendMessage(getBasicCmdUsage(s));
            return;
        }
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "reload" -> reloadCommand(s, args);
            case "give" -> giveCommand(s, args);

            default -> s.sendMessage(getBasicCmdUsage(s));
        }
    }

    private String getBasicCmdUsage(CommandSender s) {
        StringBuilder sb = new StringBuilder();
        for(CommandInfo ci:knownCommands) {
            if(!s.hasPermission(ci.permission())) continue;
            sb.append("\n").append(ci.getUsageAndDesc());
        }
        return sb.toString();
    }

    private void reloadCommand(CommandSender s, String[] args) {
        if(!s.hasPermission(RELOAD_CMD.permission())) {
            s.sendMessage(Msg.CMD_ERR_NO_PERMISSION.getMessage());
            return;
        }
        s.sendMessage(Msg.CMD_SS_RELOAD_STARTED.getMessage());
        new BukkitRunnable() {
            @Override
            public void run() {
                final String msg = Config.getInstance().reloadConfiguration();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        s.sendMessage(msg);
                    }
                }.runTask(PluginMaster.getInstance());
            }
        }.runTaskAsynchronously(PluginMaster.getInstance());
    }

    private void giveCommand(CommandSender s, String[] args) {
        if(!s.hasPermission(GIVE_CMD.permission())) {
            s.sendMessage(Msg.CMD_ERR_NO_PERMISSION.getMessage());
            return;
        }
        if(args.length==1) {
            s.sendMessage(GIVE_CMD.usage());
            return;
        }
        Player p = Bukkit.getPlayer(args[1]);
        if(p==null) {
            s.sendMessage(ChatColor.RED+"Can't find Player '"+ChatColor.GOLD+args[1]+ChatColor.RED+"'!");
            return;
        }
        ItemStack spawner = new ItemStack(Material.SPAWNER, 1);
        ItemMeta im = spawner.getItemMeta();
        assert im != null;
        im.getPersistentDataContainer().set(
                new NamespacedKey(PluginMaster.getInstance(), "spawnerType"),
                PersistentDataType.STRING, EntityType.PIG.toString());
        im.setLore(List.of(Msg.SPAWN_ITEM_LORE.getMessage(EntityType.PIG.toString())));
        spawner.setItemMeta(im);
        p.getInventory().addItem(spawner);
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String str, String[] args) {
        ArrayList<String> d = new ArrayList<>();
        int size = args.length;
        switch (size) {
            case 1 -> {
                if(s.hasPermission(GIVE_CMD.permission())) d.add("give");
                if(s.hasPermission(RELOAD_CMD.permission())) d.add("reload");
            }
            case 2 -> {
                if(args[0].equalsIgnoreCase("give") && s.hasPermission(GIVE_CMD.permission())) {
                    for(Player p:Bukkit.getOnlinePlayers()) {
                        d.add(p.getName());
                    }
                }
            }
        }
        return d;
    }

}
