package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.view.View;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.List;

public class BoardSceneController implements ViewGuiController {

    private View view;

    @FXML
    private GridPane boardGrid;

    @FXML
    public void initialize() {
        boardGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onSpaceClick(event));
    }

    private void onSpaceClick(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        Integer row = GridPane.getRowIndex(clickedNode);
        Integer col = GridPane.getColumnIndex(clickedNode);

        System.out.print("Row: " + row);
        System.out.println(", Col: " + col);
    }

    public void enableSpaces(List<Position> positionList) {
        ObservableList<Node> spaceList = boardGrid.getChildren();
        for (Node space : spaceList) {
            boardGrid.getRowIndex(space);
        }
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }
}
