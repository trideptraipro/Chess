/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MangMayTinh.Chess.Connection.Server;

import MangMayTinh.Chess.Model.Chessboard;
import MangMayTinh.Chess.Model.MessageType;
import MangMayTinh.Chess.Model.Move;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thinhle
 */
public class Match implements Runnable {
    Player firstPlayer;
    Player secondPlayer;
    Chessboard chessboard;
    private int turn = 1; // 1: first player's turn ----- 2: second player's turn
    
    public Match(Player firstPlayer, Player sencondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = sencondPlayer;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Destructed 1 match");
    }

    @Override
    public void run() {
        sendOperation(firstPlayer, MessageType.startGame);
        sendOperation(secondPlayer, MessageType.startGame);
        sendOperation(firstPlayer, MessageType.turn);
        while (true) {
            try {
                Move move = (Move) this.firstPlayer.receiver.readObject();
                if (this.turn == 1) {
                    this.sendMessageTo(secondPlayer, MessageType.move, move);
                    turn = 2;
                }
            } catch (IOException ex) {
                Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
        }
        System.out.println("Match ended!");
    }
    
    private void play() {
        
    }
    
    private <T> void sendMessageTo(Player player, MessageType type, T data) {
        try {
            player.sender.writeObject(type);
            player.sender.writeObject(data);
        } catch (Exception e) {
            System.out.print("Send data Error: ");
            System.out.println(e.toString());
        }
    }
    
    private void sendOperation(Player player, MessageType type) {
        try {
            player.sender.writeObject(type);
        } catch (Exception e) {
            System.out.print("Send data Error: ");
            System.out.println(e.toString());
        }
    }
}
