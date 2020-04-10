package network.message;

public enum MessageType {
    LOGIN_REQUEST, LOGIN_REPLY,
    NUMBEROFPLAYER_REQUEST, NUMBEROFPLAYER_REPLY,
    GODLIST,
    INIT, INIT_REPLY,
    MOVE,
    BUILD,
    WIN,
    LOSE,

    //utility:
    GAME_LOAD,
    DISCONNECTION

}
