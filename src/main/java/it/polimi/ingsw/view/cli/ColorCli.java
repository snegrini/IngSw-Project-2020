package it.polimi.ingsw.view.cli;

/**
 * This class contains all colors used in Cli.
 */
public enum ColorCli {
    //Color end string, color reset
    RESET("\033[0m"),
    CLEAR("\033[H\033[2J"),

    // Regular Colors. Normal color, no bold, background color etc.
    RED("\033[0;31m"),      // RED
    GREEN("\033[38;5;28m"),    // GREEN  "\u001B[32m"
    BLUE("\033[0;34m"),     // BLUE

    // Bold
    YELLOW_BOLD("\033[1;33m"), // YELLOW
    CYAN_BOLD("\033[1;36m");    // CYAN

    private final String code;

    ColorCli(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}