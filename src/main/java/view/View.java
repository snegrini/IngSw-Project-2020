package view;

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


    public void addListener(ViewObserver vl) {
        observers.add(vl);
    }

    public void removeListener(ViewObserver vl) {
        observers.remove(vl);
    }

    protected void notifyListeners(Consumer<ViewObserver> lambda)
    {
        for (ViewObserver listener: observers) {
            lambda.accept(listener);
        }
    }
}
