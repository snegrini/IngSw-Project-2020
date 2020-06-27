package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.ClientController.UNDO_TIME;

public class Cli extends ViewObservable implements View {

    private final PrintStream out;
    private Thread inputThread;

    private static final String STR_ROW = "Row: ";
    private static final String STR_COLUMN = "Column: ";
    private static final String STR_POSITION = "Position ";
    private static final String STR_INPUT_CANCELED = "User input canceled.";

    public Cli() {
        out = System.out;
    }


    public String readLine() throws ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(new InputReadTask());
        inputThread = new Thread(futureTask);
        inputThread.start();

        String input = null;

        try {
            input = futureTask.get();
        } catch (InterruptedException e) {
            futureTask.cancel(true);
            Thread.currentThread().interrupt();
        }
        return input;
    }

    public void init() {
        out.println("Welcome to SANTORINI board game!");

        try {
            askServerInfo();
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

    public void askServerInfo() throws ExecutionException {
        Map<String, String> serverInfo = new HashMap<>();
        String defaultAddress = "localhost";
        String defaultPort = "16847";
        boolean validInput;

        out.println("Please specify the following settings. The default value is shown between brackets.");

        do {
            out.print("Enter the server address [" + defaultAddress + "]: ");

            String address = readLine();

            if (address.equals("")) {
                serverInfo.put("address", defaultAddress);
                validInput = true;
            } else if (ClientController.isValidIpAddress(address)) {
                serverInfo.put("address", address);
                validInput = true;
            } else {
                out.println("Invalid address!");
                clearCli();
                validInput = false;
            }
        } while (!validInput);

        do {
            out.print("Enter the server port [" + defaultPort + "]: ");
            String port = readLine();

            if (port.equals("")) {
                serverInfo.put("port", defaultPort);
                validInput = true;
            } else {
                if (ClientController.isValidPort(port)) {
                    serverInfo.put("port", port);
                    validInput = true;
                } else {
                    out.println("Invalid port!");
                    validInput = false;
                }
            }
        } while (!validInput);

        notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo));
    }

    @Override
    public void askNickname() {
        out.print("Enter your nickname: ");
        try {
            String nickname = readLine();
            notifyObserver(obs -> obs.onUpdateNickname(nickname));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

    @Override
    public void askPlayersNumber() {
        int playerNumber;
        String question = "How many players are going to play? (You can choose between 2 or 3 players): ";

        try {
            playerNumber = numberInput(2, 3, List.of(), question);
            notifyObserver(obs -> obs.onUpdatePlayersNumber(playerNumber));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }


    /**
     * If gods are {@literal >} 1 and request {@literal >} 1 then you are the "god like player" and you have to pick N gods
     * If gods are {@literal >}  1 and request == 1 then You've to pick only 1 god
     * If gods are only 1, you don't have to pick any god.
     * Notifies the client controller with the selected god/gods.
     *
     * @param gods    the list of the available Gods.
     * @param request how many gods user have to pick
     */
    @Override
    public void askGod(List<ReducedGod> gods, int request) {
        clearCli();

        int godId;

        if (gods.size() > 1) {
            if (request > 1) {
                List<ReducedGod> chosenGods = new ArrayList<>();
                List<Integer> jumpList = new ArrayList<>();

                out.println("Select " + request + " Gods from the list.");
                printGodList(gods);

                out.println("Please, enter one ID per line and confirm with ENTER.");
                try {
                    for (int i = 0; i < request; i++) {
                        godId = numberInput(1, gods.size(), jumpList, (i + 1) + "° god ID: ") - 1;
                        jumpList.add(godId + 1);

                        chosenGods.add(gods.get(godId));
                    }
                    notifyObserver(obs -> obs.onUpdateGod(chosenGods));
                } catch (ExecutionException e) {
                    out.println(STR_INPUT_CANCELED);
                }
            } else {
                out.println("Select your own personal God!");
                printGodList(gods);
                try {
                    godId = numberInput(1, gods.size(), List.of(), "To select one God type in his ID: ") - 1;

                    ReducedGod finalGod = gods.get(godId);
                    notifyObserver(obs -> obs.onUpdateGod(List.of(finalGod)));
                } catch (ExecutionException e) {
                    out.println(STR_INPUT_CANCELED);
                }
            }
        } else if (gods.size() == 1) {
            out.println("You're the last player, your god is: ");
            printGodList(gods);
            ReducedGod finalGod = gods.get(0);
            notifyObserver(obs -> obs.onUpdateGod(List.of(finalGod)));
        } else {
            showErrorAndExit("no gods found in the request.");
        }
    }

    /**
     * Asks the user for a input number. The number must be between minValue and maxValue.
     * A wrong number (outside the range) or a non-number will result in a new request of the input.
     * A forbidden list of numbers inside the range can be set through jumpList parameter.
     * An output question can be set via question parameter.
     *
     * @param minValue the minimum value which can be inserted (included).
     * @param maxValue the maximum value which can be inserted (included).
     * @param jumpList a list of forbidden values inside the range [minValue, maxValue]
     * @param question a question which will be shown to the user.
     * @return the number inserted by the user.
     * @throws ExecutionException if the input stream thread is interrupted.
     */
    private int numberInput(int minValue, int maxValue, List<Integer> jumpList, String question) throws ExecutionException {
        int number = minValue - 1;

        // A null jumpList will be transformed in a empty list.
        if (jumpList == null) {
            jumpList = List.of();
        }

        do {
            try {
                out.print(question);
                number = Integer.parseInt(readLine());

                if (number < minValue || number > maxValue) {
                    out.println("Invalid number! Please try again.\n");
                } else if (jumpList.contains(number)) {
                    out.println("This number cannot be selected! Please try again.\n");
                }
            } catch (NumberFormatException e) {
                out.println("Invalid input! Please try again.\n");
            }
        } while (number < minValue || number > maxValue || jumpList.contains(number));

        return number;
    }


    /**
     * Ask player to choose the initial position of his two workers.
     * Notifies the client controller with the selected worker's position.
     *
     * @param positions list of the initial positions of two workers.
     */
    @Override
    public void askInitWorkersPositions(List<Position> positions) {
        List<Position> initPositions = new ArrayList<>();

        out.println("Select your workers' initial positions");
        try {
            for (int i = 0; i < 2; i++) {
                out.println("Position for Worker " + (i + 1));
                int chosenRow = numberInput(0, positions.get(positions.size() - 1).getRow(), List.of(), STR_ROW);
                int chosenColumn = numberInput(0, positions.get(positions.size() - 1).getColumn(), List.of(), STR_COLUMN);

                initPositions.add(new Position(chosenRow, chosenColumn));
            }

            notifyObserver(obs -> obs.onUpdateInitWorkerPosition(initPositions));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }


    /**
     * Ask Player to pick his Color.
     * Notifies the client controller with the selected color.
     *
     * @param colorList list of available colors.
     */
    @Override
    public void askInitWorkerColor(List<Color> colorList) {
        out.println("Select your workers' color!");

        try {
            Color color = colorInput(colorList);

            // only one color is chosen by a player
            notifyObserver(obs -> obs.onUpdateWorkersColor(color));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

    /**
     * Asks the user for a color input. The color must available in the colorList parameter.
     * A wrong color (not in the list) or an invalid input will result in a new request of the input.
     *
     * @param colorList list of available colors.
     * @return the color picked by the user.
     * @throws ExecutionException if the input stream thread is interrupted.
     */
    private Color colorInput(List<Color> colorList) throws ExecutionException {
        String in;
        Color color = null;

        String colorsStr = colorList.stream()
                .map(Color::getText)
                .collect(Collectors.joining(", "));

        do {
            out.print("Choose between " + colorsStr + ": ");

            try {
                in = readLine();
                color = Color.valueOf(in.toUpperCase());

                if (!colorList.contains(color)) {
                    out.println("Invalid color! Please try again.");
                }
            } catch (IllegalArgumentException e) {
                out.println("Invalid color! Please try again.");
            }
        } while (!colorList.contains(color));

        return color;
    }

    /**
     * Ask Player to pick one of his Worker by Position.
     * Notifies the client controller with the selected position.
     *
     * @param positionList list of workers Position.
     */
    @Override
    public void askMovingWorker(List<Position> positionList) {

        out.println("Your workers are in the following positions:");
        for (int i = 0; i < positionList.size(); i++) {
            out.println((i + 1) + "° worker is on Row: " +
                    positionList.get(i).getRow() + ", Column: " +
                    positionList.get(i).getColumn());
        }

        out.println("Insert the position of the worker which you want to move:");

        try {
            int chosenRow = numberInput(findMinRow(positionList), findMaxRow(positionList), null, STR_ROW);

            // Column must be filtered from positions with chosenRow as the row.
            List<Position> availableColList = new ArrayList<>();
            for (Position pos : positionList) {
                if (chosenRow == pos.getRow()) {
                    availableColList.add(pos);
                }
            }

            int chosenColumn = numberInput(findMinColumn(availableColList), findMaxColumn(availableColList), null, STR_COLUMN);

            Position pos = new Position(chosenRow, chosenColumn);
            notifyObserver(obs -> obs.onUpdatePickMovingWorker(pos));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

    /**
     * Asks the player where to move his selected worker.
     * Notifies the client controller with the selected position.
     *
     * @param positionList a list of possible positions where the worker can be moved onto.
     */
    @Override
    public void askMove(List<Position> positionList) {
        out.println("Select the new position for your Worker!");
        out.println("Here there are your Worker's possible moves:");

        if (positionList.isEmpty()) {
            out.println("Oh no! Unfortunately you can't move...");
            notifyObserver(obs -> obs.onUpdateMove(null));
        } else {
            try {
                String message = "Select where to move.";
                Position destPos = readPositionAndWait(positionList, message);

                notifyObserver(obs -> obs.onUpdateMove(destPos));
            } catch (ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
    }

    /**
     * Asks the player where to build a block on the board.
     * Notifies the client controller with the selected position.
     *
     * @param positionList a list of possible positions where a block can be builded onto.
     */
    @Override
    public void askBuild(List<Position> positionList) {
        out.println("Select in which position you want your Worker to build!");
        out.println("Your Worker can Build here:");

        if (positionList.isEmpty()) {
            out.println("Oh no! Unfortunately you can't build...");
        } else {
            try {
                String message = "Select where to build.";
                Position buildPos = readPositionAndWait(positionList, message);

                notifyObserver(obs -> obs.onUpdateBuild(buildPos));
            } catch (ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
    }

    /**
     * Performs a special move or an extra move due to some effect applied.
     * Asks the player where to move his selected worker.
     * Notifies the client controller with the selected position.
     *
     * @param positionList a list of possible positions where the worker can be moved onto.
     */
    @Override
    public void askMoveFx(List<Position> positionList) {
        out.println("Select the new position for your Worker!");
        out.println("Here there are your Worker's possible moves:");

        if (positionList.isEmpty()) {
            out.println("Oh no! Unfortunately you can't move...");
            notifyObserver(obs -> obs.onUpdateMove(null));
        } else {
            try {
                String message = "Select where to move.";
                Position destPos = readPositionAndWait(positionList, message);

                notifyObserver(obs -> obs.onUpdateApplyEffect(destPos));
            } catch (ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
    }

    /**
     * Asks the player where to build a block on the board.
     * Notifies the client controller with the selected position.
     *
     * @param positionList a list of possible positions where a block can be builded onto.
     */
    @Override
    public void askBuildFx(List<Position> positionList) {
        out.println("Select in which position you want your Worker to build!");
        out.println("Your Worker can Build here:");

        if (positionList.isEmpty()) {
            out.println("Oh no! Unfortunately you can't build...");
        } else {
            try {
                String message = "Select where to build.";
                Position buildPos = readPositionAndWait(positionList, message);

                notifyObserver(obs -> obs.onUpdateApplyEffect(buildPos));
            } catch (ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
    }

    @Override
    public void askEnableEffect(boolean forceApply) {
        if (forceApply) {
            notifyObserver(obs -> obs.onUpdateEnableEffect(true));
        } else {

            out.print("Do you want to enable your god effect? [y/N]: ");
            try {
                String response = readLine();
                notifyObserver(obs -> obs.onUpdateEnableEffect(response.equalsIgnoreCase("y")));
            } catch (ExecutionException e) {
                out.println(STR_INPUT_CANCELED);
            }
        }
    }

    @Override
    public void showMatchInfo(List<String> players, List<ReducedGod> gods, String activePlayer) {
        // Do nothing. Updates are reprinted to the terminal automatically by each method.
    }

    @Override
    public void askFirstPlayer(List<String> nicknameQueue, List<ReducedGod> gods) {
        out.println("You're the Challenger, choose the first player: ");
        out.print("Online players: " + String.join(", ", nicknameQueue));
        try {

            String nickname;
            do {
                out.print("\nType the exact name of the player: ");

                nickname = readLine();
                if (!nicknameQueue.contains(nickname)) {
                    out.println("You have selected an invalid player! Please try again.");
                }
            } while (!nicknameQueue.contains(nickname));

            String finalNickname = nickname;
            notifyObserver(obs -> obs.onUpdateFirstPlayer(finalNickname));
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
    }

    /**
     * Shows a win message on the terminal.
     *
     * @param winner the nickname of the winner.
     */
    @Override
    public void showWinMessage(String winner) {
        out.println("Game finished: " + winner + " WINS!");
    }

    /**
     * Shows the login result on the terminal.
     * On login fail, the program is terminated immediatly.
     *
     * @param nicknameAccepted     indicates if the chosen nickname has been accepted.
     * @param connectionSuccessful indicates if the connection has been successful.
     * @param nickname             the nickname of the player to be greeted.
     */
    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        clearCli();

        if (nicknameAccepted && connectionSuccessful) {
            out.println("Hi, " + nickname + "! You connected to the server.");
        } else if (connectionSuccessful) {
            askNickname();
        } else if (nicknameAccepted) {
            out.println("Max players reached. Connection refused.");
            out.println("EXIT.");

            System.exit(1);
        } else {
            showErrorAndExit("Could not contact server.");
        }
    }

    /**
     * Shows a Generic Message from Server
     *
     * @param genericMessage Generic Message from Server.
     */
    @Override
    public void showGenericMessage(String genericMessage) {
        out.println(genericMessage);
    }

    /**
     * Shows a player disconnection message and exit.
     *
     * @param nicknameDisconnected the nickname of the disconnected player.
     * @param text                 the text to be shown.
     */
    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        inputThread.interrupt();
        out.println("\n" + nicknameDisconnected + text);

        System.exit(1);
    }

    /**
     * Shows an error message and exit.
     *
     * @param error the error to be shown.
     */
    @Override
    public void showErrorAndExit(String error) {
        inputThread.interrupt();

        out.println("\nERROR: " + error);
        out.println("EXIT.");

        System.exit(1);
    }

    /**
     * Prints the Board.
     *
     * @param spaces matrix of ReducedSpace which compose the board.
     */
    @Override
    public void showBoard(ReducedSpace[][] spaces) {
        clearCli();

        out.print(printUpperIndexes());
        StringBuilder strBoardBld = new StringBuilder();
        for (int i = 0; i < Board.MAX_ROWS; i++) {
            strBoardBld.append(ColorCli.YELLOW_BOLD).append("\n   +-----+-----+-----+-----+-----+\n").append(ColorCli.RESET);
            for (int j = 0; j < Board.MAX_COLUMNS; j++) {
                if (j == 0) {
                    if (spaces[i][j].hasDome()) {
                        strBoardBld.append(i).append(ColorCli.YELLOW_BOLD).append("  |  ")
                                .append(ColorCli.BLUE).append("∩").append(ColorCli.YELLOW_BOLD).append("  |")
                                .append(ColorCli.RESET);
                    } else {
                        if (spaces[i][j].getReducedWorker() != null) {
                            strBoardBld.append(i).append(ColorCli.YELLOW_BOLD).append("  | ").append(ColorCli.RESET)
                                    .append(spaces[i][j].getLevel()).append(ColorCli.valueOf(spaces[i][j].getReducedWorker().getColor().getText()))
                                    .append(" x").append(ColorCli.YELLOW_BOLD).append(" |").append(ColorCli.RESET);
                        } else {
                            strBoardBld.append(i).append(ColorCli.YELLOW_BOLD).append("  |  ").append(ColorCli.RESET)
                                    .append(spaces[i][j].getLevel()).append(ColorCli.YELLOW_BOLD).append("  |").append(ColorCli.RESET);
                        }

                    }
                } else {
                    if (spaces[i][j].hasDome()) {
                        strBoardBld.append("  ").append(ColorCli.BLUE).append("∩").append(ColorCli.YELLOW_BOLD).append("  |").append(ColorCli.RESET);
                    } else {
                        if (spaces[i][j].getReducedWorker() != null) {
                            strBoardBld.append(" ").append(spaces[i][j].getLevel()).append(ColorCli.valueOf(spaces[i][j].getReducedWorker().getColor().getText()))
                                    .append(" x").append(ColorCli.YELLOW_BOLD).append(" |").append(ColorCli.RESET);
                        } else {
                            strBoardBld.append("  ").append(spaces[i][j].getLevel()).append(ColorCli.YELLOW_BOLD).append("  |").append(ColorCli.RESET);
                        }
                    }
                }

            }
            if (i == Board.MAX_ROWS - 1)
                strBoardBld.append(ColorCli.YELLOW_BOLD).append("\n   +-----+-----+-----+-----+-----+\n").append(ColorCli.RESET);
        }
        out.println(strBoardBld.toString());
    }

    /**
     * Shows the lobby screen on the terminal.
     *
     * @param nicknameList list of players.
     * @param numPlayers   number of players.
     */
    @Override
    public void showLobby(List<String> nicknameList, int numPlayers) {
        out.println("LOBBY:");
        for (String nick : nicknameList) {
            out.println(nick + "\n");
        }
        out.println("Waiting for other players to join: " + nicknameList.size() + " / " + numPlayers);
    }


    /**
     * Print a list of gods
     *
     * @param gods the list of gods You want to print
     */
    private void printGodList(List<ReducedGod> gods) {
        for (int i = 0; i < gods.size(); i++) {
            ReducedGod god = gods.get(i);
            out.println("ID: " + (i + 1));
            out.println("Name: " + god.getName());
            out.println("Caption: " + god.getCaption());
            out.println("Description: " + god.getDescription() + "\n");
        }
    }

    /**
     * Returns a string with the columns' indexes.
     *
     * @return a string with the columns' indexes.
     */
    private String printUpperIndexes() {
        StringBuilder strIndexBld = new StringBuilder("   ");
        for (int i = 0; i < 5; i++) {
            strIndexBld.append("   ").append(i).append("  ");
        }
        return strIndexBld.toString();
    }


    /**
     * Finds and returns the min row in a list of positions.
     * The minimum value is found by sorting the list and getting
     * the first occurrence of the sorted list.
     * A deep copy of the argument list if performed to avoid any modification
     * of the original list.
     *
     * @param positions a list of positions.
     * @return the integer of the maximum row position inside the list.
     */
    private int findMinRow(List<Position> positions) {
        List<Position> sortedList = new ArrayList<>(List.copyOf(positions));
        sortedList.sort(Comparator.comparingInt(Position::getRow)
                .thenComparingInt(Position::getColumn));
        return sortedList.get(0).getRow();
    }

    /**
     * Finds and returns the max row in a list of positions.
     * The maximum value is found by sorting the list and getting
     * the last occurrence of the sorted list.
     * A deep copy of the argument list if performed to avoid any modification
     * of the original list.
     *
     * @param positions a list of positions.
     * @return the integer of the maximum row position inside the list.
     */
    private int findMaxRow(List<Position> positions) {
        List<Position> sortedList = new ArrayList<>(List.copyOf(positions));
        sortedList.sort(Comparator.comparingInt(Position::getRow)
                .thenComparingInt(Position::getColumn));
        return sortedList.get(sortedList.size() - 1).getRow();
    }

    /**
     * Finds and returns the min column in a list of positions.
     * The minimum value is found by sorting the list and getting
     * the first occurrence of the sorted list.
     * A deep copy of the argument list if performed to avoid any modification
     * of the original list.
     *
     * @param positions a list of positions.
     * @return the integer of the maximum column position inside the list.
     */
    private int findMinColumn(List<Position> positions) {
        List<Position> sortedList = new ArrayList<>(List.copyOf(positions));
        sortedList.sort(Comparator.comparingInt(Position::getColumn)
                .thenComparingInt(Position::getColumn));
        return sortedList.get(0).getColumn();
    }

    /**
     * Finds and returns the max column in a list of positions.
     * The maximum value is found by sorting the list and getting
     * the last occurrence of the sorted list.
     * A deep copy of the argument list if performed to avoid any modification
     * of the original list.
     *
     * @param positions a list of positions.
     * @return the integer of the maximum column position inside the list.
     */
    private int findMaxColumn(List<Position> positions) {
        List<Position> sortedList = new ArrayList<>(List.copyOf(positions));
        sortedList.sort(Comparator.comparingInt(Position::getColumn)
                .thenComparingInt(Position::getColumn));
        return sortedList.get(sortedList.size() - 1).getColumn();
    }

    private Position readPositionAndWait(List<Position> positionList, String message) throws ExecutionException {
        printPositions(positionList);
        out.println(message);

        int chosenRow;
        int chosenColumn;
        List<Position> availableColList = new ArrayList<>();

        do {
            chosenRow = numberInput(findMinRow(positionList), findMaxRow(positionList), null, STR_ROW);

            // Column must be filtered from positions with chosenRow as the row.
            availableColList.clear();
            for (Position pos : positionList) {
                if (chosenRow == pos.getRow()) {
                    availableColList.add(pos);
                }
            }

            chosenColumn = numberInput(findMinColumn(availableColList), findMaxColumn(availableColList), null, STR_COLUMN);
        } while (waitForUndo());

        return new Position(chosenRow, chosenColumn);
    }

    /**
     * Starts a timer of 5 (five) seconds and wait for user input.
     * At the timeout, {@code false} is returned, meaning that the user didn't want to undo his last operation.
     * On user input, the timer is canceled immediately and {@code true} is returned if "y" is inserted,
     * {@code false} otherwise.
     *
     * @return {@code false} on timeout or on user confirm, {@code true} otherwise.
     */
    private boolean waitForUndo() {
        out.println("Waiting undo for " + UNDO_TIME / 1E3 + " seconds...");

        Timer undoTimer = new Timer();
        undoTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                inputThread.interrupt();
            }
        }, UNDO_TIME);

        try {
            out.print("Undo the last operation? [y/N]: ");
            String undoConfirm = readLine();

            undoTimer.cancel();
            return undoConfirm.equalsIgnoreCase("y");
        } catch (ExecutionException e) {
            out.println(STR_INPUT_CANCELED);
        }
        return false;
    }

    /**
     * Prints a list of positions.
     *
     * @param positionList the list of positions to be printed.
     */
    private void printPositions(List<Position> positionList) {
        for (int i = 0; i < positionList.size(); i++) {
            out.println((i + 1) + "° " + STR_POSITION + "is " + STR_ROW + positionList.get(i).getRow() +
                    " " + STR_COLUMN + positionList.get(i).getColumn());
        }
    }

    /**
     * Clears the CLI terminal.
     */
    public void clearCli() {
        out.print(ColorCli.CLEAR);
        out.flush();
    }

}
