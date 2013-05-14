package org.chessdiagram.engine.desk;

import org.chessdiagram.engine.desk.DeskImpl.DeskIteratorImpl;
import static org.chessdiagram.engine.desk.MoveVectorImpl.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class DeskIteratorTest
{
    private final DeskDataHelper dataHelper = new DeskDataHelper();


    @Test
    public void next()
    {
        DeskImpl desk = dataHelper.getConfiguredDesk();
        DeskIterator deskIterator = desk.deskIterator("A1");
        int counter = 0;
        while (deskIterator.hasNext()) {
            deskIterator.next();
            counter++;
        }

        Assert.assertEquals(counter, 63);
        Assert.assertEquals(deskIterator.currentPosition(), "H8");
    }


    @Test
    public void nextWhenDeskIsModified()
    {
        DeskImpl desk = dataHelper.getConfiguredDesk();
        desk.setPieceAt("C3", 'r');
        DeskIterator deskIterator = desk.deskIterator("B3");
        desk.setPieceAt("C3", 'R');

        Assert.assertEquals(deskIterator.next(), 'R');
    }


    @Test
    public void nextVector()
    {
        DeskImpl desk = dataHelper.getConfiguredDesk();
        DeskIterator deskIterator = desk.deskIterator("B3");
        int counter = 0;
        while (deskIterator.hasNext(MoveVectorImpl.EAST)) {
            deskIterator.next(MoveVectorImpl.EAST);
            counter++;
        }
        Assert.assertEquals(counter, 6);
        Assert.assertEquals(deskIterator.currentPosition(), "H3");
    }


    @Test
    public void getPieceAt()
    {
        DeskImpl desk = dataHelper.getConfiguredDesk();
        DeskIterator deskIterator = desk.deskIterator("B3");
        Assert.assertEquals(deskIterator.getPieceAt(SOUTH), 'N');
        Assert.assertEquals(deskIterator.getPieceAt(NORTH.plus(NORTH).plus(NORTH).plus(EAST)), 'p');
    }


    @Test
    public void toSquare()
    {
        DeskImpl desk = dataHelper.getConfiguredDesk();
        DeskIteratorImpl deskIterator = (DeskIteratorImpl) desk.deskIterator("A1");
        Assert.assertEquals(deskIterator.toSquare(0, 1), "B8");
    }
}