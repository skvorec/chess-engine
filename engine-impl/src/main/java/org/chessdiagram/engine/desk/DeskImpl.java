package org.chessdiagram.engine.desk;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.chessdiagram.engine.EngineException;
import static org.chessdiagram.engine.desk.DeskFactory.AVAILABLE_CHARS;
import static org.chessdiagram.engine.desk.DeskFactory.EMPTY_CELL_CHAR;

/**
 *
 */
public class DeskImpl implements Desk
{
    private final SquareHelper squareHelper = new SquareHelper();
    private List<RollBackEntry> rollBackEntries = new ArrayList<RollBackEntry>();
    //desk
    //in java coding: row[0] is the top row
    private final char[][] desk;


    public DeskImpl(char[][] desk)
    {
        this.desk = desk;
    }


    @Override
    public char getPieceAt(String square)
    {
        return desk[squareHelper.getRowNum(square)][squareHelper.getColumnNum(square)];
    }


    @Override
    public void setPieceAt(String square, char piece)
    {
        if (!ArrayUtils.contains(AVAILABLE_CHARS, piece) && piece != EMPTY_CELL_CHAR) {
            throw new EngineException("piece is incorrect: " + piece);
        }
        final int rowNum = squareHelper.getRowNum(square);
        final int columnNum = squareHelper.getColumnNum(square);
        rollBackEntries.add(new RollBackEntry(rowNum, columnNum, desk[rowNum][columnNum]));
        desk[rowNum][columnNum] = piece;
    }


    @Override
    public DeskIterator deskIterator(String square)
    {
        int rowNumCursor = squareHelper.getRowNum(square);
        int colNumCursor = squareHelper.getColumnNum(square);
        return new DeskIteratorImpl(rowNumCursor, colNumCursor);
    }


    @Override
    public String display()
    {
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                result.append(desk[i][j]);
            }
            result.append("\n");
        }
        return result.toString();
    }


    @Override
    public String toFen()
    {
        StringBuilder result = new StringBuilder("");
        //desk as fen
        int emptyColumnsCounter = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (desk[i][j] == EMPTY_CELL_CHAR) {
                    emptyColumnsCounter++;
                }
                else {
                    if (emptyColumnsCounter != 0) {
                        result.append(emptyColumnsCounter);
                        emptyColumnsCounter = 0;
                    }
                    result.append(desk[i][j]);
                }
            }
            if (emptyColumnsCounter != 0) {
                result.append(emptyColumnsCounter);
                emptyColumnsCounter = 0;
            }
            result.append("/");
        }
        result.deleteCharAt(result.toString().length() - 1);
        return result.toString();
    }


    @Override
    public Desk copy()
    {
        char[][] copiedDesk = new char[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(desk[i], 0, copiedDesk[i], 0, 8);
        }
        return new DeskImpl(copiedDesk);
    }


    @Override
    public void rollBack()
    {
        for (RollBackEntry entry : rollBackEntries) {
            desk[entry.rollBackRow][entry.rollBackCol] = entry.rollBackPiece;
        }
        rollBackEntries = new ArrayList<RollBackEntry>();
    }


    @Override
    public void cleanRollBackCache()
    {
        rollBackEntries = new ArrayList<RollBackEntry>();
    }

    private static class RollBackEntry
    {
        private final int rollBackRow;
        private final int rollBackCol;
        private final char rollBackPiece;


        public RollBackEntry(int rollBackRow, int rollBackCol, char rollBackPiece)
        {
            this.rollBackRow = rollBackRow;
            this.rollBackCol = rollBackCol;
            this.rollBackPiece = rollBackPiece;
        }
    }

    protected class DeskIteratorImpl implements DeskIterator
    {
        private int rowNumCursor;
        private int colNumCursor;


        public DeskIteratorImpl(int rowNumCursor, int colNumCursor)
        {
            this.rowNumCursor = rowNumCursor;
            this.colNumCursor = colNumCursor;
        }


        @Override
        public boolean hasNext()
        {
            if (rowNumCursor == 0 && colNumCursor == 7) {
                return false;
            }
            return true;
        }


        @Override
        public boolean hasNext(MoveVector mVector)
        {
            MoveVectorImpl mVectorImpl = (MoveVectorImpl) mVector;
            int newRowCursor = rowNumCursor + mVectorImpl.getRowNum();
            int newColCursor = colNumCursor + mVectorImpl.getColumnNum();
            if (newRowCursor < 0 || newRowCursor > 7 || newColCursor < 0 || newColCursor > 7) {
                return false;
            }
            return true;
        }


        @Override
        public char next()
        {
            if (colNumCursor != 7) {
                colNumCursor++;
            }
            else {
                colNumCursor = 0;
                rowNumCursor--;
            }
            return desk[rowNumCursor][colNumCursor];
        }


        @Override
        public char next(MoveVector mVector)
        {
            MoveVectorImpl mVectorImpl = (MoveVectorImpl) mVector;
            rowNumCursor = rowNumCursor + mVectorImpl.getRowNum();
            colNumCursor = colNumCursor + mVectorImpl.getColumnNum();
            return desk[rowNumCursor][colNumCursor];
        }


        @Override
        public char getPieceAt(MoveVector mVector)
        {
            MoveVectorImpl mVectorImpl = (MoveVectorImpl) mVector;
            return desk[rowNumCursor + mVectorImpl.getRowNum()][colNumCursor + mVectorImpl.getColumnNum()];
        }


        @Override
        public String getSquareAt(MoveVector mVector)
        {
            MoveVectorImpl mVectorImpl = (MoveVectorImpl) mVector;
            return toSquare(rowNumCursor + mVectorImpl.getRowNum(), colNumCursor + mVectorImpl.getColumnNum());
        }


        @Override
        public String currentPosition()
        {
            return toSquare(rowNumCursor, colNumCursor);
        }


        protected String toSquare(int rowNum, int colNum)
        {
            char colChar = (char) (65 + colNum);
            char rowChar;
            switch (rowNum) {
                case 0:
                    rowChar = '8';
                    break;
                case 1:
                    rowChar = '7';
                    break;
                case 2:
                    rowChar = '6';
                    break;
                case 3:
                    rowChar = '5';
                    break;
                case 4:
                    rowChar = '4';
                    break;
                case 5:
                    rowChar = '3';
                    break;
                case 6:
                    rowChar = '2';
                    break;
                case 7:
                    rowChar = '1';
                    break;
                default:
                    throw new EngineException("invalid rowNum: " + rowNum);
            }
            return new String(new char[]{colChar, rowChar});
        }
    }
}
