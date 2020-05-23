package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.player.ReducedWorker;
import it.polimi.ingsw.observer.ViewObservable;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

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
            if (availablePositionClicks >= 1) {
                clickedPositionList.add(new Position(row, col));
                clickedNode.setDisable(true);
                // TODO add worker to the board, but how can I retrieve the color?
                //  maybe I can put a generic worker of color grey.
                availablePositionClicks--;

                if (availablePositionClicks == 0) { // Last click done.
                    // Disable all the spaces.
                    disableAllSpaces();

                    // Notify views only when all the required positions have been selected.
                    Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateInitWorkerPosition(clickedPositionList)));
                }
            }
        }
    }

    public void setAvailablePositionClicks(int availablePositionClicks) {
        this.availablePositionClicks = availablePositionClicks;
    }

    public void updateSpaces(ReducedSpace[][] reducedSpaces) {
        ObservableList<Node> spaceList = boardGrid.getChildren();
        for (Node space : spaceList) {
            Position tempPos = new Position(GridPane.getRowIndex(space), GridPane.getColumnIndex(space));

            if (isGridPositionOnBoard(tempPos)) {
                Node styledSpace = ((StackPane) space).getChildren().get(0);
                ReducedSpace redSp = reducedSpaces[tempPos.getRow()][tempPos.getColumn()];

                setGridSpaceLevel(styledSpace, redSp.getLevel(), redSp.hasDome());
                setGridSpaceWorker(styledSpace, redSp.getReducedWorker());
            }
        }
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

    public void setDisabledSpaces(List<Position> positionList) {
        ObservableList<Node> spaceList = boardGrid.getChildren();
        for (Node space : spaceList) {
            Position tempPos = new Position(GridPane.getRowIndex(space), GridPane.getColumnIndex(space));
            if (positionList.contains(tempPos)) {
                space.setDisable(true);
            }
        }
    }

    public void disableAllSpaces() {
        ObservableList<Node> spaceList = boardGrid.getChildren();
        for (Node space : spaceList) {
            space.setDisable(true);
        }
    }

    /**
     * Sets the CSS style on the grid space argument based on the given level and optional dome.
     *
     * @param gridSpace the Node of the grid board to be modified.
     * @param level     the level to be set.
     * @param dome      a boolean value to identify the dome on the space.
     */
    private void setGridSpaceLevel(Node gridSpace, int level, boolean dome) {
        switch (level) {
            case 0:
                break;
            case 1:
                gridSpace.getStyleClass().add("lvlOne");
                break;
            default:
                break;
        }
    }

    /**
     * Sets the CSS style on the grid space argument based on the given worker.
     *
     * @param gridSpace     the Node of the grid board to be modified.
     * @param reducedWorker the worker of the space. Can be null if no worker is on the space.
     */
    private void setGridSpaceWorker(Node gridSpace, ReducedWorker reducedWorker) {
        if (reducedWorker == null) {
            return;
        }

        String strColor = reducedWorker.getColor().getText();
        strColor = strColor.substring(0, 1) + strColor.substring(1).toLowerCase();

        gridSpace.getStyleClass().add("worker" + strColor);
    }

    /**
     * Checks if the given position is on board.
     *
     * @param position the position to be checked.
     * @return {@code true} if the position is valid (on board), {@code false} otherwise.
     */
    private boolean isGridPositionOnBoard(Position position) {
        return position.getRow() >= 0 && position.getRow() < Board.MAX_ROWS
                && position.getColumn() >= 0 && position.getColumn() < Board.MAX_COLUMNS;
    }
}
