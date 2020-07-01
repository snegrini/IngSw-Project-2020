package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.player.ReducedWorker;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static it.polimi.ingsw.controller.ClientController.UNDO_TIME;
import static it.polimi.ingsw.view.gui.SceneController.GOD_IMAGE_PREFIX;

public class BoardSceneController extends ViewObservable implements GenericSceneController {

    private static final String GLASS_PANE_SELECTED = "glassPaneSelected";

    private int availablePositionClicks;
    private final List<Position> clickedPositionList;
    private MessageType spaceClickType;
    private Node tempNode;
    private Position tempPosition;
    private List<ReducedGod> gods;
    private List<Position> enabledSpaces;

    private Timer undoTimer;

    @FXML
    private GridPane boardGrid;
    @FXML
    private ImageView effectImage;
    @FXML
    private Label player1Label;
    @FXML
    private ImageView god1Image;
    @FXML
    private ImageView player1Img;
    @FXML
    private Label player2Label;
    @FXML
    private ImageView god2Image;
    @FXML
    private ImageView player2Img;
    @FXML
    private Label player3Label;
    @FXML
    private ImageView player3Img;
    @FXML
    private ImageView god3Image;
    @FXML
    private ImageView undoImg;
    @FXML
    private ImageView undoBg;
    @FXML
    private Label timerLbl;
    @FXML
    private Button skipEffectBtn;
    @FXML
    private Button confirmBtn;
    @FXML
    private Label turnInformationLabel;


