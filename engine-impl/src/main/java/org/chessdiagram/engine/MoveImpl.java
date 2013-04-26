package org.chessdiagram.engine;

/**
 *
 */
public class MoveImpl implements Move
{
    //when no toPiece is provided
    public static final char NULL_CHAR = '!';
    private final String from;
    private final String destination;
    /**
     * code of piece when pawn reaches the final line
     */
    private final char promotion;


    public MoveImpl(String from, String to)
    {
        this(from, to, NULL_CHAR);
    }


    public MoveImpl(String from, String destination, char promotion)
    {
        this.from = from;
        this.destination = destination;
        this.promotion = promotion;
    }


    @Override
    public String getFrom()
    {
        return from;
    }


    @Override
    public String getDestination()
    {
        return destination;
    }


    @Override
    public char getPromotion()
    {
        return promotion;
    }


    @Override
    public String toString()
    {
        if (promotion == NULL_CHAR) {
            return from + "-" + destination;
        }
        else {
            return from + "-" + destination + "=" + promotion;
        }
    }


    public static Move fromString(String moveAsString)
    {
        if (!moveAsString.matches("[A-H][1-8]\\-[A-H][1-8]=?[NnBbRrQq]?")) {
            return null;
        }
        String initial = moveAsString.substring(0, 2);
        String destination = moveAsString.substring(3, 5);

        char newFigure = MoveImpl.NULL_CHAR;
        if (moveAsString.length() > 6) {
            newFigure = moveAsString.charAt(6);
        }

        return new MoveImpl(initial, destination, newFigure);
    }


    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + (this.from != null ? this.from.hashCode() : 0);
        hash = 67 * hash + (this.destination != null ? this.destination.hashCode() : 0);
        hash = 67 * hash + this.promotion;
        return hash;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MoveImpl other = (MoveImpl) obj;
        if ((this.from == null) ? (other.from != null) : !this.from.equals(other.from)) {
            return false;
        }
        if ((this.destination == null) ? (other.destination != null) : !this.destination.equals(other.destination)) {
            return false;
        }
        if (this.promotion != other.promotion) {
            return false;
        }
        return true;
    }
}