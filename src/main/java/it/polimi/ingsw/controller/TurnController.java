package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.persistence.StorageData;
import it.polimi.ingsw.view.VirtualView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This Class contains all methods used to manage every single turn of the match.
 */
public class TurnController implements Serializable {

    private static final long serialVersionUID = -5987205913389392005L;
    private final Game game;
    private final List<String> nicknameQueue;
    private String activePlayer;
    private Worker activeWorker;
    private Effect appliedEffect;


    transient Map<String, VirtualView> virtualViewMap;
    private PhaseType phaseType;

    private final GameController gameController;

    /**
     * Constructor of the Turn Controller.
     * @param virtualViewMap Virtual View Map of all Clients.
     * @param gameController Game Controller.
     */
    public TurnController(Map<String, VirtualView> virtualViewMap, GameController gameController) {
        this.game = Game.getInstance();
        nicknameQueue = new ArrayList<>(game.getPlayersNicknames());

        activePlayer = nicknameQueue.get(0); // set first active player
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
    }

    /**
     * @return nickname of the active player
     */
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * Set the active player.
     *
     * @param activePlayer is the active Player.
     */
    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * Set next active player.
     */
    public void next() {

        if (null != appliedEffect) {
            appliedEffect.clear(activeWorker);
            appliedEffect = null;
        }


        int currentActive = nicknameQueue.indexOf(activePlayer);
        if (currentActive + 1 < game.getNumCurrentPlayers()) {
            currentActive = currentActive + 1;
        } else {
            currentActive = 0;
        }
        activePlayer = nicknameQueue.get(currentActive);
        phaseType = PhaseType.YOUR_MOVE;
    }

    /**
     * Set the Active Worker.
     *
     * @param worker of Active Player.
     */
    public void setActiveWorker(Worker worker) {
        this.activeWorker = worker;
    }

    /**
     * @return the Active Worker.
     */
    public Worker getActiveWorker() {
        return activeWorker;
    }

    /**
     * Set the current Phase Type.
     *
     * @param turnPhaseType Phase Type.
     */
    public void setPhaseType(PhaseType turnPhaseType) {
        this.phaseType = turnPhaseType;
    }


    /**
     * @return the current Phase Type.
     */
    public PhaseType getPhaseType() {
        return phaseType;
    }


    /**
     * Initialize a new Turn.
     */
    public void newTurn() {

        turnControllerNotify("Turn of " + activePlayer);

        StorageData storageData = new StorageData();
        storageData.store(gameController);

        setPhaseType(PhaseType.YOUR_MOVE);
        pickWorker();
    }

    /**
     * Ask to Active Player which Worker want to Move.
     */
    public void pickWorker() {

        Player player = game.getPlayerByNickname(getActivePlayer());
        List<Position> positionList = new ArrayList<>(player.getValidWorkersPositions());
        VirtualView virtualView = virtualViewMap.get(getActivePlayer());
        if (positionList.isEmpty()) {
            lose();
        } else {
            virtualView.askMovingWorker(positionList);
        }
    }

    /**
     * Ask to Active Player where to Move the Active Worker.
     *
     * @param skipEffect {@code true} if effect should be by-passed {@code false} ohterwise.
     */
    public void movePhase(boolean skipEffect) {

        setPhaseType(PhaseType.YOUR_MOVE);
        // EFFECT REQUIRE YOUR MOVE
        phaseBody(skipEffect);
    }


    /**
     * Convert every buildPhase() calls into buildPhase(false).
     */
    private void buildPhase() {
        buildPhase(false);
    }

    /**
     * Set the applied Effect.
     *
     * @param appliedEffect Effect already applied.
     */
    public void setAppliedEffect(Effect appliedEffect) {
        this.appliedEffect = appliedEffect;
    }

    /**
     * Ask to Active Player where to Build.
     *
     * @param skipEffect {@code true} if effect should be by-passed {@code false} ohterwise.
     */
    private void buildPhase(boolean skipEffect) {

        setPhaseType(PhaseType.YOUR_BUILD);
        // Check effect "your build"
        phaseBody(skipEffect);
    }

    /**
     * Shared istruction for both move & build phases.
     *
     * @param skipEffect {@code true} if effect should be by-passed {@code false} ohterwise.
     */
    private void phaseBody(boolean skipEffect) {
        VirtualView virtualView = virtualViewMap.get(getActivePlayer());
        Player player = game.getPlayerByNickname(activePlayer);

        if (checkEffectPhase(getPhaseType()) && !skipEffect && requireEffect()) {
            Effect effect = player.getGod().getEffectByType(getPhaseType());
            if (effect.isUserConfirmNeeded()) {
                virtualView.askEnableEffect(false);
            } else {
                if (getPhaseType().equals(PhaseType.YOUR_MOVE) && !player.getGod().getName().equals("Prometheus")) {
                    virtualView.askEnableEffect(true);
                } else {
                    effect.apply(activeWorker, null);
                    //effect.clear(getActiveWorker());
                    appliedEffect = effect;
                    continueGame(virtualView);
                }
            }
        } else {
            continueGame(virtualView);
        }
    }

