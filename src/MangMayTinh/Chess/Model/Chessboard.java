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
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author thinhle
 */
public class Chessboard extends javax.swing.JFrame {

    ArrayList<Piece> firstPlayerPieces = new ArrayList<>();
    ArrayList<Piece> secondPlayerPieces = new ArrayList<>();
    ArrayList<Piece> deadPieces = new ArrayList<>();
    HashMap<String, Square> squares = new HashMap<>();
    ChessboardInterface delegate;
    private Piece firstPiece = null;
    private int chessWidth = 0;
    private final Color dark = new Color(118, 150, 86);
    private final Color light = new Color(238, 238, 210);
    private final Color darkSelected = new Color(187, 203, 74);
    private final Color lightSelected = new Color(246, 246, 141);
    private Move currentMove = null;
    private boolean isFirstPlayer = true;

    /**
     * Creates new form Chessboard
     */
    public Chessboard() {
        initComponents();
    }

    public void move(Move move) {
        System.out.println("Move from: " + move.source.x + " " + move.source.y + " to : " + move.destination.x + " " + move.destination.y);
        Piece sourcePiece = this.getPieceAt(move.source);
        Piece destinationPiece = this.getPieceAt(move.destination);
        if (sourcePiece == null) {
            System.out.println("there is no piece at " + move.source);
            return;
        }
        if (destinationPiece != null) {
            System.out.println("Kill an enemy!");
            destinationPiece.setIsAlive(false);
            this.playArea.remove(destinationPiece);
            firstPlayerPieces.remove(destinationPiece);
            secondPlayerPieces.remove(destinationPiece);
            this.deadPieces.add(destinationPiece);
        }
        int x = move.getDestination().x;
        int y = move.getDestination().y;
        String sourceKey = Square.getKey(move.source);
        String destinationKey = Square.getKey(move.destination);
        this.squares.get(sourceKey).turnToSecondaryColor();
        this.squares.get(destinationKey).turnToSecondaryColor();
        sourcePiece.setLocation(x * this.chessWidth, y * this.chessWidth);
        sourcePiece.setNowPosition(move.destination);
        this.currentMove = move;
        this.turnToOriginalColor();
    }

