package org.chessdiagram.engine.evaluate;

import org.chessdiagram.engine.EngineException;
import org.chessdiagram.engine.desk.DeskFactory;

/**
 *
 */
public class PieceNameEvaluationService
{
    private static final int VALUE_PAWN = 100;
    private static final int VALUE_KNIGHT = 325;
    private static final int VALUE_BISHOP = 325;
    private static final int VALUE_ROOK = 500;
    private static final int VALUE_QUEEN = 1000;


    public int evaluate(char piece)
    {
        switch (piece) {
            case 'k':
            case 'K':
            case DeskFactory.EMPTY_CELL_CHAR:
                return 0;
            case 'Q':
                return VALUE_QUEEN;
            case 'q':
                return -VALUE_QUEEN;
            case 'R':
                return VALUE_ROOK;
            case 'r':
                return -VALUE_ROOK;
            case 'B':
                return VALUE_BISHOP;
            case 'b':
                return -VALUE_BISHOP;
            case 'N':
                return VALUE_KNIGHT;
            case 'n':
                return -VALUE_KNIGHT;
            case 'P':
                return VALUE_PAWN;
            case 'p':
                return -VALUE_PAWN;
            default:
                throw new EngineException("Illegal code char: " + piece);
        }
    }
}