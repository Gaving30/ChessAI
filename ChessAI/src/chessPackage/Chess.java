package chessPackage;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import javax.swing.*;
import java.util.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import java.awt.*; 
import java.awt.event.*;

/**
 * Gavin Gaughran
 */


public class Chess {
    //Two-dimensional Array representing the ChessBoard
    static String chessBoard[][]={
        {"r","k","b","q","a","b","k","r"},
        {"p","p","p","p","p","p","p","p"},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {"P","P","P","P","P","P","P","P"},
        {"R","K","B","Q","A","B","K","R"}};
    
    //Global variable to locate positon of king for Capital(White) and Lower(Black)
    static int kingPositionCapital, kingPositionLowercase;
    static int humanAsWhite=-1;
    static int globalDepth = 4;
    
    public static void main(String[] args) {
        //Checks the whole board, scanning all Squares lookinf for King
        while (!"A".equals(chessBoard[kingPositionCapital/8][kingPositionCapital%8])){
            kingPositionCapital++;
        }
        while (!"a".equals(chessBoard[kingPositionLowercase/8][kingPositionLowercase%8])){
            kingPositionLowercase++;
        }
       
        //Create Chess UI
        JFrame JF=new JFrame("Gavin Gaughran, x12107077, Chess AI");
        JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Calls the UI
        ChessUI UI= new ChessUI();
        JF.add(UI);
        
        JF.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JF.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                int option = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?", "Gavin Gaughran Chess AI", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(option == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
        
        //Set chess board size and make visible
        JF.setSize(800,515);
        JF.setVisible(true);
        JF.setLocationRelativeTo(null);
        
        Object[] firstPlayer={"Computer", "Human"};
        humanAsWhite = JOptionPane.showOptionDialog(null, "Who Goes First", "Chess Options Pane", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, firstPlayer, firstPlayer[1]);
        if(humanAsWhite==0){
            makeMove(alphaBetaAlgorithm(globalDepth, 1000000, -1000000, "", 0));
            flipBoard();
            JF.repaint();
        }
    }
      
    public static String alphaBetaAlgorithm(int depth, int beta, int alpha, String move, int player){
        //Return the score and the actual move we should perform next
        String list=possibleMoves();
        if (depth==0 || list.length()==0){
                                        //Returns a -1 or 1, dependant on whther its fed a 0 or 1
            return move+(PieceScore.pieceScore(list.length(), depth)*(player*2-1));
        }
        //list=sortMoves(list);
        //Player is always a 1 or a 0(Switch between opponents) Flips between players
        player=1-player;
        for(int i=0; i<list.length();i+=5){
            makeMove(list.substring(i,i+5));
            flipBoard();
            //Calling itself recursively
            String returnString=alphaBetaAlgorithm(depth-1, beta, alpha, list.substring(i,i+5), player);
            int value=Integer.valueOf(returnString.substring(5));
            flipBoard();
            undoMove(list.substring(i,i+5));
            if (player==0){
                if(value<=beta){
                    beta=value;
                    if(depth==globalDepth){
                        move=returnString.substring(0,5);
                    }
                }
            }
            else{
                if(value>alpha){
                    alpha=value;
                    if(depth==globalDepth){
                        move=returnString.substring(0,5);
                    }
                }
            }
            if(alpha>=beta){
                if(player==0){
                    return move+beta;
                }
                else{
                    return move+alpha;
                }
            }
        }
        if(player==0){
            return move+beta;
        }
        else{
            return move+alpha;
        }
    }
    
    public static String sortMoves(String list){
        int[] score=new int [list.length()/5];
        
        for(int i=0; i<list.length(); i++){
            makeMove(list.substring(i, i+5));
            score[1/5]=-PieceScore.pieceScore(-1, 0);
            undoMove(list.substring(i, i+5));
        }
        
        String newListA="", newListB=list;
        for(int i=0; i<Math.min(6, list.length()/5); i++){
            int max=-1000000, maxLocation=0;
            for(int j=0; j<list.length()/5; j++){
                if (score[j]>max){
                    max=score[j];
                    maxLocation=j;
                }
            }
            score[maxLocation]=-1000000;
            newListA+=list.substring(maxLocation*5,maxLocation*5+5);
            newListB+=newListB.replace(list.substring(maxLocation*5,maxLocation*5+5), "");  
        }
        return newListA+newListA;
    }
    
