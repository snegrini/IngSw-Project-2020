package network.message;

public enum MessageType {
    LOGIN_REQUEST, LOGIN_REPLY,
    PLAYERNUMBER_REQUEST, PLAYERNUMBER_REPLY,
    GODLIST,
    INIT,
    MOVE,
    BUILD,
    WIN,
    LOSE,

    //utility:
    GAME_LOAD,
    DISCONNECTION,
    GENERIC_ERROR_MESSAGE

}
