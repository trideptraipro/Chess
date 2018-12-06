/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MangMayTinh.Chess.Model;

import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author thinhle
 */
public class Move implements Serializable {
    Point source;
    Point destination;

    public Move(Point source, Point destination) {
        this.source = source;
        this.destination = destination;
    }

    public Point getSource() {
        return source;
    }

    public void setSource(Point source) {
        this.source = source;
    }

    public Point getDestination() {
        return destination;
    }

    public void setDestination(Point destination) {
        this.destination = destination;
    }
}
