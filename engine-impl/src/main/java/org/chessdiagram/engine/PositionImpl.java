package org.chessdiagram.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.BooleanUtils;
import static org.chessdiagram.engine.PositionFactory.EMPTY_CELL_CHAR;
import org.chessdiagram.engine.desk.Desk;
import org.chessdiagram.engine.desk.DeskIterator;
import org.chessdiagram.engine.desk.MoveVector;
import static org.chessdiagram.engine.desk.MoveVectorImpl.*;

/**
 *
 */
public class PositionImpl implements Position
{
    private static final MoveVector KNIGHT_NWW = NORTH.plus(WEST).plus(WEST);
    private static final MoveVector KNIGHT_NEE = NORTH.plus(EAST).plus(EAST);
    private static final MoveVector KNIGHT_NNW = NORTH.plus(NORTH).plus(WEST);
    private static final MoveVector KNIGHT_NNE = NORTH.plus(NORTH).plus(EAST);
    private static final MoveVector KNIGHT_SWW = SOUTH.plus(WEST).plus(WEST);
    private static final MoveVector KNIGHT_SEE = SOUTH.plus(EAST).plus(EAST);
    private static final MoveVector KNIGHT_SSW = SOUTH.plus(SOUTH).plus(WEST);
    private static final MoveVector KNIGHT_SSE = SOUTH.plus(SOUTH).plus(EAST);
    private static final MoveVector[] ALL_KNIGHT_MOVES = {KNIGHT_NWW, KNIGHT_NEE, KNIGHT_NNW, KNIGHT_NNE, KNIGHT_SWW,
        KNIGHT_SEE, KNIGHT_SSW, KNIGHT_SSE};
    private static final MoveVector[] ALL_KING_MOVES = {NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST};
    private Memento memento;
    /**
     * true is white, false is black
     */
    public boolean activePlayer;
    //castles
    public boolean K;
    public boolean Q;
    public boolean k;
    public boolean q;
    //en pasant; null if there en passant is unavailable
    public String possibleEnPassantField;
    //desk
    //in java coding: row[0] is the top row
    private Desk desk;
    //counters
    public int halfMoves;
    public int moves;


    public PositionImpl(Desk desk)
    {
        this.desk = desk;
    }


    @Override
    public Desk getDesk()
    {
        return desk;
    }


    @Override
    public boolean getActivePlayer()
    {
        return activePlayer;
    }


    @Override
    public List<Move> getLegalMoves()
    {
        List<Move> result = new ArrayList<Move>();
        DeskIterator deskIterator = desk.deskIterator("A1");
        while (deskIterator.hasNext()) {
            deskIterator.next();
            List<Move> canMoveFrom = canMoveFrom(deskIterator.currentPosition());
            for (Move move : canMoveFrom) {
                toMemento();
                preMove(move);
                if (!isCheck()) {
                    result.add(move);
                }
                fromMemento();
            }
        }
        return result;
    }


    @Override
    public String display()
    {
        return desk.display() + "\n" + "K=" + K + " Q=" + Q + " k=" + k + " q=" + q + " ep=" + possibleEnPassantField;
    }


    @Override
    public String toFen()
    {
        StringBuilder result = new StringBuilder("");
        result.append(desk.toFen());
        result.append(" ");
        result.append(activePlayer ? "w" : "b");
        result.append(" ");
        result.append(K ? "K" : "-");
        result.append(Q ? "Q" : "-");
        result.append(k ? "k" : "-");
        result.append(q ? "q" : "-");
        result.append(" ");
        result.append((possibleEnPassantField == null) ? "-" : possibleEnPassantField);
        result.append(" ");
        result.append(halfMoves);
        result.append(" ");
        result.append(moves);

        return result.toString();
    }


    private boolean differentCase(char ch1, char ch2)
    {
        boolean[] forXor = {Character.isUpperCase(ch1), Character.isUpperCase(ch2)};
        return BooleanUtils.xor(forXor);
    }


    private boolean differentColor(boolean color1, boolean color2)
    {
        boolean[] forXor = {color1, color2};
        return BooleanUtils.xor(forXor);
    }


