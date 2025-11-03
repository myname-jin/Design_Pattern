    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

import java.io.*;
import java.util.*;

public class ReservationModel {
    private final Map<String, Integer> roomTotals = new TreeMap<>();
    private final Map<String, Map<String, Integer>> roomByDay = new HashMap<>();
    private final String[] rooms = {"911", "912", "913", "914", "915", "916", "917", "918"};
    private final String[] days = {"월", "화", "수", "목", "금"};
    private final String filePath = "C:\\SWG\\JAVAPROJECKT\\src\\main\\resources/visualization.txt";

    public ReservationModel() {
        loadData();
    }

    public Map<String, Integer> getRoomTotals() {
        return roomTotals;
    }

    public Map<String, Integer> getRoomByDay(String room) {
        return roomByDay.getOrDefault(room, new HashMap<>());
    }

    private void loadData() {
        for (String room : rooms) {
            roomTotals.put(room, 0);
            roomByDay.put(room, new HashMap<>());
            for (String day : days) {
                roomByDay.get(room).put(day, 0);
            }
        }

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("월,911\n월,912\n화,911\n수,911\n수,913\n목,915\n금,911\n금,912\n화,914\n화,911\n목,911");
                }
            } catch (IOException ignored) {}
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String day = parts[0].trim();
                    String room = parts[1].trim();
                    if (roomTotals.containsKey(room)) {
                        roomTotals.put(room, roomTotals.get(room) + 1);
                        Map<String, Integer> dayMap = roomByDay.get(room);
                        dayMap.put(day, dayMap.getOrDefault(day, 0) + 1);
                    }
                }
            }
        } catch (IOException ignored) {}
    }
}
