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
public class ProfessorReservation extends AbstractReservation {

    @Override
    protected boolean isUserBanned(String userId, String userType) {
        return false;
        //교수는 banned 당할일 없다고 설정 -> 필요시 코드 추가 가능
    }

    @Override
    protected boolean studentConstraints(String userId, String date, List<String> times) {
        return true;
        // 학생 제한이니까 교수는 제한 없음 -> 원하면 코드 추가 가능
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
