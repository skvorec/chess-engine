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
    protected final int[][] pawnWeight = new int[][]{
        {0, 0, 0, 0, 0, 0, 0, 0},
        {-6, -4, 1, 1, 1, 1, -4, -6},
        {-6, -4, 1, 2, 2, 1, -4, -6},
        {-6, -4, 2, 8, 8, 2, -4, -6},
        {-6, -4, 5, 10, 10, 5, -4, -6},
        {-4, -4, 1, 5, 5, 1, -4, -4},
        {-6, -4, 1, -24, -24, 1, -4, -6},
        {0, 0, 0, 0, 0, 0, 0, 0}
    };
    protected final int[][] pawnWeightBlack = tableForBlack(pawnWeight);
    //knight weight
    protected final int[][] knightWeight = new int[][]{
        {-8, -8, -8, -8, -8, -8, -8, -8},
        {-8, 0, 0, 0, 0, 0, 0, -8},
        {-8, 0, 4, 4, 4, 4, 0, -8},
        {-8, 0, 4, 8, 8, 4, 0, -8},
        {-8, 0, 4, 8, 8, 4, 0, -8},
        {-8, 0, 4, 4, 4, 4, 0, -8},
        {-8, 0, 1, 2, 2, 1, 0, -8},
        {-8, -12, -8, -8, -8, -8, -12, -8}
    };
    protected final int[][] knightWeightBlack = tableForBlack(knightWeight);
    //bishop weight
    protected final int[][] bishopWeight = new int[][]{
        {-4, -4, -4, -4, -4, -4, -4, -4},
        {-4, 0, 0, 0, 0, 0, 0, -4},
        {-4, 0, 2, 4, 4, 2, 0, -4},
        {-4, 0, 4, 6, 6, 4, 0, -4},
        {-4, 0, 4, 6, 6, 4, 0, -4},
        {-4, 1, 2, 4, 4, 2, 1, -4},
        {-4, 2, 1, 1, 1, 1, 2, -4},
        {-4, -4, -12, -4, -4, -12, -4, -4}
    };
    protected final int[][] bishopWeightBlack = tableForBlack(bishopWeight);
    //rook weight
    protected final int[][] rookWeight = new int[][]{
        {5, 5, 5, 5, 5, 5, 5, 5},
        {20, 20, 20, 20, 20, 20, 20, 20},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {-5, 0, 0, 0, 0, 0, 0, -5},
        {0, 0, 0, 2, 2, 0, 0, 0}
    };
    protected final int[][] rookWeightBlack = tableForBlack(rookWeight);
    //queen weight
    protected final int[][] queenWeight = new int[][]{
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 1, 0, 0},
        {0, 0, 1, 2, 2, 1, 0, 0},
        {0, 0, 2, 3, 3, 2, 0, 0},
        {0, 0, 2, 3, 3, 2, 0, 0},
        {0, 0, 1, 2, 2, 1, 0, 0},
        {0, 0, 1, 1, 1, 1, 0, 0},
        {-5, -5, -5, -5, -5, -5, -5, -5}
    };
    protected final int[][] queenWeightBlack = tableForBlack(queenWeight);
    //king weight
    protected final int[][] kingWeight = new int[][]{
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-40, -40, -40, -40, -40, -40, -40, -40},
        {-15, -15, -20, -20, -20, -20, -15, -15},
        {0, 20, 30, -30, 0, -20, 30, 20}
    };
    protected final int[][] kingWeightBlack = tableForBlack(kingWeight);
    //king end game weight
    protected final int[][] kingWeightEndgame = new int[][]{
        {0, 10, 20, 30, 30, 20, 10, 0},
        {10, 20, 30, 40, 40, 30, 20, 10},
        {20, 30, 40, 50, 50, 40, 30, 20},
        {30, 40, 50, 60, 60, 50, 40, 30},
        {30, 40, 50, 60, 60, 50, 40, 30},
        {20, 30, 40, 50, 50, 40, 30, 20},
        {10, 20, 30, 40, 40, 30, 20, 10},
        {0, 10, 20, 30, 30, 20, 10, 0}
    };
    protected final int[][] kingWeightEndgameBlack = tableForBlack(kingWeightEndgame);


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
                weightTable = pawnWeightBlack;
                break;
            case 'P':
                weightTable = pawnWeight;
                break;
            case 'r':
                weightTable = rookWeightBlack;
                break;
            case 'R':
                weightTable = rookWeight;
                break;
            case 'n':
                weightTable = knightWeightBlack;
                break;
            case 'N':
                weightTable = knightWeight;
                break;
            case 'b':
                weightTable = bishopWeightBlack;
                break;
            case 'B':
                weightTable = bishopWeight;
                break;
            case 'q':
                weightTable = queenWeightBlack;
                break;
            case 'Q':
                weightTable = queenWeight;
                break;
            case 'k':
                weightTable = kingWeightBlack;
                break;
            case 'K':
                weightTable = kingWeight;
                break;
            default:
                throw new EngineException("Illegal code char: " + code);
        }
        return weightTable[squareHelper.getRowNum(square)][squareHelper.getColumnNum(square)];
    }
}