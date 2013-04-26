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
        desk[squareHelper.getRowNum(square)][squareHelper.getColumnNum(square)] = piece;
    }


    @Override
    public List<String> findPieceSquares(char piece)
    {
        List<String> result = new ArrayList<String>();
        DeskIterator deskIterator = deskIterator("A1");
        while (deskIterator.hasNext()) {
            char next = deskIterator.next();
            if (next == piece) {
                result.add(deskIterator.currentPosition());
            }
        }
        return result;
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

    private class DeskIteratorImpl implements DeskIterator
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


        private String toSquare(int rowNum, int colNum)
        {
            char c = (char) (65 + colNum);
            return String.valueOf(c) + String.valueOf(8 - rowNum);
        }
    }
}