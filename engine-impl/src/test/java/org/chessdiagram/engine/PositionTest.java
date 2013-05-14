package org.chessdiagram.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.chessdiagram.engine.desk.DeskDataHelper;
import static org.chessdiagram.engine.desk.MoveVectorImpl.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class PositionTest
{
    private static final PositionFactory POSITION_FACTORY = new PositionFactory();


    private PositionImpl getConfiguredPosition()
    {
        PositionImpl position = new PositionImpl(new DeskDataHelper().getConfiguredDesk());
        position.K = true;
        return position;
    }


    @Test
    public void display()
    {
        PositionImpl position = getConfiguredPosition();
        Assert.assertEquals(position.display(),
                "rnbqkbnr\n__p_pppp\n__p_ppp_\n________\n________\n________\nRNBQKBN_\n________\n\nK=true Q=false k=false q=false ep=null");
    }


    @Test
    public void toFen()
    {
        PositionImpl position = getConfiguredPosition();
        Assert.assertEquals(position.toFen(), "rnbqkbnr/2p1pppp/2p1ppp1/8/8/8/RNBQKBN1/8 b K--- - 0 0");
    }


    @Test
    public void addIfEmptyOrAnotherColor()
    {
        List<Move> result = new ArrayList<>();
        PositionImpl position = getConfiguredPosition();

        position.addIfEmptyOrAnotherColor("B2", NORTH.plus(NORTH).plus(NORTH).plus(NORTH).plus(EAST), result);
        Assert.assertEquals(result, Arrays.asList(new MoveImpl("B2", "C6")));
        position.addIfEmptyOrAnotherColor("B2", NORTH.plus(NORTH).plus(NORTH).plus(NORTH).plus(WEST), result);
        Assert.assertEquals(result, Arrays.asList(new MoveImpl("B2", "C6"), new MoveImpl("B2", "A6")));
        position.addIfEmptyOrAnotherColor("B2", SOUTH.plus(SOUTH).plus(SOUTH), result);
        Assert.assertEquals(result, Arrays.asList(new MoveImpl("B2", "C6"), new MoveImpl("B2", "A6")));
    }


    @Test
    public void canMoveAlongVector()
    {
        PositionImpl position = getConfiguredPosition();
        List<Move> squares = position.canMoveAlongVector("A2", NORTH);
        Assert.assertEquals(squares, Arrays.asList(
                new MoveImpl("A2", "A3"), new MoveImpl("A2", "A4"), new MoveImpl("A2", "A5"), new MoveImpl("A2", "A6"), new MoveImpl("A2", "A7"), new MoveImpl("A2", "A8")));
        squares = position.canMoveAlongVector("D8", SOUTH);
        Assert.assertEquals(squares, Arrays.asList(
                new MoveImpl("D8", "D7"), new MoveImpl("D8", "D6"), new MoveImpl("D8", "D5"), new MoveImpl("D8", "D4"), new MoveImpl("D8", "D3"), new MoveImpl("D8", "D2")));
        squares = position.canMoveAlongVector("C8", SOUTH_EAST);
        Assert.assertEquals(squares, Arrays.asList(new MoveImpl("C8", "D7")));
        squares = position.canMoveAlongVector("F2", NORTH_EAST);
        Assert.assertEquals(squares, Arrays.asList(new MoveImpl("F2", "G3"), new MoveImpl("F2", "H4")));
    }


    @Test
    public void getLegalMovesHashKingOnLastLine()
    {
        Position position = POSITION_FACTORY.fromFen("4k3/7R/8/1R6/3K4/8/8/8 b ---- - 30 30");

        final Map<Move, Position> legalMovesHash = position.getLegalMovesHash();
        Assert.assertEquals(legalMovesHash.size(), 2);
        Assert.assertEquals(
                legalMovesHash.get(MoveImpl.fromString("E8-F8")).toFen(), "5k2/7R/8/1R6/3K4/8/8/8 w ---- - 31 31");
        Assert.assertEquals(
                legalMovesHash.get(MoveImpl.fromString("E8-D8")).toFen(), "3k4/7R/8/1R6/3K4/8/8/8 w ---- - 31 31");
    }


    @Test
    public void getLegalMovesHashNotOnlyKingMovesShouldBe()
    {
        PositionImpl position = (PositionImpl) new PositionFactory().fromFen("8/2r5/5k2/8/8/4N3/4Kb2/8 w ---- - 30 30");
        System.out.println(position.display());
        Map<Move, Position> legalMoves = position.getLegalMovesHash();

        Assert.assertTrue(legalMoves.keySet().contains(MoveImpl.fromString("E3-D5")));
        Assert.assertTrue(legalMoves.keySet().contains(MoveImpl.fromString("E2-F2")));
        Assert.assertFalse(legalMoves.keySet().contains(MoveImpl.fromString("E2-E1")));
        Assert.assertEquals(legalMoves.size(), 14);

    }


    @Test
    public void isAttacked()
    {
        PositionImpl position = (PositionImpl) new PositionFactory().fromFen("8/2r5/5k2/3N4/8/8/4Kb2/8 w ---- - 30 30");
        Assert.assertFalse(position.isAttacked(false, "E2"));

        position = (PositionImpl) new PositionFactory().fromFen("rnbk1bnr/pp1pp1pp/8/2p1Np2/P3P3/1PNB4/2PP1P1K/R1BQ2R1 b ---- - 0 1");
        System.out.println(position.display());
        Assert.assertFalse(position.isAttacked(false, "H2"));
    }


    @Test
    public void attackAlongVector()
    {
        PositionImpl position = (PositionImpl) new PositionFactory().fromFen(
                "rnbk1bnr/pp1pp1pp/8/2p1Np2/P3P3/1PNB4/2PP1P1K/R1BQ2R1 b ---- - 0 1");
        Assert.assertNull(position.attackAlongVector("H8", SOUTH));
        Assert.assertEquals(position.attackAlongVector("G1", NORTH), "G7");
    }


    @Test
    public void enPassant()
    {
        Position position = new PositionFactory().fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        position.makeMove(MoveImpl.fromString("E2-E4"));
        position.makeMove(MoveImpl.fromString("B8-C6"));
        position.makeMove(MoveImpl.fromString("E4-E5"));
        position.makeMove(MoveImpl.fromString("D7-D5"));
        position.makeMove(MoveImpl.fromString("E5-D6"));

        Assert.assertEquals(position.toFen(), "r1bqkbnr/ppp1pppp/2nP4/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 3");
    }


    @Test
    public void promotionWhenFire()
    {
        Position position = new PositionFactory().fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        position.makeMove(MoveImpl.fromString("E2-E4"));
        position.makeMove(MoveImpl.fromString("B8-C6"));
        position.makeMove(MoveImpl.fromString("E4-E5"));
        position.makeMove(MoveImpl.fromString("D7-D5"));
        position.makeMove(MoveImpl.fromString("E5-E6"));
        position.makeMove(MoveImpl.fromString("D5-D4"));
        position.makeMove(MoveImpl.fromString("E6-F7"));
        position.makeMove(MoveImpl.fromString("E8-D7"));
        position.makeMove(MoveImpl.fromString("F7-G8=Q"));

        Assert.assertEquals(position.toFen(), "r1bq1bQr/pppkp1pp/2n5/8/3p4/8/PPPP1PPP/RNBQKBNR b KQ-- - 0 5");
    }


    @Test
    public void copy()
    {
        Position position = new PositionFactory().fromFen("K6k/RR6/PP6/8/8/8/8/8 w ---- - 30 30");
        Position initialPosition = position.copy();
        Assert.assertEquals(position.toFen(), initialPosition.toFen());
        position.makeMove(MoveImpl.fromString("B7-D7"));
        position.makeMove(MoveImpl.fromString("H8-G8"));

        Assert.assertNotEquals(position.toFen(), initialPosition.toFen());
    }
}
