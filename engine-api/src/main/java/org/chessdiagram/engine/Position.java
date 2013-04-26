package org.chessdiagram.engine;

import java.util.List;
import org.chessdiagram.engine.desk.Desk;

/**
 *
 */
public interface Position
{
    String toFen();


    String display();


    Desk getDesk();


    boolean makeMove(Move move);


    List<Move> getLegalMoves();


    boolean getActivePlayer();


    Position copy();


    boolean isCheck();
}