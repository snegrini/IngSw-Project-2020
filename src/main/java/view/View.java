package view;

import model.God;
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


    /**
     * Initialize the view, show welcome screen.
     */
    public abstract void init();

    public abstract void askServerInfo();

    public abstract void askNickname();

    public abstract void askWorkerToMove(List<Worker> workers);

    public abstract void askPlayersNumber();

    public abstract void askWorkersColor(List<Color> colors);

    public abstract void askGod(List<God> gods);

    public abstract String showBoard();

    public abstract void askNewBuildingPosition(Worker worker);

    public abstract void askNewPosition(Worker worker);

    public abstract void initializeBoard();

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

    public abstract void askServerInfo();

    public abstract void askNickname();

    public abstract void askPlayersNumber();

    public abstract void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful);
}
