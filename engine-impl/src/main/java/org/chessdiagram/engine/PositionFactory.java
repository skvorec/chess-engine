package org.chessdiagram.engine;

import org.apache.commons.lang.ArrayUtils;
import org.chessdiagram.engine.desk.Desk;
import org.chessdiagram.engine.desk.DeskFactory;

/**
 *
 */
public class PositionFactory
{
    public static final char EMPTY_CELL_CHAR = '_';
    private static final char[] AVAILABLE_CHARS = "rnbkqpRNBKQP".toCharArray();


    public Position fromFen(String fen)
    {
        String[] parts = fen.split(" ");
        if (parts.length != 6) {
            throw new IllegalArgumentException("fen is incorrect: " + fen);
        }
        String deskPart = parts[0];
        DeskFactory deskFactory = new DeskFactory();


        Desk desk = deskFactory.fromFen(deskPart);
        PositionImpl result = new PositionImpl(desk);

        String playerPart = parts[1];
        if (playerPart.equals("w")) {
            result.activePlayer = true;
        }
        else if (playerPart.equals("b")) {
            result.activePlayer = false;
        }
        else {
            throw new IllegalArgumentException("fen is incorrect: wrong active player description " + playerPart);
        }

        String castlesPart = parts[2];
        char K = castlesPart.charAt(0);
        if (K == 'K') {
            result.K = true;
        }
        else if (K == '-') {
            result.K = false;
        }
        else {
            throw new IllegalArgumentException("fen is incorrect: wrong castles description " + castlesPart);
        }

        char Q = castlesPart.charAt(1);
        if (Q == 'Q') {
            result.Q = true;
        }
        else if (Q == '-') {
            result.Q = false;
        }
        else {
            throw new IllegalArgumentException("fen is incorrect: wrong castles description " + castlesPart);
        }

        char k = castlesPart.charAt(2);
        if (k == 'k') {
            result.k = true;
        }
        else if (k == '-') {
            result.k = false;
        }
        else {
            throw new IllegalArgumentException("fen is incorrect: wrong castles description " + castlesPart);
        }

        char q = castlesPart.charAt(3);
        if (q == 'q') {
            result.q = true;
        }
        else if (q == '-') {
            result.q = false;
        }
        else {
            throw new IllegalArgumentException("fen is incorrect: wrong castles description " + castlesPart);
        }


        String enPassantPart = parts[3];
        if (enPassantPart.equals("-")) {
            result.possibleEnPassantField = null;
        }
        else if (enPassantPart.matches("[A-H][1-8]")) {
            result.possibleEnPassantField = enPassantPart;
        }
        else {
            throw new IllegalArgumentException("fen is incorrect: wrong en passant description " + enPassantPart);
        }

        String halfMovesPart = parts[4];
        result.halfMoves = Integer.parseInt(halfMovesPart);

        String movesPart = parts[5];
        result.moves = Integer.parseInt(movesPart);

        return result;
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
                    throw new IllegalArgumentException("fen is incorrect: wrong character " + current);
                }
                result[index] = current;
                index++;
            }
        }

        return result;
    }
}
