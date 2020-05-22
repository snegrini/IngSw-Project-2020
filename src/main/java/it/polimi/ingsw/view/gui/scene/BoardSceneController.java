package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.observer.ViewObservable;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class BoardSceneController extends ViewObservable implements GenericSceneController {

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
            if (availablePositionClicks == 1) { // Last click done.
                availablePositionClicks = 0;
                clickedPositionList.add(new Position(row, col));
                clickedNode.setDisable(true);
                clickedNode.getStyleClass().add("worker");
                Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateInitWorkerPosition(clickedPositionList)));
            } else if (availablePositionClicks > 1) {
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
}
