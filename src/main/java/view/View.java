package view;

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

    public abstract void askPlayerNumber();

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

}
