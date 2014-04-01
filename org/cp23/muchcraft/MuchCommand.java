//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and http://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuchCommand implements CommandExecutor{
    private final MuchCraft plugin;
    private static final HashMap<UUID, Long> lastCommand = new HashMap<UUID, Long>();
    
    
    public MuchCommand(MuchCraft plugin){
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Boolean sendRemind = false;
        
        if(cmd.getName().equalsIgnoreCase("wow")){
            MuchMessage message;
            
            if(args.length==1 && args[0].equalsIgnoreCase("help")){
                //Player requested help
                MuchError.sendError(MuchError.Error.HELP, sender);
                return true;
                
                
            } else if(args.length==1 && args[0].equalsIgnoreCase("reload")){
                //Player requested reload
                if(sender instanceof Player){
                    if(sender.hasPermission("muchcraft.reload")){
                        sender.sendMessage(ChatColor.GOLD + "Much reload...");
                        plugin.reload();
                        sender.sendMessage(ChatColor.GOLD + "Such success!");
                        return true;
                    } else {
                        MuchError.sendError(MuchError.Error.NO_PERM_RELOAD, sender);
                        return true;
                    }
                } else {
                    //It's the console
                    plugin.reload();
                    return true;
                }
                
                
            } else if(args.length==1 && args[0].equalsIgnoreCase("random")){
                //Produce randomised output
                message = new MuchMessage(sender);
            
            
            } else if(args.length == 0){
                //Check if auto-random is enabled
                if(!MuchCraft.autoRandom) {
                    MuchError.sendError(MuchError.Error.NO_AUTO_RANDOM, sender);
                    return true;
                }
                //Produce randomised output
                message = new MuchMessage(sender);
                
                //Remind the player that MuchCraft does more than just this (not neccessary without auto-random)
                //Only do so the first time they run the command, and if they have permission to use custom messages
                if(sender instanceof Player && lastCommand.get(((Player)sender).getUniqueId())==null && sender.hasPermission("muchcraft.custom")){
                    sendRemind = true;
                }
                
                
            } else {
                //Use player input
                message = new MuchMessage(args, sender);
            }
            
            
            if(message.hasPermissions() && message.isValid() && notSpam(sender)){
                message.send(plugin.getServer());
                if(sendRemind) MuchError.sendError(MuchError.Error.REMIND_ON_AUTO, sender);
            }
            return true;
        }
        return false;
    }
    
    public static void clearLastCommand(){
        lastCommand.clear();
    }
    
    private boolean notSpam(CommandSender sender){
        if(sender instanceof Player){
            if(!sender.hasPermission("muchcraft.nospam")){
                //Player does not have permission to bypass anti-spam
                UUID player = ((Player)sender).getUniqueId();
                
                if(lastCommand.containsKey(player)) {
                    if ((System.currentTimeMillis() - lastCommand.get(player)) / 1000.0 > MuchCraft.spamDelay) {
                        //Spam timeout has expired, reset to current time
                        lastCommand.put(player, System.currentTimeMillis());
                        return true;
                    } else {
                        //Player is still timed out
                        MuchError.sendError(MuchError.Error.NO_PERM_SPAM, sender);
                        return false;
                    }
                } else {
                    //Add player to anti-spam counter
                    lastCommand.put(player, System.currentTimeMillis());
                }
            } else {
                //If they have permission to bypass, put them here anyway - used for REMIND_ON_AUTO
                lastCommand.put(((Player)sender).getUniqueId(), System.currentTimeMillis());
            }
        }
        //Either command was sent from console, or player has permission to bypass anti-spam
        return true;
    }

}
