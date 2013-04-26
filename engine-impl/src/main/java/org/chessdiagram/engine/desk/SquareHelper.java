package org.chessdiagram.engine.desk;

import org.chessdiagram.engine.EngineException;

/**
 *
 */
public class SquareHelper
{
    public int getRowNum(String square)
    {
        int rowNum;
        char rowChar = square.charAt(1);
        switch (rowChar) {
            case '1':
                rowNum = 7;
                break;
            case '2':
                rowNum = 6;
                break;
            case '3':
                rowNum = 5;
                break;
            case '4':
                rowNum = 4;
                break;
            case '5':
                rowNum = 3;
                break;
            case '6':
                rowNum = 2;
                break;
            case '7':
                rowNum = 1;
                break;
            case '8':
                rowNum = 0;
                break;
            default:
                throw new EngineException("Illegal coordinate String: " + square);
        }
        return rowNum;
    }


    public int getColumnNum(String square)
    {
        int colNum;
        char colChar = square.charAt(0);
        switch (colChar) {
            case 'A':
                colNum = 0;
                break;
            case 'B':
                colNum = 1;
                break;
            case 'C':
                colNum = 2;
                break;
            case 'D':
                colNum = 3;
                break;
            case 'E':
                colNum = 4;
                break;
            case 'F':
                colNum = 5;
                break;
            case 'G':
                colNum = 6;
                break;
            case 'H':
                colNum = 7;
                break;
            default:
                throw new EngineException("Illegal coordinate String: " + square);
        }
        return colNum;
    }
}