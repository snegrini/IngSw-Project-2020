package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ViewObservable {

    protected final List<ViewObserver> observers = new ArrayList<>();

    public void addObserver(ViewObserver obs) {
        observers.add(obs);
    }

    public void addAllObservers(List<ViewObserver> observerList) {
        observers.addAll(observerList);
    }

    public void removeObserver(ViewObserver obs) {
        observers.remove(obs);
    }

    public void removeAllObservers(List<ViewObserver> observerList) {
        observers.removeAll(observerList);
    }

    protected void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver observer : observers) {
            lambda.accept(observer);
        }
    }
}
