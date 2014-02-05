//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and http://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuchCommand implements CommandExecutor{
    private final MuchCraft plugin;
    
    public MuchCommand(MuchCraft plugin){
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
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
                        plugin.reload();
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
                
                
            } else if(args.length == 0){
                //Produce randomised output
                message = new MuchMessage(sender);
                
                
            } else {
                //Give player input to message
                message = new MuchMessage(args, sender);
            }
            
            
            if(message.hasPermissions() && message.isValid()){
                message.send(plugin.getServer());
            }
            return true;
        }
        return false;
    }

}
