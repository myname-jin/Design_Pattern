package UserFunction;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserReservationCancelModel {

    private static final String RESERVATION_FILE = "src/main/resources/reservation.txt";
    private static final String CANCEL_FILE = "src/main/resources/cancel.txt";

    /**
     * 예약 상태를 '취소'로 변경하는 메서드
     * @param userId 학번
     * @param date 날짜
     * @param room 강의실
     * @param startTime 시작시간 (정확한 식별을 위해 필수)
     * @return 성공 여부
     */
    public boolean cancelReservation(String userId, String date, String room, String startTime) {
        List<String> allLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(RESERVATION_FILE), StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                
                // 12개 컬럼 구조: [0]학번, [5]강의실, [6]날짜, [8]시작시간, [11]상태
                if (parts.length >= 12) {
                    String rId = parts[0].trim();
                    String rRoom = parts[5].trim();
                    String rDate = parts[6].trim();
                    String rStart = parts[8].trim();
                    String rStatus = parts[11].trim();

                    // 조건 일치 (이미 취소된 건 제외)
                    if (rId.equals(userId) && rDate.equals(date) && 
                        rRoom.equals(room) && rStart.equals(startTime) && 
                        !"취소".equals(rStatus)) {
                        
                        // 상태를 '취소'로 변경
                        parts[11] = "취소"; 
                        String updatedLine = String.join(",", parts);
                        allLines.add(updatedLine);
                        found = true;
                    } else {
                        allLines.add(line); // 그대로 유지
                    }
                } else {
                    allLines.add(line); // 형식이 다른 줄도 유지
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // 변경된 내용 파일에 덮어쓰기
        if (found) {
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(RESERVATION_FILE), StandardCharsets.UTF_8))) {
                for (String l : allLines) {
                    writer.write(l);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return found;
    }

    /**
     * 취소 사유를 cancel.txt에 저장
     * 형식: 학번,취소사유
     */
    public boolean saveCancelReason(String userId, String reason) {
        File file = new File(CANCEL_FILE);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            
            // 요청하신 포맷: "학번,사유" (콤마 뒤 공백 없이)
            writer.write(userId + "," + reason);
            writer.newLine();
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}