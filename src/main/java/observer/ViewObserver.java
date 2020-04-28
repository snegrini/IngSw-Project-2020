package observer;

import model.God;
import model.ReducedGod;
import model.enumerations.Color;

import java.util.List;
import java.util.Map;

public interface ViewObserver {

    void onUpdateServerInfo(Map<String, String> serverInfo);

    void onUpdateNickname(String nickname);

    void onUpdatePlayersNumber(int playerNumber);

    void onUpdateWorkersColor(Color color);

    void onUpdateGod(List<ReducedGod> reducedGods);

    void onUpdateWorkerToMove(int chosenRow, int chosenColumn);

    void onUpdateWorkerPosition(int chosenRow, int chosenColumn);

    void onUpdateBuild(int chosenRow, int chosenColumn);

}