    public static void flipBoard(){
        String temp;
        for(int i = 0; i < 32; i++){
            int row=i/8,column=i%8;
            //If it is uppercase
            if(Character.isUpperCase(chessBoard[row][column].charAt(0))){
                temp=chessBoard[row][column].toLowerCase();
            }
            else{
                temp=chessBoard[row][column].toUpperCase();
            }
            
            if(Character.isUpperCase(chessBoard[7-row][7-column].charAt(0))){
                chessBoard[row][column]=chessBoard[7-row][7-column].toLowerCase();
            }
            else{
                chessBoard[row][column]=chessBoard[7-row][7-column].toUpperCase();
            }
            chessBoard[7-row][7-column]=temp;
        }
        int kingTemp=kingPositionCapital;
        kingPositionCapital=63-kingPositionLowercase;
        kingPositionLowercase=63-kingTemp;
    }
            
    public static void makeMove(String move){
        //Standard Pawn moves
        
        if(move.charAt(4) != 'P'){
            //x1(x location) , y1(y location), x2(x location to move to), y2(y location to move to), captured piece(piece captured)
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=" ";
            if("A".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])){
                kingPositionCapital=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
            }
        }
        //If Pawn is Promoted
        else{
            //Column1, Column2, CapturedPiece, NewPiece, Pawn Promotion
            chessBoard[1][Character.getNumericValue(move.charAt(0))]=" ";
            chessBoard[0][Character.getNumericValue(move.charAt(1))]= String.valueOf(move.charAt(3)) ;
        }
    }
    
    public static void undoMove(String move){
        if(move.charAt(4) != 'P'){
            //x1(x location) , y1(y location), x2(x location to move to), y2(y location to move to), captured piece(piece captured)
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=String.valueOf(move.charAt(4));
            if("A".equals(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])){
                kingPositionCapital=8*Character.getNumericValue(move.charAt(0))+Character.getNumericValue(move.charAt(1));
            }
        }
        //If Pawn is Promoted
        else{
            //Column1, Column2, CapturedPiece, NewPiece, Pawn Promotion
            chessBoard[1][Character.getNumericValue(move.charAt(0))]="P";
            chessBoard[0][Character.getNumericValue(move.charAt(1))]= String.valueOf(move.charAt(2));
        }   
    }
    
    public static String possibleMoves(){
        String list="";
        
        for(int i = 0; i < 64; i++){
            //Works through every square
            switch(chessBoard[i/8][i%8]){
                //(i) verifies which Pawn is moving
                //possible* returns possible moves for each piece
                case "P": list+=possiblePawnMoves(i);
                    break;
                case "R": list+=possibleRookMoves(i);
                    break;
                case "K": list+=possibleKnightMoves(i);
                    break;
                case "B": list+=possibleBishopMoves(i);
                    break; 
                case "Q": list+=possibleQueenMoves(i);
                    break;
                case "A": list+=possibleKingMoves(i);
                    break;
            }
        }
        return list; //x1(x location) , y1(y location), x2(x location to move to), y2(y location to move to), captured piece(piece captured)
    }
    
    //List of possible moves for Pawn
    public static String possiblePawnMoves(int i){
        String list="";
        String oldPiece;
        int row=i/8;
        int column=i%8;
        //Checks in a vertical line
        for(int j=-1; j<=1; j+=2){
            try{
                //Checking for possible captures                               //A move after Square, 16 is promotion(end of board)
                if(Character.isLowerCase(chessBoard[row-1][column+j].charAt(0)) && i >= 16){
                    oldPiece=chessBoard[row-1][column+j];
                    //Remove it from current location
                    chessBoard[row][column]=" ";
                    //Set new location to now be the Pawn
                    chessBoard[row-1][column+j]="P";
                    //Only move if you are not exposing your King
                    if(kingSafe()){
                        //Record the move we just performed
                        list=list+row+column+(row-1)+(column+j)+oldPiece;
                    }
                    chessBoard[row][column]="P";
                    //This is now the old piece
                    chessBoard[row-1][column+j]=oldPiece;
                }
            }
            catch (Exception e){}
            
            try{
                //Promotion and capture in the one move                         //Move after 16 is a promotion(end of board)
                if(Character.isLowerCase(chessBoard[row-1][column+j].charAt(0)) && i < 16){
                    //Array for promoting Pawns
                    String[] promotionArray={"Q","R","B","K"};
                    //Incriment through the array
                    for(int k=0; k<4; k++){
                        oldPiece=chessBoard[row-1][column+j];
                        //Remove it from current location
                        chessBoard[row][column]=" ";
                        //Set new location to now be the chosen from Array
                        chessBoard[row-1][column+j]=promotionArray[k];
                        //Only move if you are not exposing your King
                        if(kingSafe()){
                            //Column1, Column2, CapturedPiece, NewPiece, Pawn Promotion
                            list=list+column+(column+j)+oldPiece+promotionArray[k]+"P";
                        }
                        //Still can revert to Pawn if move doesnt complete
                        chessBoard[row][column]="P";
                        //This is now the old piece
                        chessBoard[row-1][column+j]=oldPiece;
                    }
                }
            }
            catch (Exception e){}
        }
        try{
            //Allow Pawn to move one space forward
            if(" ".equals(chessBoard[row-1][column]) && i >= 16){
                oldPiece=chessBoard[row-1][column];
                //Remove it from current location
                chessBoard[row][column]=" ";
                //Set new location to now be the Pawn
                chessBoard[row-1][column]="P";
                //Only move if you are not exposing your King
                if(kingSafe()){
                    //Record the move we just performed
                    list=list+row+column+(row-1)+(column)+oldPiece;
                }
                chessBoard[row][column]="P";
                //This is now the old piece
                chessBoard[row-1][column]=oldPiece;
            }
        }
        catch (Exception e){}
        
        try{
            //Pawn Promotion with no opponent piece being captured
            if(" ".equals(chessBoard[row-1][column]) && i < 16){
                String[] promotionArray={"Q","R","B","K"};
                //Incriment through the array
                for(int k=0; k<4; k++){
                    oldPiece=chessBoard[row-1][column];
                    //Remove it from current location
                    chessBoard[row][column]=" ";
                    //Set new location to now be the chosen from Array
                    chessBoard[row-1][column]=promotionArray[k];
                    //Only move if you are not exposing your King
                    if(kingSafe()){
                        //Column1, Column2, CapturedPiece, NewPiece, Pawn Promotion
                        list=list+column+(column)+oldPiece+promotionArray[k]+"P";
                    }
                    //Still can revert to Pawn if move doesnt complete
                    chessBoard[row][column]="P";
                    //This is now the old piece
                    chessBoard[row-1][column]=oldPiece;
                }
            }
        }
        catch (Exception e){}
        
        try{
            //Allow Pawn to move two spaces forward                                                 //48 Bottom two Rows
            if(" ".equals(chessBoard[row-1][column]) && " ".equals(chessBoard[row-2][column]) && i >= 48){
                oldPiece=chessBoard[row-1][column];
                //Remove it from current location
                chessBoard[row][column]=" ";
                //Set new location to now be the Pawn
                chessBoard[row-2][column]="P";
                //Only move if you are not exposing your King
                if(kingSafe()){
                    //Record the move we just performed
                    list=list+row+column+(row-2)+(column)+oldPiece;
                }
                chessBoard[row][column]="P";
                //This is now the old piece
                chessBoard[row-2][column]=oldPiece;
            }
        }
        catch (Exception e){}
        return list;
    }
    
    //List of possible moves for Rook
    public static String possibleRookMoves(int i){
        String list="";
        String oldPiece;
        int row=i/8;
        int column=i%8;
        int temp = 1;
        //Checks in a vertical line
        for(int j=-1; j<=1; j+=2){
            try{
                //While square the piece is moving to is a blank square
                while(" ".equals(chessBoard[row][column+temp*j])){
                    //Get current location before moving
                    oldPiece=chessBoard[row][column+temp*j];
                    //Remove it from current location
                    chessBoard[row][column]=" ";
                    //Set new location to now be the Rook
                    chessBoard[row][column+temp*j]="R";
                    //Only move if you are not exposing your King
                    if(kingSafe()){
                        //Record the move we just performed
                        list=list+row+column+row+(column+temp*j)+oldPiece;
                    }
                    chessBoard[row][column]="R";
                    chessBoard[row][column+temp*j]=oldPiece;
                    temp++;
                }
                //If Lowercase then its an opponent and we can capture
                if (Character.isLowerCase(chessBoard[row][column+temp*j].charAt(0))){
                    //Get current location before moving
                    oldPiece=chessBoard[row][column+temp*j];
                    //Remove it from current location
                    chessBoard[row][column]=" ";
                    //Set new location to now be the Rook
                    chessBoard[row][column+temp*j]="R";
                    //Only move if you are not exposing your King
                    if(kingSafe()){
                        //Record the move we just performed
                        list=list+row+column+row+(column+temp*j)+oldPiece;
                    }
                    chessBoard[row][column]="R";
                    chessBoard[row][column+temp*j]=oldPiece;
                }
            } catch (Exception e){}
            
            temp=1;
            
            try{
                //While square the piece is moving to is a blank square
                while(" ".equals(chessBoard[row+temp*j][column])){
                    //Get current location before moving
                    oldPiece=chessBoard[row+temp*j][column];
                    //Remove it from current location
                    chessBoard[row][column]=" ";
                    //Set new location to now be the Rook
                    chessBoard[row+temp*j][column]="R";
                    //Only move if you are not exposing your King
                    if(kingSafe()){
                        //Record the move we just performed
                        list=list+row+column+(row+temp*j)+column+oldPiece;
                    }
                    chessBoard[row][column]="R";
                    chessBoard[row+temp*j][column]=oldPiece;
                    temp++;
                }
                //If Lowercase then its an opponent and we can capture
                if (Character.isLowerCase(chessBoard[row+temp*j][column].charAt(0))){
                    //Get current location before moving
                    oldPiece=chessBoard[row+temp*j][column];
                    //Remove it from current location
                    chessBoard[row][column]=" ";
                    //Set new location to now be the Rook
                    chessBoard[row+temp*j][column]="R";
                    //Only move if you are not exposing your King
                    if(kingSafe()){
                        //Record the move we just performed
                        list=list+row+column+(row+temp*j)+column+oldPiece;
                    }
                    chessBoard[row][column]="R";
                    chessBoard[row+temp*j][column]=oldPiece;
                }
            } catch (Exception e){}
            //Before next J "level" ensure temp is at 1 to search correctly on board
            temp=1;
        }
        return list; 
    }
    
    //List of possible moves for Knight
    public static String possibleKnightMoves(int i){
        String list="";
        String oldPiece;
        int row=i/8;
        int column=i%8;
        
        //Allows piece to move diagonally(J+=2)
        for(int j=-1; j<=1; j+=2){
            for(int k=-1; k<=1; k+=2){
                try {
                    //While square the piece is moving to is a blank square. 
                    if(Character.isLowerCase(chessBoard[row+j][column+k*2].charAt(0)) || " ".equals(chessBoard[row+j][column+k*2])){
                        oldPiece=chessBoard[row+j][column+k*2];
                        chessBoard[row][column]=" ";
                        chessBoard[row+j][column+k*2]="K";
                        //Only move if you are not exposing your King
                        if(kingSafe()){
                            //Record the move we just performed
                            list=list+row+column+(row+j)+(column+k*2)+oldPiece;
                        }
                        chessBoard[row][column]="K";
                        chessBoard[row+j][column+k*2]=oldPiece;
                    }
                }
                catch (Exception e){}
                
                try {
                    //While square the piece is moving to is a blank square. 
                    if(Character.isLowerCase(chessBoard[row+j*2][column+k].charAt(0)) || " ".equals(chessBoard[row+j*2][column+k])){
                        oldPiece=chessBoard[row+j*2][column+k];
                        chessBoard[row][column]=" ";
                        chessBoard[row+j*2][column+k]="K";
                        //Only move if you are not exposing your King
                        if(kingSafe()){
                            //Record the move we just performed
                            list=list+row+column+(row+j*2)+(column+k)+oldPiece;
                        }
                        chessBoard[row][column]="K";
                        chessBoard[row+j*2][column+k]=oldPiece;
                    }
                }
                catch (Exception e){}
            }
        }
        return list; 
    }
    
    //List of possible moves for Bishop
    public static String possibleBishopMoves(int i){
        String list="";
        String oldPiece;
        int row=i/8;
        int column=i%8;
        int temp=1;
        
        //Allows piece to move diagonally(J+=2)
        for(int j=-1; j<=1; j+=2){
            for(int k=-1; k<=1; k+=2){
                try {
                    //While square the piece is moving to is a blank square
                    while(" ".equals(chessBoard[row+temp*j][column+temp*k])){
                        //Get current location before moving
                        oldPiece=chessBoard[row+temp*j][column+temp*k];
                        //Remove it from current location
                        chessBoard[row][column]=" ";
                        //Set new location to now be the Bishop
                        chessBoard[row+temp*j][column+temp*k]="B";
                        //Only move if you are not exposing your King
                        if(kingSafe()){
                            //Record the move we just performed
                            list=list+row+column+(row+temp*j)+(column+temp*k)+oldPiece;
                        }
                        chessBoard[row][column]="B";
                        //Set new location to what the old piece was
                        chessBoard[row+temp*j][column+temp*k]=oldPiece;
                        temp++;
                    }
                    //If Lowercase then its an opponent and we can capture
                    if(Character.isLowerCase(chessBoard[row+temp*j][column+temp*k].charAt(0))){
                        //Get current location before moving
                        oldPiece=chessBoard[row+temp*j][column+temp*k];
                        //Remove it from current location, set its position to now be blank
                        chessBoard[row][column]=" ";
                        //Set new location to now be the Bishop
                        chessBoard[row+temp*j][column+temp*k]="B";
                        //Only move if you are not exposing your King
                        if(kingSafe()){
                            //Record the move we just performed
                            list=list+row+column+(row+temp*j)+(column+temp*k)+oldPiece;
                        }
                        chessBoard[row][column]="B";
                        //Set new location to what the old piece was
                        chessBoard[row+temp*j][column+temp*k]=oldPiece;                        
                    }
                } catch (Exception e) {}
                //Check 1 position away from Queens position is free, then imcriment
                temp=1;
            }
        }
        return list; 
    }
    
    //List of possible moves for Queen
    public static String possibleQueenMoves(int i){
        String list="";
        String oldPiece;
        int row=i/8;
        int column=i%8;
        int temp=1;
        
        //Allows us to move diagonally
        for(int j=-1; j<=1; j++){
            for(int k=-1; k<=1; k++){
                //If both are 0 then piece is not moving anywhere, speeds search engine up
                if(j!=0 || k!=0){
                    try {
                        //While square the piece is moving to is a blank square
                        while(" ".equals(chessBoard[row+temp*j][column+temp*k])){
                            //Get current location before moving
                            oldPiece=chessBoard[row+temp*j][column+temp*k];
                            //Remove it from current location, set its position to now be blank
                            chessBoard[row][column]=" ";
                            //Set new location to now be the Queen
                            chessBoard[row+temp*j][column+temp*k]="Q";
                            //Only move if you are not exposing your King
                            if(kingSafe()){
                                //Record the move we just performed
                                list=list+row+column+(row+temp*j)+(column+temp*k)+oldPiece;
                            }
                            chessBoard[row][column]="Q";
                            //Set new location to what the old piece was
                            chessBoard[row+temp*j][column+temp*k]=oldPiece;
                            temp++;
                        }
                        //If Lowercase then its an opponent and we can capture
                        if(Character.isLowerCase(chessBoard[row+temp*j][column+temp*k].charAt(0))){
                            //Get current location before moving
                            oldPiece=chessBoard[row+temp*j][column+temp*k];
                            //Remove it from current location, set its position to now be blank
                            chessBoard[row][column]=" ";
                            //Set new location to now be the Queen
                            chessBoard[row+temp*j][column+temp*k]="Q";
                            //Only move if you are not exposing your King
                            if(kingSafe()){
                                //Record the move we just performed
                                list=list+row+column+(row+temp*j)+(column+temp*k)+oldPiece;
                            }
                            chessBoard[row][column]="Q";
                            //Set new location to what the old piece was
                            chessBoard[row+temp*j][column+temp*k]=oldPiece;                        
                        }
                    } catch (Exception e) {}
                    //Check 1 position away from Queens position is free, then imcriment
                    temp=1;
                }
            }
        }
        return list; 
    }
    
    //List of possible moves for King
    public static String possibleKingMoves(int i){
        String list="";
        String oldPiece;
        int row=i/8;
        int column=i%8;
        //King can move to 8 possible positions and the current position = 9
        for(int j=0; j<9; j++){
            if(j!=4){
                try {
                    //If Lowercase then its an opponent and we can capture
                    if(Character.isLowerCase(chessBoard[row-1+j/3][column-1+j%3].charAt(0)) || " ".equals(chessBoard[row-1+j/3][column-1+j%3])){
                        oldPiece=chessBoard[row-1+j/3][column-1+j%3];
                        //Change position we were in to a blank square
                        chessBoard[row][column]=" ";
                        //New position becomes a King
                        chessBoard[row-1+j/3][column-1+j%3]="A";

                        int kingTemp=kingPositionCapital;
                        kingPositionCapital=i+(j/3)*8+j%3-9;
                        //Ensure we are not moving the King into danger, as result of this move
                        if(kingSafe()){
                            //Record the move we just performed
                            list=list+row+column+(row-1+j/3)+(column-1+j%3)+oldPiece;
                        }
                        //Set new location to now be the King(ACE)
                        chessBoard[row][column]="A";
                        chessBoard[row-1+j/3][column-1+j%3]=oldPiece;
                        kingPositionCapital=kingTemp;
                    }
                } catch (Exception e) {}
            }
        }
        return list; 
    }
    
    //Method to verify your not moving the king into danger
    public static boolean kingSafe(){
        //Check for opponents Bishop or Queen attacking
        int temp = 1;
        for(int i=-1; i<=1; i+=2){
            for(int j=-1; j<=1; j+=2){
                try {
                    //Using a while loop as it could be any distance so goes until end of board
                    while(" ".equals(chessBoard[kingPositionCapital/8+temp*i][kingPositionCapital%8+temp*j])) {temp++;}
                    if ("b".equals(chessBoard[kingPositionCapital/8+temp*i][kingPositionCapital%8+temp*j]) || 
                        "q".equals(chessBoard[kingPositionCapital/8+temp*i][kingPositionCapital%8+temp*j])){
                        return false;
                    }
                } catch (Exception e) {}
                //Set temp to one for next check (r+q)
                temp=1;
            }
        }
        
        //Check for opponents Rook+Queen attacking
        for(int i=-1; i<=1; i+=2){
            try {
                //Using a while loop as it could be any distance so goes until end of board
                while(" ".equals(chessBoard[kingPositionCapital/8][kingPositionCapital%8+temp*i])) {temp++;}
                if ("r".equals(chessBoard[kingPositionCapital/8][kingPositionCapital%8+temp*i]) || 
                    "q".equals(chessBoard[kingPositionCapital/8][kingPositionCapital%8+temp*i])){
                    return false;
                }
            } catch (Exception e) {}
            //Set temp to one for next check (r+q)
            temp=1;
            
            try {
                //Using a while loop as it could be any distance so goes until end of board
                while(" ".equals(chessBoard[kingPositionCapital/8][kingPositionCapital%8+temp*i])) {temp++;}
                if ("r".equals(chessBoard[kingPositionCapital/8][kingPositionCapital%8+temp*i]) || 
                    "q".equals(chessBoard[kingPositionCapital/8][kingPositionCapital%8+temp*i])){
                    return false;
                }
            } catch (Exception e) {}
            //Set temp to one for next check (r+q)
            temp=1;
        }
        
        //Check for opponents Knight attacking
        for(int i=-1; i<=1; i+=2){
            for(int j=-1; j<=1; j+=2){
                try {
                    if ("k".equals(chessBoard[kingPositionCapital/8+i][kingPositionCapital%8+j*2])){
                        return false;
                    }
                }
                //Catch prevent us jumping off board(illegal move)
                catch (Exception e) {}
                
                try {
                    if ("k".equals(chessBoard[kingPositionCapital/8+i*2][kingPositionCapital%8+j])){
                        return false;
                    }
                }
                //Catch prevent us jumping off board(illegal move)
                catch (Exception e) {}
            }
        }
        
        //Check for opponents Pawn attacking
        //Greater than 16 = not in top two rows
        if(kingPositionCapital>=16) {
            try {
                if ("p".equals(chessBoard[kingPositionCapital/8-1][kingPositionCapital%8-1])){
                    return false;
                    }
            }
            //Catch prevent us jumping off board(illegal move)
            catch (Exception e) {}
            
            try {
                if ("p".equals(chessBoard[kingPositionCapital/8-1][kingPositionCapital%8+1])){
                    return false;
                    }
            }
            //Catch prevent us jumping off board(illegal move)
            catch (Exception e) {}
            
            //Check for opponents King attacking
            for(int i=-1; i<=1; i++){
                for(int j=-1; j<=1; j++){
                    //Dont need to check its own position
                    if(i!=0 || j!=0){
                        try {
                            if ("a".equals(chessBoard[kingPositionCapital/8+i][kingPositionCapital%8+j])){
                                return false;
                            }
                        }
                        //Catch prevent us jumping off board(illegal move)
                        catch (Exception e) {}
                    }
                }
            }
        }
        return true;
    }
}