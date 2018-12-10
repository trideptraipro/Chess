/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MangMayTinh.Chess.Connection.Server;

import MangMayTinh.Chess.Model.Chessboard;
import MangMayTinh.Chess.Model.Enum.MessageType;
import MangMayTinh.Chess.Model.Interface.PlayerInterface;
import MangMayTinh.Chess.Model.Move;
import java.awt.Point;

/**
 *
 * @author thinhle
 */
public class Match implements Runnable, PlayerInterface {

    Player firstPlayer;
    Player secondPlayer;
    Chessboard chessboard;
    private int turn = 1; // 1: first player's turn ----- 2: second player's turn
    private boolean isRunning = true;

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
        this.firstPlayer.setDelegate(this);
        this.secondPlayer.setDelegate(this);
        this.firstPlayer.setIsFirstPlayer(true);
        this.chessboard = new Chessboard();
        this.chessboard.setIsFirstPlayer(true);
        this.chessboard.drawChessboard();
        this.chessboard.setVisible(true);
        new Thread(this.firstPlayer).start();
        new Thread(this.secondPlayer).start();
        this.firstPlayer.sendOperation(MessageType.firstPlayer);
        while (this.isRunning) {
        }
    }

    private void transform(Move move) {
        //System.out.println("Before trans: " + move.getSource().x + " " + move.getSource().y + " to : " + move.getDestination().x + " " + move.getDestination().y);
        Point destination = move.getDestination();
        Point source = move.getSource();
        destination.y = 7 - destination.y;
        destination.x = 7 - destination.x;
        source.y = 7 - source.y;
        source.x = 7 - source.x;
        //System.out.println("After trans: " + move.getSource().x + " " + move.getSource().y + " to : " + move.getDestination().x + " " + move.getDestination().y);
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

    @Override
    public synchronized void setName(String name, boolean isFirstPlayer) {
        if (isFirstPlayer) {
            this.secondPlayer.sendMessage(MessageType.name, name);
            this.firstPlayer.setIsReady(true);
        } else {
            this.firstPlayer.sendMessage(MessageType.name, name);
            this.secondPlayer.setIsReady(true);
        }
        if (this.firstPlayer.isReady() && this.secondPlayer.isReady()) {
            this.firstPlayer.sendOperation(MessageType.turn);
            this.firstPlayer.sendOperation(MessageType.startGame);
            this.secondPlayer.sendOperation(MessageType.startGame);
        }
    }

    @Override
    public synchronized void setMessage(String message, boolean isFirstPlayer) {

    }

    @Override
    public synchronized void move(Move move, boolean isFirstPlayer) {
        if (isFirstPlayer && turn == 1) {
            this.chessboard.move(move);
            this.transform(move);
            this.secondPlayer.sendMessage(MessageType.move, move);
            turn = 2;
        } else if (turn == 2) {
            this.transform(move);
            this.chessboard.move(move);
            this.firstPlayer.sendMessage(MessageType.move, move);
            turn = 1;
        }
        int checkWinner = this.chessboard.getWinner();
        if (checkWinner == 1) {
            sendMessageTo(firstPlayer, MessageType.result, true);
            sendMessageTo(secondPlayer, MessageType.result, false);
            this.isRunning = false;
            this.firstPlayer.setIsRunning(false);
            this.secondPlayer.setIsRunning(false);
        } else if (checkWinner == 2) {
            sendMessageTo(firstPlayer, MessageType.result, false);
            sendMessageTo(secondPlayer, MessageType.result, true);
            this.isRunning = false;
            this.firstPlayer.setIsRunning(false);
            this.secondPlayer.setIsRunning(false);
        }
    }
}
