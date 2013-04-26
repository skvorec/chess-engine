package org.chessdiagram.engine.desk;

/**
 *
 */
public interface MoveVector
{
    MoveVector plus(MoveVector another);


    MoveVector negate();
}