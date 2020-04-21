package view;

import model.God;
import model.board.Position;
import model.enumerations.Color;
import model.player.Worker;
import network.message.Message;
import observer.ViewObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Defines a generic view to be implemented by each view type (e.g. CLI, GUI in JavaFX, ...).
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

    /**
     * Initialize the view, show welcome screen.
     */
    public abstract void init();

    /**
     * Asks to the user to which server he wants to connect.
     */
    public abstract void askServerInfo();

    /**
     * Asks to the to choose a Nickname.
     */
    public abstract void askNickname();

    /**
     * Asks to the user which of his workers he wants to move.
     *
     * @param positions the positions of the user's workers.
     */
    public abstract void askWorkerToMove(List<Position> positions);

    public abstract void askPlayersNumber();

    /**
     * Allows the user to choose the color of his workers.
     *
     * @param colors the list of the available colors.
     */
    public abstract void askWorkersColor(List<Color> colors);

    /**
     * Allows the user to choose his God.
     *
     * @param gods the list of the available Gods.
     */
    public abstract void askGod(List<God> gods);

    /**
     * Asks to the user to choose a new position where to build.
     *
     * @param positions the list of the available positions.
     */
    public abstract void askNewBuildingPosition(List<Position> positions);

    /**
     * Asks to the user to choose a new position where to move.
     *
     * @param positions the list of the available positions.
     */
    public abstract void askNewPosition(List<Position> positions);

    /**
     * Shows to the user if the Login succeeded.
     *
     * @param nicknameAccepted     the result of the verification of the Nickname.
     * @param connectionSuccessful indicates if the connection has been successful.
     */
    public abstract void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful);

    /**
     * Shows the Board
     *
     * @return the Board
     */
    public abstract String showBoard();
}
