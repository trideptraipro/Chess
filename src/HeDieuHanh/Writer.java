/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeDieuHanh;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author thinhle
 */
public class Writer extends Operator implements Runnable {
    static int numOfInstance = 0;
    private final ArrayList<Item> listOfItems;
    private final Semaphore resourceAccess;
    private final Semaphore serviceQueueAccess;
    private final Random random;
    private final WriterInterface delegate;
    private final String name;
    
    public Writer(ArrayList<Item> listOfItems, Semaphore resourceAccess, Semaphore serviceQueueAccess, WriterInterface delegate) {
        this.listOfItems = listOfItems;
        this.resourceAccess = resourceAccess;
        this.serviceQueueAccess = serviceQueueAccess;
        this.random = new Random();
        this.delegate = delegate;
        
        this.setBackground(Color.red);
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        Writer.numOfInstance++;
        JLabel name = new JLabel(" " + Writer.numOfInstance);
        this.name = " " + Writer.numOfInstance;
        name.setSize(20, 20);
        name.setForeground(Color.white);
        this.setLayout(new GridBagLayout());
        this.add(name);
    }
    
    @Override
    public void run() {
        try {
            this.serviceQueueAccess.acquire();
            this.resourceAccess.acquire();
            this.delegate.didEnterServiceQueue(this);
            
            Thread.sleep(this.random.nextInt(3) * 1000 + 2000);
            this.write();
            Thread.sleep(this.random.nextInt(3) * 1000 + 2000);
            
            this.delegate.didExitServiceQueue(this);
            this.serviceQueueAccess.release();
            this.resourceAccess.release();
            this.finalize();
        } catch (InterruptedException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void write() {
        if (this.random.nextInt(5) > 0) {
            Item newItem = Item.create();
            this.listOfItems.add(newItem);
            this.delegate.didWrite(true, newItem, this.name);
        } else {
            if (this.listOfItems.size() > 0) {
                int lastItemIndex = this.listOfItems.size() - 1;
                Item removedItem = this.listOfItems.remove(lastItemIndex);
                this.delegate.didWrite(false, removedItem, this.name);
            } else {
                this.delegate.didWrite(false, null, this.name);
            } 
        }
    }
}

interface WriterInterface {
    public void didWrite(boolean isAdded, Item item, String writerName);
    public void didEnterServiceQueue(Writer writer);
    public void didExitServiceQueue(Writer writer);
}
