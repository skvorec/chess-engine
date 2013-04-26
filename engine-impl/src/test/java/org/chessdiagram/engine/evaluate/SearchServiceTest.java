package org.chessdiagram.engine.evaluate;

import org.chessdiagram.engine.Move;
import org.chessdiagram.engine.MoveImpl;
import org.chessdiagram.engine.Position;
import org.chessdiagram.engine.PositionFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class SearchServiceTest
{
    @Test
    public void simpleLinearMate()
    {
        Position position = new PositionFactory().fromFen("K6k/RR6/8/8/8/8/8/8 w ---- - 30 30");
        SearchService service = new SearchService(3);
        Move bestMove = service.findBestMove(position);

        Assert.assertEquals(bestMove, MoveImpl.fromString("B7-B8"));
        Assert.assertEquals(position.toFen(), "K6k/RR6/8/8/8/8/8/8 w ---- - 30 30");
    }


    @Test
    public void simpleFork()
    {
        Position position = new PositionFactory().fromFen("8/2r5/5k2/8/8/4N3/4Kb2/8 w ---- - 30 30");

        SearchService service = new SearchService(3);
        Move bestMove = service.findBestMove(position);
        Assert.assertEquals(bestMove, MoveImpl.fromString("E3-D5"));
    }
}
