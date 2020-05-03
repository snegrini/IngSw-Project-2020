package it.polimi.ingsw.PSP016.view.gui;

import it.polimi.ingsw.PSP016.model.ReducedGod;
import it.polimi.ingsw.PSP016.model.board.Position;
import it.polimi.ingsw.PSP016.model.board.ReducedSpace;
import it.polimi.ingsw.PSP016.model.enumerations.Color;
import it.polimi.ingsw.PSP016.view.View;
import javafx.application.Application;

import java.util.List;

public class Gui extends View {

    @Override
    public void init() {
        Application.launch(JavaFXGui.class);
    }

    @Override
    public void askServerInfo() {

    }

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

    }

    @Override
    public void askInitWorkerColor(List<Color> colors) {

    }

    @Override
    public void askGod(List<ReducedGod> gods, int request) {

    }

    @Override
    public void askNewBuildingPosition(List<Position> positions) {

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
}