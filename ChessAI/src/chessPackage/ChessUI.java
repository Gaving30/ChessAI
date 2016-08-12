package chessPackage;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Gavin Gaughran
 */

public class ChessUI extends JPanel implements MouseListener, MouseMotionListener{
    static int mouseX, newMouseX, mouseY, newMouseY;
    static int boardSize=60;
   
    //Design features and UI    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Image backgroundImage;
        backgroundImage = new ImageIcon("Cream.jpg").getImage();
        g.drawImage(backgroundImage, 0, 0, this);
        
        Image darkImage;
        darkImage = new ImageIcon("Dark.jpg").getImage();
        g.drawImage(darkImage, 120, 0, this);
        
        Image lightImage;
        lightImage = new ImageIcon("Light.jpg").getImage();
        g.drawImage(lightImage, 180, 0, this);
        
        int everySecondRow=0;
        for(int i=0; i<4;i++){
            g.drawImage(darkImage, 0, everySecondRow, this);
            g.drawImage(lightImage, 60, everySecondRow, this);
            g.drawImage(darkImage, 120, everySecondRow, this);
            g.drawImage(lightImage, 180, everySecondRow, this);
            g.drawImage(darkImage, 240, everySecondRow, this);
            g.drawImage(lightImage, 300, everySecondRow, this);
            g.drawImage(darkImage, 360, everySecondRow, this);
            g.drawImage(lightImage, 420, everySecondRow, this);
            everySecondRow+=120;
        }
        
        everySecondRow=0;
        
        for(int i=0; i<4;i++){
            everySecondRow+=60;
            g.drawImage(lightImage, 0, everySecondRow, this);
            g.drawImage(darkImage, 60, everySecondRow, this);
            g.drawImage(lightImage, 120, everySecondRow, this);
            g.drawImage(darkImage, 180, everySecondRow, this);
            g.drawImage(lightImage, 240, everySecondRow, this);
            g.drawImage(darkImage, 300, everySecondRow, this);
            g.drawImage(lightImage, 360, everySecondRow, this);
            g.drawImage(darkImage, 420, everySecondRow, this);
            everySecondRow+=60;
        }
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        Image chessPieceImage;
        chessPieceImage = new ImageIcon("ChessPieces.png").getImage();
        
        //Each image is 64pixels away from each other, so x+64 == corner plus 64*64 takes one icon from image
        //g.drawImage(chessPieceImage, x, 0, x+100, y+100, 0, 0, x+100, 100, this);
        
        for(int i =0; i<64; i++){
            int j=-1,k=-1;
            switch(Chess.chessBoard[i/8][i%8]){
                case "A": j=0; k=0;
                    break;
                case "a": j=0;  k=1;
                    break;
                case "Q": j=1; k=0;
                    break;
                case "q": j=1;  k=1;
                    break;
                case "R": j=2; k=0;
                    break;
                case "r": j=2;  k=1;
                    break;
                case "B": j=3; k=0;
                    break;
                case "b": j=3;  k=1;
                    break;
                case "K": j=4; k=0;
                    break;
                case "k": j=4;  k=1;
                    break;
                case "P": j=5; k=0;
                    break;
                case "p": j=5; k=1;
                    break;
            }
            if(j!=-1 && k!=-1){
                g.drawImage(chessPieceImage, (i%8*boardSize), (i/8)*boardSize, (i%8+1)*boardSize, (i/8+1)*boardSize, j*64, k*64, (j+1)*64, (k+1)*64, this);
            }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
        
    }
    
    @Override
    public void mousePressed(MouseEvent e){
        if(e.getX()<8*boardSize && e.getY()<8*boardSize){
            mouseX=e.getX();
            mouseY=e.getY();
            repaint();
        }
    }
        
    @Override
    public void mouseReleased(MouseEvent e){
        if(e.getX()<8*boardSize && e.getY()<8*boardSize){
            newMouseX=e.getX();
            newMouseY=e.getY();
            //If its the left button
            if(e.getButton()==MouseEvent.BUTTON1){
                String dragMove;
                //If a Pawn is getting promoted by moving to row 0 on the board
                if(newMouseY/boardSize==0 && newMouseY/boardSize==1 && "P".equals(Chess.chessBoard[mouseY/boardSize][mouseX/boardSize])){
                    dragMove=""+mouseX/boardSize+newMouseX/boardSize+Chess.chessBoard[newMouseY/boardSize][newMouseX/boardSize]+"QP";
                }
                else{
                    dragMove=""+mouseY/boardSize+mouseX/boardSize+newMouseY/boardSize+newMouseX/boardSize+Chess.chessBoard[newMouseY/boardSize][newMouseX/boardSize];
                }
                
                String userPossibleMoves = Chess.possibleMoves();

                //If its a valid move then make the move
                if(userPossibleMoves.replaceAll(dragMove, "").length()<userPossibleMoves.length()){
                    //If Valid Move then proceed
                    Chess.makeMove(dragMove);
                    Chess.flipBoard();
                    Chess.makeMove(Chess.alphaBetaAlgorithm(Chess.globalDepth, 1000000, -1000000, "", 0));
                    Chess.flipBoard();
                    repaint();
                }
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e){

    }
    
    @Override
    public void mouseDragged(MouseEvent e){

    }
    
    @Override
    public void mouseEntered(MouseEvent e){
        
    }
    
    @Override
    public void mouseExited(MouseEvent e){
        
    }
}