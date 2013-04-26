package org.chessdiagram.engine.evaluate;

import org.chessdiagram.engine.EngineException;
import org.chessdiagram.engine.desk.DeskFactory;
import org.chessdiagram.engine.desk.SquareHelper;

/**
 *
 */
public class PiecePositionEvaluateService
{
    private final SquareHelper squareHelper = new SquareHelper();
    //pawn weight
    protected final int[][] pawn_weight = new int[][]{
        {0, 0, 0, 0, 0, 0, 0, 0},
        {-6, -4, 1, 1, 1, 1, -4, -6},
        {-6, -4, 1, 2, 2, 1, -4, -6},
        {-6, -4, 2, 8, 8, 2, -4, -6},
        {-6, -4, 5, 10, 10, 5, -4, -6},
        {-4, -4, 1, 5, 5, 1, -4, -4},
        {-6, -4, 1, -24, -24, 1, -4, -6},
        {0, 0, 0, 0, 0, 0, 0, 0}
    };
    protected final int[][] pawn_weight_black = tableForBlack(pawn_weight);
    //knight weight
    protected final int[][] knight_weight = new int[][]{
        {-8, -8, -8, -8, -8, -8, -8, -8},
        {-8, 0, 0, 0, 0, 0, 0, -8},
        {-8, 0, 4, 4, 4, 4, 0, -8},
        {-8, 0, 4, 8, 8, 4, 0, -8},
        {-8, 0, 4, 8, 8, 4, 0, -8},
        {-8, 0, 4, 4, 4, 4, 0, -8},
        {-8, 0, 1, 2, 2, 1, 0, -8},
        {-8, -12, -8, -8, -8, -8, -12, -8}
    };
    protected final int[][] knight_weight_black = tableForBlack(knight_weight);
    //bishop weight
    protected final int[][] bishop_weight = new int[][]{
        {-4, -4, -4, -4, -4, -4, -4, -4},
        {-4, 0, 0, 0, 0, 0, 0, -4},
        {-4, 0, 2, 4, 4, 2, 0, -4},
        {-4, 0, 4, 6, 6, 4, 0, -4},
        {-4, 0, 4, 6, 6, 4, 0, -4},
        {-4, 1, 2, 4, 4, 2, 1, -4},
        {-4, 2, 1, 1, 1, 1, 2, -4},
        {-4, -4, -12, -4, -4, -12, -4, -4}
    };
    protected final int[][] bishop_weight_black = tableForBlack(bishop_weight);
    //rook weight
    protected final int[][] rook_weight = new int[][]{
        {5, 5, 5, 5, 5, 5, 5, 5},
        {20, 20, 20, 20, 20, 20, 20, 20},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {0, 0, 0, 2, 2, 0, 0, 0}
    };
    protected final int[][] rook_weight_black = tableForBlack(rook_weight);
    //queen weight
    protected final int[][] queen_weight = new int[][]{
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 1, 0, 0},
        {0, 0, 1, 2, 2, 1, 0, 0},
        {0, 0, 2, 3, 3, 2, 0, 0},
        {0, 0, 2, 3, 3, 2, 0, 0},
        {0, 0, 1, 2, 2, 1, 0, 0},
        {0, 0, 1, 1, 1, 1, 0, 0},
        {-5, -5, -5, -5, -5, -5, -5, -5}
    };
    protected final int[][] queen_weight_black = tableForBlack(queen_weight);
    //king weight
    protected final int[][] king_weight = new int[][]{
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-15, -15, -20, -20, -20, -20, -15, -15},
        {0, 20, 30, -30, 0, -20, 30, 20}
    };
    protected final int[][] king_weight_black = tableForBlack(king_weight);
    //king end game weight
    protected final int[][] king_weight_endgame = new int[][]{
        {0, 10, 20, 30, 30, 20, 10, 0},
        {10, 20, 30, 40, 40, 30, 20, 10},
        {20, 30, 40, 50, 50, 40, 30, 20},
        {30, 40, 50, 60, 60, 50, 40, 30},
        {30, 40, 50, 60, 60, 50, 40, 30},
        {20, 30, 40, 50, 50, 40, 30, 20},
        {10, 20, 30, 40, 40, 30, 20, 10},
        {0, 10, 20, 30, 30, 20, 10, 0}
    };
    protected final int[][] king_weight_endgame_black = tableForBlack(king_weight_endgame);


    protected final int[][] tableForBlack(int[][] tableForWhite)
    {
        int[][] result = new int[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(tableForWhite[7 - i], 0, result[i], 0, 8);
        }
        return result;
    }


    public int evaluate(String square, char code)
    {
        if (code == DeskFactory.EMPTY_CELL_CHAR) {
            return 0;
        }
        int[][] weightTable;
        switch (code) {
            case 'p':
                weightTable = pawn_weight_black;
                break;
            case 'P':
                weightTable = pawn_weight;
                break;
            case 'r':
                weightTable = rook_weight_black;
                break;
            case 'R':
                weightTable = rook_weight;
                break;
            case 'n':
                weightTable = knight_weight_black;
                break;
            case 'N':
                weightTable = knight_weight;
                break;
            case 'b':
                weightTable = bishop_weight_black;
                break;
            case 'B':
                weightTable = bishop_weight;
                break;
            case 'q':
                weightTable = queen_weight_black;
                break;
            case 'Q':
                weightTable = queen_weight;
                break;
            case 'k':
                weightTable = king_weight_black;
                break;
            case 'K':
                weightTable = king_weight;
                break;
            default:
                throw new EngineException("Illegal code char: " + code);
        }
        return weightTable[squareHelper.getRowNum(square)][squareHelper.getColumnNum(square)];
    }
}