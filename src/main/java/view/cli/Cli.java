package view.cli;

import model.ReducedGod;
import model.board.Position;
import model.board.ReducedSpace;

import model.enumerations.Color;
import network.message.GenericErrorMessage;
import observer.ViewObserver;
import view.View;

import java.util.*;
import java.util.stream.Collectors;

import static model.board.Board.MAX_COLUMNS;
import static model.board.Board.MAX_ROWS;

public class Cli extends View {

    //private static final int  = 5;
    //private static final int COLUMNS = 5;
    //private String[][] board = new String[ROWS][COLUMNS];


    private Scanner scanner;


    public Cli() {
        scanner = new Scanner(System.in);
    }


    @Override
    public void init() {
        System.out.println("Welcome to SANTORINI board game!");
        askServerInfo();
    }

    @Override
    public void askServerInfo() {
        Map<String, String> serverInfo = new HashMap<>();

        System.out.print("Enter the server address: ");
        serverInfo.put("address", scanner.nextLine());

        System.out.print("Server port: ");
        serverInfo.put("port", scanner.nextLine());

        notifyObserver((ViewObserver obs) -> obs.onUpdateServerInfo(serverInfo));
    }

    @Override
    public void askNickname() {
        System.out.print("Enter your nickname: ");
        String nickname = scanner.nextLine();
        notifyObserver((ViewObserver obs) -> obs.onUpdateNickname(nickname));
    }

    @Override
    public void askPlayersNumber() {
        int playerNumber;
        System.out.print("How many players are going to play? (You can choose between 2 or 3 players): ");
        do {
            playerNumber = scanner.nextInt();
            if (playerNumber != 2 && playerNumber != 3)
                System.out.println("Remember! Only 2 or 3 players can play!");
        } while (playerNumber != 2 && playerNumber != 3);
        int finalPlayerNumber = playerNumber;
        notifyObserver((ViewObserver obs) -> obs.onUpdatePlayersNumber(finalPlayerNumber));
    }

    @Override
    public void askWorkersPositions(List<Position> positions){

    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful) {

    }



    @Override
    public void askWorkersColor(List<Color> colorList) {
        String in;
        System.out.println("Select your workers' color!");

        String colors = colorList.stream()
                .map(color -> color.getText())
                .collect(Collectors.joining(", "));

        System.out.println("You can choose between: " + colors);

        do {
            in = scanner.nextLine();
            if (!colors.contains(in.toUpperCase())) {
                System.out.println("You have not inserted a valid color! Please try again!");
            }

        } while (!colors.contains(in.toUpperCase()));
        Color color = Color.valueOf(in.toUpperCase());
        //only one color is chosen by a player
        notifyObserver((ViewObserver obs) -> obs.onUpdateWorkersColor(color));
    }

    @Override
    public void askGod(List<ReducedGod> gods) {

        System.out.println("Select your own personal God!");

        for (int i = 0; i < gods.size(); i++) {
            ReducedGod god = gods.get(i);
            System.out.println("ID: " + (i + 1));
            System.out.println("Name: " + god.getName());
            System.out.println("Caption: " + god.getCaption());
            System.out.println("Description: " + god.getDescription() + "\n");
        }
        int godId;
        System.out.print("To select one God type in his ID: ");
        do {
            godId = Integer.parseInt(scanner.nextLine()) - 1;
            if (godId < 0 || godId > gods.size()) {
                System.out.println("You have not inserted a valid ID! Please try again.");
            }
        } while (godId < 0 || godId > gods.size());

        ReducedGod finalGod = gods.get(godId);
        notifyObserver((ViewObserver obs) -> obs.onUpdateGod(finalGod));
    }


    @Override
    public void askWorkerToMove(List<Position> positions) {
        int chosenRow;
        int chosenColumn;
        System.out.println("Insert the position of the worker which you want to move:");
        for (int i = 0; i < positions.size(); i++) {
            System.out.println("Position of worker " + i + 1 + "is Row: " + positions.get(i).getRow() + "Column: " +
                    positions.get(i).getColumn());
        }
        do {
            chosenRow = Integer.parseInt(scanner.nextLine());
            chosenColumn = Integer.parseInt(scanner.nextLine());
            if (position_isNotValid(chosenRow, chosenColumn, positions))
                System.out.println("You have inserted an invalid position! Please try again!");
        } while (position_isNotValid(chosenRow, chosenColumn, positions));

        int finalChosenRow = chosenRow;
        int finalChosenColumn = chosenColumn;
        notifyObserver((ViewObserver obs) -> obs.onUpdateWorkerToMove(finalChosenRow, finalChosenColumn));

    }