    @Override
    public boolean makeMove(Move move)
    {
        String initialSquare = move.getFrom();
        if (canMoveFrom(initialSquare).contains(move)) {
            if (Character.isUpperCase(desk.getPieceAt(initialSquare)) != activePlayer) {
                String color = activePlayer ? "White" : "Black";
                System.out.println(move.toString() + " " + color + " player cannot move " + desk.getPieceAt(initialSquare));
                return false;
            }

            toMemento();
            halfMoves++;
            preMove(move);
            if (isCheck()) {
                System.out.println(move.toString() + " move was not done, it is unlegal");
                fromMemento();
                return false;
            }
            else {
                //increase move counter after black move
                if (!activePlayer) {
                    moves++;
                }

                if (halfMoves == 50) {
                    System.out.println("Half moves == 50!");
                }
                //all is ok
                activePlayer = !activePlayer;
                memento = null;//we no need more in old state
            }

        }
        else {
            System.out.println("Cannot move " + move.toString());
//            System.out.println(display());
            return false;
        }
        return true;
    }


    @Override
    public boolean isCheck()
    {
        char kingUnderAttack = activePlayer ? 'K' : 'k';
        DeskIterator deskIterator = desk.deskIterator("A1");
        while (deskIterator.hasNext()) {
            char currentPiece = deskIterator.next();
            if (currentPiece == kingUnderAttack) {
                return isAttacked(!activePlayer, deskIterator.currentPosition());
            }
        }
        return false;
    }


    //Here we do not change active player, moves, half moves but only pieces on the desk
    protected void preMove(Move move)
    {
        String initial = move.getFrom();
        String destination = move.getDestination();
        char oldInitialPiece = desk.getPieceAt(initial);
        char destInitialPiece = desk.getPieceAt(destination);
        if (destInitialPiece != EMPTY_CELL_CHAR) {
            halfMoves = 0;
        }
        //1. move
        desk.setPieceAt(destination, oldInitialPiece);
        desk.setPieceAt(initial, EMPTY_CELL_CHAR);
        String oldEnpassant = possibleEnPassantField;
        possibleEnPassantField = null;
        if (oldInitialPiece == 'p' || oldInitialPiece == 'P') {
            halfMoves = 0;
            //2. check enPassant
            //2.1 set enPassant flag
            int initialRowNumber = Character.isUpperCase(oldInitialPiece) ? 2 : 7; //we are in terms of chess desk
            int destRowNumber = Character.isUpperCase(oldInitialPiece) ? 4 : 5;
            int finalRowNumber = Character.isUpperCase(oldInitialPiece) ? 8 : 1;

            DeskIterator deskIterator = desk.deskIterator(initial);
            MoveVector addToInitial = Character.isUpperCase(oldInitialPiece) ? NORTH : SOUTH;
            if (initial.endsWith(String.valueOf(initialRowNumber))
                    && destination.endsWith(String.valueOf(destRowNumber))) {
                possibleEnPassantField = deskIterator.getSquareAt(addToInitial);
            }
            //2.2 check capturing enPassant
            if (destination.equals(oldEnpassant)) {
                DeskIterator epIterator = desk.deskIterator(oldEnpassant);
                desk.setPieceAt(epIterator.getSquareAt(addToInitial.negate()), EMPTY_CELL_CHAR);
            }
            //3. check pawn to figure transformation
            if (destination.endsWith(String.valueOf(finalRowNumber))) {
                desk.setPieceAt(destination, move.getPromotion());
            }
        }

        //4. check castling
        //4.3 this move is castling
        if (oldInitialPiece == 'K' && K && destination.equals("G1")) {
            K = false;
            Q = false;
            desk.setPieceAt("H1", EMPTY_CELL_CHAR);
            desk.setPieceAt("F1", 'R');
        }
        if (oldInitialPiece == 'K' && Q && destination.equals("C1")) {
            K = false;
            Q = false;
            desk.setPieceAt("A1", EMPTY_CELL_CHAR);
            desk.setPieceAt("D1", 'R');
        }
        if (oldInitialPiece == 'k' && k && destination.equals("G8")) {
            k = false;
            q = false;
            desk.setPieceAt("H8", EMPTY_CELL_CHAR);
            desk.setPieceAt("F8", 'r');
        }
        if (oldInitialPiece == 'k' && q && destination.equals("C8")) {
            k = false;
            q = false;
            desk.setPieceAt("A8", EMPTY_CELL_CHAR);
            desk.setPieceAt("D8", 'r');
        }
        //4.1 move of rook or king
        if (oldInitialPiece == 'K') {
            K = false;
            Q = false;
        }
        if (oldInitialPiece == 'k') {
            k = false;
            q = false;
        }
        if (oldInitialPiece == 'R') {
            if (initial.equals("A1")) {
                Q = false;
            }
            if (initial.equals("H1")) {
                K = false;
            }
        }
        if (oldInitialPiece == 'r') {
            if (initial.equals("A8")) {
                q = false;
            }
            if (initial.equals("H8")) {
                k = false;
            }
        }
        //4.2 rook is captured
        if (destination.equals("A1")) {
            Q = false;
        }
        if (destination.equals("H1")) {
            K = false;
        }
        if (destination.equals("A8")) {
            q = false;
        }
        if (destination.equals("H8")) {
            k = false;
        }
    }


