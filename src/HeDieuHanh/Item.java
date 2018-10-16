/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeDieuHanh;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author thinhle
 */
public class Item extends JPanel {
    static Item create() {
        Item newItem = new Item();
        newItem.setBackground(Color.YELLOW);
        newItem.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
        return newItem;
    }
}