    /**
     * Change phase of the active turn.
     * @param virtualView Virtual View of active Player.
     */
    private void continueGame(VirtualView virtualView) {
        switch (getPhaseType()) {
            case YOUR_MOVE:
                if (!getActiveWorker().getPossibleMoves().isEmpty()) {
                    virtualView.askMove(getActiveWorker().getPossibleMoves());
                } else {
                    lose();
                }
                break;
            case YOUR_BUILD:
                if (!getActiveWorker().getPossibleBuilds().isEmpty()) {
                    virtualView.askBuild(getActiveWorker().getPossibleBuilds());
                } else {
                    lose();
                }
                break;
            default:
                Server.LOGGER.warning("Invalid game state!");
                break;
        }
    }

    /**
     * Method for Lose Phase: if there are 3 players then remove the looser's worker and continue the game
     * else call endGame() and finish the Game.
     */
    private void lose() {
        // if players.size == 3 then remove looser's workers from board. And notify all.
        // else endgame.
        if (3 == game.getNumCurrentPlayers()) {
            game.removeWorkers(activePlayer);
            // disconnect 3° player, notify all
            turnControllerNotify(activePlayer + " LOOSE.");
            nicknameQueue.remove(activePlayer);
            game.removePlayerByNickname(activePlayer, false);
            broadcastMatchInfo();
            next();
            newTurn();
        } else {

            next();
            // next player wins.
            gameController.win();

        }
    }

    /**
     * Broadcast Match Info to all Clients.
     */
    private void broadcastMatchInfo() {
        List<ReducedGod> gods = new ArrayList<>();
        for (String s : nicknameQueue) {
            gods.add(new ReducedGod(game.getPlayerByNickname(s).getGod()));
        }

        for (VirtualView vv : virtualViewMap.values()) {
            vv.showMatchInfo(nicknameQueue, gods, activePlayer);
        }
    }


    /**
     * Go to Next Phase.
     */
    public void nextPhase() {
        switch (getPhaseType()) {

            case YOUR_MOVE:
            case YOUR_MOVE_AFTER:
                buildPhase();
                break;
            case YOUR_BUILD:
            case YOUR_BUILD_AFTER:
                next();
                newTurn();
                break;
            default: // should never reach this condition.
                // TODO
                break;
        }
    }

    /**
     * Check if Active Player's Effect is applicable in current Phase.
     *
     * @param currentPhaseType current Phase Type
     * @return {@code true} if Effect is applicable, {@code false} otherwise.
     */
    public boolean checkEffectPhase(PhaseType currentPhaseType) {
        return (game.getPlayerByNickname(getActivePlayer()).getGod().getEffectByType(currentPhaseType) != null);
    }

    /**
     * @return {@code true} if Effect' Require is satisfied, {@code false} otherwise.
     */
    public boolean requireEffect() {
        return game.getPlayerByNickname(getActivePlayer()).getGod()
                .getEffectByType(getPhaseType())
                .require(getActiveWorker());
    }

    /**
     * Resume to previous Phase.
     */
    public void resumePhase() {

        switch (getPhaseType()) {
            case YOUR_MOVE:
                movePhase(true);
                break;
            case YOUR_MOVE_AFTER:
            case YOUR_BUILD:
                buildPhase(true);
                break;
            case YOUR_BUILD_AFTER:
                next();
                newTurn();
                break;
            default: // Should never reach this condition.
                // TODO
                break;
        }
    }


    /**
     * Sends a Message which contains Turn Information to every Players in Game.
     *
     * @param messageToNotify Message to send.
     */
    private void turnControllerNotify(String messageToNotify) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showGenericMessage(messageToNotify);
            vv.showMatchInfo(null, null, activePlayer);
        }
    }

    /**
     * Returns a list of Players' nicknames.
     *
     * @return a list of String.
     */
    public List<String> getNicknameQueue() {
        return nicknameQueue;
    }

    /**
     * Set the virtual view map.
     * @param virtualViewMap Virtual View Map.
     */
    public void setVirtualViewMap(Map<String, VirtualView> virtualViewMap) {
        this.virtualViewMap = virtualViewMap;
    }

}
