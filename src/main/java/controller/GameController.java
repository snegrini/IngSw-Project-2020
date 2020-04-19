package controller;

import model.Game;
import model.God;
import model.player.Player;
import network.message.*;
import observer.ViewObserver;
import view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController { // implements ViewObserver {

    private Game game;
    private Map<String, VirtualView> virtualViews;

    private GameState gameState;
    private List<God> godList;
    private TurnController turnController;

    public GameController() {
        this.game = Game.getInstance();
        this.virtualViews = Collections.synchronizedMap(new HashMap<>());
        this.turnController = new TurnController(Game.getInstance());
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


    public TurnController getTurnController() {
        return turnController;
    }

    public Message onMessageReceived(Message receivedMessage) {

        if (gameState == GameState.LOGIN) {
            switch (receivedMessage.getMessageType()) {
                case LOGIN_REQUEST:
                    return loginRequests(receivedMessage);
                case PLAYERNUMBER_REPLY:
                    setNumTotalPlayers((PlayerNumberReply) receivedMessage); // TODO check input and create reply class.
            }
        }

        if (gameState == GameState.INIT) {

            switch (receivedMessage.getMessageType()){
                case GODLIST:
                    godListHandler((GodList) receivedMessage); // TODO check input and create reply class.
                case INIT:
                    //assegna il colore al player e le posizioni ai worker.
            }

        }


        // check if sender is in listPlayer.
        if (!Game.getInstance().isPlayerInList(receivedMessage.getNickname())) {
            return new GenericErrorMessage("Player is not in game.");
        }

        // check if sender is the active player.
        if (turnController.getPhaseController().getActivePlayer().getNickname().equals(receivedMessage.getNickname())) {
            return new GenericErrorMessage("Not your turn");
        }

        switch (receivedMessage.getMessageType()) {
            case MOVE:
                break;
            default:
                // TODO show exception
                break;
        }
        // TODO
        return null;
    }

    /**
     * If receivedMessage.getList.size > 1 create 3 god List
     * if size = 1 then player.god = god.
     * @param receivedMessage
     *
     */
    private void godListHandler(GodList receivedMessage) {
        //se size > 1 => istanzio la lista dei 3 dei.
        //se size = 1 solo => assegno il dio al giocatore e rispondo con Messaggio vuoto?
        if (receivedMessage.getGodList().size() == 3)
            godList = receivedMessage.getGodList(); // TODO check for possible mistake
        else if (receivedMessage.getGodList().size() == 1)
            Game.getInstance().getPlayerByNickname( receivedMessage.getNickname() ).setGod(receivedMessage.getGod());
    }

    private void setNumTotalPlayers(PlayerNumberReply receivedMessage) {
        Game.getInstance().setChosenMaxPlayers(receivedMessage.getPlayerNumber());
    }

    private Message loginRequests(Message receivedMessage) {

        // if is 1st add it and request number players.
        if (Game.getInstance().getNumCurrentPlayers() == 0) {
            Game.getInstance().addPlayer(new Player(receivedMessage.getNickname()));
            return new PlayerNumberRequest();
        } // if not 1st and there is some available slot check nickname.
        else if (!Game.getInstance().isPlayerInList(receivedMessage.getNickname())) {
            Game.getInstance().addPlayer(new Player(receivedMessage.getNickname()));
            // if latest player is logged then change gameState from LOGIN to INIT.
            if (Game.getInstance().getNumCurrentPlayers() == Game.getInstance().getChosenPlayersNumber())
                gameState = GameState.INIT;
            return new LoginReply(true, true);
        } else {
            return new LoginReply(false, false);
        }

    }


    void endGame() {
        // TODO send message to all players, close connections
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
