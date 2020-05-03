package it.polimi.ingsw.PSP016.observer;

import it.polimi.ingsw.PSP016.network.message.Message;

public interface Observer {
    void update(Message message);
}
