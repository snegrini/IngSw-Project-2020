package controller;

import model.Game;
import model.God;
import model.ReducedGod;
import model.board.Position;
import model.enumerations.Color;
import model.player.Player;
import model.player.Worker;
import network.message.*;
import view.VirtualView;

import java.util.*;

public class GameController {

    private Game game;
    private Map<String, VirtualView> virtualViews;

    private GameState gameState;
    private List<ReducedGod> selectedGodList;
    private List<ReducedGod> activeGodList;
    private List<ReducedGod> fullReducedGodList;

    private List<God> godList;

    private List<Color> availableColors;
    private TurnController turnController;

    public GameController() {
        this.game = Game.getInstance();
        // TODO parser god from file and insert into godList.
        godList = new ArrayList<>();
        // TODO create reduced gods and insert in fullReducedGodList.
        fullReducedGodList = new ArrayList<>();

        availableColors = getColorList();

        this.virtualViews = Collections.synchronizedMap(new HashMap<>());
        this.turnController = new TurnController(game);
        gameState = GameState.LOGIN; // Initialize Game State.
    }


    public TurnController getTurnController() {
        return turnController;
    }

    public void onMessageReceived(Message receivedMessage) {

        VirtualView virtualView = virtualViews.get(receivedMessage.getNickname());

        if (gameState == GameState.LOGIN) {
            switch (receivedMessage.getMessageType()) {
                case LOGIN_REQUEST:
                    loginRequests((LoginRequest) receivedMessage, virtualView);
                    break;
                case PLAYERNUMBER_REPLY:
                    setChosenMaxPlayers((PlayerNumberReply) receivedMessage, virtualView);
                    break;
                default:
                    // TODO show exception
                    break;
            }
        } else if (gameState == GameState.INIT) {

            switch (receivedMessage.getMessageType()) {
                case GODLIST:
                    godListHandler((GodListMessage) receivedMessage, virtualView);
                    break;
                case INIT_WORKERSPOSITIONS:
                    initWorkersPositions((WorkersPositionsMessage) receivedMessage, virtualView);
                    break;
                case INIT_COLORS:
                    initColors((ColorsMessage) receivedMessage, virtualView);
                    break;

                default:
                    // TODO show exception
                    break;
            }

        } else if (gameState == GameState.IN_GAME) {

            // check if sender's nickname is in listPlayer.
            if (!game.isPlayerInList(receivedMessage.getNickname())) {
                virtualView.showGenericErrorMessage("Player is not in game.");
            }

            // check if sender is the active player.
            if (turnController.getActivePlayer().getNickname().equals(receivedMessage.getNickname())) {
                //return new GenericErrorMessage("Not your turn.");
                virtualView.sendMessage(new GenericErrorMessage("Not your turn."));
            }

            switch (receivedMessage.getMessageType()) {
                case MOVE:
                    move((Move) receivedMessage, virtualView);
                    break;
                case BUILD:
                    break;
                default:
                    // TODO show exception
                    break;
            }
        }
    }

    private void move(Move receivedMessage, VirtualView virtualView) {
        // TODO
        // check if destination is free
        // check for some lock from gods
        // move player
        // check win condition
    }