    protected void addIfEmptyOrAnotherColor(String ourSquare, MoveVector moveVector, List<Move> result)
    {
        char ourPiece = desk.getPieceAt(ourSquare);
//        System.out.println("our square: " + ourSquare);
//        System.out.println("our piece: " + ourPiece);
        DeskIterator deskIterator = desk.deskIterator(ourSquare);
        if (deskIterator.hasNext(moveVector)) {
            char figure = deskIterator.getPieceAt(moveVector);
//            System.out.println("found figure: " + figure + " at " + deskIterator.getSquareAt(moveVector));
            if (figure == EMPTY_CELL_CHAR || differentCase(ourPiece, figure)) {
                result.add(new MoveImpl(ourSquare, deskIterator.getSquareAt(moveVector)));
            }
        }
    }


    protected List<MoveImpl> canMoveAlongVector(String square, MoveVector vector)
    {
        boolean initialColor = Character.isUpperCase(desk.getPieceAt(square));
        List<MoveImpl> result = new ArrayList<MoveImpl>();
        DeskIterator deskIterator = desk.deskIterator(square);

        while (deskIterator.hasNext(vector)) {
            char next = deskIterator.next(vector);
            if (next == EMPTY_CELL_CHAR) {
                result.add(new MoveImpl(square, deskIterator.currentPosition()));
            }
            else {
                if (differentColor(initialColor, Character.isUpperCase(next))) {
                    result.add(new MoveImpl(square, deskIterator.currentPosition()));
                }
                break;
            }
        }

        return result;
    }


    protected List<Move> canMoveKnightFrom(String ourSquare)
    {
//        System.out.println("--Can move knight from square " + ourSquare);
        List<Move> result = new ArrayList<Move>();

        for (MoveVector mVector : ALL_KNIGHT_MOVES) {
            addIfEmptyOrAnotherColor(ourSquare, mVector, result);
        }

//        System.out.println("--End of can move knight, result is " + result);
        return result;
    }


    protected List<Move> canMoveBishopFrom(String square)
    {
        List<Move> result = new ArrayList<Move>();
        result.addAll(canMoveAlongVector(square, NORTH_EAST));
        result.addAll(canMoveAlongVector(square, NORTH_WEST));
        result.addAll(canMoveAlongVector(square, SOUTH_EAST));
        result.addAll(canMoveAlongVector(square, SOUTH_WEST));
        return result;
    }


    protected List<Move> canMoveRookFrom(String square)
    {
        List<Move> result = new ArrayList<Move>();
        result.addAll(canMoveAlongVector(square, NORTH));
        result.addAll(canMoveAlongVector(square, WEST));
        result.addAll(canMoveAlongVector(square, SOUTH));
        result.addAll(canMoveAlongVector(square, EAST));

        return result;
    }


    protected List<Move> canMoveQueenFrom(String square)
    {
        List<Move> result = canMoveBishopFrom(square);
        result.addAll(canMoveRookFrom(square));

        return result;
    }


    protected boolean movesContainsDestination(List<Move> moves, String destSquare)
    {
        for (Move move : moves) {
            if (move.getDestination().equals(destSquare)) {
                return true;
            }
        }

        return false;
    }


