package org.chessdiagram.engine.desk;

/**
 *
 */
public class DeskDataHelper
{
    public DeskImpl getConfiguredDesk()
    {
        char[][] desk = {
            {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
            {'_', '_', 'p', '_', 'p', 'p', 'p', 'p'},
            {'_', '_', 'p', '_', 'p', 'p', 'p', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_'},
            {'R', 'N', 'B', 'Q', 'K', 'B', 'N', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_'}};
        DeskImpl position = new DeskImpl(desk);
        return position;
    }


    public Desk getDeskForEvaluation()
    {
        char[][] desk = {
            {'_', 'n', 'b', 'q', 'k', 'b', '_', '_'},
            {'_', '_', 'p', '_', 'p', 'p', 'p', 'p'},
            {'_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_'},
            {'_', '_', 'P', '_', 'P', 'P', '_', '_'},
            {'R', 'N', 'B', '_', 'K', '_', 'N', '_'},
            {'_', '_', '_', '_', '_', '_', '_', '_'}};
        DeskImpl position = new DeskImpl(desk);
        return position;
    }
}