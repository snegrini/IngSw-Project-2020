package view.cli;

import observer.ViewObserver;
import view.View;

import java.util.*;

public class Cli extends View {

    private Scanner scanner;

    public Cli() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void init() {
        // TODO show welcome screen
    }

    @Override
    public void askServerInfo() {
        Map<String, String> serverInfo = new HashMap<>();

        // TODO check user input
        System.out.print("Enter the server address: ");
        serverInfo.put("address", scanner.nextLine());

        // TODO check user input
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
    public void askPlayerNumber() {

    }




}
