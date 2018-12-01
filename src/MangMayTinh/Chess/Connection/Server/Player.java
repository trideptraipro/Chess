/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MangMayTinh.Chess.Connection.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author thinhle
 */
public class Player {
    Socket socket;
    ObjectOutputStream sender;
    ObjectInputStream receiver;

    public Player(Socket socket, ObjectOutputStream sender, ObjectInputStream receiver) {
        this.socket = socket;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectOutputStream getSender() {
        return sender;
    }

    public void setSender(ObjectOutputStream sender) {
        this.sender = sender;
    }

    public ObjectInputStream getReceiver() {
        return receiver;
    }

    public void setReceiver(ObjectInputStream receiver) {
        this.receiver = receiver;
    }
}
