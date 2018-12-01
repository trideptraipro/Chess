/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MangMayTinh.Chess.Model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author thinhle
 */
public abstract class Piece extends JLabel {
    private boolean isAlive = true;
    private Point nowPosition;
    static String className = "piece";
    private boolean isBelongToFirstPlayer;
    
    public Piece(Point nowPosition, BufferedImage image, boolean isBelongToFirstPlayer) {
        super(new ImageIcon(image));
        this.nowPosition = nowPosition;
        this.isBelongToFirstPlayer = isBelongToFirstPlayer;
    }
    
    public abstract boolean isMoveAccepted(Move move);

    public boolean isBelongToFirstPlayer() {
        return isBelongToFirstPlayer;
    }

    public void setIsBelongToFirstPlayer(boolean isBelongToFirstPlayer) {
        this.isBelongToFirstPlayer = isBelongToFirstPlayer;
    }
    
    public boolean isAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    
    public Point getNowPosition() {
        return nowPosition;
    }

    public void setNowPosition(Point nowPosition) {
        this.nowPosition = nowPosition;
    }
}
