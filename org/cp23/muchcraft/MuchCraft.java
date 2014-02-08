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
    private static boolean debugEnabled=false;
    public static List<String> prefix, suffix, full, color;
    public static int randomLines, customLines;
    public enum MsgSource{CUSTOM, RANDOM};
    
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
        log.info("[MuchCraft] Much reload");
        this.reloadConfig();
        loadConfig();
        log.info("[MuchCraft] Such success!");
    }
    
    private void loadConfig(){
        //Prepare config (if it doesn't exist)
        this.saveDefaultConfig();
        
        //Load config
        debugEnabled = this.getConfig().getBoolean("debug");
        if(debugEnabled) log.info("[MuchCraft] Such debug - Much enabled!");
        
        //Load lists
        prefix = this.getConfig().getStringList("prefix");
        suffix = this.getConfig().getStringList("suffix");
        full = this.getConfig().getStringList("full");
        color = this.getConfig().getStringList("colors");
        //Load limits
        randomLines = this.getConfig().getInt("randomLines");
        customLines = this.getConfig().getInt("customLines");
    }
    
}
