package it.polimi.ingsw.model.enumerations;

public enum Color {
    BLUE("BLUE", "\033[0;34m"), RED("RED", "\033[0;31m"), GREEN("GREEN", "\033[38;5;28m");

    private final String text;
    private final String code;


    Color(String text, String code) {
        this.text = text;
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + text;
    }

}
