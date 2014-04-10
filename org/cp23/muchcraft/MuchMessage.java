//Copyright (C) 2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/MuchCraft/blob/master/README and http://github.com/xCP23x/MuchCraft/blob/master/LICENSE for details

package org.cp23.muchcraft;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cp23.muchcraft.MuchCraft.MsgSource;
import org.cp23.muchcraft.MuchError.Error;

public class MuchMessage {
    private final ArrayList lines = new ArrayList<>();
    private final MsgSource Source;
    private final CommandSender sender;
    private final boolean isValid;

    
    public MuchMessage(String[] rawLines, CommandSender lineSender){
        //Use lines from raw input
        sender = lineSender;
        isValid = readRawLines(rawLines, sender);
        Source = MsgSource.CUSTOM;
    }
    
    public MuchMessage(CommandSender lineSender){
        //Use randomised lines
        sender = lineSender;
        Source = MsgSource.RANDOM;
        generateLines(MuchCraft.randomLines);
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
                        MuchError.sendError(Error.NO_PERM_LINES, sender);
                        return false;
                    }
                }
                //Fewer lines than limit
                return true;
                
            } else {
                //The player doesn't have permission to send this type of message
                Error Err = Source==MsgSource.CUSTOM ? Error.NO_PERM_CUSTOM : Error.NO_PERM_RANDOM;
                MuchError.sendError(Err, sender);
                return false;
            }
        } else {
            //It's the console
            return true;
        }
    }
    
    public boolean isValid(){
        return isValid;
    }
    
    public void send(Server server){
        //First, broadcast pre-message
        String msg = MuchCraft.broadcastMessage;
        
        if(msg !=null){
            server.broadcastMessage(getPreMessage(msg, sender));
        }
        
        //Now send doge lines
        //rList stores which colors have been used to reduce repetition
        ArrayList<Integer> rList = new ArrayList<Integer>();
        //prevOffset stores previous offset value
        int prevOffset = -1; //Set to -1 to avoid checking on first line
        
        for (Object line : lines) {
            //Get randomised offset
            int r;
            String l = line.toString();
            int minDiff = (int) (MuchCraft.MIN_OFF_DIFF*0.01* (double)(MuchCraft.LINE_WIDTH - l.length()) );
            
            do{
                r = ranInt(0, MuchCraft.LINE_WIDTH - l.length());
            } while (prevOffset<0 ? false: (r < prevOffset+minDiff && r > prevOffset-minDiff));
            
            String offset = getOffsetString(r);
            prevOffset = r;
            
            //Get randomised colour
            //Clear rList when all colours have been used  
            if(rList.size() >= MuchCraft.color.size() -1) rList.clear();
            do {
                r = ranInt(0, MuchCraft.color.size() -1);
            } while(rList.contains(r));
            rList.add(r);
            
            //Get colour and broadcast
            ChatColor cCol = ChatColor.getByChar(MuchCraft.color.get(r));
            server.broadcastMessage(offset + cCol + l);
        }
    }
    
    private String getPreMessage(String msg, CommandSender sender){
        //Replace player name
        if (msg.contains("%player%")) {
            msg = msg.replace("%player%", sender.getName());
        }

        //Insert chat colours
        if (msg.toLowerCase().contains("%")) {
            String[] split = msg.split("%");
            msg = "";

            //If number of elements is even, there are an odd number of %% - invalid
            if (split.length % 2 == 0) {
                MuchCraft.log.warning("[MuchCraft] Error in broadcastMessage syntax: Odd number of % signs");
            } else {
                //Iterate through split string
                for (int i = 0; i < split.length; i++) {
                    String tmp = split[i];
                    ChatColor col;

                    //Only apply colour for odd values of i, i.e. values inside %%
                    if (i % 2 != 0 && !tmp.equals("")) {
                        col = ChatColor.getByChar(tmp);

                        if (col == null) {
                            MuchCraft.log.warning("[MuchCraft] Error in broadcastMessage syntax:");
                            MuchCraft.log.warning("[MuchCraft] " + tmp + " is not a valid colour");
                        } else {
                            msg = msg + col;
                        }
                    } else {
                        //Otherwise, put message back together
                        msg = msg + tmp;
                    }                    
                }
            }
        }
        return msg;
    }
    
    private String getOffsetString(int offset){
        String offStr = "";
        //Correct for space width (compared to widest character)
        offset = (int) (((double) offset)*MuchCraft.SPACE_WIDTH);
        for(int i=0; i<offset; i++){
            offStr = offStr + " ";
        }
        return offStr;
    }
    
    private void generateLines(int nLines){
        //rList stores which phrases have been used to reduce repetition
        ArrayList<Integer> rList = new ArrayList<Integer>(), sList = new ArrayList<Integer>();
        
        for(int i=0; i<nLines; i++){
            //Choose which full message or prefix to use - Numbered full first, then prefixed
            int fSize = MuchCraft.full.size();
            int pSize = MuchCraft.prefix.size();
            int r, rPre, rSuf;
            
            //Clear rList when full            
            if(rList.size() >= fSize+pSize) rList.clear();
            do {
                r = ranInt(0,fSize+pSize -1);
            //} while(listContains(rList, r));
            } while(rList.contains(r));
            
            rList.add(r);
            
            if(r<fSize){
                //A full message has been chosen
                lines.add(MuchCraft.full.get(r));
            } else {
                //Prefix chosen - generate suffix
                
                if(sList.size() >= fSize+pSize) sList.clear();
                do {
                    rSuf = ranInt(0, MuchCraft.suffix.size() -1);
                } while(sList.contains(rSuf));
                sList.add(rSuf);
                
                rPre = r-fSize;
                lines.add(MuchCraft.prefix.get(rPre) + " " + MuchCraft.suffix.get(rSuf));
            }
        }
    }
    /*
    private boolean listContains(ArrayList<Integer> l, int i){
        for (Integer n : l) {
            if (n == i) return true;
        }
        return false;
    }
    */
    private int ranInt(int min, int max){
        Random ran = new Random();
        return ran.nextInt((max-min) +1) +min;
    }
    
    private boolean readRawLines(String[] rawLines, CommandSender sender){
        //Produce custom output
        //Add trailing comma (if there isn't one already)    
        if(!rawLines[rawLines.length-1].endsWith(",")) rawLines[rawLines.length-1] += ",";
        
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
                
                //Only continue if it isn't blank
                if(!tmp.equals("")){ 
                    //Check that it's shorter than one line
                    if(tmp.length() >= MuchCraft.LINE_WIDTH){
                        MuchError.sendError(Error.LINE_TOO_LONG, sender);
                        return false;
                    }
                    
                    lines.add(tmp);
                }
                
                tmp = ""; space = "";
                n = n+1;
            }
        }
        
        return true;
    }
    
}