    @Override
    public void askNewPosition(List<Position> positions) {
        int chosenRow;
        int chosenColumn;
        System.out.println("Select the new position for your Worker!");
        System.out.println("Here there are your Worker's possible moves:");
        if (positions.isEmpty()) {
            System.out.println("Oh no! Unfortunately you can't move...");
        } else {
            for (int i = 0; i < positions.size(); i++) {
                System.out.println("Position " + i + 1 + ":" + "Row: " + positions.get(i).getRow() +
                        "Column: " + positions.get(i).getColumn());
            }
            System.out.println("Select the new position:");
            do {
                chosenRow = Integer.parseInt(scanner.nextLine());
                chosenColumn = Integer.parseInt(scanner.nextLine());
                if (position_isNotValid(chosenRow, chosenColumn, positions))
                    System.out.println("You have inserted an invalid position! Please try again!");
            } while (position_isNotValid(chosenRow, chosenColumn, positions));
            int finalChosenRow = chosenRow;
            int finalChosenColumn = chosenColumn;
            notifyObserver((ViewObserver obs) -> obs.onUpdateWorkerPosition(finalChosenRow, finalChosenColumn));
        }
    }


    @Override
    public void askNewBuildingPosition(List<Position> positions) {
        int chosenRow;
        int chosenColumn;
        System.out.println("Select in which position you want your Worker to build!");
        System.out.println("Your Worker can Build here:");
        if (positions.isEmpty()) {
            System.out.println("Oh no! Unfortunately you can't build...");
        } else {
            for (int i = 0; i < positions.size(); i++) {
                System.out.println("Position " + i + 1 + ":" + "Row: " + positions.get(i).getRow() +
                        "Column: " + positions.get(i).getColumn());
            }
            System.out.println("Select where to build:");
            do {
                chosenRow = Integer.parseInt(scanner.nextLine());
                chosenColumn = Integer.parseInt(scanner.nextLine());
                if (position_isNotValid(chosenRow, chosenColumn, positions))
                    System.out.println("You have inserted an invalid position! Please try again!");
            } while (position_isNotValid(chosenRow, chosenColumn, positions));
            int finalChosenRow = chosenRow;
            int finalChosenColumn = chosenColumn;
            notifyObserver((ViewObserver obs) -> obs.onUpdateBuild(finalChosenRow, finalChosenColumn));
        }

    }


    @Override
    public void showGenericErrorMessage(String Error) {
        System.out.println(Error);
    }


    @Override
    public String showBoard(ReducedSpace[][] spaces) {
        System.out.print(printUpperIndexes());
        String strBoard = "";
        for (int i = 0; i < MAX_ROWS; i++) {
            strBoard += ColorCli.YELLOW_BOLD + "\n   +-----+-----+-----+-----+-----+\n" + ColorCli.RESET;
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if (j == 0) {
                    if (spaces[i][j].hasDome()) {
                        strBoard += Integer.toString(i) + ColorCli.YELLOW_BOLD + "  |  " + Color.BLUE + "∩"
                                + ColorCli.YELLOW_BOLD +
                                "  |" + ColorCli.RESET;
                    } else {
                        if (spaces[i][j].getReducedWorker() != null) {
                            strBoard += Integer.toString(i) + ColorCli.YELLOW_BOLD + "  | " + ColorCli.RESET +
                                    spaces[i][j].getLevel() +
                                    spaces[i][j].getReducedWorker().getColor().getCode() + " x" + ColorCli.YELLOW_BOLD
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
                                    spaces[i][j].getReducedWorker().getColor().getCode() + " x" +
                                    ColorCli.YELLOW_BOLD + " |"
                                    + ColorCli.RESET;
                        } else {
                            strBoard += "  " + spaces[i][j].getLevel() + ColorCli.YELLOW_BOLD + "  |" + ColorCli.RESET;
                        }
                    }
                }

            }
            if (i == MAX_ROWS - 1)
                strBoard += ColorCli.YELLOW_BOLD + "\n   +-----+-----+-----+-----+-----+\n" + ColorCli.RESET;
        }
        return strBoard;
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
