package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scene.*;
import javafx.application.Platform;

import java.util.List;

public class Gui extends ViewObservable implements View {

    @Override
    public void askNickname() {
        SceneController.changeRootPane(observers, "login_scene.fxml");
    }

    @Override
    public void askPlayersNumber() {
        PlayersNumberSceneController pnsc = new PlayersNumberSceneController();
        pnsc.addAllObservers(observers);
        pnsc.setPlayersRange(2, 3);
        Platform.runLater(() -> SceneController.changeRootPane(pnsc, "players_number_scene.fxml"));
    }

    @Override
    public void askInitWorkerColor(List<Color> colors) {
        ColorSceneController csc = new ColorSceneController();
        csc.addAllObservers(observers);
        csc.setAvailableColors(colors);
        Platform.runLater(() -> SceneController.changeRootPane(csc, "color_scene.fxml"));
    }

    @Override
    public void askGod(List<ReducedGod> gods, int request) {
        GodsSceneController gsc = new GodsSceneController();
        gsc.addAllObservers(observers);
        gsc.setGods(gods);
        gsc.setNumberRequest(request);
        Platform.runLater(() -> SceneController.changeRootPane(gsc, "gods_scene.fxml"));
    }

    @Override
    public void askInitWorkersPositions(List<Position> positions) {
        BoardSceneController bsc = (BoardSceneController) SceneController.getActiveController();
        bsc.setAvailablePositionClicks(2);
        bsc.setSpaceClickType(MessageType.INIT_WORKERSPOSITIONS);
        Platform.runLater(() -> bsc.setEnabledSpaces(positions));
    }

    @Override
    public void askMovingWorker(List<Position> positions) {
        BoardSceneController bsc = (BoardSceneController) SceneController.getActiveController();
        bsc.setAvailablePositionClicks(1);
        bsc.setSpaceClickType(MessageType.PICK_MOVING_WORKER);
        Platform.runLater(() -> bsc.setEnabledSpaces(positions));
    }

    @Override
    public void askMove(List<Position> positions) {
        BoardSceneController bsc = (BoardSceneController) SceneController.getActiveController();
        bsc.setAvailablePositionClicks(1);
        bsc.setSpaceClickType(MessageType.MOVE);
        Platform.runLater(() -> bsc.setEnabledSpaces(positions));
    }

    @Override
    public void askBuild(List<Position> positions) {
        BoardSceneController bsc = (BoardSceneController) SceneController.getActiveController();
        bsc.setAvailablePositionClicks(1);
        bsc.setSpaceClickType(MessageType.BUILD);
        Platform.runLater(() -> bsc.setEnabledSpaces(positions));
    }

    @Override
    public void askMoveFx(List<Position> positions) {
        BoardSceneController bsc = (BoardSceneController) SceneController.getActiveController();
        bsc.setAvailablePositionClicks(1);
        bsc.setSpaceClickType(MessageType.MOVE_FX);
        Platform.runLater(() -> bsc.setEnabledSpaces(positions));
    }

    @Override
    public void askBuildFx(List<Position> positions) {
        BoardSceneController bsc = (BoardSceneController) SceneController.getActiveController();
        bsc.setAvailablePositionClicks(1);
        bsc.setSpaceClickType(MessageType.BUILD_FX);
        Platform.runLater(() -> bsc.setEnabledSpaces(positions));
    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        if (nicknameAccepted && connectionSuccessful) {
            // TODO show welcome screen and lobby
        } else if (connectionSuccessful) {
            SceneController.showAlert("ERROR", "Nickname already taken.");
            Platform.runLater(() -> SceneController.changeRootPane(observers, "login_scene.fxml"));
        } else {
            SceneController.showAlert("ERROR", "Could not contact server.");
            Platform.runLater(() -> SceneController.changeRootPane(observers, "menu_scene.fxml"));
        }
    }

    @Override
    public void showGenericMessage(String genericMessage) {

    }

    @Override
    public void showErrorAndExit(String error) {

    }

    /**
     * Show the board. A new root pane will be set if no board is already on scene, otherwise only the values will be
     * updated without changing the current root pane.
     *
     * @param spaces the board to be shown.
     */
    @Override
    public void showBoard(ReducedSpace[][] spaces) {
        BoardSceneController bsc;

        try {
            bsc = (BoardSceneController) SceneController.getActiveController();
        } catch (ClassCastException e) {
            bsc = new BoardSceneController();
            bsc.addAllObservers(observers);
            BoardSceneController finalBsc = bsc;
            Platform.runLater(() -> SceneController.changeRootPane(finalBsc, "board_scene.fxml"));
        }

        BoardSceneController finalBsc = bsc;
        Platform.runLater(() -> finalBsc.updateSpaces(spaces));
    }

    @Override
    public void showLobby(List<String> nicknameList, int maxPlayers) {
        LobbySceneController lsc;

        try {
            lsc = (LobbySceneController) SceneController.getActiveController();
            lsc.setNicknames(nicknameList);
            lsc.setMaxPlayers(maxPlayers);
            Platform.runLater(lsc::updateValues);
        } catch (ClassCastException e) {
            lsc = new LobbySceneController();
            lsc.addAllObservers(observers);
            lsc.setNicknames(nicknameList);
            lsc.setMaxPlayers(maxPlayers);
            LobbySceneController finalLsc = lsc;
            Platform.runLater(() -> SceneController.changeRootPane(finalLsc, "lobby_scene.fxml"));
        }
    }

    @Override
    public void askEnableEffect() {
        BoardSceneController bsc = (BoardSceneController) SceneController.getActiveController();
        Platform.runLater(() -> bsc.enableEffectControls(true));
    }

    @Override
    public void showMatchInfo(List<String> players, List<ReducedGod> gods, String activePlayer) {
        BoardSceneController bsc = (BoardSceneController) SceneController.getActiveController();
        Platform.runLater(() -> bsc.updateMatchInfo(players, gods, activePlayer));
    }
}