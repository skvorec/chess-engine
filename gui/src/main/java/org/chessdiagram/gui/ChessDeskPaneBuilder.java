package org.chessdiagram.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import org.chessdiagram.engine.desk.Desk;
import org.chessdiagram.engine.desk.DeskIterator;

/**
 *
 */
public class ChessDeskPaneBuilder
{
    private Desk desk;


    public void setDesk(Desk desk)
    {
        this.desk = desk;
    }


    private Pane blackCellPane()
    {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #d18b47;");
        return pane;
    }


    private Pane whiteCellPane()
    {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #ffce9e;");
        return pane;
    }


    private Label centredLabel(String text)
    {
        return new Label("      " + text);
    }


    public GridPane build()
    {
        GridPane result = new GridPane();       
        result.setAlignment(Pos.TOP_CENTER);
        DeskIterator deskIterator = desk.deskIterator("A1");
        ImageFactory factory = new ImageFactory();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                Pane pane;
                if ((i + j) % 2 == 1) {
                    pane = blackCellPane();
                }
                else {
                    pane = whiteCellPane();
                }
                char pieceCode = desk.getPieceAt(deskIterator.currentPosition());
                if (deskIterator.hasNext()) {
                    deskIterator.next();
                }
                ImageView view = new ImageView();
                final Image createByFen = factory.createByFen(pieceCode);
                if (createByFen != null) {
                    view.setImage(createByFen);
                    pane.getChildren().add(view);
                }

                result.add(pane, j + 2, i);
            }
        }

        result.add(new Label("8"), 1, 0);
        result.add(new Label("7"), 1, 1);
        result.add(new Label("6"), 1, 2);
        result.add(new Label("5"), 1, 3);
        result.add(new Label("4"), 1, 4);
        result.add(new Label("3"), 1, 5);
        result.add(new Label("2"), 1, 6);
        result.add(new Label("1"), 1, 7);


        result.getColumnConstraints().add(new ColumnConstraints(25.0));
        result.getColumnConstraints().add(new ColumnConstraints(20.0));
        for (int i = 0; i < 8; i++) {
            result.getColumnConstraints().add(new ColumnConstraints(50.0));
            result.getRowConstraints().add(new RowConstraints(50.0));
        }

        result.add(centredLabel("A"), 2, 8);
        result.add(centredLabel("B"), 3, 8);
        result.add(centredLabel("C"), 4, 8);
        result.add(centredLabel("D"), 5, 8);
        result.add(centredLabel("E"), 6, 8);
        result.add(centredLabel("F"), 7, 8);
        result.add(centredLabel("G"), 8, 8);
        result.add(centredLabel("H"), 9, 8);


        return result;
    }
}
