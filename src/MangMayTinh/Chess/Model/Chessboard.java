/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MangMayTinh.Chess.Model;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author thinhle
 */
public class Chessboard extends javax.swing.JFrame {
    ArrayList<Piece> firstPlayerPieces = new ArrayList<>();
    ArrayList<Piece> secondPlayerPieces = new ArrayList<>();
    ChessboardInterface delegate;
    private Piece firstPiece = null;
    private int chessWidth = 0;
    private final Color dark = new Color(118, 150, 86);
    private final Color light = new Color(238, 238, 210);
    private final Color darkSelected = new Color(187, 203, 74);
    private final Color lightSelected = new Color(246, 246, 141);

    /**
     * Creates new form Chessboard
     */
    public Chessboard() {
        initComponents();
        drawChessboard();
    }
    
    public void move(Move move) {
        Piece piece = this.getPieceAt(move.source);
        if (piece == null) {
            System.out.println("there is no piece at " + move.source);
            return;
        }
        int x = move.getDestination().x;
        int y = move.getDestination().y;
        piece.setLocation(x * this.chessWidth, y * this.chessWidth);
        piece.setNowPosition(move.destination);
    }
    
    public void setDelegate(ChessboardInterface delegate) {
        this.delegate = delegate;
    }
    
    public void setMessage(String message) {
        this.message.setText(message);
    }
    
    //---------------------- private function -------------------------
    
    private Piece getPieceAt(Point point) {
        for (Piece piece: this.firstPlayerPieces) {
            if (point.equals(piece.getNowPosition())) {
                return piece;
            }
        }
        
        for (Piece piece: this.secondPlayerPieces) {
            if (point.equals(piece.getNowPosition())) {
                return piece;
            }
        }
        return null;
    }
    
    private void drawChessboard() {
        this.getContentPane().setBackground(new Color(48, 46, 43));
        int width = this.playArea.getWidth() / 8;
        int height = this.playArea.getHeight() / 8;
        this.chessWidth = width;
        
        this.addPieces(firstPlayerPieces, true);
        this.addPieces(secondPlayerPieces, false);
        
        for (Piece piece : this.firstPlayerPieces) {
            int x = piece.getNowPosition().x;
            int y = piece.getNowPosition().y;
            piece.setBounds(x * width, y * width, width, height);
            this.playArea.add(piece);
        }
        
        for (Piece piece : this.secondPlayerPieces) {
            int x = piece.getNowPosition().x;
            int y = piece.getNowPosition().y;
            piece.setBounds(x * width, y * width, width, height);
            this.playArea.add(piece);
        }
        
        boolean isLightStart = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel newJPanel = new JPanel();
                newJPanel.setBounds(i * width, j * height, width, height);
                newJPanel.setBackground(isLightStart ? j % 2 == 0 ? light : dark : j % 2 == 0 ? dark : light);
                this.playArea.add(newJPanel);
                if (j == 7) {
                    isLightStart = !isLightStart;
                } 
            }
        }
        
        this.playArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / width;
                int y = e.getY() / height;
                Point point = new Point(x, y);
                System.out.println("Point: " + point.toString());
                Piece piece = getPieceAt(point);
                if (firstPiece == null) {
                    if (piece != null) {
                        firstPiece = piece;
                    }
                } else {
                    Move move = new Move(firstPiece.getNowPosition(), point);
                    if (piece != null && piece.isBelongToFirstPlayer()) {
                        firstPiece = piece;
                    } else if ((piece == null || (!piece.isBelongToFirstPlayer())) && firstPiece.isMoveAccepted(move) && delegate != null) {
                        delegate.didMove(move);
                        //System.out.println("move from " + move.source.toString() + " to " + move.destination.toString());
                        firstPiece = null;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
    }
    
    private void addPieces(ArrayList<Piece> playerPieces, boolean isLight) {
        String path = "src/MangMayTinh/Resource/Images/";
        int position = 7;
        if (isLight) {
            path += "FirstPlayer/";
        } else {
            path += "SecondPlayer/";
            position = 0;
        }
        File file;
        BufferedImage pieceImage;
        try {
            file = new File(path + King.className + ".gif");
            pieceImage = ImageIO.read(file);
            King king = new King(new Point(3, position), pieceImage, isLight);
            file = new File(path + Queen.className + ".gif");
            pieceImage = ImageIO.read(file);
            Queen queen = new Queen(new Point(4, position), pieceImage, isLight);
            playerPieces.add(queen);
            playerPieces.add(king);
            
            file = new File(path + Rook.className + ".gif");
            pieceImage = ImageIO.read(file);
            Rook leftRook = new Rook(new Point(0, position), pieceImage, isLight);
            playerPieces.add(leftRook);
            Rook rightRook = new Rook(new Point(7, position), pieceImage, isLight);
            playerPieces.add(rightRook);
            
            file = new File(path + Knight.className + ".gif");
            pieceImage = ImageIO.read(file);
            Knight leftKnight = new Knight(new Point(1, position), pieceImage, isLight);
            playerPieces.add(leftKnight);
            Knight rightKnight = new Knight(new Point(6, position), pieceImage, isLight);
            playerPieces.add(rightKnight);
            
            file = new File(path + Bishop.className + ".gif");
            pieceImage = ImageIO.read(file);
            Bishop leftBishop = new Bishop(new Point(2, position), pieceImage, isLight);
            playerPieces.add(leftBishop);
            Bishop rightBishop = new Bishop(new Point(5, position), pieceImage, isLight);
            playerPieces.add(rightBishop);
            
            for (int i = 0; i < 8; i++) {
                file = new File(path + Pawn.className + ".gif");
            pieceImage = ImageIO.read(file);
                Pawn pawn = new Pawn(new Point(i, Math.abs(position - 1)), pieceImage, isLight);
                playerPieces.add(pawn);
            }
        } catch (IOException ex) {
            System.out.println("Load image error: " + ex.toString());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        playArea = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        message = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(48, 46, 43));
        setForeground(new java.awt.Color(48, 46, 43));
        setResizable(false);

        playArea.setBackground(new java.awt.Color(255, 255, 255));
        playArea.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        playArea.setBounds(new java.awt.Rectangle(0, 0, 640, 640));
        playArea.setPreferredSize(new java.awt.Dimension(640, 640));

        javax.swing.GroupLayout playAreaLayout = new javax.swing.GroupLayout(playArea);
        playArea.setLayout(playAreaLayout);
        playAreaLayout.setHorizontalGroup(
            playAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 636, Short.MAX_VALUE)
        );
        playAreaLayout.setVerticalGroup(
            playAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 636, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(340, 150));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(51, 255, 51));
        jPanel2.setForeground(new java.awt.Color(102, 255, 102));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        message.setForeground(new java.awt.Color(0, 51, 255));
        message.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        message.setText("message from server");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(177, 177, 177)
                .addComponent(message)
                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(message)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(playArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(76, 76, 76))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Chessboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chessboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chessboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chessboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chessboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel message;
    private javax.swing.JPanel playArea;
    // End of variables declaration//GEN-END:variables

}
