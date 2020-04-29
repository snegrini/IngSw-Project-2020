package observer;

import model.God;
import model.ReducedGod;
import model.board.Position;
import model.enumerations.Color;

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

}
