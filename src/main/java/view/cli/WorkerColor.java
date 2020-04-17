package view.cli;

import model.player.Worker;

public enum WorkerColor
{
    ANSI_BLUE("\u001B[34m"),
    ANSI_GRAY("\u001b[31m"),
    ANSI_WHITE("\u001b[32m");


    static final String RESET = "\u001B[0m";

    private String escape;
    WorkerColor(String escape)
    {
        this.escape = escape;
    }
    public String getEscape()
    {
        return escape;
    }
    @Override
    public String toString()
    {
        return escape;
    }
}