    /**
     * If client provides 2 free positions then assign them to his workers
     *
     * @param receivedMessage message from client
     * @param virtualView     virtual view
     */
    private void initWorkersPositions(WorkersPositionsMessage receivedMessage, VirtualView virtualView) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        if (game.getBoard().arePositionsFree(receivedMessage.getPositionList())) {
            for (Position p : receivedMessage.getPositionList()) {
                player.addWorker(new Worker(p));
            }
            virtualView.askWorkersColor(availableColors);
        } else {
            virtualView.askWorkersPositions(game.getBoard().getFreePositions());
        }

    }

    /**
     * If color picked by client is available (and not picked from another client)
     * then assign color to workers of the current player.
     *
     * @param receivedMessage message from client
     * @param virtualView     virtual view
     */
    private void initColors(ColorsMessage receivedMessage, VirtualView virtualView) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        if (receivedMessage.getColorList().size() == 1) {
            if (isInAvailableColor(receivedMessage.getColorList().get(1))) {
                for (Worker w : player.getWorkers()) {
                    w.setColor(receivedMessage.getColorList().get(1));
                    // TODO End init phase
                }
            } else {
                virtualView.askWorkersColor(availableColors);
            }
        } else {
            virtualView.askWorkersColor(availableColors);
        }
    }

    /**
     * Check if color picked by client is available
     *
     * @param color color picked by client
     * @return true or false
     */
    private boolean isInAvailableColor(Color color) {
        for (Color c : availableColors) {
            if (c.equals(color))
                return true;
        }
        return false;
    }


    /**
     * @return a list with all possible colors
     */
    public List<Color> getColorList() {
        List<Color> colorList = new ArrayList<>();
        colorList.add(Color.BLUE);
        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        return colorList;
    }


    /**
     * If receive a godList with multiple gods then create the list.
     * If receive a single god in list then assign it to the current player.
     *
     * @param receivedMessage message from client
     * @param virtualView     virtual view
     */
    private void godListHandler(GodListMessage receivedMessage, VirtualView virtualView) {

        // if received contains a list
        if (receivedMessage.getGodList().size() > 1) {
            if (receivedMessage.getGodList().size() == game.getChosenPlayersNumber()) {
                Collections.copy(selectedGodList, receivedMessage.getGodList());
                Collections.copy(activeGodList, receivedMessage.getGodList());

            } else {
                virtualView.askGod(fullReducedGodList);
            }
        } // else receivedMessage contains only 1 god
        else {
            if (isInSelectedGodList(receivedMessage.getGodList().get(1))) {
                God god = getGodFromReducedGod(receivedMessage.getGodList().get(1));

                game.getPlayerByNickname(receivedMessage.getNickname()).setGod(god);
                selectedGodList.remove(receivedMessage.getGodList().get(1));

                if (selectedGodList.size() == 0) {
                    askWorkersPositions(1); // Ask to first player his workers' positions
                }

            } else {
                virtualView.askGod(selectedGodList);
            }
        }
    }

    private void askWorkersPositions(int playerID) {
        // Instatiate virtual view for the first player logged
        VirtualView virtualView = virtualViews.get(game.getPlayers().get(playerID).getNickname());
        // Send available positions to the first client.
        virtualView.askWorkersPositions(game.getBoard().getFreePositions());
    }

    /**
     * Convert ReducedGod to God in order to set it to a player.
     *
     * @param reducedGod god from client
     * @return God
     */
    private God getGodFromReducedGod(ReducedGod reducedGod) {
        for (God g : godList) {
            if (g.getName().equals(reducedGod.getName()))
                return g;
        }
        return null;
    }

    /**
     * Check if parameter is in the selectedGodList
     *
     * @param god god picked by client
     * @return true if correct else false
     */
    private boolean isInSelectedGodList(ReducedGod god) {
        for (ReducedGod g : selectedGodList) {
            if (g.getName().equals(god.getName()))
                return true;
        }
        return false;
    }

    /**
     * Set the number of players in game
     *
     * @param receivedMessage message from client
     * @param virtualView     virtual view
     */
    private void setChosenMaxPlayers(PlayerNumberReply receivedMessage, VirtualView virtualView) {
        if (receivedMessage.getPlayerNumber() < 4 && receivedMessage.getPlayerNumber() > 1) {
            game.setChosenMaxPlayers(receivedMessage.getPlayerNumber());
            // Don't send ack to client, number accepted.
        } else {
            virtualView.askPlayersNumber();
        }
    }

    /**
     * if first client then ask number of players he wants.
     * if not first client check his nickname, add player to the game
     * eventually change game state
     *
     * @param receivedMessage message from client
     * @param virtualView     virtual view
     */
    private void loginRequests(LoginRequest receivedMessage, VirtualView virtualView) {
        String nickname = receivedMessage.getNickname();

        if (!nickname.equals("server")) {
            if (game.getNumCurrentPlayers() == 0) { // First player logged. Ask number of players.
                game.addPlayer(new Player(nickname));
                virtualView.askPlayersNumber();
            } else if (!(game.isPlayerInList(nickname))) { // If not exist yet then add it
                game.addPlayer(new Player(nickname));
                virtualView.showLoginResult(true, true);

                if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber()) { // If all players logged
                    gameState = GameState.INIT;
                    askGods(0); // FIXME -> to random not to first
                }
            } else {
                virtualView.showLoginResult(false, true);
            }


        } else {
            // NICKNAME CAN'T BE "server".
            virtualView.showLoginResult(false, true);
        }
    }

    private void askGods(int playerID) {
        // Instatiate virtual view for the first player logged
        VirtualView virtualView = virtualViews.get(game.getPlayers().get(playerID).getNickname());
        // virtualView.askGod(fullReducedGodList);
        virtualView.showGenericErrorMessage("lista Dei"); // FIXME
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
