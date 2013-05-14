package org.chessdiagram.engine.desk;

import org.chessdiagram.engine.EngineException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class DeskTest
{
    private final DeskDataHelper dataHelper = new DeskDataHelper();


    @Test
    public void display()
    {
        Desk desk = dataHelper.getConfiguredDesk();
        Assert.assertEquals(desk.display(),
                "rnbqkbnr\n__p_pppp\n__p_ppp_\n________\n________\n________\nRNBQKBN_\n________\n");
        System.out.println(desk.display());
    }


    @Test
    public void toFen()
    {
        Desk desk = dataHelper.getConfiguredDesk();
        Assert.assertEquals(desk.toFen(), "rnbqkbnr/2p1pppp/2p1ppp1/8/8/8/RNBQKBN1/8");
    }


    @Test
    public void getPieceAt()
    {
        Desk desk = dataHelper.getConfiguredDesk();
        Assert.assertEquals(desk.getPieceAt("A1"), '_');
        Assert.assertEquals(desk.getPieceAt("B2"), 'N');
        Assert.assertEquals(desk.getPieceAt("H8"), 'r');
    }


    @Test(expectedExceptions = {EngineException.class})
    public void getPieceAtException()
    {
        Desk desk = dataHelper.getConfiguredDesk();
        desk.getPieceAt("A9");
    }


    @Test
    public void copy()
    {
        Desk desk = dataHelper.getConfiguredDesk();
        Desk copy = desk.copy();
        Assert.assertEquals(desk.toFen(), copy.toFen());
        desk.setPieceAt("A1", 'P');
        Assert.assertNotEquals(desk.toFen(), copy.toFen());
        copy = desk.copy();
        Assert.assertEquals(desk.toFen(), copy.toFen());
        copy.setPieceAt("B1", 'P');
        Assert.assertNotEquals(desk.toFen(), copy.toFen());
    }


    @Test
    public void rollBack()
    {
        Desk desk = dataHelper.getConfiguredDesk();
        desk.setPieceAt("A8", 'R');
        desk.setPieceAt("B8", 'N');

        Assert.assertEquals(desk.getPieceAt("A8"), 'R');
        Assert.assertEquals(desk.getPieceAt("B8"), 'N');

        desk.rollBack();

        Assert.assertEquals(desk.getPieceAt("A8"), 'r');
        Assert.assertEquals(desk.getPieceAt("B8"), 'n');
    }
}