    protected boolean isAttacked(boolean byColor, String square)
    {
        DeskIterator deskIterator = desk.deskIterator("A1");
        while (deskIterator.hasNext()) {
            char currentPiece = deskIterator.next();
            if ((currentPiece != EMPTY_CELL_CHAR) && (Character.isUpperCase(currentPiece) == byColor)) {
                String currentPosition = deskIterator.currentPosition();
                switch (currentPiece) {
                    case 'n':
                    case 'N':
                        if (movesContainsDestination(canMoveKnightFrom(currentPosition), square)) {
//                            System.out.println("by knight " + currentPosition);
                            return true;
                        }
                        break;
                    case 'b':
                    case 'B':
                        if (movesContainsDestination(canMoveBishopFrom(currentPosition), square)) {
//                            System.out.println("by bishop " + currentPosition);
                            return true;
                        }
                        break;
                    case 'r':
                    case 'R':
                        if (movesContainsDestination(canMoveRookFrom(currentPosition), square)) {
//                            System.out.println("by rook " + currentPosition);
                            return true;
                        }
                        break;
                    case 'q':
                    case 'Q':
                        if (movesContainsDestination(canMoveQueenFrom(currentPosition), square)) {
//                            System.out.println("by queen " + currentPosition);
                            return true;
                        }
                        break;
                    case 'p':
                    case 'P':
                        MoveVector basicMoveVector = Character.isUpperCase(currentPiece) ? NORTH : SOUTH;
                        if ((deskIterator.hasNext(basicMoveVector.plus(EAST))
                                && deskIterator.getSquareAt(basicMoveVector.plus(EAST)).equals(square))
                                || (deskIterator.hasNext(basicMoveVector.plus(WEST))
                                && deskIterator.getSquareAt(basicMoveVector.plus(WEST)).equals(square))) {
//                            System.out.println("by pawn " + currentPosition);
                            return true;
                        }
                        break;
                    case 'k':
                    case 'K':
                        for (MoveVector mVector : ALL_KING_MOVES) {
                            if (deskIterator.hasNext(mVector) && square.equals(deskIterator.getSquareAt(mVector))) {
//                                System.out.println("by king " + currentPosition);
                                return true;
                            }
                        }
                        break;
                }
            }
        }

        return false;
    }


    protected List<Move> canMoveFrom(String square)
    {
        List<Move> result = new ArrayList<Move>();

        //assert initial coordinates are inside desk
        char piece = desk.getPieceAt(square);
        if ((piece == EMPTY_CELL_CHAR) || (Character.isUpperCase(piece) != activePlayer)) {
            return Collections.EMPTY_LIST;
        }
        switch (piece) {
            case 'n':
            case 'N':
                return canMoveKnightFrom(square);
            case 'b':
            case 'B':
                return canMoveBishopFrom(square);
            case 'r':
            case 'R':
                return canMoveRookFrom(square);
            case 'q':
            case 'Q':
                return canMoveQueenFrom(square);
            case 'p':
            case 'P':
                MoveVector basicMoveVector = Character.isUpperCase(piece) ? NORTH : SOUTH;
                char[] possibleTransforms = Character.isUpperCase(piece)
                        ? new char[]{'R', 'N', 'B', 'Q'} : new char[]{'r', 'n', 'b', 'q'};
                int initialRowNumber = Character.isUpperCase(piece) ? 2 : 7; //we are in terms of chess desk
                int finalRowNumber = Character.isUpperCase(piece) ? 8 : 1; //we are in terms of chess desk
                DeskIterator deskIterator = desk.deskIterator(square);

                if (deskIterator.getPieceAt(basicMoveVector) == EMPTY_CELL_CHAR) {
                    final String destination = deskIterator.getSquareAt(basicMoveVector);
                    if (destination.endsWith(String.valueOf(finalRowNumber))) {
                        for (char transform : possibleTransforms) {
                            result.add(new MoveImpl(square, destination, transform));
                        }
                    }
                    else {
                        result.add(new MoveImpl(square, destination));
                    }
                }
                if (deskIterator.currentPosition().endsWith(String.valueOf(initialRowNumber))
                        && deskIterator.getPieceAt(basicMoveVector) == EMPTY_CELL_CHAR
                        && deskIterator.getPieceAt(basicMoveVector.plus(basicMoveVector)) == EMPTY_CELL_CHAR) {
                    result.add(new MoveImpl(square, deskIterator.getSquareAt(basicMoveVector.plus(basicMoveVector))));
                }
                MoveVector fireEast = basicMoveVector.plus(EAST);
                if (deskIterator.hasNext(fireEast) && deskIterator.getPieceAt(fireEast) != EMPTY_CELL_CHAR
                        && differentCase(piece, deskIterator.getPieceAt(fireEast))) {

                    final String destination = deskIterator.getSquareAt(fireEast);
                    if (destination.endsWith(String.valueOf(finalRowNumber))) {
                        for (char transform : possibleTransforms) {
                            result.add(new MoveImpl(square, destination, transform));
                        }
                    }
                    else {
                        result.add(new MoveImpl(square, destination));
                    }

                }
                MoveVector fireWest = basicMoveVector.plus(WEST);
                if (deskIterator.hasNext(fireWest) && deskIterator.getPieceAt(fireWest) != EMPTY_CELL_CHAR
                        && differentCase(piece, deskIterator.getPieceAt(fireWest))) {

                    final String destination = deskIterator.getSquareAt(fireWest);
                    if (destination.endsWith(String.valueOf(finalRowNumber))) {
                        for (char transform : possibleTransforms) {
                            result.add(new MoveImpl(square, destination, transform));
                        }
                    }
                    else {
                        result.add(new MoveImpl(square, destination));
                    }
                }
                //en passant
                if (possibleEnPassantField != null) {
                    if ((deskIterator.hasNext(fireEast) && deskIterator.getSquareAt(fireEast).equals(possibleEnPassantField))
                            || (deskIterator.hasNext(fireWest) && deskIterator.getSquareAt(fireWest).equals(possibleEnPassantField))) {
                        result.add(new MoveImpl(square, possibleEnPassantField));
                    }
                }
                return result;
            case 'k':
                for (MoveVector mVector : ALL_KING_MOVES) {
                    addIfEmptyOrAnotherColor(square, mVector, result);
                }
                //At first we should do quick inspection
                if (k
                        && desk.getPieceAt("F8") == EMPTY_CELL_CHAR
                        && desk.getPieceAt("G8") == EMPTY_CELL_CHAR
                        && !isAttacked(true, square)
                        && !isAttacked(true, "F8")
                        && !isAttacked(true, "G8")) {
                    result.add(new MoveImpl(square, "G8"));
                }
                if (q
                        && desk.getPieceAt("F8") == EMPTY_CELL_CHAR
                        && desk.getPieceAt("C8") == EMPTY_CELL_CHAR
                        && desk.getPieceAt("B8") == EMPTY_CELL_CHAR
                        && !isAttacked(true, square)
                        && !isAttacked(true, "D8")
                        && !isAttacked(true, "C8")) {
                    result.add(new MoveImpl(square, "C8"));
                }
                return result;
            case 'K':
                for (MoveVector mVector : ALL_KING_MOVES) {
                    addIfEmptyOrAnotherColor(square, mVector, result);
                }
                //At first we should do quick inspection
                if (K
                        && desk.getPieceAt("F1") == EMPTY_CELL_CHAR
                        && desk.getPieceAt("G1") == EMPTY_CELL_CHAR
                        && !isAttacked(false, square)
                        && !isAttacked(false, "F1")
                        && !isAttacked(false, "G1")) {
                    result.add(new MoveImpl(square, "G1"));
                }
                if (Q
                        && desk.getPieceAt("F1") == EMPTY_CELL_CHAR
                        && desk.getPieceAt("C1") == EMPTY_CELL_CHAR
                        && desk.getPieceAt("B1") == EMPTY_CELL_CHAR
                        && !isAttacked(false, square)
                        && !isAttacked(false, "D1")
                        && !isAttacked(false, "C1")) {
                    result.add(new MoveImpl(square, "C1"));
                }
                return result;
            default:
                //we never can be here
                return null;
        }
    }


