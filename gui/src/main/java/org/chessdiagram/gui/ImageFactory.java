package org.chessdiagram.gui;

import javafx.scene.image.Image;

/**
 *
 */
public class ImageFactory
{
    public Image createByFen(char c)
    {
        switch (c) {
            case 'p':
                return new Image(getClass().getResourceAsStream("/pictures/chess/bp.png"));
            case 'P':
                return new Image(getClass().getResourceAsStream("/pictures/chess/wp.png"));

            case 'r':
                return new Image(getClass().getResourceAsStream("/pictures/chess/br.png"));
            case 'R':
                return new Image(getClass().getResourceAsStream("/pictures/chess/wr.png"));

            case 'n':
                return new Image(getClass().getResourceAsStream("/pictures/chess/bn.png"));
            case 'N':
                return new Image(getClass().getResourceAsStream("/pictures/chess/wn.png"));

            case 'b':
                return new Image(getClass().getResourceAsStream("/pictures/chess/bb.png"));
            case 'B':
                return new Image(getClass().getResourceAsStream("/pictures/chess/wb.png"));

            case 'k':
                return new Image(getClass().getResourceAsStream("/pictures/chess/bk.png"));
            case 'K':
                return new Image(getClass().getResourceAsStream("/pictures/chess/wk.png"));

            case 'q':
                return new Image(getClass().getResourceAsStream("/pictures/chess/bq.png"));
            case 'Q':
                return new Image(getClass().getResourceAsStream("/pictures/chess/wq.png"));
        }

        return null;
    }
}