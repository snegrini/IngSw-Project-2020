package it.polimi.ingsw.model.enumerations;

/**
 * This Class contains all possible Colors that can be picked by Players for their workers on the game board.
 */
public enum Color {
    BLUE("BLUE"), RED("RED"), GREEN("GREEN");

    private final String text;

    Color(String text) {
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
