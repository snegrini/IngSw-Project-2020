package model.player;

import model.God;
import model.board.Position;
import model.enumerations.PlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {

    private static final int MAX_WORKERS = 2;

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

    public Worker getWorkerByPosition(Position position) {
        return workers.stream()
                .filter(w -> position.equals(w.getPosition()))
                .findFirst()
                .orElse(null);
    }

    public void addWorker(Worker worker) {
        // TODO: check workers with unique id
        workers.add(worker);
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
