package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.view.View;
import javafx.scene.Scene;

import java.util.List;

public class Gui extends View {

    private Scene scene;

    @Override
    public void askNickname() {

    }

    @Override
    public void askMovingWorker(List<Position> positionList) {

    }

    @Override
    public void askMove(List<Position> positionList) {

    }

    @Override
    public void askInitWorkersPositions(List<Position> positions) {

    }

    @Override
    public void askPlayersNumber() {
        SceneController.changeRootPane(this, scene, "board_scene.fxml");
    }

    @Override
    public void askInitWorkerColor(List<Color> colors) {

    }

    @Override
    public void askGod(List<ReducedGod> gods, int request) {

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

    }

    @Override
    public void showGenericMessage(String genericMessage) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showBoard(ReducedSpace[][] spaces) {

    }

    @Override
    public void askEnableEffect() {

    }

    public void setMainScene(Scene scene) {
        this.scene = scene;
    }
}