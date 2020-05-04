package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.observer.ViewObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Defines a generic view to be implemented by each it.polimi.ingsw.view type (e.g. CLI, GUI in JavaFX, ...).
 */
public abstract class View {

    private final List<ViewObserver> observers = new ArrayList<>();

    public void addObserver(ViewObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(ViewObserver obs) {
        observers.remove(obs);
    }

    protected void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver observer : observers) {
            lambda.accept(observer);
        }
    }

    public abstract void init();

    /**
     * Asks to the user to which server he wants to connect.
     */
    public abstract void askServerInfo();

    /**
     * Asks to the user to choose a Nickname.
     */
    public abstract void askNickname();

    public abstract void askMovingWorker(List<Position> positionList);

    public abstract void askMove(List<Position> positionList);

    public abstract void askInitWorkersPositions(List<Position> positions);

    public abstract void askPlayersNumber();

    /**
     * Allows the user to choose his workers' color.
     *
     * @param colors the list of the available colors.
     */
    public abstract void askInitWorkerColor(List<Color> colors);

    /**
     * Allows the user to choose his God.
     *
     * @param gods the list of the available Gods.
     */
    public abstract void askGod(List<ReducedGod> gods, int request);

    /**
     * Asks to the user to choose a new position where to build.
     *
     * @param positions the list of the available positions.
     */
    public abstract void askNewBuildingPosition(List<Position> positions);


    /**
     * Shows to the user if the Login succeeded.
     *
     * @param nicknameAccepted     indicates if the chosen nickname has been accepted.
     * @param connectionSuccessful indicates if the connection has been successful.
     * @param nickname             the nickname of the player to be greeted.
     */
    public abstract void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname);

    public abstract void showGenericMessage(String genericMessage);

    public abstract void showError(String error);

    /**
     * Shows the Board
     */
    public abstract void showBoard(ReducedSpace[][] spaces);
}
