package org.chessdiagram.engine.desk;

/**
 *
 */
public interface DeskIterator
{
    /**
     * Iteration from left to right, from bottom to top. So, if we start at A1 than finally we reach H8
     *
     * @return has next cell or not
     */
    boolean hasNext();


    boolean hasNext(MoveVector mVector);


    char next();


    /**
     *
     * @param mVector
     * @return piece and moves to new square
     */
    char next(MoveVector mVector);


    /**
     *
     * @param mVector
     * @return piece but does not move to new square
     */
    char getPieceAt(MoveVector mVector);


    /**
     *
     * @param mVector
     * @return square coordinates, like A1 or H8
     */
    String getSquareAt(MoveVector mVector);


    String currentPosition();
}