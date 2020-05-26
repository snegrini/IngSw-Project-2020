package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.player.ReducedWorker;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class BoardSceneController extends ViewObservable implements GenericSceneController {

    private int availablePositionClicks;
    private List<Position> clickedPositionList;
    private MessageType spaceClickType;

    @FXML
    private GridPane boardGrid;
    @FXML
    private ImageView effectImage;
    @FXML
    private ImageView playerGodImage;
    @FXML
    private Button skipEffectBtn;

    public BoardSceneController() {
        availablePositionClicks = 0;
        clickedPositionList = new ArrayList<>();
        spaceClickType = MessageType.INIT_WORKERSPOSITIONS;
    }

    @FXML
    public void initialize() {
        boardGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSpaceClick);
        effectImage.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onEffectImageClick);
        skipEffectBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSkipEffectBtnClick);

        Image img = new Image(getClass().getResourceAsStream("/images/cards/" + SceneController.getGod().getName().toLowerCase() + ".png"));
        playerGodImage.setImage(img);
    }

    private void onSpaceClick(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        Integer row = GridPane.getRowIndex(clickedNode);
        Integer col = GridPane.getColumnIndex(clickedNode);

        if (row != null && col != null && availablePositionClicks >= 1) {
            availablePositionClicks--;
            Position clickedPosition = new Position(row, col);
            handleSpaceClickType(clickedNode, clickedPosition);
        }
    }

    private void onEffectImageClick(MouseEvent event) {
        enableEffectControls(false);
        Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateEnableEffect(true)));
    }

    private void onSkipEffectBtnClick(MouseEvent event) {
        enableEffectControls(false);
        Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateEnableEffect(false)));
    }

    private void handleSpaceClickType(Node clickedNode, Position clickedPosition) {
        switch (spaceClickType) {
            case INIT_WORKERSPOSITIONS:
                handleInitWorkers(clickedNode, clickedPosition);
                break;
            case PICK_MOVING_WORKER:
                handlePickMovingWorker(clickedNode, clickedPosition);
                break;
            case MOVE:
                handleMove(clickedNode, clickedPosition);
                break;
            case BUILD:
                handleBuild(clickedNode, clickedPosition);
                break;
            case MOVE_FX:
                handleMoveFx(clickedNode, clickedPosition);
                break;
            case BUILD_FX:
                handleBuildFx(clickedNode, clickedPosition);
                break;
            default:
                break;
        }
    }

    private void handleInitWorkers(Node clickedNode, Position clickedPosition) {
        clickedPositionList.add(clickedPosition);
        clickedNode.setDisable(true);
        clickedNode.getStyleClass().add("glassPaneSelected");

        if (availablePositionClicks == 0) { // Last click done.
            // Disable all the spaces.
            disableAllSpaces();
            removeCssClassFromAllSpaces("glassPaneSelected");

            // Notify views only when all the required positions have been selected.
            Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateInitWorkerPosition(clickedPositionList)));
        }
    }

    private void handlePickMovingWorker(Node clickedNode, Position clickedPosition) {
        disableAllSpaces();
        clickedNode.getStyleClass().add("glassPaneSelected");
        Platform.runLater(() -> notifyObserver(obs -> obs.onUpdatePickMovingWorker(clickedPosition)));
    }

    private void handleMove(Node clickedNode, Position clickedPosition) {
        disableAllSpaces();
        removeCssClassFromAllSpaces("glassPaneSelected");
        Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateMove(clickedPosition)));
    }

    private void handleBuild(Node clickedNode, Position clickedPosition) {
        disableAllSpaces();
        Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateBuild(clickedPosition)));
    }

    private void handleMoveFx(Node clickedNode, Position clickedPosition) {
        disableAllSpaces();
        removeCssClassFromAllSpaces("glassPaneSelected");
        Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateApplyEffect(clickedPosition)));
    }

    private void handleBuildFx(Node clickedNode, Position clickedPosition) {
        disableAllSpaces();
        removeCssClassFromAllSpaces("glassPaneSelected");
        Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateApplyEffect(clickedPosition)));
    }

    public void setSpaceClickType(MessageType spaceClickType) {
        this.spaceClickType = spaceClickType;
    }

    private void removeCssClassFromAllSpaces(String cssClass) {
        ObservableList<Node> spaceList = boardGrid.getChildren();
        for (Node space : spaceList) {
            space.getStyleClass().remove(cssClass);
        }
    }

    public void setAvailablePositionClicks(int availablePositionClicks) {
        this.availablePositionClicks = availablePositionClicks;
    }

    /**
     * Updates the spaces on the board with the latest info received from the server.
     *
     * @param reducedSpaces every spaces of the board.
     */
    public void updateSpaces(ReducedSpace[][] reducedSpaces) {
        ObservableList<Node> spaceList = boardGrid.getChildren();
        for (Node space : spaceList) {
            Position tempPos = new Position(GridPane.getRowIndex(space), GridPane.getColumnIndex(space));

            if (isGridPositionOnBoard(tempPos)) {
                Node styledSpace = ((StackPane) space).getChildren().get(0);
                ReducedSpace redSp = reducedSpaces[tempPos.getRow()][tempPos.getColumn()];

                // NOTE: always call first setGridSpaceLevel, because it clears all the other styles.
                setGridSpaceLevel(styledSpace, redSp.getLevel(), redSp.hasDome());
                setGridSpaceWorker(styledSpace, redSp.getReducedWorker());
            }
        }
    }

    /**
     * Enables the spaces on the board. All the others spaces will be disabled.
     *
     * @param positionList the list of spaces to enable.
     */
    public void setEnabledSpaces(List<Position> positionList) {
        //boardGrid.getStyleClass().add("grayed");
        ObservableList<Node> spaceList = boardGrid.getChildren();
        for (Node space : spaceList) {
            Position tempPos = new Position(GridPane.getRowIndex(space), GridPane.getColumnIndex(space));
            space.setDisable(!positionList.contains(tempPos)); // Enabled if in list.
        }
    }

    /**
     * Disables the spaces on the board. The others spaces will not be affected.
     *
     * @param positionList the list of spaces to disable.
     */
    public void setDisabledSpaces(List<Position> positionList) {
        ObservableList<Node> spaceList = boardGrid.getChildren();
        for (Node space : spaceList) {
            Position tempPos = new Position(GridPane.getRowIndex(space), GridPane.getColumnIndex(space));
            if (positionList.contains(tempPos)) {
                space.setDisable(true);
            }
        }
    }

    /**
     * Disables all the spaces on the board.
     */
    private void disableAllSpaces() {
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
        //gridSpace.getStyleClass().clear();

        switch (level) {
            case 0:
                // no buildings over it.
                break;
            case 1:
                gridSpace.getStyleClass().add("lvlOne");
                break;
            case 2:
                gridSpace.getStyleClass().add("lvlTwo");
                break;
            case 3:
                gridSpace.getStyleClass().add("lvlThree");
            default:
                break;
        }

        if (dome) {
            gridSpace.getStyleClass().add("dome");
        }
    }

    /**
     * Sets the CSS style on the grid space argument based on the given worker.
     *
     * @param gridSpace     the Node of the grid board to be modified.
     * @param reducedWorker the worker of the space. Can be null if no worker is on the space.
     */
    private void setGridSpaceWorker(Node gridSpace, ReducedWorker reducedWorker) {
        // Remove the previous worker (if any)
        gridSpace.getStyleClass().removeIf(s -> s.startsWith("worker"));

        if (reducedWorker != null) {
            String strColor = reducedWorker.getColor().getText();
            strColor = strColor.substring(0, 1) + strColor.substring(1).toLowerCase();

            gridSpace.getStyleClass().add("worker" + strColor);
        }
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

    /**
     * Enables or disables the effect controls.
     *
     * @param enable set to {@code true} to enable the controls, {@code false} to disable.
     */
    public void enableEffectControls(boolean enable) {
        effectImage.setDisable(!enable);
        skipEffectBtn.setDisable(!enable);
    }
}
