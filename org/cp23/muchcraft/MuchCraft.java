//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and http://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class MuchCraft extends JavaPlugin {
    
    public static final Logger log = Logger.getLogger("Minecraft");
    public static MuchCraft plugin;
    public enum MsgSource{CUSTOM, RANDOM};
    
    //Config variables:
    public static int randomLines, customLines;
    public static List<String> prefix, suffix, full, color;
    public static boolean broadcastSender;
    public static String broadcastMessage;
    private static boolean debugEnabled;
    
    //Constants to use:
    public static final int LINE_WIDTH = 52; //Maximum line width
    public static final double SPACE_WIDTH = 52.0/34.0; //Size of space
    public static final int MIN_OFF_DIFF = 20; //Minimum percentage difference in offsets between lines
    
    @Override
    public void onEnable(){
        plugin = this;
        getCommand("wow").setExecutor(new MuchCommand(this));
        loadConfig();
    }
    
    @Override
    public void onDisable(){
    }
    
    public static void debug(String msg){
        if(debugEnabled==true){
            log.info("[MuchCraft][DEBUG]: "+msg);
        }
    }
    
    public void reload(){
        log.info("[MuchCraft] Much reload...");
        this.reloadConfig();
        loadConfig();
        log.info("[MuchCraft] Such success!");
    }
    
    private void loadConfig(){   
        
        //To add new entries to old config files, we need to use copyDefaults(true)
        //This breaks any formatting due to YAML parsing, so comments must be defined in a header:
        
        String header = (
            "MuchCraft configuration file\n"
            +"See http://dev.bukkit.org/bukkit-plugins/muchcraft/ for more info \n\n"
            +"customLines: Maximum number of lines for custom messages\n"
            +"randomLines: Number of lines for random messages\n\n"
            +"Lines can  be generated from a full phrase, or prefix and a suffix:\n"
            +"full: Full random messages to use\n"
            +"prefix: Prefix of random message to use\n"
            +"suffix: Suffix of random message to use\n\n"
            +"colors: Colors to use (in raw hex values)\n\n"
            +"broadcastSender: Broadcast a message containing the name of the sender (true/false)\n"
            +"broadcastMessage: Message to send - %player% is replaced by the player's name, surround a color value in %% to use it\n\n"
            +"debug : Enable debug messages - may spam console (true/false)\n"
        );
        
        this.getConfig().options().copyDefaults(true);
        this.getConfig().options().copyHeader(true);
        this.getConfig().options().header(header);
        this.saveConfig();
        
        //Load debug setting
        debugEnabled = this.getConfig().getBoolean("debug");
        if(debugEnabled) log.info("[MuchCraft] Such debug - Much enabled!");
        
        //Load config
        customLines = this.getConfig().getInt("customLines");
        randomLines = this.getConfig().getInt("randomLines");
        full = this.getConfig().getStringList("full");
        prefix = this.getConfig().getStringList("prefix");
        suffix = this.getConfig().getStringList("suffix");
        color = this.getConfig().getStringList("colors");
    }
    
}
