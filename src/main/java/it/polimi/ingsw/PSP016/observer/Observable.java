package it.polimi.ingsw.PSP016.observer;

import it.polimi.ingsw.PSP016.network.message.Message;

import java.util.ArrayList;
import java.util.List;

public class Observable {

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
}
