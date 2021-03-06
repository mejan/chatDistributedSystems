/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.heka1203.main;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author mejan
 */
public class ChatPanel extends javax.swing.JPanel {

    /**
     * Creates new form ChatWindow
     */
    public ChatPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupTextField = new javax.swing.JTextField();
        joinButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();
        leaveButton = new javax.swing.JButton();
        sendTextField = new javax.swing.JTextField();
        SendButton = new javax.swing.JButton();
        messageTextArea = new javax.swing.JTextArea();
        reChoice = new javax.swing.JButton();

        groupTextField.setText("Chat Room");
        groupTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupTextFieldActionPerformed(evt);
            }
        });

        joinButton.setText("Join Group");

        createButton.setText("Create Group");

        leaveButton.setText("Leave Group");

        sendTextField.setText("text to send");

        SendButton.setText("Send");

        messageTextArea.setColumns(20);
        messageTextArea.setRows(5);

        reChoice.setText("Rechoice Order");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(messageTextArea)
                    .addComponent(sendTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(groupTextField)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(joinButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(createButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(leaveButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(SendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reChoice, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(groupTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(joinButton)
                    .addComponent(createButton)
                    .addComponent(leaveButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SendButton)
                    .addComponent(reChoice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageTextArea, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void groupTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groupTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_groupTextFieldActionPerformed

    public JButton getReChoice(){
        return reChoice;
    }
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton SendButton;
    public javax.swing.JButton createButton;
    private javax.swing.JTextField groupTextField;
    public javax.swing.JButton joinButton;
    public javax.swing.JButton leaveButton;
    private javax.swing.JTextArea messageTextArea;
    public javax.swing.JButton reChoice;
    public javax.swing.JTextField sendTextField;
    // End of variables declaration//GEN-END:variables

    public JButton getCreateButton() {
        return createButton;
    }

    public JTextField getGroupTextField() {
        return groupTextField;
    }

    public JButton getJoinButton() {
        return joinButton;
    }

    public JButton getLeaveButton() {
        return leaveButton;
    }

    public JTextArea getMessageTextArea() {
        return messageTextArea;
    }

    public JButton getSendButton() {
        return SendButton;
    }

    public JTextField getSendTextField() {
        return sendTextField;
    }
    
}
