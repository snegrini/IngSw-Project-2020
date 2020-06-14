package it.polimi.ingsw.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class InputReadTask implements Callable<String> {
    private final BufferedReader br;

    public InputReadTask() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String call() throws IOException, InterruptedException {
        String input;
        try {
            // wait until there is data to complete a readLine()
            while (!br.ready()) {
                Thread.sleep(200);
            }
            input = br.readLine();
        } catch (InterruptedException e) {
            System.out.println("InputReadTask InterruptedException");
            throw e;
        }
        return input;
    }
}
