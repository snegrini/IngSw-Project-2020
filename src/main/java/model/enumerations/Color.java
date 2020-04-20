package model.enumerations;

public enum Color {
    BLUE("BLUE"), GRAY("GRAY"), WHITE("WHITE");

    private final String text;

    Color(final String text) {
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
