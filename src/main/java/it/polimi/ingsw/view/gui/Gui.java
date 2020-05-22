package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scene.*;

import java.util.List;

public class Gui extends ViewObservable implements View {

    @Override
    public void askNickname() {
        SceneController.changeRootPane(observers, "login_scene.fxml");
    }

    @Override
    public void askMovingWorker(List<Position> positionList) {

    }

    @Override
    public void askMove(List<Position> positionList) {

    }

    @Override
    public void askInitWorkersPositions(List<Position> positions) {
        BoardSceneController bsc = (BoardSceneController) SceneController.getActiveController();
        bsc.addAllObservers(observers);
        bsc.setEnabledSpaces(positions);
        bsc.setAvailablePositionClicks(2);
    }

    @Override
    public void askPlayersNumber() {
        PlayersNumberSceneController pnsc = new PlayersNumberSceneController();
        pnsc.addAllObservers(observers);
        pnsc.setPlayersRange(2, 3);
        SceneController.changeRootPane(pnsc, "players_number_scene.fxml");
    }

    @Override
    public void askInitWorkerColor(List<Color> colors) {
        ColorSceneController csc = new ColorSceneController();
        csc.addAllObservers(observers);
        csc.setAvailableColors(colors);
        SceneController.changeRootPane(csc, "color_scene.fxml");
    }

    @Override
    public void askGod(List<ReducedGod> gods, int request) {
        GodsSceneController gsc = new GodsSceneController();
        gsc.addAllObservers(observers);
        gsc.setGods(gods);
        gsc.setNumberRequest(request);
        SceneController.changeRootPane(gsc, "gods_scene.fxml");
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
            SceneController.changeRootPane(observers, "login_scene.fxml");
        } else {
            SceneController.showAlert("ERROR", "Could not contact server.");
            SceneController.changeRootPane(observers, "menu_scene.fxml");
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

        SceneController.changeRootPane(bsc, "board_scene.fxml");
    }

    @Override
    public void showLobby(List<String> nicknameList, int maxPlayers) {
        LobbySceneController lsc = new LobbySceneController();
        lsc.addAllObservers(observers);
        lsc.setNicknames(nicknameList);
        lsc.setMaxPlayers(maxPlayers);

        SceneController.changeRootPane(lsc, "lobby_scene.fxml");
    }

    @Override
    public void askEnableEffect() {

    }
}