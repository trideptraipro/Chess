/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MangMayTinh.Chess.Connection.Client;

import MangMayTinh.Chess.Model.Chessboard;
import MangMayTinh.Chess.Model.Entity.Account;
import MangMayTinh.Chess.Model.Entity.UserInfo;
import MangMayTinh.Chess.Model.Interface.ChessboardInterface;
import MangMayTinh.Chess.Model.Enum.MessageType;
import MangMayTinh.Chess.Model.Move;
import MangMayTinh.Chess.View.Information;
import MangMayTinh.Chess.View.Login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.*;

/**
 *
 * @author thinhle
 */
public class Client extends javax.swing.JFrame implements ChessboardInterface {

    Socket socket = null;
    int port = 0;
    String host = "";
    ObjectInputStream receiverChess=null;
    ObjectOutputStream senderChess=null;
    ObjectOutputStream sender = null;
    ObjectInputStream receiver = null;
    String myName = "";
    String secondPlayerName = "";
    Thread listener;
    Chessboard chessboard;
    boolean isMyTurn = false;
    boolean isFirstPlayer = false;
    boolean isRunning = false;
    UserInfo userInfo;
    Login login;
    public Client(UserInfo userInfo, Login login){
        this.userInfo=userInfo;
        this.login= login;
        this.sender = login.getSender();
        this.receiver = login.getReceiver();
        initComponents();
        this.getRootPane().setDefaultButton(joinButton);
        this.nameTextFiled.setText(userInfo.getUsername());
    }

    /**
     * Creates new form Client
     */
    public Client() {
        initComponents();
        this.getRootPane().setDefaultButton(joinButton);
    }

