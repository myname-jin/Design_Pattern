package UserFunction;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jms5310
 */
public class UserReservationCancelModel {

    private static  String RESERVATION_FILE = "src/main/resources/reservation.txt";
    private static  String CANCEL_FILE = "src/main/resources/cancel.txt";

    // 예약 상태를 '취소'로 변경
    public boolean cancelReservation(String userId, String date, String room, String startTime) {
        List<String> allLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(RESERVATION_FILE), StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                
                if (parts.length >= 12) {
                    String rId = parts[0].trim();
                    String rRoom = parts[5].trim();
                    String rDate = parts[6].trim();
                    String rStart = parts[8].trim();

                    if (rId.equals(userId) && rDate.equals(date) && rRoom.equals(room) && rStart.equals(startTime)) {
                        // 상태를 '취소'로 변경하여 저장
                        parts[11] = "취소"; 
                        String updatedLine = String.join(",", parts);
                        allLines.add(updatedLine);
                        found = true;
                    } else {
                        allLines.add(line); 
                    }
                } else {
                    allLines.add(line); 
                }
            }

            if (found) {
                try (BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(RESERVATION_FILE), StandardCharsets.UTF_8))) {
                    for (String l : allLines) {
                        writer.write(l);
                        writer.newLine();
                    }
                }
            }

            return found;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 취소 사유 저장
    public boolean saveCancelReason(String userId, String reason) {
        try {
            File file = new File(CANCEL_FILE);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
                writer.write(userId + "," + reason);
                writer.newLine();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}