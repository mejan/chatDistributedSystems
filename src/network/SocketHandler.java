/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.heka1203.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.heka1203.main.Message;

/**
 *
 * @author heka1203
 */
public class SocketHandler extends Thread{
    private DatagramSocket socket = null;

    public DatagramSocket getSocket() {
        return socket;
    }
    private final String broadCastAddress = "255.255.255.255";
    private int sendPortNumber = 1234;
    private int recPortNumber = 1234;
    private volatile boolean connected = false;
    private final int packetSize = 1024; //Hard to determine
    private OnMessageListener msgListener = null; 
    
    public SocketHandler() throws SocketException{
   
    }
    public void bind() throws SocketException, UnknownHostException{
        if(socket != null)
            socket.close();
        socket = new DatagramSocket(recPortNumber);
        socket.setSoTimeout(2*1000);
        startRunning();
    }
    public void stopRunning(){
        connected = false;
        //socket.close();
    }
    
    public void startRunning(){
        
        connected = true;
    }
    public boolean isRunning(){
        return connected;
    }
    public DatagramPacket msgToPacket(Message msg) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(msg);
        byte[] buffer = baos.toByteArray();
        return new DatagramPacket(buffer, buffer.length, InetAddress.getByName(broadCastAddress), sendPortNumber);        
    }
    public Message packetToMsg(DatagramPacket packet) throws IOException, ClassNotFoundException{
        byte[] buffer = packet.getData();

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Message)ois.readObject();
        
        
    }
    //TODO: fix packet to msg
    public void sendMessage(Message msg) throws IOException{
        
        DatagramPacket packet = msgToPacket(msg);
        socket.send(packet);
        
    }
    public void run(){
        
        while(connected){
            //this size may be a problem
            byte[] buffer = new byte[packetSize];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                
                socket.receive(packet);
                //Check length of what was actually received
                if(packet.getLength() > 0){
                    //process data

                    Message msg = packetToMsg(packet); 
                    if(msgListener != null && msg != null)
                        msgListener.messageReceived(msg);

                }
                
                
            } 
            catch(SocketTimeoutException ex){
                if(msgListener != null){
                    msgListener.socketTimeout();
                }
            }
            catch (IOException ex) {
                Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex){
                ex.printStackTrace();
            }
   
        }
        socket.close();
        return;
    }
    public void setSendPortNumber(int portNumber){
        this.sendPortNumber = portNumber;
    }
    
    public void setRecPortNumber(int portNumber){
        this.recPortNumber = portNumber;
    }
    public void setOnMessageListener(OnMessageListener msgListener){
        this.msgListener = msgListener;
        
    }
    public interface OnMessageListener{
        void messageReceived(Message msg);
        void socketTimeout();
    }
}
