package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Custom observable class that can be observed by implementing the {@link ViewObserver} interface and registering as listener.
 */
public abstract class ViewObservable {

    protected final List<ViewObserver> observers = new ArrayList<>();

    /**
     * Adds an observer.
     *
     * @param obs the observer to be added.
     */
    public void addObserver(ViewObserver obs) {
        observers.add(obs);
    }

    /**
     * Adds a list of observers.
     *
     * @param observerList the list of observers to be added.
     */
    public void addAllObservers(List<ViewObserver> observerList) {
        observers.addAll(observerList);
    }

    /**
     * Removes an observer.
     *
     * @param obs the observer to be removed.
     */
    public void removeObserver(ViewObserver obs) {
        observers.remove(obs);
    }

    /**
     * Removes a list of observers.
     *
     * @param observerList the list of observers to be removed.
     */
    public void removeAllObservers(List<ViewObserver> observerList) {
        observers.removeAll(observerList);
    }

    /**
     * Notifies all the current observers through the lambda argument.
     *
     * @param lambda the lambda to be called on the observers.
     */
    protected void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver observer : observers) {
            lambda.accept(observer);
        }
    }
}
