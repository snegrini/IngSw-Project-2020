package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.observer.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player extends Observable {

    private final String nickname;
    private List<Worker> workers;
    private God god;
    private PlayerState state;

    public Player(String nickname) {
        this.nickname = nickname;
        this.workers = new ArrayList<>();
    }

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

    public God getGod() {
        return god;
    }

    public void setGod(God god) {
        this.god = god;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public List<Position> getWorkersPositions() {
        List<Position> positionList = new ArrayList<>();
        for (Worker w : workers) {
            positionList.add(w.getPosition());
        }
        return positionList;
    }

    public List<Position> getValidWorkersPositions() {
        List<Position> positionList = getWorkersPositions();

        for (Position p : positionList) {
            List<Position> possibleMoves = getWorkerByPosition(p).getPossibleMoves();
            if (possibleMoves.isEmpty()) {
                positionList.remove(p);
            } else {
                for (Position pos : possibleMoves) {
                    Worker tempWorker = new Worker(pos);
                    if (tempWorker.getPossibleBuilds().isEmpty()) {
                        possibleMoves.remove(pos);
                    }
                }
                if (possibleMoves.isEmpty()) {
                    positionList.remove(p);
                }

            }
        }
        return positionList;
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
}
