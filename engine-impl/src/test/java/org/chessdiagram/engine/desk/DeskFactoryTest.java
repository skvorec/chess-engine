package org.chessdiagram.engine.desk;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class DeskFactoryTest
{
    @Test
    public void fromFen()
    {
        DeskFactory factory = new DeskFactory();
        final String fenRow = "rnbqkbnr/1p1ppp2/8/8/8/8/PPPPPPPP/RNBQKBNR";
        Desk desk = factory.fromFen(fenRow);
        Assert.assertEquals(fenRow, desk.toFen());
    }
}