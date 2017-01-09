/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.heka1203.main;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author heka1203
 */
//This is not a good class to serialize...s
public class Message implements Serializable {
    private final String groupId; //Sent to
    private final long clientId; //Sent from
    private final long id;
    private long lastMsgRecId;
    private String text;
    private MessageType msgType;
    private Map<Long, Integer> clock;
    private int seqNum;


    
    public Message(String groupId, long clientId){
        this.groupId = groupId;
        this.clientId = clientId;
        this.text = "";
        this.id = System.currentTimeMillis();
        this.seqNum = -1;
        this.clock = new TreeMap<>();
    }

    public Message(String groupId, long clientId, MessageType msgType){
        this(groupId, clientId);
        this.msgType = msgType;
    }
    public Message(String groupId, long clientId, String text, MessageType msgType){
        this(groupId, clientId);
        this.text = text;
        this.msgType = msgType;
        
    }
    public Message(String groupId, long clientId, String text, Map<Long,Integer> clock){
        this(groupId, clientId, text, MessageType.TEXTMSG);
        this.clock.putAll(clock);

    }
    public Message(String groupId, long clientId, MessageType msgType, long lastMsgRecId){
        this(groupId, clientId, msgType);
        this.lastMsgRecId = lastMsgRecId;
        
    }
    public Message(String groupId, long clientId, MessageType msgType, long lastMsgRecId, int seqNum){
        this(groupId, clientId, msgType, lastMsgRecId);
        this.seqNum = seqNum;  
    }
    
    public String getGroupId(){
        return groupId;
    }
    public long getClientId(){
        return clientId;
    }
    public long getId(){
        return this.id;
    }
    public long getLastMsgRecId(){
        return this.lastMsgRecId;
    }
    public String getText(){
        return text;
    }
    public MessageType getMsgType(){
        return msgType;
    }
    public Map<Long,Integer> getClock(){
        return clock;
    }
    public int getSeqNum(){
        return seqNum;
    }
    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }    
    public String toString(){
        return "Group id " + groupId 
                + " client id " + clientId 
                + " text " + text; 
    }
}
