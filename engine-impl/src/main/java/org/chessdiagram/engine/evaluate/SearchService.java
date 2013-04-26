package org.chessdiagram.engine.evaluate;

import java.util.List;
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
        Position positionForModify = position.copy();

        List<Move> legalMoves = positionForModify.getLegalMoves();

        Move bestMove = legalMoves.isEmpty() ? null : legalMoves.get(0);
        int bestValue = -EvaluationService.CHECKMATE_VALUE;

//        System.out.println("Finding best move for position:");
//        System.out.println(position.display());

        for (Move move : legalMoves) {
//            System.out.println("__________________________________________");
            positionForModify.makeMove(move);
//            System.out.println("-evaluation position after move: " + move);
            int score = -alphaBetaPruning(positionForModify,
                    -EvaluationService.CHECKMATE_VALUE, EvaluationService.CHECKMATE_VALUE, depth);
//            System.out.println("-score is " + score);
            if (score > bestValue) {
                bestValue = score;
                bestMove = move;
            }
            positionForModify = position.copy();
        }
        return bestMove;
    }


    /**
     * The highest score for the best move
     */
    protected int alphaBetaPruning(Position position, int alpha, int beta, int depth)
    {
        String color = position.getActivePlayer() ? "white" : "black";
//        System.out.println("--inputting alpha beta pruning, color=" + color);
        int value;
        List<Move> legalMoves = position.getLegalMoves();
        final Position copiedPosition = position.copy();
        for (Move move : legalMoves) {
            position.makeMove(move);
//            String color2 = position.getActivePlayer() ? "white" : "black";
//            System.out.println("--processing move = " + move);
//            System.out.println("--now we evaluating position with active player " + color2);
//            System.out.println(position.display());
            ply++;
            if (depth > 0) {
                value = -1 * alphaBetaPruning(position, -beta, -alpha, depth - 1);
            }
            else {
                value = evaluationService.evaluate(position);
                if (position.getActivePlayer()) {
                    value *= -1;
                }
            }

            //undo move
            position = copiedPosition.copy();
            ply--;

            if (value > alpha) {
                alpha = value;
            }
            if (value >= beta) {
//                System.out.println("--returning beta, color=" + color + " score = " + beta);
                return beta;
            }
        }

        if (legalMoves.isEmpty()) {

            // if no moves, than position is checkmate or stealmate
            if (position.isCheck()) {
//                System.out.println("--returning checkmate, color=" + color + " score = " + (-EvaluationService.CHECKMATE_VALUE + ply));
                return -EvaluationService.CHECKMATE_VALUE + ply;
            }
            else {
//                System.out.println("--returning stealmate, color=" + color + " score = " + 0);
                return 0;
            }
        }

//        System.out.println("--returning alpha, color=" + color + " score = " + alpha);
        return alpha;
    }
}