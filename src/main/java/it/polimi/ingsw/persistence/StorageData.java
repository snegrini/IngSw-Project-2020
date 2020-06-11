package it.polimi.ingsw.persistence;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.server.Server;

import java.io.*;

public class StorageData {

    public StorageData(){

    }

    public void store(GameController gameController) {
        Persistence persistence = new Persistence(gameController);

        try ( FileOutputStream fileOutputStream = new FileOutputStream(new File("match.bless")) ) {

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(persistence);
            Server.LOGGER.info("Game Saved.");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameController restore() {
        Persistence persistence;

        try (FileInputStream fileInputStream = new FileInputStream(new File("match.bless"))) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            persistence = (Persistence) objectInputStream.readObject();
            GameController gameController = persistence.getGameController();

            return gameController;

        } catch (IOException e) {
            Server.LOGGER.info("No File Found.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
