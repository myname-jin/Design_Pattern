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
public class CheckUserBannedHandler extends ReservationCheckHandler {

    
    
    @Override
    protected boolean validate(ReservationRequest request) {
        String userId = request.getUserId();   // 비교 대상은 userId
        String filePath = "src/main/resources/banlist.txt";

        List<String> bannedUsers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                bannedUsers.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("제한 사용자 파일 읽기 실패: " + e.getMessage());
        }

        // ❗ banlist에 있으면 예약 "불가" → false
        if (bannedUsers.contains(userId)) {
            //request.getView().showMessage("예약이 제한된 사용자입니다.");
            return false;
        }

        // ✔ banlist에 없으면 통과 → true
        return true;
    }

    @Override
    protected String getErrorMessage() {
        return "제한된 사용자입니다.";
    }

}
