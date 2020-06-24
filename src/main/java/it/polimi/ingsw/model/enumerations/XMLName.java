package it.polimi.ingsw.model.enumerations;

/**
 * This Class contains all name associated to XML tag of the XML database of Gods.
 */
public enum XMLName {
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
    USERCONFIRM("userconfirm"),

    // Attributes
    AGAIN("again"),
    OVER("over"),
    SWAP_SPACE("swapspace"),
    GO_BACK("goback"),
    LEVEL("lvl"),
    LOCK("lock"),
    DOME("dome"),
    FORCE_DOME("forcedome"),
    ALL("all"),
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
