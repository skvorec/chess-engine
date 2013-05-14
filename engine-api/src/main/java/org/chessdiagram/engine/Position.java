package org.chessdiagram.engine;

import java.util.Map;
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


    Map<Move, Position> getLegalMovesHash();


    boolean getActivePlayer();


    Position copy();


    boolean isCheck(boolean whoIsUnderCheck);
}