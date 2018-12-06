/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MangMayTinh.Chess.Connection.Server;

import MangMayTinh.Chess.Model.Chessboard;
import MangMayTinh.Chess.Model.MessageType;
import MangMayTinh.Chess.Model.Move;
import java.awt.Point;
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
        sendOperation(firstPlayer, MessageType.firstPlayer);
        sendOperation(firstPlayer, MessageType.startGame);
        sendOperation(secondPlayer, MessageType.startGame);
        sendOperation(firstPlayer, MessageType.turn);
        this.chessboard = new Chessboard();
        this.chessboard.drawChessboard();
        while (true) {
            try {
                int checkWinner = this.chessboard.getWinner();
                if (checkWinner == 1) {
                    sendMessageTo(firstPlayer, MessageType.result, true);
                    sendMessageTo(secondPlayer, MessageType.result, false);
                    break;
                } else if (checkWinner == 2) {
                    sendMessageTo(firstPlayer, MessageType.result, false);
                    sendMessageTo(secondPlayer, MessageType.result, true);
                    break;
                }
                if (this.turn == 1) {
                    Move move = (Move) this.firstPlayer.receiver.readObject();
                    this.transform(move); /////------------------ check
                    this.sendMessageTo(secondPlayer, MessageType.move, move);
                    turn = 2;
                } else {
                    Move move = (Move) this.secondPlayer.receiver.readObject();
                    this.transform(move); /////------------------ check
                    this.sendMessageTo(firstPlayer, MessageType.move, move);
                    turn = 1;
                }
            } catch (IOException ex) {
                Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //this.finalize();
        System.out.println("Match ended!");
    }

    private void transform(Move move) {
        Point destination = move.getDestination();
        Point source = move.getSource();
        destination.y = 7 - destination.y;
        destination.x = 7 - destination.x;
        source.y = 7 - source.y;
        source.x = 7 - source.x;
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
