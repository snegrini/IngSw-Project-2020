package it.polimi.ingsw.network.message;

public enum MessageType {
    LOGIN_REQUEST, LOGIN_REPLY,
    PLAYERNUMBER_REQUEST, PLAYERNUMBER_REPLY,
    GODLIST,
    BOARD,
    INIT_WORKERSPOSITIONS,
    INIT_COLORS,
    PICK_MOVING_WORKER,
    PICK_BUILDING_WORKER,
    MOVE,
    BUILD,
    WIN,
    LOSE,

    //utility:
    GAME_LOAD,
    DISCONNECTION,
    GENERIC_MESSAGE

}
