/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author namw2
 */
public class StudentReservation extends AbstractReservation {

    @Override
    protected boolean isUserBanned(String userId, String userType) {
        if (isUserTypeStudent(userType) == true) {
            List<String> bannedUsers = new ArrayList<>();
            String filePath = "src/main/resources/banlist.txt";

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    bannedUsers.add(line.trim());
                }
            } catch (IOException e) {
                System.out.println("제한 사용자 파일 읽기 실패: " + e.getMessage());
            }
            
            return bannedUsers.contains(userId);

        }
        else return false;
    }

    @Override
    protected boolean studentConstraints(String userId, String date, List<String> times) {
        if (isUserAlreadyReserved(userId, date)) {
            view.showMessage("학생은 하루 1회만 예약할 수 있습니다.");
            return false; // 1일 1회 제한 초과 시 로직 종료
        }
        int totalMinutes = calculateTotalDuration(times);
        if (totalMinutes > 120) {
            view.showMessage("총 예약 시간이 2시간(120분)을 초과할 수 없습니다.");
            return false; // 예약 시간 제한 초과 시 로직 종료
        }
        return true;
        
        
    }

    @Override
    protected String confirmReservation(String userType) {
          if (userType.equals("교수")) {
                return "예약확정";
            } else {
                return "예약대기";
            }
    }

}
