package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scene.*;
import javafx.scene.Scene;

import java.util.List;

public class Gui extends View {

    private Scene scene;
    private ViewGuiController viewGuiController;

    @Override
    public void askNickname() {
        viewGuiController = SceneController.changeRootPane(this, scene, "login_scene.fxml");
    }

    @Override
    public void askMovingWorker(List<Position> positionList) {

    }

    @Override
    public void askMove(List<Position> positionList) {

    }

    @Override
    public void askInitWorkersPositions(List<Position> positions) {
        BoardSceneController bsc = (BoardSceneController) viewGuiController;
        bsc.setEnabledSpaces(positions);
        bsc.setAvailablePositionClicks(2);
    }

    @Override
    public void askPlayersNumber() {
        PlayersNumberSceneController pnsc = new PlayersNumberSceneController();
        pnsc.setView(this);
        pnsc.setPlayersRange(2, 3);
        viewGuiController = pnsc;
        SceneController.changeRootPane(pnsc, scene, "players_number_scene.fxml");
    }

    @Override
    public void askInitWorkerColor(List<Color> colors) {
        ColorSceneController csc = new ColorSceneController();
        csc.setView(this);
        csc.setAvailableColors(colors);
        viewGuiController = csc;
        SceneController.changeRootPane(csc, scene, "color_scene.fxml");
    }

    @Override
    public void askGod(List<ReducedGod> gods, int request) {
        GodsSceneController gsc = new GodsSceneController();
        gsc.setView(this);
        gsc.setGods(gods);
        gsc.setNumberRequest(request);
        viewGuiController = gsc;
        SceneController.changeRootPane(gsc, scene, "gods_scene.fxml");
    }

    @Override
    public void askBuild(List<Position> positions) {

    }

    @Override
    public void askMoveFx(List<Position> positions) {

    }

    @Override
    public void askBuildFx(List<Position> positions) {

    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        if (nicknameAccepted && connectionSuccessful) {
            // TODO show welcome screen and lobby
        } else if (connectionSuccessful) {
            SceneController.showAlert("ERROR", "Nickname already taken.");
            viewGuiController = SceneController.changeRootPane(this, scene, "login_scene.fxml");
        } else {
            SceneController.showAlert("ERROR", "Could not contact server.");
            viewGuiController = SceneController.changeRootPane(this, scene, "menu_scene.fxml");
        }
    }

    @Override
    public void showGenericMessage(String genericMessage) {

    }

    @Override
    public void showErrorAndExit(String error) {

    }

    @Override
    public void showBoard(ReducedSpace[][] spaces) {
        BoardSceneController bsc = new BoardSceneController();
        bsc.setView(this);

        viewGuiController = bsc;
        SceneController.changeRootPane(bsc, scene, "board_scene.fxml");
    }

    @Override
    public void showLobby(List<String> nicknameList, int maxPlayers) {
        LobbySceneController lsc = new LobbySceneController();
        lsc.setView(this);
        lsc.setNicknames(nicknameList);
        lsc.setMaxPlayers(maxPlayers);

        viewGuiController = lsc;
        SceneController.changeRootPane(lsc, scene, "lobby_scene.fxml");
    }

    @Override
    public void askEnableEffect() {

    }

    public void setMainScene(Scene scene) {
        this.scene = scene;
    }
}