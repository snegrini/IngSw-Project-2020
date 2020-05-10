package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.enumerations.TargetType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.parser.GodParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game extends Observable {

    private static Game instance;

    public static final int MAX_PLAYERS = 3;
    public static final String SERVER_NICKNAME = "server";


    private Board board;
    private List<Player> players;
    private List<God> gods;
    private int chosenPlayersNumber;

    private Game() {
        this.board = new Board();
        this.players = new ArrayList<>();
        this.gods = GodParser.parseGods();
    }

    /**
     * @return the singleton instance.
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
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
     *
     * @param player the player to add to the game.
     */
    public void addPlayer(Player player) {
        players.add(player);
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
            return true;
        }
        return false;
    }

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

    public Board getBoard() {
        return board;
    }


    public void initWorkersOnBoard(List<Worker> workers) {
        board.initWorkers(workers);
    }

    public void moveWorker(Worker worker, Position dest) {
        board.moveWorker(worker, dest);
    }

    public void buildBlock(Worker worker, Position dest) {
        board.buildBlock(worker, dest);
    }


    public ReducedSpace[][] getReducedSpaceBoard() {
        return board.getReducedSpaceBoard();
    }

    public List<Position> getFreePositions() {
        return board.getFreePositions();
    }

    public boolean arePositionsFree(List<Position> positionList) {
        return board.arePositionsFree(positionList);
    }

    public Space getNextSpaceInLine(Position orig, Position dest) {
        return board.getNextSpaceInLine(orig, dest);
    }

    public List<Position> getNeighbours(Position position) {
        return board.getNeighbours(position);
    }

    public List<Position> getNeighbourWorkers(Position position, boolean oppOnly) {
        return board.getNeighbourWorkers(position, oppOnly);
    }

    public MoveType getMoveTypeByLevel(Position orig, Position dest) {
        return board.getMoveTypeByLevel(orig, dest);
    }

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
}
