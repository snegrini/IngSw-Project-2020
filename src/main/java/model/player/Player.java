package model.player;

import model.God;
import model.enumerations.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private static final int NUM_WORKERS = 2;

    private static String nickname;
    private List<Worker> workers;
    private God god;
    private PlayerState state;

    public Player() {
        this.workers = new ArrayList<Worker>();
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public static void setNickname(String nickname) {
        Player.nickname = nickname;
    }

    public void setGod(God god) {
        this.god = god;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public static String getNickname() {
        return nickname;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public God getGod() {
        return god;
    }

    public PlayerState getState() {
        return state;
    }
}
