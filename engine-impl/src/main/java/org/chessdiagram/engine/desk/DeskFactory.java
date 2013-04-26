package org.chessdiagram.engine.desk;

import org.apache.commons.lang.ArrayUtils;
import org.chessdiagram.engine.EngineException;

/**
 *
 */
public class DeskFactory
{
    public static final char EMPTY_CELL_CHAR = '_';
    public static final char[] AVAILABLE_CHARS = "rnbkqpRNBKQP".toCharArray();


    public Desk fromFen(String fenPart)
    {
        String[] rows = fenPart.split("/");
        if (rows.length != 8) {
            throw new EngineException("fen is incorrect: " + rows.length + " rows found instead of 8");
        }

        char[][] desk = new char[8][8];
        for (int i = 0; i < 8; i++) {
            char[] figuresCode = getFigureArray(rows[i]);
            desk[i] = figuresCode;
        }
        return new DeskImpl(desk);
    }


    protected char[] getFigureArray(String rowInFen)
    {
        char[] result = new char[8];

        int index = 0;
        for (int i = 0; i < rowInFen.length(); i++) {
            char current = rowInFen.charAt(i);
            if (Character.isDigit(current)) {
                for (int j = 0; j < Integer.parseInt(String.valueOf(current)); j++) {
                    result[index] = (EMPTY_CELL_CHAR);
                    index++;
                }
            }
            else {
                if (!ArrayUtils.contains(AVAILABLE_CHARS, current)) {
                    throw new EngineException("fen is incorrect: wrong character " + current);
                }
                result[index] = current;
                index++;
            }
        }

        return result;
    }
}