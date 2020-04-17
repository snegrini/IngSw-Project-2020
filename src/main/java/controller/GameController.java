package controller;

import model.Game;
import network.message.Message;
import observer.ViewObserver;
import view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameController { // implements ViewObserver {

    private Game game;
    private Map<String, VirtualView> virtualViews;

    public GameController() {
        this.game = Game.getInstance();
        this.virtualViews = Collections.synchronizedMap(new HashMap<>());
    }

   /*@Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {

    }

    @Override
    public void onUpdateNickname(String nickname) {

    }

    @Override
    public void onUpdatePlayersNumber(int chosenPlayersNumber) {

    }*/

    /**
     * Takes action based on the message received from a client.
     */
    public void onMessageReceived(Message message) {
        switch (message.getMessageType()) {
            case PLAYERNUMBER_REQUEST:
                break;
            default:
                // TODO show exception
                break;
        }
    }

    /**
     * Adds a Player VirtualView to the controller if the first player max_players is not exceeded.
     * Then adds a controller observer to the view.
     * dds the VirtualView to the game model observers.
     *
     * @param nickname    the player nickname to identify his associated VirtualView.
     * @param virtualView the virtualview to be added.
     */
    public void addVirtualView(String nickname, VirtualView virtualView) {
        // This is the first player connecting
        if (virtualViews.size() == 0) {
            // FIXME virtualView.addObserver(this);
            virtualViews.put(nickname, virtualView);
            game.addObserver(virtualView);
            virtualView.askPlayersNumber();
        } else if (virtualViews.size() < game.getChosenPlayersNumber()) {
            // FIXME virtualView.addObserver(this);
            virtualViews.put(nickname, virtualView);
            game.addObserver(virtualView);
            virtualView.showLoginResult(true, true);
        } else {
            virtualView.showLoginResult(true, false);
        }
    }

    /**
     * Removes a VirtualView from the controller.
     *
     * @param nickname the nickname of the VirtualView associated.
     */
    public void removeVirtualView(String nickname) {
        VirtualView vv = virtualViews.remove(nickname);
        game.removeObserver(vv);
    }
}
