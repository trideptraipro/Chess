/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HeDieuHanh;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thinhle
 */
public class ProcessUI extends javax.swing.JFrame implements WriterInterface, ReaderInterface {
    private int itemHeight;
    private int itemWidth;
    private int operatorHeight;
    private int operatorWidth;
    
    private final int numOfItem = 20;
    private final int numOfWaitingOperator = 20;
    private final Semaphore drawServiceAccess;
    private final Semaphore drawReaderAccess;
    private final Semaphore drawWriterAccess;
    
    private final Semaphore resourceAccess;
    private final Semaphore readerCountAccess;
    private final Semaphore serviceQueueAccess;
    
    ArrayList<Item> listOfItems = new ArrayList<>();
    private final ArrayList<Writer> writers = new ArrayList<>();
    private final ArrayList<Reader> readers = new ArrayList<>();
    private final ArrayList<Operator> operatorInServiceQueue = new ArrayList<>();
    private final int[] availableWriterPosition = new int[numOfWaitingOperator];
    private final int[] availableReaderPosition = new int[numOfWaitingOperator];
    
    public ProcessUI() {
        initComponents();
        this.readerCountAccess = new Semaphore(1);
        this.resourceAccess = new Semaphore(1);
        this.serviceQueueAccess = new Semaphore(1);
        this.drawServiceAccess = new Semaphore(1);
        this.drawReaderAccess = new Semaphore(1);
        this.drawWriterAccess = new Semaphore(1);
        setUp();
    }
    