    @Override
    public Position copy()
    {
        PositionImpl result = new PositionImpl(desk.copy());
        result.activePlayer = this.activePlayer;
        result.K = this.K;
        result.Q = this.Q;
        result.k = this.k;
        result.q = this.q;
        result.possibleEnPassantField = this.possibleEnPassantField;

        result.halfMoves = this.halfMoves;
        result.moves = this.moves;

        return result;
    }

    private static class Memento
    {
        public boolean activePlayer;
        public boolean K;
        public boolean Q;
        public boolean k;
        public boolean q;
        public String possibleEnPassantField;
        public Desk desk;
        public int halfMoves;
        public int moves;
    }


    private void toMemento()
    {
        if (memento != null) {
            throw new IllegalStateException("Cannt put data to memento, it is not empty!");
        }
        memento = new Memento();
        memento.activePlayer = this.activePlayer;
        memento.K = this.K;
        memento.Q = this.Q;
        memento.k = this.k;
        memento.q = this.q;
        memento.possibleEnPassantField = this.possibleEnPassantField;
        memento.desk = this.desk.copy();
        memento.halfMoves = this.halfMoves;
        memento.moves = this.moves;
    }


    private void fromMemento()
    {
        this.activePlayer = memento.activePlayer;
        this.K = memento.K;
        this.Q = memento.Q;
        this.k = memento.k;
        this.q = memento.q;
        this.possibleEnPassantField = memento.possibleEnPassantField;
        this.desk = memento.desk;
        this.halfMoves = memento.halfMoves;
        this.moves = memento.moves;
        memento = null;
    }
}
