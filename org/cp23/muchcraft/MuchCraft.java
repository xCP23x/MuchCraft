//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and https://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class MuchCraft extends JavaPlugin {
    
    static final Logger log = Logger.getLogger("Minecraft");
    public static MuchCraft plugin;
    private static boolean debugEnabled=false;
    public static List<String> prefix, suffix, full;
//    public enum listType{PREFIX,SUFFIX,full};
    public int randomLines, customLines;
    
    @Override
    public void onEnable(){
        plugin = this;
        getCommand("wow").setExecutor(new MuchCommand(this));
        
        //Prepare config
        this.saveDefaultConfig();
        
        //Load config
        debugEnabled = this.getConfig().getBoolean("debug");
        if(debugEnabled) log.info("[MuchCraft] Such debug - Much enabled!");
        
        //Load lists
        prefix = this.getConfig().getStringList("prefix");
        suffix = this.getConfig().getStringList("suffix");
        full = this.getConfig().getStringList("full");
        //Load limits
        randomLines = this.getConfig().getInt("randomLines");
        customLines = this.getConfig().getInt("customLines");
    }
    
    @Override
    public void onDisable(){
    }
    
    public static void debug(String msg){
        if(debugEnabled==true){
            MuchCraft.log.info("[MuchCraft][DEBUG]: "+msg);
        }
    }
    
}
