package org.example.repositories;

import org.example.models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality to persist and retrieve user data to and from a file.
 * <p>
 * This class handles the saving and loading of user data by serializing and deserializing
 * the {@code User} objects to and from a specified file path.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class UserDataStorage {
    private final String filePath;

    /**
     * Constructs a {@code UserDataStorage} with the specified file path.
     *
     * @param filePath the path where the user data will be stored
     */
    public UserDataStorage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Persists a list of users to a file by serializing the list of users and saving it to disk.
     * If the file already exists, it is deleted and replaced with the new data.
     *
     * @param users the list of users to be saved
     */
    public void persistUserData(List<User> users) {
        File file = new File(filePath);

        // Delete the existing file if it exists
        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("Error deleting existing file");
                return;
            }
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            file.createNewFile();
            objectOutputStream.writeObject(users);

        } catch (IOException ex) {
            System.out.println("Failed to save data: " + ex.getMessage());
        }
    }

    /**
     * Retrieves the persisted user data from a file by deserializing the file's contents.
     * If the file does not exist or an error occurs during deserialization, an empty list is returned.
     *
     * @return the list of users read from the file, or an empty list if an error occurs
     */
    public ArrayList<User> getPersistedUserData() {
        File file = new File(filePath);

        // Check if the file exists
        if (!file.exists()) {
            System.out.println("Save file is missing.");
            return new ArrayList<>();
        }

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            return (ArrayList<User>) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Failed to read the save file: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