    public int getWinner() {
        for (Piece piece : this.deadPieces) {
            if (piece instanceof King) {
                if (piece.isBelongToFirstPlayer()) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }
        return 0; // 1: first player win -- 2: second player win -- 0: unknown
    }

    public void setDelegate(ChessboardInterface delegate) {
        this.delegate = delegate;
    }

    public void setIsFirstPlayer(boolean isFirstPlayer) {
        this.isFirstPlayer = isFirstPlayer;
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public boolean isInsideChessboard(Point point) {
        int x = point.x;
        int y = point.y;
        return !(x < 0 || x > 7 || y < 0 || y > 7);
    }

    public Piece getPieceAt(Point point) {
        for (Piece piece : this.firstPlayerPieces) {
            if (point.equals(piece.getNowPosition())) {
                return piece;
            }
        }

        for (Piece piece : this.secondPlayerPieces) {
            if (point.equals(piece.getNowPosition())) {
                return piece;
            }
        }
        return null;
    }

    public void drawChessboard() {
        this.getContentPane().setBackground(new Color(48, 46, 43));
        int width = this.playArea.getWidth() / 8;
        int height = this.playArea.getHeight() / 8;
        this.chessWidth = width;

        if (this.isFirstPlayer) {
            this.addPieces(firstPlayerPieces, true, true);      // Mau trang -- O duoi
            this.addPieces(secondPlayerPieces, false, false);   // Mau den -- O tren
        } else {
            this.addPieces(firstPlayerPieces, false, true);
            this.addPieces(secondPlayerPieces, true, false);
        }

        for (Piece piece : this.firstPlayerPieces) {
            int x = piece.getNowPosition().x;
            int y = piece.getNowPosition().y;
            piece.setBounds(x * width, y * width, width, height);
            this.playArea.add(piece, 2, 0);
        }

        for (Piece piece : this.secondPlayerPieces) {
            int x = piece.getNowPosition().x;
            int y = piece.getNowPosition().y;
            piece.setBounds(x * width, y * width, width, height);
            this.playArea.add(piece, 2, 0);
        }

        boolean isLightStart = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = new Square();
                square.setBounds(i * width, j * height, width, height);
                square.setOriginalColor(isLightStart ? j % 2 == 0 ? light : dark : j % 2 == 0 ? dark : light);
                square.setSecondaryColor(isLightStart ? j % 2 == 0 ? lightSelected : darkSelected : j % 2 == 0 ? darkSelected : lightSelected);
                square.setPosition(new Point(i, j));
                square.turnToOriginalColor();
                this.playArea.add(square);
                this.squares.put(square.getKey(), square);
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
                Piece piece = getPieceAt(point);
                turnToOriginalColor();
                if (firstPiece == null) {
                    if (piece != null && piece.isBelongToFirstPlayer()) {
                        firstPiece = piece;
                        showPossibleDestinations(piece);
                    } else {
                        System.out.println("Not your piece!");
                    }
                } else {
                    Move move = new Move(firstPiece.getNowPosition(), point);
                    if (piece == null) {
                        if (firstPiece.isMoveAccepted(move) && delegate != null) {
                            delegate.didMove(move);
                            firstPiece = null;
                        } else {
                            firstPiece = null;
                        }
                    } else if (!piece.isBelongToFirstPlayer()) {
                        if (firstPiece.isMoveAccepted(move) && delegate != null) {
                            delegate.didMove(move);
                            firstPiece = null;
                        } else {
                            firstPiece = null;
                        }
                    } else {
                        firstPiece = piece;
                        showPossibleDestinations(piece);
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

    public void destruct() {
        for (Piece piece : this.deadPieces) {
            piece.chessboard = null;
            piece = null;
        }
        for (Piece piece : this.firstPlayerPieces) {
            piece.chessboard = null;
            piece = null;
        }
        for (Piece piece : this.secondPlayerPieces) {
            piece.chessboard = null;
            piece = null;
        }
        this.delegate = null;
    }

    //---------------------- private function -------------------------
    private void addPieces(ArrayList<Piece> playerPieces, boolean isLight, boolean isFirstPlayer) {
        String path = "src/MangMayTinh/Resource/Images/";
        int position = 7;
        if (isLight) {
            path += "WhitePlayer/";
        } else {
            path += "BlackPlayer/";
        }

        if (isFirstPlayer) {
            position = 7;
        } else {
            position = 0;
        }

        File file;
        BufferedImage pieceImage;
        try {
            file = new File(path + King.className + ".gif");
            pieceImage = ImageIO.read(file);
            King king = new King(new Point(Math.abs(position - 3), position), pieceImage, isFirstPlayer, this);
            file = new File(path + Queen.className + ".gif");
            pieceImage = ImageIO.read(file);
            Queen queen = new Queen(new Point(Math.abs(position - 4), position), pieceImage, isFirstPlayer, this);
            playerPieces.add(queen);
            playerPieces.add(king);

            file = new File(path + Rook.className + ".gif");
            pieceImage = ImageIO.read(file);
            Rook leftRook = new Rook(new Point(0, position), pieceImage, isFirstPlayer, this);
            playerPieces.add(leftRook);
            Rook rightRook = new Rook(new Point(7, position), pieceImage, isFirstPlayer, this);
            playerPieces.add(rightRook);

            file = new File(path + Knight.className + ".gif");
            pieceImage = ImageIO.read(file);
            Knight leftKnight = new Knight(new Point(1, position), pieceImage, isFirstPlayer, this);
            playerPieces.add(leftKnight);
            Knight rightKnight = new Knight(new Point(6, position), pieceImage, isFirstPlayer, this);
            playerPieces.add(rightKnight);

            file = new File(path + Bishop.className + ".gif");
            pieceImage = ImageIO.read(file);
            Bishop leftBishop = new Bishop(new Point(2, position), pieceImage, isFirstPlayer, this);
            playerPieces.add(leftBishop);
            Bishop rightBishop = new Bishop(new Point(5, position), pieceImage, isFirstPlayer, this);
            playerPieces.add(rightBishop);

            for (int i = 0; i < 8; i++) {
                file = new File(path + Pawn.className + ".gif");
                pieceImage = ImageIO.read(file);
                Pawn pawn = new Pawn(new Point(i, Math.abs(position - 1)), pieceImage, isFirstPlayer, this);
                playerPieces.add(pawn);
            }
        } catch (IOException ex) {
            System.out.println("Load image error: " + ex.toString());
        }
    }

    private void showPossibleDestinations(Piece piece) {
        piece.generatePossibleDestination();
        for (Point destination : piece.possibleDestinations) {
            String key = Square.getKey(destination);
            Square square = squares.get(key);
            square.turnToSecondaryColor();
        }
    }

    private void turnToOriginalColor() {
        for (Map.Entry pair : this.squares.entrySet()) {
            Square square = (Square) pair.getValue();
            Point point = square.getPosition();
            if (this.currentMove != null) {

                if ((point.equals(this.currentMove.destination) || point.equals(this.currentMove.source))) {
                    continue;
                }
            }
            square.turnToOriginalColor();
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        message = new javax.swing.JLabel();
        playArea = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(48, 46, 43));
        setForeground(new java.awt.Color(48, 46, 43));
        setPreferredSize(new java.awt.Dimension(1200, 800));
        setResizable(false);

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
            .addGap(0, 443, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

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

        playArea.setBackground(new java.awt.Color(255, 255, 255));
        playArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        playArea.setForeground(new java.awt.Color(255, 255, 255));
        playArea.setPreferredSize(new java.awt.Dimension(640, 640));

        javax.swing.GroupLayout playAreaLayout = new javax.swing.GroupLayout(playArea);
        playArea.setLayout(playAreaLayout);
        playAreaLayout.setHorizontalGroup(
            playAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );
        playAreaLayout.setVerticalGroup(
            playAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
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
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(playArea, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE))
                .addGap(80, 80, 80))
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
    private javax.swing.JLayeredPane playArea;
    // End of variables declaration//GEN-END:variables

}
