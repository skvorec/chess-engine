package org.chessdiagram.engine.evaluate;

import java.util.Map;
import org.chessdiagram.engine.Move;
import org.chessdiagram.engine.Position;

/**
 *
 */
public class SearchService
{
    private final int depth;
    private final EvaluationService evaluationService = new EvaluationService();
    private int ply;


    public SearchService(int depth)
    {
        this.depth = depth;
    }


    public Move findBestMove(final Position position)
    {
        ScoreAndMove bestResult = alphaBetaPruning(position, -EvaluationService.CHECKMATE_VALUE, EvaluationService.CHECKMATE_VALUE, depth);
        return bestResult.move;
    }


    /**
     * The highest score for the best move
     */
    protected ScoreAndMove alphaBetaPruning(Position position, int alpha, int beta, int depth)
    {
        int value;
        Map<Move, Position> legalMoves = position.getLegalMovesHash();
        Move toReturn = null;
        for (Move move : legalMoves.keySet()) {
            Position afterMove = legalMoves.get(move);

            ply++;
            if (depth > 0) {
                value = -alphaBetaPruning(afterMove, -beta, -alpha, depth - 1).score;
            }
            else {
                value = evaluationService.evaluate(afterMove);
                if (afterMove.getActivePlayer()) {
                    value *= -1;
                }
            }

            //undo move
            ply--;

            if (value > alpha) {
                alpha = value;
                toReturn = move;
            }
            if (value >= beta) {
                return new ScoreAndMove(beta, move);
            }
        }

        if (legalMoves.isEmpty()) {
            // if no moves, than position is checkmate or stealmate
            if (position.isCheck(position.getActivePlayer())) {
                return new ScoreAndMove(-EvaluationService.CHECKMATE_VALUE + ply, null);
            }
            else {
                return new ScoreAndMove(0, null);
            }
        }

        return new ScoreAndMove(alpha, toReturn);
    }

    protected static class ScoreAndMove
    {
        int score;
        Move move;


        public ScoreAndMove(int score, Move move)
        {
            this.score = score;
            this.move = move;
        }
    }
}