    private void setUp() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.itemHeight = this.stack.getHeight() / this.numOfItem;
        this.itemWidth = this.stack.getWidth();
        this.operatorHeight = this.serviceArea.getHeight() / this.numOfWaitingOperator;
        this.operatorWidth = this.serviceArea.getWidth();
    }
    
    @Override
    public void didEnterServiceQueue(Writer writer) {
        try {
            int position = writer.getX() / this.operatorHeight;
            this.drawWriterAccess.acquire();
            this.writers.remove(writer);
            this.writerArea.remove(writer);
            this.availableWriterPosition[position] = 0;
            this.drawWriterAccess.release();
            
            this.drawServiceAccess.acquire();
            this.operatorInServiceQueue.add(writer);
            int lastOperatorIndex = this.operatorInServiceQueue.size() - 1;
            writer.setBounds(0, lastOperatorIndex * this.operatorHeight, this.operatorWidth, this.operatorHeight);
            this.serviceArea.add(writer);
            this.drawServiceAccess.release();
            this.repaint();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void didExitServiceQueue(Writer writer) {
        try {
            this.drawServiceAccess.acquire();
            this.serviceArea.remove(writer);
            this.repaint();
            this.operatorInServiceQueue.remove(writer);
            this.drawServiceAccess.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void didEnterServiceQueue(Reader reader) {
        try {
            int position = reader.getX() / this.operatorHeight;
            this.drawReaderAccess.acquire();
            this.readers.remove(reader);
            this.readerArea.remove(reader);
            this.availableReaderPosition[position] = 0;
            this.drawReaderAccess.release();
            
            this.drawServiceAccess.acquire();
            this.operatorInServiceQueue.add(reader);
            int lastOperatorIndex = this.operatorInServiceQueue.size() - 1;
            reader.setBounds(0, lastOperatorIndex * this.operatorHeight, this.operatorWidth, this.operatorHeight);
            this.serviceArea.add(reader);
            this.drawServiceAccess.release();
            this.repaint();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void didExitServiceQueue(Reader reader) {
        try {
            this.drawServiceAccess.acquire();
            this.serviceArea.remove(reader);
            this.repaint();
            this.operatorInServiceQueue.remove(reader);
            this.drawServiceAccess.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void didWrite(boolean isAdded, Item item, String writerName) {
        String oldLog = this.logScreen.getText();
        if (isAdded) {
            String newLog = " Writer " + writerName + " access. Added 1 Item. \n";
            item.setBounds(0, this.itemHeight * (this.listOfItems.size() - 1) + 1, this.itemWidth, this.itemHeight);
            this.stack.add(item);
            this.repaint();
            this.logScreen.setText(oldLog + newLog);
        } else {
            String newLog;
            if (item == null) {
                newLog = " Writer " + writerName + " access. No Item in stack. \n";
            } else {
                newLog = " Writer " + writerName + " access. Removed 1 Item. \n";
                this.stack.remove(item);
            }
            this.repaint();
            this.logScreen.setText(oldLog + newLog);
        }
    }
    
    @Override
    public void didRead(int numOfItems, String readerName) {
        String oldLog = this.logScreen.getText();
        String newLog = " Reader " + readerName + " access. Number of Items in stack: " + numOfItems + "\n";
        this.logScreen.setText(oldLog + newLog);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        stack = new javax.swing.JPanel();
        serviceArea = new javax.swing.JPanel();
        writerArea = new javax.swing.JPanel();
        readerArea = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logScreen = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        addWriter = new javax.swing.JButton();
        addReader = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Demo Reader/writer problem");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        stack.setBackground(new java.awt.Color(255, 255, 255));
        stack.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout stackLayout = new javax.swing.GroupLayout(stack);
        stack.setLayout(stackLayout);
        stackLayout.setHorizontalGroup(
            stackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 61, Short.MAX_VALUE)
        );
        stackLayout.setVerticalGroup(
            stackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        serviceArea.setBackground(new java.awt.Color(255, 255, 255));
        serviceArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        serviceArea.setPreferredSize(new java.awt.Dimension(100, 2));

        javax.swing.GroupLayout serviceAreaLayout = new javax.swing.GroupLayout(serviceArea);
        serviceArea.setLayout(serviceAreaLayout);
        serviceAreaLayout.setHorizontalGroup(
            serviceAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        serviceAreaLayout.setVerticalGroup(
            serviceAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        writerArea.setBackground(new java.awt.Color(255, 255, 255));
        writerArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        writerArea.setPreferredSize(new java.awt.Dimension(431, 100));

        javax.swing.GroupLayout writerAreaLayout = new javax.swing.GroupLayout(writerArea);
        writerArea.setLayout(writerAreaLayout);
        writerAreaLayout.setHorizontalGroup(
            writerAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
        );
        writerAreaLayout.setVerticalGroup(
            writerAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );

        readerArea.setBackground(new java.awt.Color(255, 255, 255));
        readerArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout readerAreaLayout = new javax.swing.GroupLayout(readerArea);
        readerArea.setLayout(readerAreaLayout);
        readerAreaLayout.setHorizontalGroup(
            readerAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
        );
        readerAreaLayout.setVerticalGroup(
            readerAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );

        jLabel1.setBackground(new java.awt.Color(0, 51, 255));
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Stack");

        jLabel2.setBackground(new java.awt.Color(0, 51, 255));
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("Writer waiting area");

        jLabel3.setBackground(new java.awt.Color(0, 51, 255));
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("Reader waiting area");

        jLabel4.setBackground(new java.awt.Color(0, 51, 255));
        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setText("Service Area");

        logScreen.setEditable(false);
        logScreen.setColumns(20);
        logScreen.setRows(5);
        jScrollPane1.setViewportView(logScreen);

        jLabel5.setBackground(new java.awt.Color(0, 51, 255));
        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setText("Log Screen");

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setForeground(new java.awt.Color(51, 51, 51));
        jPanel1.setAlignmentX(getAlignmentX());

        addWriter.setBackground(new java.awt.Color(51, 255, 51));
        addWriter.setText("Add Writer");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, stack, org.jdesktop.beansbinding.ELProperty.create("${alignmentX}"), addWriter, org.jdesktop.beansbinding.BeanProperty.create("alignmentX"));
        bindingGroup.addBinding(binding);

        addWriter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWriterActionPerformed(evt);
            }
        });

        addReader.setBackground(new java.awt.Color(51, 255, 51));
        addReader.setText("Add Reader");
        addReader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addReaderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(addReader, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(addWriter))
                .addGap(58, 58, 58))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addWriter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addReader)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel1)
                            .addComponent(stack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel4)
                            .addComponent(serviceArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(writerArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(readerArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(142, 142, 142)))
                .addGap(48, 48, 48))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {readerArea, writerArea});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(writerArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 191, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(readerArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(stack, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(serviceArea, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {readerArea, writerArea});

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addWriterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWriterActionPerformed
        try {
            Writer newWriter = new Writer(this.listOfItems, this.resourceAccess, this.serviceQueueAccess, this);
            this.drawWriterAccess.acquire();
            this.writers.add(newWriter);
            int position = 0;
            for (int i = 0; i < this.availableWriterPosition.length; i++) {
                if (this.availableWriterPosition[i] == 0) {
                    position = i;
                    this.availableWriterPosition[i] = 1;
                    break;
                }
            }
            newWriter.setBounds(position * this.operatorHeight, 0, this.operatorHeight, this.operatorWidth);
            this.writerArea.add(newWriter);
            this.drawWriterAccess.release();
            this.repaint();
            Thread writerThread = new Thread(newWriter);
            writerThread.start();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addWriterActionPerformed

    private void addReaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addReaderActionPerformed
        try {
            Reader newReader = new Reader(this.listOfItems, this.resourceAccess, this.readerCountAccess,this.serviceQueueAccess, this);
            this.drawReaderAccess.acquire();
            this.readers.add(newReader);
            int position = 0;
            for (int i = 0; i < this.availableReaderPosition.length; i++) {
                if (this.availableReaderPosition[i] == 0) {
                    position = i;
                    this.availableReaderPosition[i] = 1;
                    break;
                }
            }
            newReader.setBounds(position * this.operatorHeight, 0, this.operatorHeight, this.operatorWidth);
            this.readerArea.add(newReader);
            this.drawReaderAccess.release();
            this.repaint();
            Thread readerThread = new Thread(newReader);
            readerThread.start();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addReaderActionPerformed

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
            java.util.logging.Logger.getLogger(ProcessUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProcessUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProcessUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProcessUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ProcessUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addReader;
    private javax.swing.JButton addWriter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logScreen;
    private javax.swing.JPanel readerArea;
    private javax.swing.JPanel serviceArea;
    private javax.swing.JPanel stack;
    private javax.swing.JPanel writerArea;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
