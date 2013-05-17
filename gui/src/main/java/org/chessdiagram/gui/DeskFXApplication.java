package org.chessdiagram.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.chessdiagram.engine.Move;
import org.chessdiagram.engine.MoveImpl;
import org.chessdiagram.engine.Position;
import org.chessdiagram.engine.PositionFactory;
import org.chessdiagram.engine.evaluate.SearchService;

/**
 * Demonstrates a drag-and-drop feature.
 */
public class DeskFXApplication extends Application
{
    private final Position currentPosition =
            new PositionFactory().fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    private final BorderPane borderpane = new BorderPane();


    private static GridPane getDesk(Position position)
    {
        ChessDeskPaneBuilder builder = new ChessDeskPaneBuilder();
        builder.setDesk(position.getDesk());
        return builder.build();
    }


    private GridPane getTextField()
    {
        GridPane grid = new GridPane();

        TextField moveTextField = new TextField();
        moveTextField.setPromptText("Enter your move");
        moveTextField.setPrefColumnCount(10);
        moveTextField.getText();
        GridPane.setConstraints(moveTextField, 0, 0);
        grid.getChildren().add(moveTextField);
        //Defining the Submit button
        Button submit = new Button("Submit");
        submit.setOnAction(new SubmitEventHandler(moveTextField, borderpane, currentPosition));
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);
        return grid;
    }


    @Override
    public void start(Stage stage)
    {
        Group root = new Group();
        Scene scene = new Scene(root, 500, 500);
        GridPane currentDesk = getDesk(currentPosition);

        borderpane.setCenter(currentDesk);
        borderpane.setBottom(getTextField());

        root.getChildren().add(borderpane);

        stage.setTitle("Desk FX Application");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args)
    {
        Application.launch(args);
    }

    private static class SubmitEventHandler implements EventHandler<ActionEvent>
    {
        private volatile SearchService service = new SearchService(5);
        private volatile TextField moveTextField;
        private volatile BorderPane borderpane;
        private volatile Position position;


        public SubmitEventHandler(TextField moveTextField, BorderPane borderpane, Position currentPosition)
        {
            this.moveTextField = moveTextField;
            this.borderpane = borderpane;
            this.position = currentPosition;
        }


        @Override
        public void handle(ActionEvent e)
        {
            final String moveAsStr = moveTextField.getText();
            if (moveAsStr == null || moveAsStr.isEmpty()) {
                return;
            }
            moveTextField.setText("");
            Move move = MoveImpl.fromString(moveAsStr.toUpperCase());
            if (move == null) {
                System.out.println("incorrect move format: " + moveAsStr);
                return;
            }

            boolean moveDone = position.makeMove(move);
            if (!moveDone) {
                return;
            }

            borderpane.setCenter(getDesk(position));
            moveTextField.setEditable(false);
            System.out.println("position fen: " + position.toFen());

            System.out.println("Searching The Best Move");
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    Move bestMove = service.findBestMove(position);
                    System.out.println("The Best Move: " + bestMove);
                    position.makeMove(bestMove);

                    borderpane.setCenter(getDesk(position));
                    moveTextField.setEditable(true);
                    System.out.println("position fen: " + position.toFen());
                }
            };

            Platform.runLater(runnable);
        }
    }
}
