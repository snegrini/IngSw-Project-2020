package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.List;
import java.util.Map;

public interface ViewObserver {

    void onUpdateServerInfo(Map<String, String> serverInfo);

    void onUpdateNickname(String nickname);

    void onUpdatePlayersNumber(int playerNumber);

    void onUpdateWorkersColor(Color color);

    void onUpdateGod(List<ReducedGod> reducedGods);

    void onUpdatePickMovingWorker(Position pst_worker);

    void onUpdateMove(Position destination);

    void onUpdateBuild(Position newBuild);

    void onUpdateInitWorkerPosition(List<Position> positions);

    void onUpdateEnableEffect(boolean response);

    void onUpdateApplyEffect(Position dest);
}
