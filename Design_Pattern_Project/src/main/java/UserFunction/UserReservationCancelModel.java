/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jms5310
 */
public class UserReservationCancelModel {
    private static final String RESERVATION_FILE = "src/main/resources/reservation.txt";
    private static final String CANCEL_FILE = "src/main/resources/cancel.txt";
    
    // reservation.txt에서 예약 삭제
    public boolean cancelReservation(String userId, String date, String room) {
        List<String> updatedReservations = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(RESERVATION_FILE), "UTF-8"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // parts[2]: userId, parts[6]: date, parts[5]: roomNumber
                if (parts.length >= 12 && parts[2].equals(userId) && 
                    parts[6].equals(date) && parts[5].equals(room)) {
                    found = true; // 삭제할 예약 찾음
                } else {
                    updatedReservations.add(line); // 유지할 예약
                }
            }
            
            if (found) {
                try (BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(RESERVATION_FILE), "UTF-8"))) {
                    for (String reservation : updatedReservations) {
                        writer.write(reservation);
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
    
    // cancel.txt에 취소 이력 기록
  public boolean saveCancelReason(String userId, String reason) {
    try {
        // 기존 파일이 있는지 확인하고, 있으면 내용 읽기
        File file = new File(CANCEL_FILE);
        boolean fileExists = file.exists() && file.length() > 0;
        
        // 파일 열기 (true는 append 모드)
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(CANCEL_FILE, true), "UTF-8"))) {
            
            // 파일이 이미 존재하고 내용이 있으면, 줄바꿈 추가
            if (fileExists) {
                writer.newLine();  // 이 부분이 중요합니다
            }
            
            // 취소 정보 작성
            writer.write(userId + ", " + reason);
            
            return true;
        }
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}
}

