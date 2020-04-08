package model.enumerations;

public enum XMLName {
    // Elements
    GODS("gods"),
    GOD("god"),
    NAME("name"),
    CAPTION("caption"),
    DESCRIPTION("description"),
    EFFECTS("effects"),
    EFFECT("effect"),
    TYPE("type"),
    REQUIREMENTS("requirements"),
    PARAMETERS("parameters"),
    TARGET("target"),
    BUILD("build"),
    MOVE("move"),
    SPACE("space"),

    // Attributes
    ID("id"),
    AGAIN("again"),
    OVER("over"),
    SWAP_SPACE("swapspace"),
    ACTIVE("active"),
    BACK("back"),
    LEVEL("lvl"),
    LOCK("lock"),
    DOME("dome"),
    FREE("free"),
    ALL("all"),
    MIN_BLOCK("minblock"),
    PUSH_BACK("pushback"),
    SAME_SPACE("samespace");

    private final String text;


    XMLName(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
