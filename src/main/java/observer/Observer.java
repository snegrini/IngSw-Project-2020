package observer;

import network.message.Message;

public interface Observer {
    void update(Message message);
}
