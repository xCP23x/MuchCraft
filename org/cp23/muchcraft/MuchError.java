//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and http://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MuchError {
    public enum Error{NO_SPACE_AFTER_COMMA, LINE_TOO_LONG, HELP, INFO, NO_PERM_INFO, NO_PERM_CUSTOM, NO_PERM_RANDOM, NO_PERM_LINES, NO_PERM_RELOAD, NO_PERM_SPAM, NO_AUTO_RANDOM, REMIND_ON_AUTO}
    private static final ChatColor red = ChatColor.RED;
    private static final ChatColor gold = ChatColor.GOLD;
    private static final ChatColor gray = ChatColor.GRAY;
    
     public static void sendError(Error err, CommandSender sender){
        String head = gold + "MuchCraft " + red + "Error: " + gray;
        
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
            case NO_PERM_INFO:
                sender.sendMessage(head + "You do not have permission to get plugin infi - Permission: " + gray + "muchcraft.info");
                sender.sendMessage(gold + "Use " + gray + "/wow help " + gold + "for usage");
                break;
            case NO_AUTO_RANDOM:
                sender.sendMessage(head + "No command specified");
                sender.sendMessage(gold + "If you meant to send a randomised message, use" + gray + " /wow random");
                sender.sendMessage(gold + "Use " + gray + "/wow help " + gold + "for usage");
                break;
            case REMIND_ON_AUTO:
                sender.sendMessage(gold + "Did you know you can specify custom messages? Try " + gray + "/wow help");
                break;
            case INFO:
                sender.sendMessage(gold + "MuchCraft version " + gray + MuchCraft.plugin.getDescription().getVersion());
                sender.sendMessage(gold + "Description: " + gray + MuchCraft.plugin.getDescription().getDescription());
                sender.sendMessage(gold + "Updates available from " + gray + MuchCraft.plugin.getDescription().getWebsite());
                sender.sendMessage(gold + "Source code (GNU GPL v2) available from " + gray + "https://github.com/xCP23x/MuchCraft");
                break;
            case HELP:
                //Build help message based on permissions and config options
                String help = "";
                
                //Console always has permission, so we don't need to do any instanceof checks
                if(sender.hasPermission("muchcraft.random")){
                    if(MuchCraft.autoRandom){
                        help += gold + "/wow " + gray + "- Gives a randomly generated Doge phrase\n";
                    } else {
                        help += gold + "/wow random" + gray + "- Gives a randomly generated Doge phrase\n";
                    }
                }
                
                if(sender.hasPermission("muchcraft.custom")){
                    help += gold + "/wow message one, message two, etc... " + gray + "- Gives a user defined Doge phrase (lines must be comma separated)\n";
                }
                
                if(sender.hasPermission("muchcraft.reload")){
                    help += gold + "/wow reload " + gray + "- Reloads the config";
                }
                
                if(sender.hasPermission("muchcraft.info")){
                    help += gold + "/wow info " + gray + "- Returns information about MuchCraft";
                }
                
                if(help.equals("")){
                    //Player has no permissions
                    help = gold + "Sorry, you don't have permission to use MuchCraft!";
                } else {
                    help = gold + "MuchCraft usage:\n" + help;
                }
                sender.sendMessage(help);
                break;
            default:
                MuchCraft.debug("MuchError switch statement DEFAULT on: " + err.name());
                break;
        }
    }
}
