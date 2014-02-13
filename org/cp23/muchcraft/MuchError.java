//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and http://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MuchError {
    public enum Error{NO_SPACE_AFTER_COMMA, LINE_TOO_LONG, HELP, NO_PERM_CUSTOM, NO_PERM_RANDOM, NO_PERM_LINES, NO_PERM_RELOAD, NO_PERM_SPAM}
    private static final ChatColor red = ChatColor.RED;
    private static final ChatColor gold = ChatColor.GOLD;
    private static final ChatColor gray = ChatColor.GRAY;
    
    
     public static void sendError(Error err, CommandSender sender){
        String head = gold + "MuchCraft " + red + "Error: " + gold;
        switch(err){
            case NO_SPACE_AFTER_COMMA:
                sender.sendMessage(head + "Commas must be followed by a space");
                break;
            case LINE_TOO_LONG:
                sender.sendMessage(head + "One of the lines you typed was too long - Maximum length is " + MuchCraft.LINE_WIDTH + " characters");
                break;
            case NO_PERM_CUSTOM:
                sender.sendMessage(head + "You do not have permission to send a custom message - Permission: " + gray + "muchcraft.custom");
                sender.sendMessage(gold + "Use " + gray + "/wow help " + gold + "for usage");
                break;
            case NO_PERM_RANDOM:
                sender.sendMessage(head + "You do not have permission to send a random message - Permission: " + gray + "muchcraft.random");
                sender.sendMessage(gold + "Use " + gray + "/wow help " + gold + "for usage");
                break;
            case NO_PERM_LINES:
                sender.sendMessage(head + "Too many lines - Maximum is limited to " + gray + MuchCraft.customLines);
                sender.sendMessage(gold + "Permission node " + gray + "muchcraft.nolimit" + gold + " would bypass this");
                break;
            case NO_PERM_RELOAD:
                sender.sendMessage(head + "You do not have permission to reload the config - Permission: " + gray + "muchcraft.reload");
                sender.sendMessage(gold + "Use " + gray + "/wow help " + gold + "for usage");
                break;
            case NO_PERM_SPAM:
                sender.sendMessage(head + "Spam protection - You must wait " + gray + MuchCraft.spamDelay + gold + " seconds between messages");
                sender.sendMessage(gold + "Permission node " + gray + "muchcraft.nospam" + gold + " would bypass this");
                break;
            case HELP:
                sender.sendMessage(gold + "MuchCraft usage:");
                sender.sendMessage(gold + "/wow " + gray + "- Gives a randomly generated Doge phrase");
                sender.sendMessage(gold + "/wow [message one, message two, etc...] " + gray + "- Gives a user defined Doge phrase (lines must be comma separated)");
                sender.sendMessage(gold + "/wow reload " + gray + "- Reloads the config");
                break;
        }
    }
}
