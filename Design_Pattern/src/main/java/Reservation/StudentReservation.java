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
        // 학생인 경우에만 체크
        if (isUserTypeStudent(userType)) {
            String filePath = "src/main/resources/banlist.txt";

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // 콤마로 분리하여 첫 번째 항목(학번)만 비교
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].trim().equals(userId)) {
                        return true; // 차단된 사용자 발견
                    }
                }
            } catch (IOException e) {
                System.out.println("제한 사용자 파일 읽기 실패: " + e.getMessage());
            }
            return false; // 목록에 없음
        } 
        return false; // 학생 아님
    }

    @Override
    protected boolean checkUserConstraints(String userId, String date, List<String> times) {
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
    protected String confirmReservation() {
        return "예약대기";
    }


    @Override
    protected boolean processTimeSlotConflict(String userId, String date, List<String> times, String roomName) {
        // 학생은 이미 예약된 시간이면 예약 불가
        if (isTimeSlotAlreadyReserved(roomName, date, times, userId)) {
            view.showMessage("선택한 시간대에 이미 예약이 존재합니다.");
            return false; // 진행 중단
        }
        return true; // 충돌 없으면 진행
    }

}
