package it.polimi.ingsw.persistence;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.server.Server;

import java.io.*;

public class StorageData {

    public StorageData() {
    }

    /**
     * Save current Game Controller on a file named "match.bless".
     *
     * @param gameController current Game Controller.
     */
    public void store(GameController gameController) {
        Persistence persistence = new Persistence(gameController);

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File("match.bless"))) {

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(persistence);
            Server.LOGGER.info("Game Saved.");


        } catch (IOException e) {
            Server.LOGGER.severe(e.getMessage());
        }
    }

    /**
     * Restore a Saved Game.
     *
     * @return Game Controller of saved Game.
     */
    public GameController restore() {
        Persistence persistence;

        try (FileInputStream fileInputStream = new FileInputStream(new File("match.bless"))) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            persistence = (Persistence) objectInputStream.readObject();

            return persistence.getGameController();

        } catch (IOException e) {
            Server.LOGGER.severe("No File Found.");
        } catch (ClassNotFoundException e) {
            Server.LOGGER.severe(e.getMessage());
        }
        return null;
    }

    /**
     * Delete Saved Game.
     */
    public void delete() {
        File file = new File("match.bless");
        if (file.exists())
            file.delete();
    }
}
