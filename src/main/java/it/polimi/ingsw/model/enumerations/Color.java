package it.polimi.ingsw.model.enumerations;

/**
 * This enum contains all possible Colors that can be picked by Players for their workers on the game board.
 */
public enum Color {
    BLUE("BLUE"), RED("RED"), GREEN("GREEN");

    private final String text;

    /**
     * Default constructor.
     *
     * @param text the string representation of the color.
     */
    Color(String text) {
        this.text = text;
    }

    /**
     * Returns the text of the color.
     *
     * @return a String containing the text of the color.
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
