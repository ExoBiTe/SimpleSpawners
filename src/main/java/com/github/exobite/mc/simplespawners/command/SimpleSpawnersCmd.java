package com.github.exobite.mc.simplespawners.command;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.util.Config;
import com.github.exobite.mc.simplespawners.util.Msg;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SimpleSpawnersCmd implements CommandExecutor, TabCompleter {

    private static final String CMD_SIMPLESPAWNERS_RELOAD_PERM = "simplespawners.cmd.reload";

    private static final String BASIC_CMD_USAGE = ChatColor.RED + "/SimpleSpawners <reload|give|remove>";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        executeCommand(sender, args==null ? new String[]{} : args);
        return true;
    }

    private void executeCommand(CommandSender s, String[] args) {
        if(args.length==0) {
            s.sendMessage(BASIC_CMD_USAGE);
            return;
        }
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "reload" -> reloadCommand(s, args);
        }
    }

    private void reloadCommand(CommandSender s, String[] args) {
        if(!s.hasPermission(CMD_SIMPLESPAWNERS_RELOAD_PERM)) {
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        return new ArrayList<>();
    }

}
