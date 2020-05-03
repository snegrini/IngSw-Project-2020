package it.polimi.ingsw.PSP016.view.cli;

import it.polimi.ingsw.PSP016.controller.ClientController;
import it.polimi.ingsw.PSP016.model.ReducedGod;
import it.polimi.ingsw.PSP016.model.board.Board;
import it.polimi.ingsw.PSP016.model.board.Position;
import it.polimi.ingsw.PSP016.model.board.ReducedSpace;
import it.polimi.ingsw.PSP016.model.enumerations.Color;
import it.polimi.ingsw.PSP016.view.View;

import it.polimi.ingsw.PSP016.observer.ViewObserver;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class Cli extends View {

    private Scanner scanner;
    private PrintStream out;

    public Cli() {
        scanner = new Scanner(System.in);
        out = System.out;
    }


    @Override
    public void init() {
        out.println("Welcome to SANTORINI board game!");
        askServerInfo();
    }

    @Override
    public void askServerInfo() {
        Map<String, String> serverInfo = new HashMap<>();
        String defaultAddress = "localhost";
        String defaultPort = "16847";
        boolean validInput = false;

        out.println("Please specify the following settings. The default value is shown between brackets.");
        do {
            out.print("Enter the server address (" + defaultAddress + "): ");
            String address = scanner.nextLine();

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

        clearCli();
        validInput = false;

        do {
            out.print("Enter the server port (" + defaultPort + "): ");
            String port = scanner.nextLine();

            if (port.equals("")) {
                serverInfo.put("port", defaultPort);
                validInput = true;
            } else if (ClientController.isValidPort(Integer.parseInt(port))) {
                serverInfo.put("port", port);
                validInput = true;
            } else {
                out.println("Invalid port!");
                validInput = false;
            }
        } while (!validInput);

        notifyObserver((ViewObserver obs) -> obs.onUpdateServerInfo(serverInfo));
    }

    @Override
    public void askNickname() {
        out.print("Enter your nickname: ");
        String nickname = scanner.nextLine();
        notifyObserver((ViewObserver obs) -> obs.onUpdateNickname(nickname));
    }

    @Override
    public void askPlayersNumber() {
        int playerNumber;
        out.print("How many players are going to play? (You can choose between 2 or 3 players): ");
        do {
            playerNumber = Integer.parseInt(scanner.nextLine());
        } while (playerNumber != 2 && playerNumber != 3);
        int finalPlayerNumber = playerNumber;
        notifyObserver((ViewObserver obs) -> obs.onUpdatePlayersNumber(finalPlayerNumber));
    }


    /**
     * If gods are > 1 and request > 1 then You're the "god like player" and You have to pick N gods
     * If gods are > 1 and request == 1 then You've to pick only 1 god
     * If gods are only 1 You don't have to pick any god.
     *
     * @param gods    the list of the available Gods.
     * @param request how many gods user have to pick
     */
    @Override
    public void askGod(List<ReducedGod> gods, int request) {

        int godId = 0;


        if (gods.size() > 1) {
            if (request > 1) {
                out.println("Select " + request + " Gods!");
                printGodList(gods);
                out.println("Insert Gods' IDs");
                int[] chosenIndexes = new int[3];
                for (int i = 0; i < 3; i++) {
                    chosenIndexes[i] = -1;
                }
                // TODO for cycle in order to pick N (=request) gods and put in reducedGodList.
                List<ReducedGod> reducedGodList = new ArrayList<>();
                for (int i = 0; i < request; i++) {
                    while (true) {
                        try {
                            do {
                                godId = Integer.parseInt(scanner.nextLine()) - 1;
                                if ((godId < 0 || godId > gods.size()) || (god_already_chosen(godId, chosenIndexes))) {
                                    out.println("You have not inserted a valid ID! Please try again.");
                                }
                            } while ((godId < 0 || godId > gods.size()) || (god_already_chosen(godId, chosenIndexes)));
                            chosenIndexes[i] = godId;
                            reducedGodList.add(gods.get(godId));
                            break;
                        } catch (NumberFormatException e) {
                            out.println("You have not inserted an integer number! Please try again!");
                        }
                    }
                }


                /*out.println("DEBUG: I picked first 3 gods for You.");
                List<ReducedGod> reducedGodList = new ArrayList<>();
                reducedGodList.add(gods.get(0));
                reducedGodList.add(gods.get(1));
                reducedGodList.add(gods.get(2));*/

                notifyObserver((ViewObserver obs) -> obs.onUpdateGod(reducedGodList));
            } else {
                out.println("Select your own personal God!");
                printGodList(gods);
                out.print("To select one God type in his ID: ");
                do {
                    godId = Integer.parseInt(scanner.nextLine()) - 1;
                    if (godId < 0 || godId > gods.size()) {
                        out.println("You have not inserted a valid ID! Please try again.");
                    }
                } while (godId < 0 || godId > gods.size());

                ReducedGod finalGod = gods.get(godId);
                notifyObserver((ViewObserver obs) -> obs.onUpdateGod(List.of(finalGod)));
            }
        } else if (gods.size() == 1) {
            out.println("You're the last player, your god is: ");
            printGodList(gods);
            ReducedGod finalGod = gods.get(0);
            notifyObserver((ViewObserver obs) -> obs.onUpdateGod(List.of(finalGod)));
        }
    }


    /**
     * Ask player to choose the initial position of his two workers.
     *
     * @param positions list of the initial positions of two workers.
     */
    @Override
    public void askInitWorkersPositions(List<Position> positions) {
        List<Position> initPositions = new ArrayList<>();

        int chosenRow, chosenColumn;

        out.println("Select your workers' initial positions");

        for (int i = 0; i < 2; i++) {
            while (true) {
                try {
                    do {
                        out.println("Position for Worker " + i);
                        out.print("Row: ");
                        chosenRow = Integer.parseInt(scanner.nextLine());
                        out.print("Column: ");
                        chosenColumn = Integer.parseInt(scanner.nextLine());
                        out.println();
                        if ((chosenRow < 0 || chosenRow > 4) || (chosenColumn < 0 || chosenColumn > 4)) {
                            out.println("You have inserted an invalid position! Please try Again!");
                        }
                    } while ((chosenRow < 0 || chosenRow > 4) || (chosenColumn < 0 || chosenColumn > 4));
                    initPositions.add(new Position(chosenRow, chosenColumn));
                    break;
                } catch (NumberFormatException e) {
                    out.println("You have not inserted an integer number! Please try again!");
                    out.println();
                }
            }
        }

        notifyObserver((ViewObserver obs) -> obs.onUpdateInitWorkerPosition(initPositions));
    }


    /**
     * Ask Player to pick his Color.
     *
     * @param colorList list of available Colors.
     */
    @Override
    public void askInitWorkerColor(List<Color> colorList) {

        // TODO scanner flush
        scanner = new Scanner(System.in);

        String in;
        out.println("Select your workers' color!");

        String colors = colorList.stream()
                .map(color -> color.getText())
                .collect(Collectors.joining(", "));

        out.println("You can choose between: " + colors);

        do {
            in = scanner.nextLine();
            if (!colors.contains(in.toUpperCase())) {
                out.println("You have not inserted a valid color! Please try again!");
            }

        } while (!colors.contains(in.toUpperCase()));
        Color color = Color.valueOf(in.toUpperCase());
        //only one color is chosen by a player
        notifyObserver((ViewObserver obs) -> obs.onUpdateWorkersColor(color));
    }

    /**
     * Ask Player to pick one of his Worker by Position.
     *
     * @param positionList list of workers Position.
     */
    @Override
    public void askMovingWorker(List<Position> positionList) {
        int chosenRow;
        int chosenColumn;
        out.println("Insert the position of the worker which you want to move:");
        for (int i = 0; i < positionList.size(); i++) {
            out.println("Position of worker " + (i + 1) + " is Row: " +
                    positionList.get(i).getRow() + " Column: " +
                    positionList.get(i).getColumn());
        }
        while (true) {
            try {
                do {
                    out.print("Row: ");
                    chosenRow = Integer.parseInt(scanner.nextLine());
                    out.print("Column: ");
                    chosenColumn = Integer.parseInt(scanner.nextLine());
                    if (position_isNotValid(chosenRow, chosenColumn, positionList))
                        out.println("You have inserted an invalid position! Please try again!");
                } while (position_isNotValid(chosenRow, chosenColumn, positionList));
                int finalChosenRow = chosenRow;
                int finalChosenColumn = chosenColumn;
                Position pst_worker = new Position(chosenRow, chosenColumn);
                notifyObserver((ViewObserver obs) -> obs.onUpdatePickMovingWorker(pst_worker));
                break;
            } catch (NumberFormatException e) {
                out.println("You have not inserted an integer number! Please try again!");
            }
        }
    }

    /**
     * Ask Player where to move his selected Worker.
     *
     * @param positionList possible Position to move on.
     */
    @Override
    public void askMove(List<Position> positionList) {
        int chosenRow;
        int chosenColumn;
        out.println("Select the new position for your Worker!");
        out.println("Here there are your Worker's possible moves:");
        if (positionList.isEmpty()) {
            out.println("Oh no! Unfortunately you can't move...");
            notifyObserver((ViewObserver obs) -> obs.onUpdateMove(null));
        } else {
            for (int i = 0; i < positionList.size(); i++) {
                out.println("Position " + (i + 1) + ": " + "Row: " + positionList.get(i).getRow() +
                        " Column: " + positionList.get(i).getColumn());
            }
            out.println("Select the new position:");
            while (true) {
                try {
                    do {
                        out.print("Row: ");
                        chosenRow = Integer.parseInt(scanner.nextLine());
                        out.print("Column: ");
                        chosenColumn = Integer.parseInt(scanner.nextLine());
                        if (position_isNotValid(chosenRow, chosenColumn, positionList))
                            out.println("You have inserted an invalid position! Please try again!");
                    } while (position_isNotValid(chosenRow, chosenColumn, positionList));
                    int finalChosenRow = chosenRow;
                    int finalChosenColumn = chosenColumn;
                    Position dest = new Position(chosenRow, chosenColumn);
                    notifyObserver((ViewObserver obs) -> obs.onUpdateMove(dest));
                    break;
                } catch (NumberFormatException e) {
                    out.println("You have not inserted an integer number! Please try again!");
                }
            }
        }
    }


    @Override
    public void askNewBuildingPosition(List<Position> positions) {
        int chosenRow;
        int chosenColumn;
        out.println("Select in which position you want your Worker to build!");
        out.println("Your Worker can Build here:");
        if (positions.isEmpty()) {
            out.println("Oh no! Unfortunately you can't build...");
        } else {
            for (int i = 0; i < positions.size(); i++) {
                out.println("Position " + (i + 1) + ": " + "Row: " + positions.get(i).getRow() +
                        " Column: " + positions.get(i).getColumn());
            }
            out.println("Select where to build:");
            while (true) {
                try {
                    do {
                        out.print("Row: ");
                        chosenRow = Integer.parseInt(scanner.nextLine());
                        out.print("Column: ");
                        chosenColumn = Integer.parseInt(scanner.nextLine());
                        if (position_isNotValid(chosenRow, chosenColumn, positions))
                            out.println("You have inserted an invalid position! Please try again!");
                    } while (position_isNotValid(chosenRow, chosenColumn, positions));
                    int finalChosenRow = chosenRow;
                    int finalChosenColumn = chosenColumn;
                    Position newBuild = new Position(chosenRow, chosenColumn);
                    notifyObserver((ViewObserver obs) -> obs.onUpdateBuild(newBuild));
                    break;
                } catch (NumberFormatException e) {
                    out.println("You have not inserted an integer number! Please try again!");
                }
            }
        }

    }


    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        clearCli();
        if (nicknameAccepted && connectionSuccessful) {
            out.println("Hi, " + nickname + "! You connected to the server.");
        } else if (connectionSuccessful && !nicknameAccepted) {
            askNickname();
        } else {
            out.println("Could not contact server.");
            out.println("\nPress any key to exit.");
            scanner.nextLine();
            System.exit(1);
        }

    }

    /**
     * Shows a Generic Message from Server
     *
     * @param genericMessage Generic Message from Server.
     */
    @Override
    public void showGenericMessage(String genericMessage) {
        out.println("Message From Server: " + genericMessage);
    }

    /**
     * Shows an error message.
     *
     * @param error the error to be shown.
     */
    @Override
    public void showError(String error) {
        out.println("ERROR: " + error);
        System.exit(1);
    }


    /**
     * Print the Board.
     *
     * @param spaces matrix of Reduced Space which compose the Board.
     */
    @Override
    public void showBoard(ReducedSpace[][] spaces) {

        // TODO CLEAR TERMINAL

        out.print(printUpperIndexes());
        String strBoard = "";
        for (int i = 0; i < Board.MAX_ROWS; i++) {
            strBoard += ColorCli.YELLOW_BOLD + "\n   +-----+-----+-----+-----+-----+\n" + ColorCli.RESET;
            for (int j = 0; j < Board.MAX_COLUMNS; j++) {
                if (j == 0) {
                    if (spaces[i][j].hasDome()) {
                        strBoard += Integer.toString(i) + ColorCli.YELLOW_BOLD + "  |  " + Color.BLUE + "∩"
                                + ColorCli.YELLOW_BOLD +
                                "  |" + ColorCli.RESET;
                    } else {
                        if (spaces[i][j].getReducedWorker() != null) {
                            strBoard += Integer.toString(i) + ColorCli.YELLOW_BOLD + "  | " + ColorCli.RESET +
                                    spaces[i][j].getLevel() +
                                    ColorCli.valueOf(spaces[i][j].getReducedWorker().getColor().getText()) + " x" + ColorCli.YELLOW_BOLD
                                    + " |" + ColorCli.RESET;
                        } else {
                            strBoard += Integer.toString(i) + ColorCli.YELLOW_BOLD + "  |  " + ColorCli.RESET
                                    + spaces[i][j].getLevel()
                                    + ColorCli.YELLOW_BOLD + "  |"
                                    + ColorCli.RESET;
                        }

                    }
                } else {
                    if (spaces[i][j].hasDome()) {
                        strBoard += "  " + Color.BLUE + "∩" + ColorCli.YELLOW_BOLD + "  |" + ColorCli.RESET;
                    } else {
                        if (spaces[i][j].getReducedWorker() != null) {
                            strBoard += " " + spaces[i][j].getLevel() +
                                    ColorCli.valueOf(spaces[i][j].getReducedWorker().getColor().getText()) + " x" +
                                    ColorCli.YELLOW_BOLD + " |"
                                    + ColorCli.RESET;
                        } else {
                            strBoard += "  " + spaces[i][j].getLevel() + ColorCli.YELLOW_BOLD + "  |" + ColorCli.RESET;
                        }
                    }
                }

            }
            if (i == Board.MAX_ROWS - 1)
                strBoard += ColorCli.YELLOW_BOLD + "\n   +-----+-----+-----+-----+-----+\n" + ColorCli.RESET;
        }
        out.println(strBoard);
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
        String strIndex = "   ";
        for (int i = 0; i < 5; i++) {
            strIndex += "   " + i + "  ";
        }
        return strIndex;
    }

    /**
     * Returns {@code true} if the position inserted is not valid, {@code false} otherwise.
     * This method is used in the "Ask" type methods to check if the position inserted by the user
     * is correct. If it is incorrect input is asked again.
     *
     * @param chosenRow    the Row chosen by the user.
     * @param chosenColumn the Column chosen by the user.
     * @param positions    the List of valid positions.
     * @return {@code true} if the position inserted is not valid, {@code false} otherwise.
     */
    private boolean position_isNotValid(int chosenRow, int chosenColumn, List<Position> positions) {
        for (Position position : positions) {
            if (chosenRow == position.getRow() && chosenColumn == position.getColumn()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if the god has already been chosen, {@code false} otherwise.
     *
     * @param godId         the Id of the chosen god.
     * @param chosenIndexes the Indexes of the already chosen gods.
     * @return Returns {@code true} if the god has already been chosen, {@code false} otherwise.
     */
    private boolean god_already_chosen(int godId, int[] chosenIndexes) {
        for (int i = 0; i < 3; i++) {
            if (godId == chosenIndexes[i]) {
                return true;
            }
        }
        return false;
    }


    public void clearCli() {
        out.print(ColorCli.CLEAR);
        out.flush();
    }

    //method to check if a position to build is valid
    /*boolean position_isNotValid2(int chosenRow, int chosenColumn, Worker worker) {
        boolean is_NotValid = (worker.getPossibleBuilds().get(0).getRow() != chosenRow) ||
                (worker.getPossibleBuilds().get(0).getColumn() != chosenColumn);

        for (int i = 1; i < worker.getPossibleBuilds().size(); i++) {
            is_NotValid = (is_NotValid && ((worker.getPossibleBuilds().get(i).getRow() != chosenRow) ||
                    (worker.getPossibleBuilds().get(i).getColumn() != chosenColumn)));
        }

        return is_NotValid;

    }

    //checks if the position inserted for a worker is correct
    boolean pstWorkerNotValid(int chosenRow, int chosenColumn, List<Worker> workers) {
        return ((workers.get(0).getPosition().getRow() != chosenRow) ||
                (workers.get(0).getPosition().getColumn() != chosenColumn)) &&
                ((workers.get(1).getPosition().getRow() !=
                        chosenRow) || (workers.get(1).getPosition().getColumn() != chosenColumn));
    }*/

}
