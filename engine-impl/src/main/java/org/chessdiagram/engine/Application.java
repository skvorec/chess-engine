package org.chessdiagram.engine;

import java.util.Scanner;
import org.chessdiagram.engine.evaluate.SearchService;

/**
 *
 */
public class Application
{
    public void playerVsPlayer(Position position)
    {
        while (true) {
            Scanner sc = new Scanner(System.in);
            String moveAsStr = sc.next();

            Move move = MoveImpl.fromString(moveAsStr);
            if (move == null) {
                System.out.println("incorrect move format: " + moveAsStr);
                continue;
            }
            position.makeMove(move);
            System.out.println(position.display());
        }
    }


    public void playerVsCPU(Position position)
    {
        SearchService service = new SearchService(2);
        System.out.println("Enter color (W/B): ");
        Scanner sc = new Scanner(System.in);
        String color = sc.next();


        if (color.equalsIgnoreCase("W")) {
            System.out.println("Play!");
            while (true) {
                boolean userMadeMove = false;

                while (!userMadeMove) {
                    String moveAsStr = sc.next();

                    Move move = MoveImpl.fromString(moveAsStr);
                    if (move == null) {
                        System.out.println("incorrect move format: " + moveAsStr);
                        continue;
                    }
                    userMadeMove = position.makeMove(move);
                }
                System.out.println(position.display());
                Move bestMove = service.findBestMove(position);
                System.out.println("bestMove: " + bestMove);
                position.makeMove(bestMove);
                System.out.println(position.display());
            }
        }

    }


    public static void main(String[] args)
    {
        Position position = new PositionFactory().fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        System.out.println(position.display());
        Application app = new Application();
        app.playerVsCPU(position);
    }
}