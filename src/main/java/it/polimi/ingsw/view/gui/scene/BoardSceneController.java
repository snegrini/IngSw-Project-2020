package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class BoardSceneController implements ViewGuiController {

    private View view;

    private int availablePositionClicks;
    private List<Position> clickedPositionList;

    @FXML
    private GridPane boardGrid;

    public BoardSceneController() {
        availablePositionClicks = 0;
        clickedPositionList = new ArrayList<>();
    }

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

        if (row != null && col != null) {
            // Notify views only when all the required positions have been selected.
            if (availablePositionClicks - 1 == 0) {
                availablePositionClicks = -1; // disable
                Platform.runLater(() -> view.notifyObserver(obs -> obs.onUpdateInitWorkerPosition(clickedPositionList)));
            } else {
                clickedPositionList.add(new Position(row, col));
                availablePositionClicks--;
            }
        }
    }

    public void setAvailablePositionClicks(int availablePositionClicks) {
        this.availablePositionClicks = availablePositionClicks;
    }

    public void setEnabledSpaces(List<Position> positionList) {
        ObservableList<Node> spaceList = boardGrid.getChildren();
        for (Node space : spaceList) {
            Position tempPos = new Position(GridPane.getRowIndex(space), GridPane.getColumnIndex(space));
            if (positionList.contains(tempPos)) {
                space.setDisable(false);
            }
        }
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }
}
