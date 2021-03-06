package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.observer.Observable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Player is the main character of the Game.
 */
public class Player extends Observable implements Serializable {

    private static final long serialVersionUID = 7470069648548034935L;

    private final String nickname;
    private final List<Worker> workers;
    private God god;
    private PlayerState state;

    /**
     * Default constructor.
     *
     * @param nickname nickname of the Player.
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.workers = new ArrayList<>();
    }

    /**
     * Return the nickname of the Player.
     *
     * @return nickname of the Player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the worker given his position.
     *
     * @param position the position where the worker should be returned.
     * @return Returns the worker given his position.
     * {@code null} is returned if no worker is found.
     */
    public Worker getWorkerByPosition(Position position) {
        return workers.stream()
                .filter(w -> position.equals(w.getPosition()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds the given worker to the Player.
     *
     * @param worker the worker to be added.
     */
    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    /**
     * Sets the workers at the given positions. This method should be called only on game start.
     * The positions list argument must be of the same size of the player workers.
     *
     * @param positions a list of positions.
     */
    public void initWorkers(List<Position> positions) {
        for (int i = 0; i < workers.size(); i++) {
            workers.get(i).initPosition(positions.get(i));
        }
    }

    /**
     * Return the God associated to this Player.
     *
     * @return God of the Player.
     */
    public God getGod() {
        return god;
    }

    /**
     * Set the God associated to the PLayer.
     *
     * @param god God of the Player.
     */
    public void setGod(God god) {
        this.god = god;
    }

    /**
     * Return the State of the Player.
     *
     * @return State of the Player.
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * Set the State of the Player.
     *
     * @param state State of the Player.
     */
    public void setState(PlayerState state) {
        this.state = state;
    }

    /**
     * Return a list of positions of player's workers.
     *
     * @return Position List of Workers.
     */
    public List<Position> getWorkersPositions() {
        List<Position> positionList = new ArrayList<>();
        for (Worker w : workers) {
            positionList.add(w.getPosition());
        }
        return positionList;
    }

    /**
     * Returns a list of positions only of the player's workers that can be moved.
     *
     * @return Position List of Workers.
     */
    public List<Position> getValidWorkersPositions() {
        List<Position> positionList = getWorkersPositions();
        List<Position> badPositions = new ArrayList<>();

        for (Position pos : positionList) {
            List<Position> possibleMoves = getWorkerByPosition(pos).getPossibleMoves();

            // Check effect requirements
            if (possibleMoves.isEmpty() && !checkEffect(pos)) {
                badPositions.add(pos); // Add here and remove later
            }
        }
        positionList.removeAll(badPositions);

        return positionList;
    }

    /**
     * Checks the effect of the player's god.
     * Returns {@code true} if the effect is requireable and is of type YOUR_MOVE, {@code false} otherwise.
     *
     * @param position the position to be used to check the effect.
     * @return {@code true} if the effect is requireable and is of type YOUR_MOVE, {@code false} otherwise.
     */
    private boolean checkEffect(Position position) {
        Effect effect = this.getGod().getEffectByType(PhaseType.YOUR_MOVE);

        return effect != null && effect.require(getWorkerByPosition(position));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return nickname.equals(player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

    /**
     * Returns the color of the player's workers.
     *
     * @return color of the workers.
     */
    public Color getWorkersColor() {
        return workers.get(0).getColor();
    }
}
