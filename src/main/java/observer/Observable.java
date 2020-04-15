package observer;

import network.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Observable { //<T> {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    protected void notifyObserver(Message message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    /*protected void notifyObserver(Consumer<T> lambda) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }*/
}