    /**
     * Default constructor.
     */
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
        undoImg.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUndoImgClick);
        confirmBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmBtnClick);

        god1Image.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onGod1ImageClick);
        god2Image.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onGod2ImageClick);
        god3Image.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onGod3ImageClick);

        player1Label.setText("");
        player2Label.setText("");
        player3Label.setText("");

        turnInformationLabel.setText("");

        timerLbl.setVisible(false);
        undoImg.setVisible(false);
        undoBg.setVisible(false);
        confirmBtn.setVisible(false);
        god3Image.setVisible(false);

        player1Img.setVisible(false);
        player2Img.setVisible(false);
        player3Img.setVisible(false);
    }


    /**
     * Handle the click on the first player god image.
     * Info about the god will be shown.
     *
     * @param event the mouse click event.
     */
    private void onGod1ImageClick(MouseEvent event) {
        ReducedGod god = gods.get(0);
        SceneController.showGodInformation(god.getName(), god.getCaption(), god.getDescription());
    }

    /**
     * Handle the click on the second player god image.
     * Info about the god will be shown.
     *
     * @param event the mouse click event.
     */
    private void onGod2ImageClick(MouseEvent event) {
        ReducedGod god = gods.get(1);
        SceneController.showGodInformation(god.getName(), god.getCaption(), god.getDescription());
    }

    /**
     * Handle the click on the third player god image.
     * Info about the god will be shown.
     *
     * @param event the mouse click event.
     */
    private void onGod3ImageClick(MouseEvent event) {
        ReducedGod god = gods.get(2);
        SceneController.showGodInformation(god.getName(), god.getCaption(), god.getDescription());
    }

    /**
     * Handle the click on a generic space.
     * The opacity of the selected space will be edited.
     *
     * @param event the mouse click event.
     */
    private void onSpaceClick(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        Integer row = GridPane.getRowIndex(clickedNode);
        Integer col = GridPane.getColumnIndex(clickedNode);

        if (row != null && col != null && availablePositionClicks >= 1) {
            availablePositionClicks--;
            clickedNode.setOpacity(10);
            Position clickedPosition = new Position(row, col);
            handleSpaceClickType(clickedNode, clickedPosition);
        }
    }

    /**
     * Handle the click on the undo image.
     * Action of the player will be cancelled.
     *
     * @param event the mouse click event.
     */
    private void onUndoImgClick(MouseEvent event) {
        undoTimer.cancel();
        timerLbl.setVisible(false);
        undoImg.setVisible(false);
        undoBg.setVisible(false);
        confirmBtn.setVisible(false);
        tempNode.getStyleClass().remove(GLASS_PANE_SELECTED);
        setEnabledSpaces(enabledSpaces);

        availablePositionClicks++;
    }

    /**
     * Handle the click on the Confirm Button.
     * Action of the player will be confirmed.
     *
     * @param event the mouse click event.
     */
    private void onConfirmBtnClick(MouseEvent event) {
        undoTimer.cancel();
        timerLbl.setVisible(false);
        undoImg.setVisible(false);
        undoBg.setVisible(false);
        confirmBtn.setVisible(false);

        availablePositionClicks--;

        switch (spaceClickType) {
            case MOVE:
                handleMove(tempPosition);
                break;
            case BUILD:
                handleBuild(tempPosition);
                break;
            case BUILD_FX:
            case MOVE_FX:
                handleApplyFx(tempPosition);
                break;
            default:
                break;
        }

        tempPosition = null;
    }

    /**
     * Handle the click on the effect image.
     * Effect of the player's god will be activated.
     *
     * @param event the mouse click event.
     */
    private void onEffectImageClick(MouseEvent event) {
        enableEffectControls(false);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateEnableEffect(true))).start();
    }

    /**
     * Handle the click on the skip effect button.
     * Effect of the player's god will not be activated.
     *
     * @param event the mouse click event.
     */
    private void onSkipEffectBtnClick(MouseEvent event) {
        enableEffectControls(false);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateEnableEffect(false))).start();
    }

    /**
     * Handle the type of the space clicked by player.
     * @param clickedNode clicked node of the board.
     * @param clickedPosition clicked position of the board.
     */
    private void handleSpaceClickType(Node clickedNode, Position clickedPosition) {
        switch (spaceClickType) {
            case INIT_WORKERSPOSITIONS:
                handleInitWorkers(clickedNode, clickedPosition);
                break;
            case PICK_MOVING_WORKER:
                handlePickMovingWorker(clickedNode, clickedPosition);
                break;
            case MOVE:
            case BUILD:
            case MOVE_FX:
            case BUILD_FX:
                waitForUndo(clickedNode, clickedPosition);
                break;
            default:
                break;
        }
    }


    /**
     * Waits 5 seconds before sending the data to the server.
     * The user can Undo his last operation by clicking on the undo button.
     * After 5 seconds an automatic confirm will be sent to the server.
     * The user can skip this delay by clicking on the confirm button.
     *
     * @param clickedNode     the clicked node of the grid.
     * @param clickedPosition the clicked position on the grid.
     */
    private void waitForUndo(Node clickedNode, Position clickedPosition) {
        clickedNode.setDisable(true);
        disableAllSpaces();
        clickedNode.getStyleClass().add(GLASS_PANE_SELECTED);

        undoImg.setVisible(true);
        undoBg.setVisible(true);
        confirmBtn.setVisible(true);
        timerLbl.setVisible(true);
        tempNode = clickedNode;
        tempPosition = clickedPosition;

        undoTimer = new Timer();
        undoTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                onConfirmBtnClick(null);
            }
        }, UNDO_TIME);
    }


    /**
     * Handles the initial setup of the workers onto the board.
     * The player may click twice on the board to setup the initial workers' position.
     * Data will be sent to the server after the second click has been committed.
     *
     * @param clickedNode     the clicked node.
     * @param clickedPosition the clicked position on the grid.
     */
    private void handleInitWorkers(Node clickedNode, Position clickedPosition) {
        clickedPositionList.add(clickedPosition);
        clickedNode.setDisable(true);
        clickedNode.getStyleClass().add(GLASS_PANE_SELECTED);

        if (availablePositionClicks == 0) { // Last click done.
            // Disable all the spaces.
            disableAllSpaces();
            removeCssClassFromAllSpaces(GLASS_PANE_SELECTED);

            // Notify views only when all the required positions have been selected.
            new Thread(() -> notifyObserver(obs -> obs.onUpdateInitWorkerPosition(clickedPositionList))).start();
        }
    }

    /**
     * Handles the click for the worker to be used in this turn.
     *
     * @param clickedNode     the clicked node.
     * @param clickedPosition the clicked position on the grid.
     */
    private void handlePickMovingWorker(Node clickedNode, Position clickedPosition) {
        disableAllSpaces();
        clickedNode.getStyleClass().add(GLASS_PANE_SELECTED);
        new Thread(() -> notifyObserver(obs -> obs.onUpdatePickMovingWorker(clickedPosition))).start();
    }

    /**
     * Handles the click for the move position of the worker.
     *
     * @param clickedPosition the clicked position on the grid.
     */
    private void handleMove(Position clickedPosition) {
        disableAllSpaces();
        removeCssClassFromAllSpaces(GLASS_PANE_SELECTED);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateMove(clickedPosition))).start();
    }

    /**
     * Handles the click for the build position.
     *
     * @param clickedPosition the clicked position on the grid.
     */
    private void handleBuild(Position clickedPosition) {
        disableAllSpaces();
        removeCssClassFromAllSpaces(GLASS_PANE_SELECTED);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateBuild(clickedPosition))).start();
    }

    /**
     * Handles the click for the move or build position of the worker during an effect.
     *
     * @param clickedPosition the clicked position on the grid.
     */
    private void handleApplyFx(Position clickedPosition) {
        disableAllSpaces();
        removeCssClassFromAllSpaces(GLASS_PANE_SELECTED);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateApplyEffect(clickedPosition))).start();
    }

    /**
     * Sets the click type which will be handled after the click itself.
     * This is needed in order to differentiate the various click on the same grid
     * and perform the right operation.
     *
     * @param spaceClickType the click type for the next phase.
     */
    public void setSpaceClickType(MessageType spaceClickType) {
        this.spaceClickType = spaceClickType;
    }

    /**
     * Removes a CSS class from all the grid spaces.
     *
     * @param cssClass the name of the css class to be removed.
     */
    private void removeCssClassFromAllSpaces(String cssClass) {
        ObservableList<Node> spaceList = boardGrid.getChildren();
        for (Node space : spaceList) {
            space.getStyleClass().remove(cssClass);
        }
    }

    /**
     * Sets the clickable positions on the board grid.
     *
     * @param availablePositionClicks the new positions which can be clicked.
     */
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
                ObservableList<Node> children = ((StackPane) space).getChildren();
                Node spaceNode = children.get(0); // panel for the levels
                Node spaceWorkerNode = children.get(1); // panel for the dome/worker
                ReducedSpace redSp = reducedSpaces[tempPos.getRow()][tempPos.getColumn()];

                // NOTE: always call first setGridSpaceLevel, because it clears all the other styles.
                // The dome will be set in the worker panel since they mutually exclude.
                setGridSpaceLevel(spaceNode, redSp.getLevel());
                setGridSpaceWorker(spaceWorkerNode, redSp.getReducedWorker(), redSp.hasDome());
            }
        }
    }

    /**
     * Enables the spaces on the board. All the others spaces will be disabled.
     *
     * @param positionList the list of spaces to enable.
     */
    public void setEnabledSpaces(List<Position> positionList) {
        this.enabledSpaces = positionList;

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
     * Sets the CSS style on the grid space argument based on the given level.
     *
     * @param gridSpace the Node of the grid board to be modified.
     * @param level     the level to be set.
     */
    private void setGridSpaceLevel(Node gridSpace, int level) {
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
                break;
            default:
                break;
        }
    }

    /**
     * Sets the CSS style on the grid space argument based on the given worker or optional dome.
     *
     * @param gridSpace     the Node of the grid board to be modified.
     * @param reducedWorker the worker of the space. Can be null if no worker is on the space.
     * @param dome          a boolean value to identify the dome on the space.
     */
    private void setGridSpaceWorker(Node gridSpace, ReducedWorker reducedWorker, boolean dome) {
        // Remove the previous worker (if any)
        gridSpace.getStyleClass().removeIf(s -> s.startsWith("worker"));

        if (reducedWorker != null) {
            String strColor = reducedWorker.getColor().getText();
            strColor = strColor.substring(0, 1) + strColor.substring(1).toLowerCase();

            gridSpace.getStyleClass().add("worker" + strColor);
        } else if (dome) {
            gridSpace.getStyleClass().add("dome");
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

    /**
     * Updates the displayed infos about the current match.
     * Infos are about the list of players, the turn of the player.
     *
     * @param players      a list of player's nicknames.
     * @param gods         a list of player's gods.
     * @param activePlayer the nickname of the playing player.
     */
    public void updateMatchInfo(List<String> players, List<ReducedGod> gods, String activePlayer) {
        if (players != null && gods != null) {
            this.gods = gods; // save gods for later usage.
            player1Label.setText(players.get(0));
            Image img1 = new Image(getClass().getResourceAsStream(GOD_IMAGE_PREFIX + gods.get(0).getName().toLowerCase() + ".png"));
            god1Image.setImage(img1);
            player1Img.setVisible(true);

            player2Label.setText(players.get(1));
            Image img2 = new Image(getClass().getResourceAsStream(GOD_IMAGE_PREFIX + gods.get(1).getName().toLowerCase() + ".png"));
            god2Image.setImage(img2);
            player2Img.setVisible(true);

            turnInformationLabel.setText("Turn of " + activePlayer);

            if (players.size() == 3 && gods.size() == 3) {
                // Sets 3rd player information.
                player3Label.setText(players.get(2));

                Image img3 = new Image(getClass().getResourceAsStream(GOD_IMAGE_PREFIX + gods.get(2).getName().toLowerCase() + ".png"));
                god3Image.setImage(img3);

                god3Image.setVisible(true);
                player3Img.setVisible(true);
            } else {
                god3Image.setVisible(false);
                player3Label.setVisible(false);
                player3Img.setVisible(false);
            }
        } else {
            turnInformationLabel.setText("Turn of " + activePlayer);
        }
    }
}
