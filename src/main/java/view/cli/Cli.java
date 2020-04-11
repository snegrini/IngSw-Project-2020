package view.cli;

import view.View;
import view.ViewObserver;

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

        notifyListeners((ViewObserver lis) -> lis.doConnect(serverInfo));
    }

    @Override
    public void askNickname() {
        System.out.print("Enter your nickname: ");
        String nickname = scanner.nextLine();
        notifyListeners((ViewObserver lis) -> lis.checkNickname(nickname));
    }

    @Override
    public void askPlayerNumber() {

    }




}
