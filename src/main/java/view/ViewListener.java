package view;

import java.io.IOException;
import java.util.Map;

public interface ViewListener {

    void doConnect(Map<String, String> serverInfo);

    void checkNickname(String nickname);
}
