//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and http://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import java.util.ArrayList;
import org.bukkit.command.CommandSender;

public class DogeMessage {
    
    private ArrayList lines = new ArrayList<>();
    
    public void useLines(String[] rawLines, CommandSender sender){
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
                    MuchError.sendError(MuchError.error.NO_SPACE_AFTER_COMMA, sender);
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
            MuchError.sendError(MuchError.error.TOO_MANY_LINES, sender);
            return;
        }
        
        for(int i=0; i<lines.size(); i++){
            MuchCraft.debug((String)lines.get(i));
        }
    }
    
    
}
