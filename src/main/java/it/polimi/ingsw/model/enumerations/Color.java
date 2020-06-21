package it.polimi.ingsw.model.enumerations;

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
