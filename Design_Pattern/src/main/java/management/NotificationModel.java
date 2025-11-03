/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author suk22
 */
public class NotificationModel {

    private final String reservationFile;

    public NotificationModel() {
        this("src/main/resources/reservation.txt");
    }

    // 테스트용 생성자 (경로 주입 가능)
    public NotificationModel(String reservationFile) {
        this.reservationFile = reservationFile;
    }

    public List<String> getPendingReservations() {
        List<String> pendingList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(reservationFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int statusIndex = parts.length - 1;
                if ("예약대기".equals(parts[statusIndex].trim())) {
                    String name       = parts[0].trim();
                    String roomType   = parts[4].trim();
                    String roomNumber = parts[5].trim();
                    String date       = parts[6].trim();
                    String dayOfWeek  = parts[7].trim();
                    String startTime  = parts[8].trim();
                    String endTime    = parts[9].trim();
                    String purpose    = parts[10].trim();

                    String message = String.format(
                        "%s님이 %s %s호를 %s(%s) %s~%s에 %s 목적으로 예약 신청하였고, 현재 예약 대기 상태입니다.",
                        name, roomType, roomNumber, date, dayOfWeek, startTime, endTime, purpose
                    );
                    pendingList.add(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pendingList;
    }

    public List<String> getAllReservations() {
        List<String> allList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(reservationFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allList;
    }
}
