package controller;

import model.enumerations.Color;
import network.server.Server;
import observer.ViewObserver;
import view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController implements ViewObserver {

    private Map<String, VirtualView> virtualViews;

    public GameController() {
        this.virtualViews = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {

    }

    @Override
    public void onUpdateNickname(String nickname) {

    }

    @Override
    public void onUpdatePlayersNumber(int playerNumber) {

    }

    @Override
    public void onUpdateWorkersColor(Color color) {

    }

    /**
     * Adds a VirtualView to the controller.
     * An observer to this controller is also added to the VirtualView.
     *
     * @param nickname    the player nickname to identify his associated VirtualView.
     * @param virtualView the virtualview to be added.
     */
    public void addVirtualView(String nickname, VirtualView virtualView) {
        virtualViews.put(nickname, virtualView);
        virtualView.addObserver(this);
    }

    /**
     * Removes a VirtualView from the controller.
     *
     * @param nickname the nickname of the VirtualView associated.
     */
    public void removeVirtualView(String nickname) {
        virtualViews.remove(nickname);
    }
}
