package org.chessdiagram.engine.desk;

/**
 *
 */
public interface Desk
{
    String display();


    String toFen();


    char getPieceAt(String square);


    void setPieceAt(String square, char piece);


    DeskIterator deskIterator(String square);


    Desk copy();


    void rollBack();


    void cleanRollBackCache();
}