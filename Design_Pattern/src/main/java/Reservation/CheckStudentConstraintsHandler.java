/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author namw2
 */
public class CheckStudentConstraintsHandler extends ReservationCheckHandler {

    private String message = "";

    protected boolean isUserAlreadyReserved(String userId, String date) {
        String path = "src/main/resources/reservation.txt";
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    if (parts[2].equals(userId) && parts[6].equals(date)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("예약 기록 읽기 실패: " + e.getMessage());
        }
        return false;
    }

    protected int calculateTotalDuration(List<String> times) {
        int total = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (String time : times) {
            try {
                String[] parts = time.split("~");
                Date start = sdf.parse(parts[0]);
                Date end = sdf.parse(parts[1]);
                long diff = (end.getTime() - start.getTime()) / (1000 * 60);
                total += diff;
            } catch (ParseException e) {
                System.out.println("시간 파싱 오류: " + time);
            }
        }
        return total;
    }

    @Override
    protected boolean validate(ReservationRequest request) {
        if (isUserAlreadyReserved(request.getUserId(), request.getDate())) {
            //view.showMessage("학생은 하루 1회만 예약할 수 있습니다.");
            message = "학생은 하루 1회만 예약할 수 있습니다.";
            return false; // 1일 1회 제한 초과 시 로직 종료
        }
        int totalMinutes = calculateTotalDuration(request.getTimes());
        if (totalMinutes > 120) {
            //view.showMessage("총 예약 시간이 2시간(120분)을 초과할 수 없습니다.");
            message = "총 예약 시간이 2시간(120분)을 초과할 수 없습니다.";
            return false; // 예약 시간 제한 초과 시 로직 종료
        }
        return true;
    }

    @Override
    protected String getErrorMessage() {
        return message;
    }

}
