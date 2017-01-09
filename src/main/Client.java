/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.heka1203.main;

import java.io.IOException;

import java.net.SocketException;
import java.net.UnknownHostException;


import se.heka1203.network.SocketHandler;
import se.heka1203.network.SocketHandler.OnMessageListener;
import se.heka1203.ordering.CausalOrder;
import se.heka1203.ordering.TotalOrder;

/**
 *
 * @author heka1203
 */
public class Client {
    private long id;
    private String groupId = null;
    private SocketHandler socketHandler;
    private boolean isLeader = false;
    private long lastReceived;
    private boolean isCausal;
    private boolean isTotal;
    private CausalOrder causalOrder;
    private TotalOrder totalOrder;
    private ChatPanel chat;
    private final int sendPortNumber;
    private final int recPortNumber;

    public ChatPanel getChat() {
        return chat;
    }

    //private Total total;

    private OnMessageListener msgListener = new OnMessageListener() {
    
            @Override
            public void messageReceived(Message msg) { //filter method
                try{
                    if(msg.getGroupId().equals(groupId)){
                        switch(msg.getMsgType()){
                            
                            case ELECTION:
                                if(isTotal)
                                    totalOrder.resetTotal();
                                else
                                    causalOrder.resetCausal();
                                
                                if(id > msg.getClientId()){
                                   sendMessage(MessageType.ELECTION);
                                   isLeader = true;
                                   System.out.println("Client " + getId() + " is now leader.");
                                }
                                    
                                else if(id < msg.getClientId()){
                                   isLeader = false;
                                }
                                break;
                                    
                            
                            case ALIVE:
                                lastReceived = System.currentTimeMillis();                               

                                    
                                break;
                            case TEXTMSG:
                                //add discovered processes as an entry to this client vectorclock
                                if(isCausal){
                                    causalOrder.receiveMessage(msg);
                                }
                                else if(isTotal){
                                    totalOrder.receiveMessage(msg);
                                   
                                }

                                break;
                            
                            case TEXTMSG_LOST:
                                if(isCausal){
                                    causalOrder.resendMessages(msg);
                                }
                                else if(isTotal && isLeader){
                                    totalOrder.resendMessages(msg.getSeqNum());
                                }                                
                                
                                break;
                                
                                

                        }
                        
                    }                    
                }catch(IOException ex){
                    ex.printStackTrace();
                }

                    
            }

        @Override
        public void socketTimeout() {
            try{
                if(groupId != null){
                    if(isLeader){
                        sendMessage(MessageType.ALIVE);
                        System.out.println("Alive message from: " + id + " timestamped: " + System.currentTimeMillis());
                    }
                    else if(System.currentTimeMillis() - lastReceived > 5*1000){
                        sendMessage(MessageType.ELECTION);
                        System.out.println("Not heard from any leader, re-election started by: " + id);

                    }
                }

            }catch(IOException ex){
                ex.printStackTrace();
            }

        }
    };
    
    
    
    /*public Client() throws SocketException{
        id = System.currentTimeMillis();
        socketHandler = new SocketHandler();
        socketHandler.setOnMessageListener(msgListener);
        socketHandler.start();
        
        //Create and start socket instance

    }*/
    public Client(int sendPortNumber, int recPortNumber, ChatPanel chat){
        id = System.currentTimeMillis();
        this.chat = chat;
        this.sendPortNumber = sendPortNumber;
        this.recPortNumber = recPortNumber;
        this.socketHandler = null;
        //init vClock for this client
        


    }
    private void createSocketHandler() throws SocketException, UnknownHostException{
        socketHandler = new SocketHandler();
        socketHandler.setSendPortNumber(sendPortNumber);
        socketHandler.setRecPortNumber(recPortNumber);
        socketHandler.bind();
        socketHandler.setOnMessageListener(msgListener);
        
        socketHandler.start();        
    }
    public void sendMessage(String text) throws IOException{
        System.out.println("client sendmessage");
        if(groupId == null) throw new RuntimeException("You are not in a group.");
        //increment this clients vector clock before sending
        
        if(isCausal)
            causalOrder.sendMessage(text);
        else if(isTotal)
            totalOrder.sendMessage(text);
    }
    public void sendMessage(MessageType msgType) throws IOException{
        if(groupId == null) throw new RuntimeException("You are not in a group.");
        Message msg = new Message(groupId, id, msgType);
        socketHandler.sendMessage(msg);
    }

    public void sendMessage(Message msg) throws IOException{
        socketHandler.sendMessage(msg);
    }

    public void joinGroup(String groupId) throws IOException{
        if(this.groupId != null) throw new RuntimeException("You are already in a group.");
        
        if(socketHandler != null){
            socketHandler = null;
        }
        createSocketHandler(); 


        this.id = System.currentTimeMillis();
        //initialize clock
        if(isCausal){
            causalOrder = new CausalOrder(this);

            
            //skapa causal object
        }
        else if(isTotal = true){
            //total object
            totalOrder = new TotalOrder(this);

        }
        

        this.groupId = groupId;
        sendMessage(MessageType.ELECTION);
        this.isLeader = true;


    }
    public String leaveGroup() throws InterruptedException{
        String prevGroup = groupId;
        this.groupId = null;
        socketHandler.stopRunning();
        socketHandler.join();
        return prevGroup;
    }

    public long getId(){
        return id;
    }
    public String getGroupId(){
        return groupId;
    }
    public void setGroupId(String groupId){
        this.groupId = groupId;
    }
    public boolean isLeader(){
        return isLeader;
    }
    public void setCausal(){
        this.isCausal = true;
    }
    public void setTotal(){
        this.isTotal = true;
    }

    
}
