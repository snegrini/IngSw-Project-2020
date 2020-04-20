package view.cli;


import model.God;
import model.board.Board;
import model.board.Position;
import model.enumerations.Color;
import model.player.Worker;
import network.message.Init;
import network.message.Message;
import observer.ViewObserver;
import view.View;

import java.util.*;
import java.util.stream.Collectors;

public class Cli extends View {

    private static final int ROWS = 5;
    private static final int COLUMNS = 5;
    private String[][] board = new String[ROWS][COLUMNS];


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
        System.out.println("How many players are going to play? (You can choose between 2 or 3 players)");
        do {
            playerNumber = scanner.nextInt();
            if (playerNumber != 2 && playerNumber != 3)
                System.out.println("Remember! Only 2 or 3 players can play!");
        } while (playerNumber != 2 && playerNumber != 3);
        int finalPlayerNumber = playerNumber;
        notifyObserver((ViewObserver obs) -> obs.onUpdatePlayersNumber(finalPlayerNumber));
    }

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

    public void askGod(List<God> gods) {
        int in;
        int index;
        System.out.println("Select your own personal God!");
        for (God g : gods) {
            System.out.println("id: " + g.getId());
            System.out.println("Name: " + g.getName());
            System.out.println("Caption: " + g.getCaption());
            System.out.println("Description: " + g.getDescription());
            System.out.println(" ");
        }
        System.out.println("To select one God type in his ID");
        do {
            in = scanner.nextInt();
            if (in != 1 && in != 2 && in != 3 && in != 4 && in != 5 && in != 6 && in != 7 && in != 8 && in != 9 && in != 10)
                System.out.println("You have not inserted a valid ID! Please try again!");
        } while (in != 1 && in != 2 && in != 3 && in != 4 && in != 5 && in != 6 && in != 7 && in != 8 && in != 9 && in != 10);

        for (index = 0; index < gods.size(); index++) {
            if (in == gods.get(index).getId()) {
                break;
            }
        }
        int finalIndex = index;
        notifyObserver((ViewObserver obs) -> obs.onUpdateGod(gods.get(finalIndex)));
    }

    public void askWorkerToMove(List<Worker> workers) {
        int chosenRow;
        int chosenColumn;
        System.out.println("Insert the position of the worker which you want to move:");
        for (int i = 0; i < workers.size(); i++) {
            System.out.println("Position of worker " + i + 1 + "is Row: " + workers.get(i).getPosition().getRow() + "Column: " +
                    workers.get(i).getPosition().getColumn());
        }
        do {
            chosenRow = scanner.nextInt();
            chosenColumn = scanner.nextInt();
            if (((chosenRow != workers.get(0).getPosition().getRow()) || (chosenColumn != workers.get(0).getPosition().getRow()))
                    && ((chosenRow != workers.get(1).getPosition().getRow()) || (chosenColumn != workers.get(1).getPosition().getRow())))
                System.out.println("You have inserted an invalid position! Please try again!");
        } while (((chosenRow != workers.get(0).getPosition().getRow()) || (chosenColumn != workers.get(0).getPosition().getRow()))
                && ((chosenRow != workers.get(1).getPosition().getRow()) || (chosenColumn != workers.get(1).getPosition().getRow())));

        int finalChosenRow = chosenRow;
        int finalChosenColumn = chosenColumn;
        notifyObserver((ViewObserver obs) -> obs.onUpdateWorkerToMove(finalChosenRow, finalChosenColumn));

    }

    public void askNewPosition(Worker worker) {
        int chosenRow;
        int chosenColumn;
        System.out.println("Select the new position for your Worker!");
        System.out.println("Here there are your Worker's possible moves:");
        if (worker.getPossibleMoves().isEmpty()) {
            System.out.println("Oh no! Unfortunately you can't move...");
        } else {
            for (int i = 0; i < worker.getPossibleMoves().size(); i++) {
                System.out.println("Position " + i + 1 + ":" + "Row: " + worker.getPossibleMoves().get(i).getRow() +
                        "Column: " + worker.getPossibleMoves().get(i).getColumn());
            }
            System.out.println("Select the new position:");
            do {
                chosenRow = scanner.nextInt();
                chosenColumn = scanner.nextInt();
            } while (position_isNotValid(chosenRow, chosenColumn, worker));
            int finalChosenRow = chosenRow;
            int finalChosenColumn = chosenColumn;
            notifyObserver((ViewObserver obs) -> obs.onUpdateWorkerPosition(finalChosenRow, finalChosenColumn));
        }
    }


    public void askNewBuildingPosition(Worker worker) {
        int chosenRow;
        int chosenColumn;
        System.out.println("Select in which position you want your Worker to build!");
        System.out.println("Your Worker can Build here:");
        if (worker.getPossibleBuilds().isEmpty()) {
            System.out.println("Oh no! Unfortunately you can't build...");
        } else {
            for (int i = 0; i < worker.getPossibleBuilds().size(); i++) {
                System.out.println("Position " + i + 1 + ":" + "Row: " + worker.getPossibleBuilds().get(i).getRow() +
                        "Column: " + worker.getPossibleBuilds().get(i).getColumn());
            }
            System.out.println("Select where to build:");
            do {
                chosenRow = scanner.nextInt();
                chosenColumn = scanner.nextInt();
            } while (position_isNotValid(chosenRow, chosenColumn, worker));
            int finalChosenRow = chosenRow;
            int finalChosenColumn = chosenColumn;
            notifyObserver((ViewObserver obs) -> obs.onUpdateBuild(finalChosenRow, finalChosenColumn));
        }

    }


    public void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = "       ";
            }
        }
    }

    @Override
    public String showBoard() {
        String strBoard = "";
        for (int i = 0; i < ROWS; i++) {
            strBoard += "\n+-----+-----+-----+-----+-----+\n";
            for (int j = 0; j < COLUMNS; j++) {
                if (j == 0)
                    strBoard += "|" + board[i][j] + "|";
                else
                    strBoard += board[i][j] + "|";
            }
            if (i == ROWS - 1)
                strBoard += "\n+-----+-----+-----+-----+-----+\n";
        }
        return strBoard;

    }

    //method to check if a position is valid
    boolean position_isNotValid(int chosenRow, int chosenColumn, Worker worker) {
        boolean is_True = (worker.getPossibleMoves().get(0).getRow() != chosenRow) ||
                (worker.getPossibleMoves().get(0).getColumn() != chosenColumn);
        for (int i = 1; i < worker.getPossibleMoves().size(); i++) {
            is_True = (is_True && ((worker.getPossibleMoves().get(i).getRow() != chosenRow) ||
                    (worker.getPossibleMoves().get(i).getColumn() != chosenColumn)));
        }

        return is_True;

    }

}
