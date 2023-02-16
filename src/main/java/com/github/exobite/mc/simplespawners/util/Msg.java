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
            "The Lore of Mined Spawner Items"),

    SPAWNER_ALREADY_IN_EDIT(ChatColor.RED+"You can't edit this spawner, as it is already being edited by someone else!",
            true,
            "Sent to a Player when they try to edit a Spawner, that is already being edited by someone else"),

    SPAWNER_GUI_TITLE(ChatColor.AQUA+"Monster Spawner",
            true,
            "The Title of the Monster Spawner Change GUI"),

    SPAWNER_GUI_ITEM_NAME(ChatColor.GOLD+"Set spawner type to "+ChatColor.AQUA+"%[0]",
            true,
            "The Displayname of all 'Buttons'(Items) in the Spawner GUI"),

    SPAWNER_GUI_COST(ChatColor.GOLD+"Price: "+ChatColor.LIGHT_PURPLE+"%[0]",
            true,
            "The Price-Text for all Buttons in the Spawner GUI"),

    SPAWNER_GUI_COST_ISFREE(ChatColor.GOLD+"Price: "+ChatColor.AQUA+"FREE",
            true,
            "The Price-Text when an Option is Free for all Buttons in the Spawner GUI"),

    SPAWNER_GUI_CAN_BUY(ChatColor.GREEN+"Click to Buy!",
            true,
            "Is displayed when the Players has enough Funds in order to buy the selected Option"),

    SPAWNER_GUI_CANNOT_BUY(ChatColor.RED+"You can't afford that!",
            true,
            "Is displayed when the Players has not enough Funds in order to buy the selected Option"),

    SPAWNER_GUI_CURRENT_TYPE_NAME(ChatColor.GOLD+"Current Type: "+ChatColor.AQUA+"%[0]"+ChatColor.GOLD+"!",
            true,
            "The Displayname of the GUI-Item showing the current Spawner Type"),

    ECO_TRANSACTION_ERR_INSUFFICIENT_FUNDS(ChatColor.RED+"Transaction failed, you have insufficient funds",
            true,
            "Sent to the Player when the Purchase of an Option fails because the Players hasn't enough funds"),

    ECO_TRANSACTION_UNKNOWN_ERR(ChatColor.RED+"Transaction failed, an unknown error occurred",
            true,
            "Sent to the Player when an unknown Error during a Purchase occurs"),

    ECO_TRANSACTION_SUCCESS(ChatColor.GREEN+"Changed the Spawner Type successfully!",
            true,
            "Sent to the Player when an option has been purchased successfully"),

    CMD_ERR_NO_PERMISSION(ChatColor.RED+"You don't have the Permission to do that.",
            true,
            "Sent to the Player when they use a Feature locked behind a Permission which they don't have"),

    CMD_SS_RELOAD_STARTED(ChatColor.GRAY+"Reloading Plugin Configuration...",
            true,
            "Sent to the Player upon using the '/ss reload'-Command"),

    ;

    private static final String FILE_NAME = "lang.yml";
    private static File configFile;
    private static boolean writeComments;

    public static void registerMessages() {
        if(configFile!=null) return; //If configFile is initialized, the class has already been loaded.
        writeComments = PluginMaster.getInstance().writeCommentsToFile();
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
                if(writeComments) conf.setComments(msg.toString(), stringToList(msg.comment));
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
            if(writeComments) conf.setComments(msg.toString(), stringToList(msg.comment));
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
        String replaced = msg.replaceAll("%\\[\\d]", "%[#]");
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
