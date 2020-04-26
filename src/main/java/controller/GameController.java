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
                case PLAYERNUMBER_REPLY:
                    setChosenMaxPlayers((PlayerNumberReply) receivedMessage, virtualView);
                default:
                    // TODO show exception
                    break;
            }
        }

        if (gameState == GameState.INIT) {

            switch (receivedMessage.getMessageType()) {
                case GODLIST:
                    godListHandler((GodListMessage) receivedMessage, virtualView);
                case INIT_WORKERSPOSITIONS:
                    initWorkersPositions((WorkersPositionsMessage) receivedMessage, virtualView);
                case INIT_COLORS:
                    initColors((ColorsMessage) receivedMessage, virtualView);

                default:
                    // TODO show exception
                    break;
            }

        }


        // check if sender is in listPlayer.
        if (!game.isPlayerInList(receivedMessage.getNickname())) {
            //return new GenericErrorMessage("Player is not in game.");
        }


        // check if sender is the active player.
        if (turnController.getActivePlayer().getNickname().equals(receivedMessage.getNickname())) {
            //return new GenericErrorMessage("Not your turn.");
        }

        switch (receivedMessage.getMessageType()) {
            case MOVE:
                break;
            case BUILD:
                break;
            default:
                // TODO show exception
                break;
        }
    }


    private void initWorkersPositions(WorkersPositionsMessage receivedMessage, VirtualView virtualView) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        if(arePositionsFree(receivedMessage.getPositionList())) {
            for (Position p : receivedMessage.getPositionList()){
                player.addWorker(new Worker(p));
            }
            virtualView.askWorkersColor(availableColors);
        } else {
            virtualView.askWorkersPositions(game.getBoard().getFreePositions());
        }

    }

    private boolean arePositionsFree(List<Position> positionList) {
        boolean areFree = true;
        for(Position p : positionList) {
            if(!game.getBoard().getSpace(p).isFree()) {
                areFree = false;
            }
        }
        return areFree;
    }


    private void initColors(ColorsMessage receivedMessage, VirtualView virtualView) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        if (receivedMessage.getColorList().size()!=1) {
            if(isInAvailableColor(receivedMessage.getColorList().get(1))) {
                for(Worker w : player.getWorkers()) {
                    w.setColor(receivedMessage.getColorList().get(1));
                    //End init phase
                }
            } else {
                virtualView.askWorkersColor(availableColors);
            }
        } else {
            virtualView.askWorkersColor(availableColors);
        }
    }

    private boolean isInAvailableColor(Color color) {
        for (Color c : availableColors) {
            if (c.equals(color))
                return true;
        }
        return false;
    }

    public List<Color> getColorList() {
        List<Color> colorList = new ArrayList<>();
        colorList.add(Color.BLUE);
        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        return colorList;
    }




    /**
     * If receivedMessage.getList.size == numPlayer then create the godList
     * if size = 1 then player.god = god.
     *
     * @param receivedMessage
     */
    private void godListHandler(GodListMessage receivedMessage, VirtualView virtualView) {

        // if received contains a list
        if (receivedMessage.getGodList().size()>1) {
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
            } else {
                virtualView.askGod(selectedGodList);
            }
        }
    }

    private God getGodFromReducedGod(ReducedGod reducedGod){
        for(God g : godList) {
            if (g.getName().equals(reducedGod.getName()))
                return g;
        }
        return null;
    }

    private boolean isInSelectedGodList(ReducedGod god) {
        for (ReducedGod g : selectedGodList) {
            if (g.getName().equals(god.getName()))
                return true;
        }
        return false;
    }

    private void setChosenMaxPlayers(PlayerNumberReply receivedMessage, VirtualView virtualView) {
        if (receivedMessage.getPlayerNumber() < 4 && receivedMessage.getPlayerNumber() > 1) {
            game.setChosenMaxPlayers(receivedMessage.getPlayerNumber());
            // Don't send ack to client, number accepted.
        } else {
            virtualView.askPlayersNumber();
        }
    }

    private void loginRequests(LoginRequest receivedMessage, VirtualView virtualView) {

        // if is 1st add it and request number players.
        if (game.getNumCurrentPlayers() == 0) {
            game.addPlayer(new Player(receivedMessage.getNickname()));
            virtualView.askPlayersNumber();
        } // if not 1st and there is some available slot check nickname.
        else if (!(game.isPlayerInList(receivedMessage.getNickname())) &&
                !(receivedMessage.getNickname().equals( "server" ))) {
            game.addPlayer(new Player(receivedMessage.getNickname()));
            // if latest player is logged then change gameState from LOGIN to INIT.
            if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber()) {
                gameState = GameState.INIT;
            }
            virtualView.showLoginResult(true, true);
        } else {
            virtualView.showLoginResult(false, true);
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
