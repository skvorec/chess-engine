package org.chessdiagram.engine;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class MoveTest
{
    @Test
    public void fromString()
    {
        Move move = MoveImpl.fromString("E2-E4");
        Assert.assertEquals(move.getFrom(), "E2");
        Assert.assertEquals(move.getDestination(), "E4");
        Assert.assertEquals(move.getPromotion(), MoveImpl.NULL_CHAR);

        move = MoveImpl.fromString("D2-D4=Q");
        Assert.assertEquals(move.getFrom(), "D2");
        Assert.assertEquals(move.getDestination(), "D4");
        Assert.assertEquals(move.getPromotion(), 'Q');
    }


    @Test
    public void fromStringWrongFormat()
    {
        Move move = MoveImpl.fromString("E2-E9");
        Assert.assertNull(move);
    }
}