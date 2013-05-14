package org.chessdiagram.engine.evaluate;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class PiecePositionEvaluateServiceTest
{
    @Test
    public void tableForBlack()
    {
        PiecePositionEvaluateService service = new PiecePositionEvaluateService();
        int[][] pawnsForBlack = service.tableForBlack(service.pawnWeight);
        int[][] expectedArray = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0},
            {-6, -4, 1, -24, -24, 1, -4, -6},
            {-4, -4, 1, 5, 5, 1, -4, -4},
            {-6, -4, 5, 10, 10, 5, -4, -6},
            {-6, -4, 2, 8, 8, 2, -4, -6},
            {-6, -4, 1, 2, 2, 1, -4, -6},
            {-6, -4, 1, 1, 1, 1, -4, -6},
            {0, 0, 0, 0, 0, 0, 0, 0}
        };

        int[][] initialArray = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0},
            {-6, -4, 1, 1, 1, 1, -4, -6},
            {-6, -4, 1, 2, 2, 1, -4, -6},
            {-6, -4, 2, 8, 8, 2, -4, -6},
            {-6, -4, 5, 10, 10, 5, -4, -6},
            {-4, -4, 1, 5, 5, 1, -4, -4},
            {-6, -4, 1, -24, -24, 1, -4, -6},
            {0, 0, 0, 0, 0, 0, 0, 0}
        };

        Assert.assertEquals(initialArray, service.pawnWeight);
        Assert.assertEquals(expectedArray, pawnsForBlack);
    }


    @Test
    public void evaluate()
    {
        PiecePositionEvaluateService service = new PiecePositionEvaluateService();
        Assert.assertEquals(service.evaluate("E2", 'P'), -24);
        Assert.assertEquals(service.evaluate("E2", 'p'), 1);
        Assert.assertEquals(service.evaluate("E2", 'N'), 2);
        Assert.assertEquals(service.evaluate("H4", 'N'), -8);
        Assert.assertEquals(service.evaluate("H4", 'n'), -8);
        Assert.assertEquals(service.evaluate("G8", 'n'), -12);
    }
}