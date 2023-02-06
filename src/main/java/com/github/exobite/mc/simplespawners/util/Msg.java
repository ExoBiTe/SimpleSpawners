package com.github.exobite.mc.simplespawners.util;

import com.github.exobite.mc.simplespawners.PluginMaster;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public enum Msg {

    SPAWN_ITEM_LORE(ChatColor.GRAY+"Spawns: "+ChatColor.GOLD+"%[0]",
            true,
            ""),

    SPAWNER_ALREADY_IN_EDIT(ChatColor.RED+"You can't edit this spawner, as it is already being edited by someone else!",
            true,
            "");

    private static final String FILE_NAME = "lang.yml";
    private static PluginMaster main;
    private static File configFile;

    public static void registerMessages() {
        if(configFile!=null) return; //If configFile is initialized, the class has already been loaded.
        configFile = new File(PluginMaster.getInstance().getDataFolder() + File.separator + FILE_NAME);
        loadMessages();
    }

    private static void loadMessages() {
        boolean fileIsUpToDate = false;
        if(!configFile.exists()) {
            createNewMessageFile();
            fileIsUpToDate = true;
        }
        if(!fileIsUpToDate) {
            //Check if all Values exist in Cfg, if not add them.
            readFileDataAndUpdateFile();
        }
    }

    private static void readFileDataAndUpdateFile() {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(configFile);
        Set<String> existingKeys = conf.getKeys(false);
        boolean changedConfig = false;
        for(Msg msg:Msg.values()) {
            if(!existingKeys.contains(msg.toString())) {
                if(!msg.showInFile) continue;   //Skip hidden Messages
                //Message doesn't exist in Cfg, add it
                conf.set(msg.toString(), msg.getRawMessage());
                conf.setComments(msg.toString(), stringToList(msg.comment));
                if(!changedConfig) changedConfig = true;
            }else{
                //Msg exists in Cfg, read Data from it
                msg.setNewMessage(conf.getString(msg.toString()));
            }
        }
        if(changedConfig) {
            try {
                conf.save(configFile);
                PluginMaster.sendConsoleMessage(Level.INFO, "Updated '"+FILE_NAME+"!");
            } catch (IOException e) {
                PluginMaster.sendConsoleMessage(Level.SEVERE, "Couldn't update '"+FILE_NAME+"'. Using defaults for now.");
                e.printStackTrace();
            }
        }
    }

    private static void createNewMessageFile() {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(configFile);
        for(Msg msg:Msg.values()) {
            conf.set(msg.toString() , msg.getRawMessage());
            conf.setComments(msg.toString(), stringToList(msg.comment));
        }
        try {
            conf.save(configFile);
            PluginMaster.sendConsoleMessage(Level.INFO, "Created a new '"+FILE_NAME+"' File!");
        } catch (IOException e) {
            PluginMaster.sendConsoleMessage(Level.SEVERE, "Error while trying to write 'sounds.yml'!");
            e.printStackTrace();
        }
    }

    private static List<String> stringToList(String s) {
        List<String> l = new ArrayList<>();
        l.add(null);
        if(s.contains("\n")) {
            String[] split = s.split("\n");
            l.addAll(Arrays.asList(split));
        }else{
            l.add(s);
        }
        return l;
    }


    private String message;
    private int argAmount;
    private final String comment;
    private final boolean showInFile;

    Msg(String message, boolean showInFile, String comment) {
        this.showInFile = showInFile;
        this.comment = comment;
        setNewMessage(message, true);
    }

    public void setNewMessage(String msg) {
        setNewMessage(msg, false);
    }

    private void setNewMessage(String msg, boolean initial) {
        String replaced = msg.replaceAll("%\\[[0-9]]", "%[#]");
        int newArgs = Utils.countMatches(replaced, "%[#]");
        if(initial) {
            this.argAmount = newArgs;
        }else if(argAmount!=newArgs) {
            PluginMaster.sendConsoleMessage(Level.SEVERE, "You used a wrong Amount of Args for Message '"+this+"'!\n" +
                    "Change the Args to "+argAmount+" instead of "+newArgs+"!\n" +
                    "Using the default Message for now...");
            return;
        }
        message = msg;
    }

    public String getMessage(String ... args) {
        if(argAmount==0) return getRawMessage();
        if(args==null || args.length==0) {
            PluginMaster.sendConsoleMessage(Level.WARNING, "Too few args given for Msg '"+this+"' (got 0, expected "+argAmount+")!");
            return "ERR_TOO_FEW_ARGS_GIVEN__"+this;
        }
        if(args.length>argAmount) {
            PluginMaster.sendConsoleMessage(Level.WARNING, "Too many args given for Msg '"+this+"'!");
        }
        String msg = getRawMessage();
        for(int i=0;i<args.length && i<argAmount;i++) {
            if(args[i] == null) {
                PluginMaster.sendConsoleMessage(Level.WARNING, "Arg NULL given for Msg '"+this+"' for arg "+i+"!");
                msg = msg.replace("%["+i+"]", "NULL_ERR");
            }else{
                msg = msg.replace("%["+i+"]", args[i]);
            }
        }
        return ChatColor.translateAlternateColorCodes(Config.getInstance().getColorCode(), msg);
    }

    public String getRawMessage() {
        return message;
    }

    public int getArgAmount() {
        return argAmount;
    }



}