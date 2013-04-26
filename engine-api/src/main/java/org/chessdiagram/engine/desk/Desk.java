package org.chessdiagram.engine.desk;

import java.util.List;

/**
 *
 */
public interface Desk
{
    String display();


    String toFen();


    char getPieceAt(String square);


    void setPieceAt(String square, char piece);


    List<String> findPieceSquares(char piece);


    DeskIterator deskIterator(String square);
    
    Desk copy();
}