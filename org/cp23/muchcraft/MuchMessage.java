//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and http://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cp23.muchcraft.MuchCraft.MsgSource;
import org.cp23.muchcraft.MuchError.Error;

public class MuchMessage {
    private final ArrayList lines = new ArrayList<>();
    private final MsgSource Source;
    private CommandSender sender;
    private final Boolean isValid;
    
    public MuchMessage(String[] rawLines, CommandSender lineSender){
        //Use lines from raw input
        isValid = readRawLines(rawLines, sender);
        sender = lineSender;
        Source = MsgSource.CUSTOM;
    }
    
    public MuchMessage(CommandSender lineSender){
        //Use randomised lines
        sender = lineSender;
        Source = MsgSource.RANDOM;
        generateLines();
        isValid = true;
    }
    
    public boolean hasPermissions(){
        if(sender instanceof Player){
            if(sender.hasPermission("muchcraft." + Source.name().toLowerCase())){
                //The player has permission to send this type of message
                
                if(lines.size() > MuchCraft.customLines){
                    if(sender.hasPermission("muchcraft.nolimit")){
                        return true;
                    } else {
                        MuchError.sendError(Error.TOO_MANY_LINES, sender);
                        return false;
                    }
                }
                //Fewer lines than limit
                return true;
                
            } else {
                //The player doesn't have permission to send this type of message
                Error Err;
                Err = Source==MsgSource.CUSTOM ? Error.NO_PERM_CUSTOM : Error.NO_PERM_RANDOM;
                MuchError.sendError(Err, sender);
                return false;
            }
        } else {
            //It's the console
            return true;
        }
    }
    
    public Boolean isValid(){
        return isValid;
    }
    
    public void send(){
        //Send message
        
    }
    
    private void generateLines(){
        //Generate lines here
        
    }
    
    private boolean readRawLines(String[] rawLines, CommandSender sender){
        //Produce custom output
        //Add trailing comma (if there isn't one already)
        rawLines[rawLines.length-1] += rawLines[rawLines.length-1].endsWith(",") ? "": ",";        
        
        int n=0;
        String tmp = "";
        String space=""; //Allows us to easily enable or disable space insertion
        
        for (String rawLine : rawLines) {
            
            if(!rawLine.contains(",")){
                //Add to current line
                tmp = tmp + space + rawLine;
                space = " ";
            } else {
                //End of line: finish this one, then move to next line
                if(rawLine.indexOf(",", 0) < rawLine.length()-1){
                    MuchError.sendError(Error.NO_SPACE_AFTER_COMMA, sender);
                    return false;
                }
                
                tmp = tmp + space + rawLine.replace(",", "");
                if(!tmp.equals("")){
                    //Only add if it isn't blank'
                    lines.add(tmp);
                }
                
                tmp = ""; space = "";
                n = n+1;
            }
        }
        
        for(int i=0; i<lines.size(); i++){
            MuchCraft.debug((String)lines.get(i));
        }
        return true;
    }
    
}
