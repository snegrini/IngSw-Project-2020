package controller;

import model.Game;
import model.God;
import model.player.Player;
import model.player.Worker;
import network.message.*;
import view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    private Game game;
    private Map<String, VirtualView> virtualViews;

    private GameState gameState;
    private List<God> selectedGodList;
    private List<God> activeGodList;
    private TurnController turnController;

    public GameController() {
        this.game = Game.getInstance();
        this.virtualViews = Collections.synchronizedMap(new HashMap<>());
        this.turnController = new TurnController(game);
    }


    public TurnController getTurnController() {
        return turnController;
    }

    public Message onMessageReceived(Message receivedMessage) {

        if (gameState == GameState.LOGIN) {
            switch (receivedMessage.getMessageType()) {
                case LOGIN_REQUEST:
                    return loginRequests(receivedMessage);
                case PLAYERNUMBER_REPLY:
                    return setChosenMaxPlayers((PlayerNumberReply) receivedMessage);
                default:
                    // TODO show exception
                    break;
            }
        }

        if (gameState == GameState.INIT) {

            switch (receivedMessage.getMessageType()){
                case GODLIST:
                    return godListHandler((GodList) receivedMessage);
                case INIT:
                    return init((Init) receivedMessage);

                default:
                    // TODO show exception
                    break;
            }

        }


        // check if sender is in listPlayer.
        if (!game.isPlayerInList(receivedMessage.getNickname())) {
            return new GenericErrorMessage("Player is not in game.");
        }



        // check if sender is the active player.
        if (turnController.getActivePlayer().getNickname().equals(receivedMessage.getNickname())) {
            return new GenericErrorMessage("Not your turn.");
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

    private Message init(Init receivedMessage) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        if(receivedMessage.getPositionList().size()<=2)
            for (int i = 0 ; i<receivedMessage.getPositionList().size(); i++)
                player.addWorker(new Worker(receivedMessage.getColor(), receivedMessage.getPositionList().get(i)));

    }

    /**
     * If receivedMessage.getList.size == numPlayer then create the godList
     * if size = 1 then player.god = god.
     * @param receivedMessage
     *
     */
    private Message godListHandler(GodList receivedMessage) {

        // if received contains a list
        if (!receivedMessage.getGodList().isEmpty()) {
            if (receivedMessage.getGodList().size() == game.getChosenPlayersNumber()) {
                Collections.copy(selectedGodList, receivedMessage.getGodList());
                Collections.copy(activeGodList, receivedMessage.getGodList());
                return new GodListAck(true);
            } else {
                return new GodListAck(false);
            }
        } // else received contains only 1 god
        else{
            if (isGodInList(receivedMessage.getGod())) {
                game.getPlayerByNickname(receivedMessage.getNickname()).setGod(receivedMessage.getGod());
                selectedGodList.remove(receivedMessage.getGod());
                return new GodListAck(true);
            }
            else {
                return new GodListAck(false);
            }
        }
    }

    private boolean isGodInList(God god) {
        for (God g : selectedGodList) {
            if (g.getName().equals(god.getName()))
                return true;
        }
        return false;
    }

    private Message setChosenMaxPlayers(PlayerNumberReply receivedMessage) {
        if(receivedMessage.getPlayerNumber()<4 && receivedMessage.getPlayerNumber()>1) {
            game.setChosenMaxPlayers(receivedMessage.getPlayerNumber());
            return new PlayerNumberAck(true);
        }
        else {
            return new PlayerNumberAck(false);
        }

    }

    private Message loginRequests(Message receivedMessage) {

        // if is 1st add it and request number players.
        if (game.getNumCurrentPlayers() == 0) {
            game.addPlayer(new Player(receivedMessage.getNickname()));
            return new PlayerNumberRequest();
        } // if not 1st and there is some available slot check nickname.
        else if (!(game.isPlayerInList(receivedMessage.getNickname())) && !(receivedMessage.getNickname() == "server")) {
            game.addPlayer(new Player(receivedMessage.getNickname()));
            // if latest player is logged then change gameState from LOGIN to INIT.
            if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber())
                gameState = GameState.INIT;
            return new LoginReply(true, true);
        } else {
            return new LoginReply(true, false);
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
