// 파일: ReservationFileUtil.java
package Reservation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationFileUtil {

    private static final String RESERVATION_PATH = "src/main/resources/reservation.txt";

    public static List<String> readAllLines() throws IOException {
        Path p = Paths.get(RESERVATION_PATH);
        if (!Files.exists(p)) return new ArrayList<>();
        return Files.readAllLines(p, StandardCharsets.UTF_8);
    }

    /**
     * 특정 예약(line)을 식별하는 키가 일치하면 그 라인의 status 컬럼(마지막)을 새로운 상태로 바꿉니다.
     *
     * match: parts[2] == userId && parts[6] == date && parts[8] == startTime && parts[5] == roomNumber
     *
     * @return true if updated at least one line
     */
    public static boolean updateReservationStatus(String userId, String roomNumber, String date, String startTime, String fromStatus, String toStatus) {
        Path p = Paths.get(RESERVATION_PATH);
        Path temp = Paths.get(RESERVATION_PATH + ".tmp");

        boolean updated = false;
        try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8);
             BufferedWriter bw = Files.newBufferedWriter(temp, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 12) {
                    String uId = parts[2];
                    String rNum = parts[5];
                    String rDate = parts[6];
                    String rStart = parts[8];
                    String status = parts[11];

                    if (uId.equals(userId) && rNum.equals(roomNumber) && rDate.equals(date) && rStart.equals(startTime) && status.equals(fromStatus)) {
                        parts[11] = toStatus;
                        String newLine = String.join(",", parts);
                        bw.write(newLine);
                        bw.newLine();
                        updated = true;
                        continue;
                    }
                }
                // 기본: 원라인 그대로 씀
                bw.write(line);
                bw.newLine();
            }
        } catch (NoSuchFileException nsf) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // 원래 파일 교체 (원자적이라 완전한 교체 보장)
        try {
            Files.move(temp, p, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException amnse) {
            // 플랫폼에서 atomic 지원 안되면 대체로 교체
            try {
                Files.move(temp, p, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return updated;
    }
}
