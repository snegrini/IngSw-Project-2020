package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TurnController {

    private Game game;
    private List<String> nicknameQueue;
    private String activePlayer;
    private Worker activeWorker;

    Map<String, VirtualView> virtualViewMap;
    private EffectType phaseType;

    public TurnController(Map<String, VirtualView> virtualViewMap) {
        this.game = Game.getInstance();
        nicknameQueue = new ArrayList<>(game.getPlayersNicknames());

        activePlayer = nicknameQueue.get(0); // TODO set first active player
        this.virtualViewMap = virtualViewMap;
    }

    /**
     * @return nickname of the active player
     */
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * Set next active player.
     */
    public void next() {

        int currentActive = nicknameQueue.indexOf(activePlayer);
        if (currentActive + 1 < game.getNumCurrentPlayers()) {
            currentActive = currentActive + 1;
        } else {
            currentActive = 0;
        }
        activePlayer = nicknameQueue.get(currentActive);
        phaseType = EffectType.YOUR_MOVE;
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
    public void setPhaseType(EffectType turnPhaseType) {
        this.phaseType = turnPhaseType;
    }


    /**
     * @return the current Phase Type.
     */
    public EffectType getPhaseType() {
        return phaseType;
    }


    /**
     * Initialize a new Turn.
     */
    public void newTurn() {
        setPhaseType(EffectType.YOUR_MOVE);
        pickWorker();
    }

    /**
     * Ask to Active Player which Worker want to Move.
     */
    private void pickWorker() {

        Player player = game.getPlayerByNickname(getActivePlayer());
        List<Position> positionList = new ArrayList<>(player.getWorkersPositions());
        VirtualView virtualView = virtualViewMap.get(getActivePlayer());
        virtualView.askMovingWorker(positionList);
    }

    /**
     * Ask to Active Player where to Move the Active Worker.
     *
     * @param skipEffect Skip Effect if Effect is already been checked.
     */
    public void movePhase(boolean skipEffect) {


        VirtualView virtualView = virtualViewMap.get(getActivePlayer());
        setPhaseType(EffectType.YOUR_MOVE);

        // EFFECT REQUIRE YOUR MOVE

        if (checkEffectPhase(getPhaseType()) && !skipEffect) {
            if (requireEffect()) {
                virtualView.askEnableEffect();
            } else {
                virtualView.askMove(getActiveWorker().getPossibleMoves());
            }
        } else {
            virtualView.askMove(getActiveWorker().getPossibleMoves());
        }
    }


    /**
     * Convert every buildPhase() calls into buildPhase(false).
     */
    private void buildPhase() {
        buildPhase(false);
    }

    /**
     * Ask to Active Player where to Build.
     */
    private void buildPhase(boolean skipEffect) {

        VirtualView virtualView = virtualViewMap.get(getActivePlayer());
        setPhaseType(EffectType.YOUR_BUILD);

        // CHECK EFFECT YOUR_BUILD
        if (checkEffectPhase(getPhaseType()) && !skipEffect) {
            if (requireEffect()) {
                virtualView.askEnableEffect();
            } else {
                virtualView.askBuild(getActiveWorker().getPossibleBuilds());
            }
        } else {
            virtualView.askBuild(getActiveWorker().getPossibleBuilds());
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
            case YOUR_TURN: // TODO valutare se rimuovere queste ENUM.
            case MOVE_AGAIN:
            case BUILD_AGAIN:
            case OPP_TURN:
            case WIN_COND:
                break;
        }
    }

    /**
     * Check if Active Player's Effect is applicable in current Phase.
     *
     * @param currentPhaseType current Phase Type
     * @return {@code true} if Effect is applicable, {@code false} otherwise.
     */
    public boolean checkEffectPhase(EffectType currentPhaseType) {
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
            case YOUR_TURN: // TODO valutare se rimuovere queste ENUM.
            case MOVE_AGAIN:
            case BUILD_AGAIN:
            case OPP_TURN:
            case WIN_COND:
                break;
        }
    }

}
