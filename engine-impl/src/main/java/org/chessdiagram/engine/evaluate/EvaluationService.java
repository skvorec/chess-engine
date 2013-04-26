package org.chessdiagram.engine.evaluate;

import org.chessdiagram.engine.Position;
import org.chessdiagram.engine.desk.Desk;
import org.chessdiagram.engine.desk.DeskIterator;

/**
 *
 */
public class EvaluationService
{
    private PiecePositionEvaluateService positionEvaluationService = new PiecePositionEvaluateService();
    private PieceNameEvaluationService pieceNameEvaluationService = new PieceNameEvaluationService();
    public static final int CHECKMATE_VALUE = 10000;


    //the more the better for white
    public int evaluate(Position position)
    {
        int result = 0;
        Desk desk = position.getDesk();
        DeskIterator deskIterator = desk.deskIterator("A1");
        final char theFirstPiece = desk.getPieceAt("A1");
        result += positionEvaluationService.evaluate("A1", theFirstPiece);
        result += pieceNameEvaluationService.evaluate(desk.getPieceAt("A1"));
        while (deskIterator.hasNext()) {
            char piece = deskIterator.next();
            result += positionEvaluationService.evaluate(deskIterator.currentPosition(), piece);
            result += pieceNameEvaluationService.evaluate(piece);
        }
        return result;
    }
}