package org.chessdiagram.engine;

/**
 *
 */
public interface Move
{
    String getFrom();


    String getDestination();


    char getPromotion();
}