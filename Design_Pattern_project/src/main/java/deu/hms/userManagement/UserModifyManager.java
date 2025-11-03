/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.userManagement;

import java.io.*;
import java.util.*;

/**
 *
 * @author yunhe
 */
public class UserModifyManager {
    public static void modifyUser(String id, User updatedUser) {
        List<User> users = loadUsersFromFile("users.txt");
        for (User user : users) {
            if (user.getId().equals(id)) {
                user.setName(updatedUser.getName());
                user.setPassword(updatedUser.getPassword());
                user.setRole(updatedUser.getRole());
            }
        }
        saveUsersToFile(users);
    }

    private static List<User> loadUsersFromFile(String filePath) {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userFields = line.split(", ");
                if (userFields.length == 4) {
                    users.add(new User(userFields[0], userFields[1], userFields[2], userFields[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static void saveUsersToFile(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (User user : users) {
                writer.write(user.getId() + ", " + user.getPassword() + ", " + user.getName() + ", " + user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
