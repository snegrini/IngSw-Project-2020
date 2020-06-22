package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.enumerations.TargetType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.BoardMessage;
import it.polimi.ingsw.network.message.LobbyMessage;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.parser.GodParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game extends Observable implements Serializable {

    private static final long serialVersionUID = 4405183481677036856L;
    private static Game instance;

    public static final int MAX_PLAYERS = 3;
    public static final String SERVER_NICKNAME = "server";

    private final Board board;
    private List<Player> players;
    private List<God> gods;
    private int chosenPlayersNumber;

    private Game() {
        this.board = new Board();
        this.players = new ArrayList<>();
        this.gods = GodParser.parseGods("gods.xml");
    }

    /**
     * @return the singleton instance.
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }


    public void restoreGame(Game instance, Board board, List<Player> players, List<God> gods, int chosenPlayersNumber) {
        //this.instance = instance;
        //this.board = board;
        this.board.restoreBoard(board.getSpaces());
        this.players = players;
        /*for( int i = 0; i < Game.getInstance().getNumCurrentPlayers(); i++) {
            Player p = players.get(i);
            this.players.get(i).restorePlayer(p.getWorkers(), p.getGod(), p.getState());
        }*/
        this.gods = gods;
        this.chosenPlayersNumber = chosenPlayersNumber;
    }

    /**
     * Returns a player given his {@code nickname}.
     * Only the first occurrence is returned because
     * the player nickname is considered to be unique.
     * If no player is found {@code null} is returned.
     *
     * @param nickname the nickname of the player to be found.
     * @return Returns the player given his {@code nickname}, {@code null} otherwise.
     */
    public Player getPlayerByNickname(String nickname) {
        return players.stream()
                .filter(player -> nickname.equals(player.getNickname()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a player to the game.
     * Notifies all the views if the playersNumber is already set.
     *
     * @param player the player to add to the game.
     */
    public void addPlayer(Player player) {
        players.add(player);
        if (chosenPlayersNumber != 0) {
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.chosenPlayersNumber));
        }
    }

    /**
     * Removes a player from the game.
     * Notifies all the views if the notifyEnabled argument is set to {@code true}.
     *
     * @param nickname      the nickname of the player to remove from the game.
     * @param notifyEnabled set to {@code true} to enable a lobby disconnection message, {@code false} otherwise.
     * @return {@code true} if the player is removed, {@code false} otherwise.
     */
    public boolean removePlayerByNickname(String nickname, boolean notifyEnabled) {
        boolean result = players.remove(getPlayerByNickname(nickname));

        if (notifyEnabled) {
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.chosenPlayersNumber));
        }

        return result;
    }

    /**
     * Removes all the workers of the given player nickname from the board.
     * A notification with the updated board is sent to all the views.
     *
     * @param activePlayerNickname the nickname of the player whose workers must be removed.
     */
    public void removeWorkers(String activePlayerNickname) {

        for (Position p : getPlayerByNickname(activePlayerNickname).getWorkersPositions()) {
            board.getSpace(p).removeWorker();
        }

        notifyObserver(new BoardMessage(Game.SERVER_NICKNAME, MessageType.BOARD, getReducedSpaceBoard()));
    }

    /**
     * Number of current players added in the game.
     *
     * @return the number of players.
     */
    public int getNumCurrentPlayers() {
        return players.size();
    }

    /**
     * Sets the max number of players chosen by the first player joining the game.
     *
     * @param chosenMaxPlayers the max players number. Value can be {@code 0 < x < MAX_PLAYERS}.
     * @return {@code true} if the argument value is {@code 0 < x < MAX_PLAYERS}, {@code false} otherwise.
     */
    public boolean setChosenMaxPlayers(int chosenMaxPlayers) {
        if (chosenMaxPlayers > 0 && chosenMaxPlayers <= MAX_PLAYERS) {
            this.chosenPlayersNumber = chosenMaxPlayers;
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.chosenPlayersNumber));
            return true;
        }
        return false;
    }

    /**
     * Returns the number of players chosen by the first player.
     *
     * @return the number of players chosen by the first player.
     */
    public int getChosenPlayersNumber() {
        return chosenPlayersNumber;
    }

    /**
     * Returns a worker given the position argument.
     *
     * @param position the position of the worker.
     * @return the worker found, {@code null} if not found.
     */
    public Worker getWorkerByPosition(Position position) {
        return board.getWorkerByPosition(position);
    }

    /**
     * Search a nickname in the current Game.
     *
     * @param nickname the nickname of the player.
     * @return {@code true} if the nickname is found, {@code false} otherwise.
     */
    public boolean isNicknameTaken(String nickname) {
        return players.stream()
                .anyMatch(p -> nickname.equals(p.getNickname()));
    }

    /**
     * Resets the game instance. After this operations, all the game data is lost.
     */
    public static void resetInstance() {
        Game.instance = null;
    }

    /**
     * Returns a list of reduced gods.
     *
     * @return a list with all reduced gods.
     */
    public List<ReducedGod> getReduceGodList() {
        List<ReducedGod> reducedGods = new ArrayList<>();
        for (God god : gods) {
            reducedGods.add(new ReducedGod(god));
        }
        return reducedGods;
    }

    /**
     * Return the god given his name.
     *
     * @param godName the name of the god.
     * @return the corresponding God object, {@code null} if it is not found.
     */
    public God getGodByName(String godName) {
        return gods.stream()
                .filter(god -> godName.equals(god.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a list of player nicknames that are already in-game.
     *
     * @return a list with all nicknames in the Game
     */
    public List<String> getPlayersNicknames() {
        List<String> nicknames = new ArrayList<>();
        for (Player p : players) {
            nicknames.add(p.getNickname());
        }
        return nicknames;
    }

    /**
     * Returns the current board.
     *
     * @return the board of the game.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the workers on the board at the worker position. This method should be called only on game start.
     * See {@link it.polimi.ingsw.model.board.Board#initWorkers}.
     *
     * @param workers a list of workers.
     */
    public void initWorkersOnBoard(List<Worker> workers) {
        board.initWorkers(workers);
    }

    /**
     * Moves a worker to the given {@code Position}.
     * Finally, a notification to the views is sent.
     * See {@link it.polimi.ingsw.model.board.Board#moveWorker}.
     *
     * @param worker the worker to be moved.
     * @param dest   the destination of the move.
     */
    public void moveWorker(Worker worker, Position dest) {
        board.moveWorker(worker, dest);
    }

    /**
     * Builds a single block over the {@code Space} at the given position.
     * See {@link it.polimi.ingsw.model.board.Board#buildBlock}.
     *
     * @param worker the worker who builds.
     * @param dest   the space position to build onto.
     */
    public void buildBlock(Worker worker, Position dest) {
        board.buildBlock(worker, dest);
    }

    /**
     * Returns a matrix of ReducedSpace which is an immutable object.
     * See {@link it.polimi.ingsw.model.board.Board#getReducedSpaceBoard}.
     *
     * @return a board of reduced spaces
     */
    public ReducedSpace[][] getReducedSpaceBoard() {
        return board.getReducedSpaceBoard();
    }

    /**
     * Returns the free positions on the board.
     * See {@link it.polimi.ingsw.model.board.Board#getFreePositions}.
     *
     * @return the free positions on the board.
     */
    public List<Position> getFreePositions() {
        return board.getFreePositions();
    }

    /**
     * Check if positionList refers only to free spaces.
     * See {@link it.polimi.ingsw.model.board.Board#arePositionsFree}.
     *
     * @param positionList positionList from client.
     * @return {@code true} if all the positions in the argument list are free, {@code false} otherwise.
     */
    public boolean arePositionsFree(List<Position> positionList) {
        return board.arePositionsFree(positionList);
    }

    /**
     * Return the next Space on the line passing between {@code orig} and {@code dest}.
     * See {@link it.polimi.ingsw.model.board.Board#getNextSpaceInLine}.
     *
     * @param orig the starting position.
     * @param dest the destination position.
     * @return the next Space on the line passing between {@code orig} and {@code dest},
     * {@code null} if the next Space on the line is invalid.
     */
    public Space getNextSpaceInLine(Position orig, Position dest) {
        return board.getNextSpaceInLine(orig, dest);
    }

    /**
     * Returns a list of positions that are adjacent to the position argument.
     * See {@link it.polimi.ingsw.model.board.Board#getNeighbours}.
     *
     * @param position The position to look for the neighbours.
     * @return The list of spaces adjacent to this space.
     */
    public List<Position> getNeighbours(Position position) {
        return board.getNeighbours(position);
    }

    /**
     * Returns a list of positions that are adjacent to the position argument and are occupied by a worker.
     * See {@link it.polimi.ingsw.model.board.Board#getNeighbourWorkers}.
     *
     * @param position The position to look for the neighbours.
     * @param oppOnly  If set to {@code true} only opponent workers are checked.
     * @return The list of spaces adjacent to this space.
     */
    public List<Position> getNeighbourWorkers(Position position, boolean oppOnly) {
        return board.getNeighbourWorkers(position, oppOnly);
    }

    /**
     * Returns the MoveType needed to perform the move from the first position argument to
     * the second position argument. Comparison is done by checking the current levels
     * of the spaces.
     * Returns {@code null} if the arguments are not neighbours.
     * See {@link it.polimi.ingsw.model.board.Board#getMoveTypeByLevel}.
     *
     * @param orig the starting position.
     * @param dest the destination position.
     * @return the MoveType needed to perform the move from the first position argument to
     * the second position argument. Returns {@code null} if the arguments are not neighbours or if
     * the origin position is the same as the destination.
     */
    public MoveType getMoveTypeByLevel(Position orig, Position dest) {
        return board.getMoveTypeByLevel(orig, dest);
    }

    /**
     * Returns the level of the given space.
     *
     * @param position the position of the space to be checked.
     * @return the level of the given space.
     */
    public int getSpaceLevel(Position position) {
        return board.getSpace(position).getLevel();
    }

    /**
     * Returns the enemy workers given a worker.
     *
     * @param worker the target worker.
     * @return a List of Worker that are enemies of the argument.
     */
    public List<Worker> getEnemyWorkers(Worker worker) {
        List<Worker> allWorkers = new ArrayList<>();
        for (Player player : players) {
            for (Position position : player.getWorkersPositions()) {
                allWorkers.add(board.getWorkerByPosition(position));
            }
        }
        return allWorkers.stream()
                .filter(w -> !w.getColor().equals(worker.getColor()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the ally workers given a worker.
     *
     * @param worker the target worker.
     * @return a List of Worker that are ally of the argument worker.
     */
    public List<Worker> getAllyWorkers(Worker worker) {
        List<Worker> allWorkers = new ArrayList<>();

        for (Player player : players) {
            for (Position position : player.getWorkersPositions()) {
                Worker tmpWorker = board.getWorkerByPosition(position);

                if (tmpWorker.getColor().equals(worker.getColor())) {
                    allWorkers.add(tmpWorker);
                }
            }
        }

        return allWorkers;
    }


    /**
     * Returns the other player worker given a worker.
     *
     * @param worker a player worker.
     * @return the other player worker, {@code null} if none is found.
     */
    public Worker getOtherWorker(Worker worker) {
        for (Player player : players) {
            for (Position position : player.getWorkersPositions()) {
                Worker tmpWorker = board.getWorkerByPosition(position);

                if (!tmpWorker.equals(worker) && tmpWorker.getColor().equals(worker.getColor())) {
                    return tmpWorker;
                }
            }
        }
        return null;
    }

    /**
     * Returns a list of workers based on the target type parameter and the base worker.
     *
     * @param worker     the base worker used for comparison.
     * @param targetType the target type.
     * @return a list of workers.
     */
    public List<Worker> getWorkersByTargetType(Worker worker, TargetType targetType) {
        List<Worker> workerList;

        switch (targetType) {
            case ALL_YOUR_WORKERS:
                workerList = getAllyWorkers(worker);
                break;
            case ALL_OPP_WORKERS:
                workerList = getEnemyWorkers(worker);
                break;
            case YOUR_ACTIVE_WORKER:
                workerList = List.of(worker);
                break;
            case YOUR_WORKER:
                workerList = List.of(getOtherWorker(worker));
                break;
            default: // should never reach this condition.
                workerList = List.of();
                break;
        }
        return workerList;
    }

    /**
     * Returns a list of players.
     *
     * @return the players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns a list of gods available in the game.
     *
     * @return a list of all the gods available in the game.
     */
    public List<God> getGods() {
        return gods;
    }
}
