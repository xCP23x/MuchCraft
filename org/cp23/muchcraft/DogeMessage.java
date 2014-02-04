//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and http://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.cp23.muchcraft.MuchCraft.MsgSource;

public class DogeMessage {
    private ArrayList lines = new ArrayList<>();
    private MsgSource source;
    private CommandSender sender;
    
    public DogeMessage(String[] rawLines, CommandSender lineSender){
        //Use lines from raw input
        readRawLines(rawLines, sender);
        sender = lineSender;
        source = MsgSource.CUSTOM;
    }
    
    public DogeMessage(CommandSender lineSender){
        //Use randomised lines
        generateLines();
        sender = lineSender;
        source = MsgSource.RANDOM;
    }
    
    public boolean hasPermissions(){
        
        return false;
    }
    
    public void sendMessage(){
        //Send message
        
    }
    
    public MsgSource getSource(){
        return source;
    }
    
    private void generateLines(){
        //Generate lines here
        
    }
    
    private void readRawLines(String[] rawLines, CommandSender sender){
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
                    MuchError.sendError(MuchError.Error.NO_SPACE_AFTER_COMMA, sender);
                    return;
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
        
        if (lines.size() > MuchCraft.customLines){
            MuchError.sendError(MuchError.Error.TOO_MANY_LINES, sender);
            return;
        }
        
        for(int i=0; i<lines.size(); i++){
            MuchCraft.debug((String)lines.get(i));
        }
    }
    
}
