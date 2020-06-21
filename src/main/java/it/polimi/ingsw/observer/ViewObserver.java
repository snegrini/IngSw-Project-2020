package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.List;
import java.util.Map;

public interface ViewObserver {

    /**
     * Create a new connection to the server with the updated info.
     *
     * @param serverInfo a map of server address and server port.
     */
    void onUpdateServerInfo(Map<String, String> serverInfo);

    /**
     * Sends a message to the server with the updated nickname.
     *
     * @param nickname the nickname to be sent.
     */
    void onUpdateNickname(String nickname);

    /**
     * Sends a message to the server with the player number chosen by the user.
     *
     * @param playersNumber the number of players.
     */
    void onUpdatePlayersNumber(int playersNumber);

    /**
     * Sends a message to the server with the workers color chosen by the user.
     *
     * @param color the color of the workers.
     */
    void onUpdateWorkersColor(Color color);

    /**
     * Sends a message to the server with the gods chosen by the user.
     *
     * @param reducedGods the list of gods chosen by the user.
     */
    void onUpdateGod(List<ReducedGod> reducedGods);

    /**
     * Sends a message to the server with the position of the worker to be moved chosen by the user.
     *
     * @param position the position of the worker to be moved.
     */
    void onUpdatePickMovingWorker(Position position);

    /**
     * Sends a message to the server with the initial position of the workers chosen by the user.
     *
     * @param positions the list of the initial position of the workers.
     */
    void onUpdateInitWorkerPosition(List<Position> positions);

    /**
     * Sends a message to the server with the new position of the moving worker chosen by the user.
     *
     * @param destination the new position of the moving worker.
     */
    void onUpdateMove(Position destination);

    /**
     * Sends a message to the server with the position of the block to be built chosen by the user.
     *
     * @param position the position of the block to be built.
     */
    void onUpdateBuild(Position position);

    /**
     * Sends a message to the server with the choice of the user about his god effect.
     *
     * @param response the choice of the user about his god effect.
     */
    void onUpdateEnableEffect(boolean response);

    /**
     * Sends a message to the server with the choice of the user about his god effect.
     *
     * @param dest the choice of the user about his god effect.
     */
    void onUpdateApplyEffect(Position dest);

    /**
     * Sends a message to the server with the nickname of the first player chosen by the user.
     *
     * @param nickname the nickname of the first player.
     */
    void onUpdateFirstPlayer(String nickname);

    /**
     * Handles a disconnection wanted by the user.
     * (e.g. a click on the back button into the GUI).
     */
    void onDisconnection();
}