    // --------------------- private function ---------------------------
    private void play() {
        this.isRunning = true;
        this.listener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        MessageType type = (MessageType) receiverChess.readObject();
                        System.out.println("type: " + type);
                        switch (type) {
                            case string:
                                String messageFromServer = (String) receiverChess.readObject();
                                messageLabel.setText(messageFromServer);
                                break;
                            case move:
                                Move move = (Move) receiverChess.readObject();
                                Move newMove = move.clone();
                                moveOpponent(newMove);
                                break;
                            case result:
                                boolean isWinner = (boolean) receiverChess.readObject();
                                informResult(isWinner);
                                break;
                            case startGame:
                                startGame();
                                break;
                            case turn:
                                isMyTurn = true;
                                break;
                            case isFirstPlayer:
                                boolean isFirst = (boolean) receiverChess.readObject();
                                isFirstPlayer = isFirst;
                                sendMessageToServer(MessageType.name, myName);
                                break;
                            case name:
                                secondPlayerName = (String) receiverChess.readObject();
                                break;
                            case message:
                                String message = (String) receiverChess.readObject();
                                chessboard.addMessageHistory(message, !isFirstPlayer);
                                break;
                            default:
                                System.out.println("Can not cast data from socket to expect message type!");
                                break;
                        }
                    } catch (IOException ex) {
                        System.out.println("IO Exception: From client (play): " + ex.getMessage());
                        System.out.println("caused by: " + ex.getCause().toString());
                    } catch (ClassNotFoundException ex) {
                        System.out.println("Class Not Found Exception: From client (play)");
                        ex.printStackTrace();
                    }
                }
            }
        });
        this.listener.start();
    }

    private void moveOpponent(Move move) {
        System.out.println("Opponent's move: ");
        this.chessboard.move(move);
        this.isMyTurn = true;
        this.chessboard.setMessage("Your turn!");
        if (isFirstPlayer) {
            this.chessboard.switchTurn(1);
        } else {
            this.chessboard.switchTurn(2);
        }
    }

    private void informResult(boolean isWinner) {
        String title = this.myName;
        String message = "YOU WIN!";
        if (!isWinner) {
            message = "YOU LOSE!";
        }
        this.messageLabel.setText(message);
        this.chessboard.setMessage(message);
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        this.chessboard.destruct();
        this.chessboard.dispose();
        this.joinButton.setEnabled(true);
        this.isRunning = false;
        this.sendOperation(MessageType.endGame);
    }

    private void startGame() {
        this.chessboard = new Chessboard();
        this.chessboard.setIsFirstPlayer(isFirstPlayer);
        this.chessboard.drawChessboard();
        this.chessboard.setDelegate(this);
        this.chessboard.setVisible(true);
        if (this.isMyTurn) {
            chessboard.setMessage("It's your turn!");
        } else {
            this.chessboard.setMessage("Your opponent's turn!");
        }
        if (this.isFirstPlayer) {
            this.chessboard.setPlayerName(this.myName, secondPlayerName);
        } else {
            this.chessboard.setPlayerName(secondPlayerName, this.myName);
        }
    }

    private void didChangeInput() {
        if (!this.serverIPTextField.getText().equals("") && !this.portTextField.getText().equals("") && !this.nameTextFiled.getText().equals("")) {
            this.joinButton.setEnabled(true);
        } else {
            this.joinButton.setEnabled(false);
        }
    }

    private <T> void sendMessageToServer(MessageType type, T data) {
        try {
            senderChess.writeObject(type);
            senderChess.writeObject(data);
        } catch (Exception e) {
            System.out.print("Send data Error: Client");
            System.out.println(e.toString());
        }
    }

    public void sendOperation(MessageType type) {
        try {
            this.senderChess.writeObject(type);
        } catch (Exception e) {
            System.out.print("Send data Error: ");
            System.out.println(e.toString());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        serverIPTextField = new javax.swing.JTextField();
        portTextField = new javax.swing.JTextField();
        nameTextFiled = new javax.swing.JTextField();
        joinButton = new javax.swing.JButton();
        infoButton =new JButton();
        messageLabel = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jMenuItem1.setText("jMenuItem1");

        jMenu3.setText("jMenu3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chess client");
        setResizable(false);

        jLabel1.setText("ServerInfo IP:");

        jLabel2.setText("Port:");

        jLabel3.setText("Name:");

        serverIPTextField.setText("localhost");
        serverIPTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                didChangeServerIP(evt);
            }
        });

        portTextField.setText("6969");
        portTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                didChangePort(evt);
            }
        });

        nameTextFiled.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        nameTextFiled.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nameDidChange(evt);
            }
        });
        infoButton.setText("Thông tin");
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoButtonActionPerform(e);
            }
        });
        joinButton.setText("New Game");
        joinButton.setEnabled(false);
        joinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinButtonActionPerformed(evt);
            }
        });

        messageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(168, 168, 168)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nameTextFiled, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(29, 29, 29)
                        .addComponent(serverIPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(193, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(messageLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(320, 320, 320)
                .addComponent(joinButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(320,320, 320)
                .addComponent(infoButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(messageLabel)
                .addGap(93, 93, 93)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(serverIPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(nameTextFiled, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addComponent(joinButton)
                .addContainerGap(209, Short.MAX_VALUE)
                .addGap(47,47,47)
                .addComponent(infoButton)
                .addContainerGap(209, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void infoButtonActionPerform(ActionEvent e) {//event join Button
        Information information= new Information(userInfo);
        information.setVisible(true);
    }

    private void didChangeServerIP(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_didChangeServerIP
        this.didChangeInput();
    }//GEN-LAST:event_didChangeServerIP

    private void didChangePort(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_didChangePort
        this.didChangeInput();
    }//GEN-LAST:event_didChangePort

    private void nameDidChange(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameDidChange
        this.didChangeInput();
    }//GEN-LAST:event_nameDidChange

    private void joinButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinButtonActionPerformed
        String portString = this.portTextField.getText();
        String host = this.serverIPTextField.getText();
        String name = this.nameTextFiled.getText();
        try {
            int port = Integer.parseInt(portString);
            this.messageLabel.setForeground(Color.blue);
            this.messageLabel.setText("Wait!");
            if (this.socket == null){
                this.socket = new Socket(host, port);
                this.senderChess = new ObjectOutputStream(this.socket.getOutputStream());
                this.receiverChess = new ObjectInputStream(this.socket.getInputStream());
                this.messageLabel.setForeground(Color.blue);
            }
            this.messageLabel.setText("Connected to server!");

            //this.sendMessageToServer(MessageType.name, name);
            this.host = host;
            this.port = port;
            this.myName = name;
            this.joinButton.setEnabled(false);
            this.play();
        } catch (Exception e) {
            System.out.println(e);
            this.messageLabel.setText("Failed to connect to server: " + host + " with port: " + portString);
        }
    }//GEN-LAST:event_joinButtonActionPerformed

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getPlayerName() {
        return myName;
    }

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
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JButton joinButton;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTextField nameTextFiled;
    private javax.swing.JTextField portTextField;
    private javax.swing.JTextField serverIPTextField;
    private JButton infoButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void didMove(Move move) {
        //System.out.println("Move from client: " + move.getSource().x + " " + move.getSource().y + " to : " + move.getDestination().x + " " + move.getDestination().y);
        if (!isMyTurn) {
            this.chessboard.setMessage("It's not your turn!");
            return;
        }
        //System.out.println("My move: ");
        this.chessboard.setMessage("Your opponent's turn!");
        this.sendMessageToServer(MessageType.move, move);
        this.chessboard.move(move);
        this.isMyTurn = false;
        if (isFirstPlayer) {
            this.chessboard.switchTurn(2);
        } else {
            this.chessboard.switchTurn(1);
        }
    }

    @Override
    public void didSendMessage(String message) {
        this.sendMessageToServer(MessageType.message, message);
    }

    @Override
    public void didClickCloseChessboard() {
        this.sendOperation(MessageType.surrender);
    }
}
