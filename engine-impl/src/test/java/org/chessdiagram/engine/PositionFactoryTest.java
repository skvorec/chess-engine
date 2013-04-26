package org.chessdiagram.engine;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class PositionFactoryTest
{
    @Test
    public void fromFen()
    {
        PositionFactory factory = new PositionFactory();
        final String fenRow = "rnbqkbnr/1p1ppp2/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Position position = factory.fromFen(fenRow);
        Assert.assertEquals(fenRow, position.toFen());
    }
}