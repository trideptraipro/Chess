/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MangMayTinh.Chess.Connection.Server;

import MangMayTinh.Chess.Model.DAO.ModifyAccount;
import MangMayTinh.Chess.Model.Entity.Account;
import MangMayTinh.Chess.Model.Entity.UserInfo;
import MangMayTinh.Chess.Model.Enum.MessageType;
import MangMayTinh.Chess.Packet.LoginPacket;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author thinhle
 */
public class ServerInfo extends javax.swing.JFrame {
    ArrayList<Player> clients = new ArrayList<>();
    Thread listener;
    ObjectOutputStream sender = null;
    ObjectInputStream receiver = null;


    
    /**
     * Creates new form ServerInfo
     */
    public ServerInfo() {
        initComponents();
        InetAddress inetAddress = this.getInetAddress();
        this.ipAddressLabel.setText(inetAddress.getHostAddress());
        this.getRootPane().setDefaultButton(startServerButton);
    }
    
    private void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            ServerInfo server=this;
            this.listener= new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            System.out.println("Waiting client to connect!");
                            Socket socket = serverSocket.accept();
                            receiver= new ObjectInputStream(socket.getInputStream());
                            sender= new ObjectOutputStream(socket.getOutputStream());
                            System.out.println("Đã kết nối");
                            new HandleServer(socket,receiver, sender).start();

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            this.listener.start();
            this.messageLabel.setText("ServerInfo started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        //------------------------------ private function ----------------------------------
    synchronized void addClient(Player client) {
        System.out.println("Adding client");
        clients.add(client);
        System.out.println("num of client: " + clients.size());
        if (clients.size() >= 2) {
            Player firstPlayer = clients.remove(0);
            Player secondPlayer = clients.remove(0);
            Match match = new Match(firstPlayer, secondPlayer);
            Thread thread=new Thread(match);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Start a new match");
        }
    }
    
    private InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while( b.hasMoreElements()){
                for (InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if ( f.getAddress().isSiteLocalAddress()) {
                        return f.getAddress();
                    }
            }
        } catch (SocketException e) {
            System.out.println(e.toString());
            messageLabel.setForeground(Color.red);
            this.messageLabel.setText("Failed to find inet address");
        }
        return null;
    }
    

    
    private void log(String log) {
        System.out.println(log);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        startServerButton = new javax.swing.JButton();
        stopServerButton = new javax.swing.JButton();
        messageLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        portTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        ipAddressLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jMenu3.setText("jMenu3");

        jMenu4.setText("jMenu4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chess ServerInfo");
        setResizable(false);

        startServerButton.setText("Start server");
        startServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startServerButtonActionPerformed(evt);
            }
        });

        stopServerButton.setText("Stop server");
        stopServerButton.setToolTipText("stop server");
        stopServerButton.setEnabled(false);
        stopServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopServerButtonActionPerformed(evt);
            }
        });

        messageLabel.setForeground(new java.awt.Color(255, 0, 0));
        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageLabel.setText("ServerInfo: Closed");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel1.setText("Port:");

        portTextField.setText("5555");
        portTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        portTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                portTextFieldDidChange(evt);
            }
        });

        jLabel2.setText("IP:");

        ipAddressLabel.setForeground(new java.awt.Color(0, 0, 255));
        ipAddressLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ipAddressLabel.setText("IP Adress");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(ipAddressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(53, 53, 53))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ipAddressLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fileMenu.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(773, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(messageLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(328, 328, 328)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(stopServerButton)
                    .addComponent(startServerButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(240, 240, 240)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(messageLabel)
                .addGap(94, 94, 94)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startServerButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopServerButton)
                .addContainerGap(196, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void portTextFieldDidChange(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_portTextFieldDidChange
        if (!portTextField.getText().equals("")) {
            startServerButton.setEnabled(true);
        } else {
            startServerButton.setEnabled(false);
        }
    }//GEN-LAST:event_portTextFieldDidChange

    private void startServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startServerButtonActionPerformed
        String portString = portTextField.getText();
        try {
            System.out.println("Start");
            int port = Integer.parseInt(portString);
            messageLabel.setForeground(Color.green);
            messageLabel.setText("ServerInfo started!");
            System.out.println("RED");
            startServerButton.setEnabled(false);
            stopServerButton.setEnabled(true);
            this.startServer(port);
        } catch (Exception e) {
            System.out.println(e.toString());
            messageLabel.setForeground(Color.red);
            messageLabel.setText("Failed to start server with port number: " + portString);
        }
    }//GEN-LAST:event_startServerButtonActionPerformed

    private void stopServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopServerButtonActionPerformed
        System.out.println("Stoping server");
        startServerButton.setEnabled(true);
        stopServerButton.setEnabled(false);
    }//GEN-LAST:event_stopServerButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerInfo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel ipAddressLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTextField portTextField;
    private javax.swing.JButton startServerButton;
    private javax.swing.JButton stopServerButton;
    // End of variables declaration//GEN-END:variables
    /** Private Class*/
    private class HandleServer extends Thread{
        ObjectOutputStream sender=null;
        ObjectInputStream receiver=null;
        ServerInfo server;
        Socket socket;
        HandleServer( Socket socket, ObjectInputStream receiver, ObjectOutputStream sender){
            this.socket=socket;
            System.out.println("1");
            this.sender=sender;
            this.receiver=receiver;
        }
        <T> void sendMessageToClient(MessageType type, T data) {
            try {
                sender.writeObject(type);
                sender.writeObject(data);
            } catch (Exception e) {
                System.out.print("Send data Error: ");
                System.out.println(e.toString());
            }
        }

        @Override
        public void run() {
            while (true){
                System.out.println("2");
                try {
                    MessageType mess=(MessageType) receiver.readObject();
                    switch (mess){
                        case login:
                            Account account= (Account)receiver.readObject();
                            LoginPacket loginPacket=null;
                            if(MangMayTinh.Chess.Model.DAO.ModifyAccount.checkAccount(account)){
                                loginPacket= new LoginPacket(account, "Accept");
                                loginPacket.setUserInfo(ModifyAccount.getUserInfoByUserName(account.getUsername()));
                                sendMessageToClient(MessageType.login,loginPacket );
                            }else {
                                System.out.println("12");
                                loginPacket= new LoginPacket(account, "Deny");
                                sendMessageToClient(MessageType.login, loginPacket);
                            }
                            break;
                        case play:
                            sendMessageToClient(MessageType.string, "Connected. Waiting for second player!");
                            Player newClient = new Player(socket, sender, receiver);
                            addClient(newClient);
                            break;
                        case register:
                            Account account1=(Account)receiver.readObject();
                            UserInfo userInfo=(UserInfo)receiver.readObject();
                            if(MangMayTinh.Chess.Model.DAO.ModifyAccount.addAccount(account1,userInfo)!=0){
                                System.out.println("aaa");
                                sendMessageToClient(MessageType.register, "Accept");
                            }else {
                                System.out.println("aaa1");
                                sendMessageToClient(MessageType.register, "Deny");
                            }
                            break;
                        default:
                            System.out.println("No anything");
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }
        }
    }
}

