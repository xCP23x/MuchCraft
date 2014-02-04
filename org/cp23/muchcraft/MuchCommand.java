//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and https://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MuchCommand implements CommandExecutor{
    private final MuchCraft plugin;
    
    public MuchCommand(MuchCraft plugin){
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("wow")){
            //do stuff
            if(args.length == 0){
                //Produce randomised output
                        
            } else if(args.length==1 && args[0].equalsIgnoreCase("help")){
                //Display help
                
            } else {
                //Produce custom output
                int n=0, nLines=1;
                String space=""; //Allows us to easily enable or disable space insertion
                
                //Get number of lines in message
                for (String arg : args) {
                    if(!arg.contains(",")){
                        nLines ++;
                    }
                }
                
                if (nLines > plugin.customLines){
                    //Too many lines
                    plugin.debug("too many lines");
                    return true;
                }
                
                String[] linesStr = new String[nLines-1];
                
                for (String arg : args) {
                    if(linesStr[n]==null) linesStr[n]="";
                    
                    if(!arg.contains(",")){
                        linesStr[n] = linesStr[n] + space + arg;
                        space = " ";
                    } else {
                        linesStr[n] = linesStr[n] + space + arg.replace(",", "");
                        n = n+1;
                        space = "";
                    }
                }
                
                for (String line : linesStr) {
                    plugin.debug(line);
                }
                
            }
            
            return true;
        }
        return false;
    }

}
