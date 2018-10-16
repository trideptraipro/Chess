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
public class Reader extends Operator implements Runnable {
    static int numOfInstance = 0;
    private final ArrayList<Item> listOfItems;
    private final Semaphore resourceAccess;
    private final Semaphore readCountAccess;
    private final Semaphore serviceQueueAccess;
    private final String name;
    
    static int numOfReaderAccess = 0;
    private final ReaderInterface delegate;
    private final Random random;
    
    public Reader(ArrayList<Item> listOfItems, Semaphore resourceAccess, Semaphore readCountAccess,Semaphore serviceQueueAccess, ReaderInterface delegate) {
        this.listOfItems = listOfItems;
        this.readCountAccess = readCountAccess;
        this.resourceAccess = resourceAccess;
        this.serviceQueueAccess = serviceQueueAccess;
        this.delegate = delegate;
        this.random = new Random();
        
        Reader.numOfInstance++;
        this.setBackground(Color.green);
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        JLabel name = new JLabel("" + Reader.numOfInstance);
        this.name = "" + Reader.numOfInstance;
        name.setSize(20, 20);
        name.setForeground(Color.darkGray);
        this.setLayout(new GridBagLayout());
        this.add(name);
    }
    
    @Override
    public void run() {
        try {
            this.serviceQueueAccess.acquire();
            this.readCountAccess.acquire();
            this.delegate.didEnterServiceQueue(this);
            Reader.numOfReaderAccess++;
            if (Reader.numOfReaderAccess == 1) {
                this.resourceAccess.acquire();
            }
            this.serviceQueueAccess.release();
            this.readCountAccess.release();
            
            Thread.sleep(this.random.nextInt(3) * 1000 + 2000);
            this.read();
            Thread.sleep(this.random.nextInt(3) * 1000 + 2000);
            
            this.readCountAccess.acquire();
            Reader.numOfReaderAccess--;
            this.delegate.didExitServiceQueue(this);
            if (Reader.numOfReaderAccess == 0) {
                this.resourceAccess.release();
            }
            this.readCountAccess.release();
            this.finalize();
        } catch (InterruptedException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int read() {
        if (this.delegate != null) {
            this.delegate.didRead(this.listOfItems.size(), this.name);
        }
        return this.listOfItems.size();
    }
}

interface ReaderInterface {
    public void didRead(int numOfItems, String readerName);
    public void didEnterServiceQueue(Reader reader);
    public void didExitServiceQueue(Reader reader);
}
