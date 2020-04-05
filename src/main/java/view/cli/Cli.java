package view.cli;

import view.ViewListener;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Cli {

    private Scanner scanner;
    private List<ViewListener> listeners;

    public Cli() {
        scanner = new Scanner(System.in);
        listeners = new ArrayList<>();

    }

    public void init() {
        Map<String, String> serverInfo = askServerInfo();
        notifyListeners((ViewListener lis) -> lis.doConnect(serverInfo));
    }

    private Map<String, String> askServerInfo() {
        Map<String, String> serverInfo = new HashMap<>();

        // TODO check user input
        System.out.print("Enter the server address: ");
        serverInfo.put("address", scanner.nextLine());

        // TODO check user input
        System.out.print("Server port: ");
        serverInfo.put("port", scanner.nextLine());

        return serverInfo;
    }

    public void askNickname() {
        // TODO check user input
        System.out.print("Enter your nickname: ");
        String nickname = scanner.nextLine();
        notifyListeners((ViewListener lis) -> lis.checkNickname(nickname));
    }

    public void addListener(ViewListener lis) {
        listeners.add(lis);
    }

    public void removeListener(ViewListener lis) {
        listeners.remove(lis);
    }

    private void notifyListeners(Consumer<ViewListener> lambda)
    {
        for (ViewListener listener: listeners) {
            lambda.accept(listener);
        }
    }



}
