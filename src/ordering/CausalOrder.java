/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.heka1203.ordering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import se.heka1203.main.Client;
import se.heka1203.main.Message;
import se.heka1203.main.MessageType;


/**
 *
 * @author heka1203
 */
public class CausalOrder {
    private final Map<Long, Integer> clock;
    //Last received msg from each process
    private final Map<Long, Long> lastRecMessages;
    private final Queue<Message> holdMessages;
    private final List<Message> sentMessages;
    private final Client client;


    
    public CausalOrder(Client client){
        this.client = client;
        
        holdMessages = new LinkedList<>();
        sentMessages = new ArrayList<>();
        clock = new TreeMap<>();
        clock.put(client.getId(), 0);
        lastRecMessages = new TreeMap<>();
        
    }
    public void resetCausal(){
        clock.clear();
        lastRecMessages.clear();
        holdMessages.clear();
        sentMessages.clear();
    }
    public void sendMessage(String text) throws IOException{
        incrementClock(client.getId());
        
        //include clock in each message
        Message msg = new Message(client.getGroupId(), client.getId(), text, clock);
        System.out.println("Innan meddelande läggs till i listan. Klockvärde: " + clock.toString());
        sentMessages.add(msg);
        deliver(msg);
        System.out.println("Client " + client.getId() + " skickar ett meddelande. Clock: " + clock.toString());
        if(Math.random() > 0.2)
            client.sendMessage(msg);


    }
    public void requestMessages() throws IOException{
        if(!holdMessages.isEmpty()){
            for(Message msg : holdMessages){
               for(Map.Entry<Long, Integer> clock : msg.getClock().entrySet()){

                   if(clock.getValue() > this.clock.get(clock.getKey())){
                       long lastMsgRecId = 0;
                       if(lastRecMessages.containsKey(clock.getKey()))
                           lastMsgRecId = lastRecMessages.get(clock.getKey());
                       Message replyMsg = new Message(client.getGroupId(), client.getId(), MessageType.TEXTMSG_LOST, lastMsgRecId);
                       client.sendMessage(replyMsg);
                       
                       System.out.println("Client: " + client.getId() + " ber om omskickning. Clock: " + this.clock.toString());
                       return;
                   }
               }
           }           
        }

    }
    public void incrementClock(long clientId){
        if(!clock.containsKey(clientId))
            clock.put(clientId, 0);
        
        int newTime = clock.get(clientId) + 1;
        clock.put(clientId, newTime);
    }

    private void tryDelivery() throws IOException{
        Iterator<Message> it = holdMessages.iterator();
        while(it.hasNext()){
            Message msg = it.next();
            if(isCausalDelivery(msg.getClock(), msg.getClientId())){
                deliver(msg);
                incrementClock(msg.getClientId());
                
                it.remove();
                
                //If one msg could be delivered, reset and check if previous checked can now be delivered.
                it = holdMessages.iterator();
                
            }
            
            
        }
        requestMessages();
    }
    public void deliver(Message msg){
        lastRecMessages.put(msg.getClientId(), msg.getId());
        client.getChat().getMessageTextArea().append(msg.getClientId() + "\t" + msg.getText() + "\n");
        
    }
    public boolean isCausalDelivery(Map<Long, Integer> compClock, long recId){
        
        if(clock.get(recId) + 1 != compClock.get(recId)) return false;
        for(Long recKey : compClock.keySet()){
            if(recKey != recId){
                if(compClock.get(recKey) > clock.get(recKey))
                    return false;
            } 
            
        
        }
        return true;
    }
    
    public void receiveMessage(Message msg) throws IOException{

        
        if(msg.getClientId() != client.getId()){
            System.out.println("Mottaget meddelandes klocka: " + msg.getClock().toString());
            initClock(msg.getClock());
            if(isCausalDelivery(msg.getClock(), msg.getClientId())){
                incrementClock(msg.getClientId());
                deliver(msg);
                //check queue for messages that may be delivered
                
            }
            else{
                holdMessages.add(msg);
                
            }
            if(!holdMessages.isEmpty())
                tryDelivery();
        }
        

    }
    public void resendMessages(Message msg) throws IOException{
        if(msg.getClientId() != client.getId()){
            for(Message msgToResend : sentMessages){
                if(msgToResend.getId() == msg.getLastMsgRecId()) return;
                System.out.println("Process id " + client.getId() + " skickar om ett meddelande: " + msgToResend.getId() +" text: "+ msgToResend.getText() +" Clock: " + msgToResend.getClock().toString());
                client.sendMessage(msgToResend);                
            }
           
        }


    }
    private void initClock(Map<Long, Integer> clock){
        for(Long key : clock.keySet()){
            if(!this.clock.containsKey(key))
                this.clock.put(key, 0);
        }
    } 

}
