/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.heka1203.ordering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import se.heka1203.main.Client;
import se.heka1203.main.Message;
import se.heka1203.main.MessageType;

/**
 *
 * @author heka1203
 */
public class TotalOrder {
//http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.3.4709&rep=rep1&type=pdf

    private int seqNum;
    private int nextDeliver;
    private long lastMsgRecId;
    private PriorityQueue<Message> pending;
    private List<Message> repliedMessages;
    private Client client;

    
    public TotalOrder(Client client){
        this.client = client;
        this.seqNum = 1;
        this.nextDeliver = 1;
        pending = new PriorityQueue<>(11, new Comparator<Message>(){
           @Override
           public int compare(Message m1, Message m2){
                
               return (m1.getSeqNum() < m2.getSeqNum() ? -1 : 1);
           } 
        });
        repliedMessages = new ArrayList<>();
    }
    public void sendMessage(String text) throws IOException{
        Message msg = new Message(client.getGroupId(), client.getId(), text, MessageType.TEXTMSG);
        if(client.isLeader()){
            System.out.println("Leader " + client.getId() + " skickar ett meddelande. SeqNum: " + seqNum);
            sendLeaderMessage(msg);
        }
        else{
            System.out.println("Client " + client.getId() + " skickar ett meddelande. Nextdeliver: " + nextDeliver);
            if(Math.random() > 0.5)
                client.sendMessage(msg); 
        }
            
    }
    public void receiveMessage(Message msg) throws IOException{
        if(msg.getSeqNum() != -1 && !client.isLeader()){
            if(msg.getSeqNum() >= nextDeliver){
                pending.add(msg);
                tryDelivery();                
            }


        } 
        else if(msg.getSeqNum() == -1 && client.isLeader()){
            sendLeaderMessage(msg);
        }
            
     
    }
    public void sendLeaderMessage(Message msg) throws IOException{
        msg.setSeqNum(seqNum);
        
        //if not in replied messages deliver and reply, else dont
        deliver(msg);
        repliedMessages.add(msg);
        incrementSeqNum();
        System.out.println("Ledare svarar med sequencenummer: " + msg.getSeqNum());
        if(Math.random() > 0.5)
            client.sendMessage(msg);
    }
    public void incrementSeqNum(){
        seqNum += 1;
    }
    public void tryDelivery() throws IOException{
        Iterator<Message> it = pending.iterator();
        while(it.hasNext()){
            Message msg = it.next();
            if(msg.getSeqNum() == nextDeliver){
                lastMsgRecId = msg.getId();
                deliver(msg);
                it.remove();
            }
        }
        if(!pending.isEmpty()){
            if(pending.peek().getSeqNum() > nextDeliver)
                requestMessages(lastMsgRecId);            
        }


    }
    public void requestMessages(long lastMsgRecId) throws IOException{
        Message msg = new Message(client.getGroupId() ,client.getId(), MessageType.TEXTMSG_LOST, lastMsgRecId, nextDeliver);
        System.out.println("Client id : " + client.getId() + " requestar msg id: " + lastMsgRecId + " nextDeliver: " + nextDeliver);
        client.sendMessage(msg);
    }
    public void resendMessages(int nextDeliver) throws IOException{
        ListIterator<Message> it = repliedMessages.listIterator(repliedMessages.size());
        while(it.hasPrevious()){
            Message msgToResend = it.previous();
            System.out.println("Process id " + client.getId() + " skickar om ett meddelande med id: " + msgToResend.getId() + " with seq num" + msgToResend.getSeqNum());
            client.sendMessage(msgToResend); 
            if(msgToResend.getSeqNum() == nextDeliver) return;
        }

    }
    public void resetTotal(){
        pending.clear();
        repliedMessages.clear();
        this.seqNum = 1;
        this.nextDeliver = 1;        
    }
    
    public void setReqNum(int reqNum) {
        this.nextDeliver = reqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }
    public void incrementNextDeliver(){
        this.nextDeliver += 1;
    }
    public int getSeqNum() {
        return seqNum;
    }

    public int getReqNum() {
        return nextDeliver;
    }

    private void deliver(Message msg) {
        
        client.getChat().getMessageTextArea().append(msg.getClientId() + "\t" + msg.getText() + "\n");
        System.out.println("Delivered : " + msg.getText());
        incrementNextDeliver();
    }


    
}
