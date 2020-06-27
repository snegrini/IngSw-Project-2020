package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.List;

/**
 * Defines a generic view to be implemented by each view type (e.g. CLI, GUI in JavaFX, ...).
 */
public interface View {
    /**
     * Asks the user to choose a Nickname.
     */
    void askNickname();

    /**
     * Asks the user to choose the worker to be moved.
     *
     * @param positionList the list of available workers' positions.
     */
    void askMovingWorker(List<Position> positionList);

    /**
     * Asks the user the position where he wants to move the worker.
     *
     * @param positionList the list of valid move positions.
     */
    void askMove(List<Position> positionList);

    /**
     * Asks the user the initial positions of his workers.
     *
     * @param positions the list of available positions.
     */
    void askInitWorkersPositions(List<Position> positions);

    /**
     * Asks the user how many players he wants to play with.
     */
    void askPlayersNumber();

    /**
     * Allows the user to choose his workers' color.
     *
     * @param colors the list of the available colors.
     */
    void askInitWorkerColor(List<Color> colors);

    /**
     * Allows the user to choose his God.
     *
     * @param gods    the list of the available Gods.
     * @param request the number of gods to be selected by the user.
     */
    void askGod(List<ReducedGod> gods, int request);

    /**
     * Asks to the user to choose a new position where to build.
     *
     * @param positions the list of the available positions.
     */
    void askBuild(List<Position> positions);

    /**
     * Asks to the user to choose a new position where to move the worker.
     * This method is called only when a effect is applied.
     *
     * @param positions the list of the available positions.
     */
    void askMoveFx(List<Position> positions);

    /**
     * Asks to the user to choose a new position where to build.
     * This method is called only when a effect is applied.
     *
     * @param positions the list of the available positions.
     */
    void askBuildFx(List<Position> positions);

    /**
     * Shows to the user if the Login succeeded.
     *
     * @param nicknameAccepted     indicates if the chosen nickname has been accepted.
     * @param connectionSuccessful indicates if the connection has been successful.
     * @param nickname             the nickname of the player to be greeted.
     */
    void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname);

    /**
     * Shows a generic message.
     *
     * @param genericMessage the generic message to be shown.
     */
    void showGenericMessage(String genericMessage);

    /**
     * Shows a disconnection message.
     *
     * @param nicknameDisconnected the nickname of the player who has disconnected.
     * @param text                 a generic info text message.
     */
    void showDisconnectionMessage(String nicknameDisconnected, String text);

    /**
     * Shows an error message.
     *
     * @param error the error message to be shown.
     */
    void showErrorAndExit(String error);

    /**
     * Shows the board on the screen.
     *
     * @param spaces the board to be shown.
     */
    void showBoard(ReducedSpace[][] spaces);

    /**
     * Shows the lobby with connected players.
     *
     * @param nicknameList list of players.
     * @param numPlayers   number of players.
     */
    void showLobby(List<String> nicknameList, int numPlayers);

    /**
     * Asks the user if he wants to enable the effect.
     *
     * @param forceApply set to {@code true} to enable the effect without user interaction, {@code false} otherwise.
     */
    void askEnableEffect(boolean forceApply);

    /**
     * Shows the match info.
     * Infos are about the active player, the other players and their gods.
     *
     * @param players      the list of currently playing players.
     * @param gods         the list of gods chosen by each player.
     * @param activePlayer the currently active player (player's turn).
     */
    void showMatchInfo(List<String> players, List<ReducedGod> gods, String activePlayer);

    /**
     * Asks the "chosen player" who he wants the game to start from.
     *
     * @param nicknameList the list of nicknames of all the players.
     * @param godList      the list of gods of the players.
     */
    void askFirstPlayer(List<String> nicknameList, List<ReducedGod> godList);

    /**
     * Shows a win message.
     *
     * @param winner the nickname of the winner.
     */
    void showWinMessage(String winner);

}
