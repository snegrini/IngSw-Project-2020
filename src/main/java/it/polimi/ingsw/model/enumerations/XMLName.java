package it.polimi.ingsw.model.enumerations;

public enum XMLName {
    // Elements
    GODS("gods"),
    GOD("god"),
    NAME("name"),
    CAPTION("caption"),
    DESCRIPTION("description"),
    EFFECTS("effects"),
    EFFECT("effect"),
    PHASE("phase"),
    REQUIREMENTS("requirements"),
    PARAMETERS("parameters"),
    TARGET("target"),
    BUILD("build"),
    MOVE("move"),
    SPACE("space"),
    USERCONFIRM("userconfirm"),

    // Attributes
    ID("id"),
    AGAIN("again"),
    OVER("over"),
    SWAP_SPACE("swapspace"),
    ACTIVE("active"),
    GO_BACK("goback"),
    LEVEL("lvl"),
    QUANTITY("quantity"),
    LOCK("lock"),
    DOME("dome"),
    FORCE_DOME("forcedome"),
    FREE("free"),
    ALL("all"),
    MIN_BLOCK("minblock"),
    PUSH_BACK("pushback"),
    SAME_SPACE("samespace"),
    FORCE_SAME_SPACE("forcesamespace"),
    WIN("win");


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
