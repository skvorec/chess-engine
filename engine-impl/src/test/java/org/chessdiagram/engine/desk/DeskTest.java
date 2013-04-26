package org.chessdiagram.engine.desk;

import java.util.Arrays;
import java.util.List;
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
        Desk position = dataHelper.getConfiguredDesk();
        Assert.assertEquals(position.display(),
                "rnbqkbnr\n__p_pppp\n__p_ppp_\n________\n________\n________\nRNBQKBN_\n________\n");
        System.out.println(position.display());
    }


    @Test
    public void toFen()
    {
        Desk position = dataHelper.getConfiguredDesk();
        Assert.assertEquals(position.toFen(), "rnbqkbnr/2p1pppp/2p1ppp1/8/8/8/RNBQKBN1/8");
    }


    @Test
    public void getPieceAt()
    {
        Desk position = dataHelper.getConfiguredDesk();
        Assert.assertEquals(position.getPieceAt("A1"), '_');
        Assert.assertEquals(position.getPieceAt("B2"), 'N');
        Assert.assertEquals(position.getPieceAt("H8"), 'r');
    }


    @Test(expectedExceptions = {EngineException.class})
    public void getPieceAtException()
    {
        Desk position = dataHelper.getConfiguredDesk();
        position.getPieceAt("A9");
    }


    @Test
    public void findPieceSquares()
    {
        Desk position = dataHelper.getConfiguredDesk();
        List<String> squares = position.findPieceSquares('k');
        Assert.assertEquals(squares, Arrays.asList("E8"));
        squares = position.findPieceSquares('P');
        Assert.assertTrue(squares.isEmpty());
        squares = position.findPieceSquares('N');
        Assert.assertEquals(squares, Arrays.asList("B2", "G2"));
    }


    @Test
    public void copy()
    {
        Desk position = dataHelper.getConfiguredDesk();
        Desk copy = position.copy();
        Assert.assertEquals(position.toFen(), copy.toFen());
        position.setPieceAt("A1", 'P');
        Assert.assertNotEquals(position.toFen(), copy.toFen());
        copy = position.copy();
        Assert.assertEquals(position.toFen(), copy.toFen());
        copy.setPieceAt("B1", 'P');
        Assert.assertNotEquals(position.toFen(), copy.toFen());
    }
}