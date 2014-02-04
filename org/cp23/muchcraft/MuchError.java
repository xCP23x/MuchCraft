//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and http://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MuchError {
    public enum error{NO_SPACE_AFTER_COMMA, TOO_MANY_LINES, HELP}
    private static final ChatColor red = ChatColor.RED;
    private static final ChatColor gold = ChatColor.GOLD;
    private static final ChatColor gray = ChatColor.GRAY;
    
    
     public static void sendError(error err, CommandSender sender){
        String head = gold + "MuchCraft " + red + "Error: " + gold;
        switch(err){
            case NO_SPACE_AFTER_COMMA:
                sender.sendMessage(head + "Commas must be followed by a space");
                break;
            case TOO_MANY_LINES:
                sender.sendMessage(head + "Too many lines - Maximum is limited to " + gray + MuchCraft.customLines);
                break;
            case HELP:
                sender.sendMessage(gold + "MuchCraft usage:");
                sender.sendMessage(gold + "/wow " + gray + "- Gives a randomly generated Doge phrase");
                sender.sendMessage(gold + "/wow message one, message two, etc... " + gray + "- Gives a user defined Doge phrase (lines must be comma separated)");
                break;
        }
    }
    
    
    public static void sendDoge(String[] lines){
        
    }
}
