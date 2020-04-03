package model.enumerations;

public enum XMLName {
    GODS("gods"),
    GOD("god"),
    ID("id"),
    NAME("name"),
    CAPTION("caption"),
    DESCRIPTION("description"),
    EFFECTS("effects"),
    EFFECT("effect"),
    TYPE("type"),
    PARAMETERS("parameters"),
    SWAP_SPACE("swapspace"),
    NUM_OF_MOVES("numofmoves"),
    GO_BACK("go_back"),
    NUM_OF_LEVELS("numoflevels"),
    BUILD_DOME("builddome"),
    SAME_SPACE("samespace");

    private final String text;


    XMLName(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
