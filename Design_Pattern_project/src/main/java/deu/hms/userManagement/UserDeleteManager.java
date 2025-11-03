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
public class UserDeleteManager {
    public static void deleteUser(String userId, String filePath) {
        List<String> lines = new ArrayList<>();
        boolean userDeleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userFields = line.split(", ");
                if (!userFields[0].equals(userId)) {
                    lines.add(line);
                } else {
                    userDeleted = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userDeleted) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
