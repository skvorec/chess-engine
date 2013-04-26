package org.chessdiagram.engine.desk;

/**
 *
 */
public class MoveVectorImpl implements MoveVector
{
    public static final MoveVector NORTH = new MoveVectorImpl(-1, 0);
    public static final MoveVector SOUTH = new MoveVectorImpl(1, 0);
    public static final MoveVector WEST = new MoveVectorImpl(0, -1);
    public static final MoveVector EAST = new MoveVectorImpl(0, 1);
    public static final MoveVector NORTH_WEST = NORTH.plus(WEST);
    public static final MoveVector NORTH_EAST = NORTH.plus(EAST);
    public static final MoveVector SOUTH_WEST = SOUTH.plus(WEST);
    public static final MoveVector SOUTH_EAST = SOUTH.plus(EAST);
    private final int rowNum;
    private final int columnNum;


    /*
     * Private constructor, because no one should know about 2-dimension-array implementation of Desk
     */
    private MoveVectorImpl(int rowNum, int columnNum)
    {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
    }


    /**
     * Getters can be used inside package only
     *
     * @return row num
     */
    protected int getRowNum()
    {
        return rowNum;
    }


    protected int getColumnNum()
    {
        return columnNum;
    }


    @Override
    public MoveVector negate()
    {
        return new MoveVectorImpl(-rowNum, -columnNum);
    }


    @Override
    public MoveVector plus(MoveVector another)
    {
        MoveVectorImpl anotherImpl = (MoveVectorImpl) another;
        return new MoveVectorImpl(
                rowNum + anotherImpl.getRowNum(), columnNum + anotherImpl.getColumnNum());
    }
}