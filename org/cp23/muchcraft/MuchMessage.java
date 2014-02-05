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
    private boolean isValid;
    private final int LINE_WIDTH = 52;
    private final double SPACE_WIDTH = 52.0/34.0;
    
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
    
    public boolean isValid(){
        return isValid;
    }
    
    public void send(Server server){
        //Send message
        //rList stores which colors have been used to reduce repetition
        ArrayList<Integer> rList = new ArrayList<Integer>();
        
        for(int i=0; i<lines.size(); i++){
            int r;
            String l = lines.get(i).toString();
            String offset = getOffsetString(ranInt(0, LINE_WIDTH - l.length()));
            
            //Clear rList when full  
            if(rList.size() >= MuchCraft.color.size() -1) rList.clear();
            do {
                r = ranInt(0, MuchCraft.color.size() -1);
            } while(listContains(rList, r));
            rList.add(r);
            
            //Get color and broadcast
            ChatColor cCol = ChatColor.getByChar(MuchCraft.color.get(r));
            server.broadcastMessage(offset + cCol + l);
        }
    }
    
    private String getOffsetString(int offset){
        String offStr = "";
        //Correct for space width (compared to widest character)
        offset = (int) (((double) offset)*SPACE_WIDTH);
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
            } while(listContains(rList, r));
            
            rList.add(r);
            
            if(r<fSize){
                //A full message has been chosen
                lines.add(MuchCraft.full.get(r));
            } else {
                //Prefix chosen - generate suffix
                
                if(sList.size() >= fSize+pSize) sList.clear();
                do {
                    rSuf = ranInt(0, MuchCraft.suffix.size() -1);
                } while(listContains(sList, rSuf));
                sList.add(rSuf);
                
                rPre = r-fSize;
                lines.add(MuchCraft.prefix.get(rPre) + " " + MuchCraft.suffix.get(rSuf));
            }
        }
    }
    
    private boolean listContains(ArrayList<Integer> l, int i){
        for(int n=0; n<l.size(); n++){
            if(l.get(n)==i) return true;
        }
        return false;
    }
    
    private int ranInt(int min, int max){
        Random ran = new Random();
        return ran.nextInt((max-min) +1) +min;
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
        
        return true;
    }
    
}
