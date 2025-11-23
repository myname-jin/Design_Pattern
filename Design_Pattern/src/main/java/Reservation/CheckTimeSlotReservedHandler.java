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

/**
 *
 * @author namw2
 */
public class CheckTimeSlotReservedHandler extends ReservationCheckHandler {

    @Override
    protected boolean validate(ReservationRequest request) {
        String path = "src/main/resources/reservation.txt";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    String reservedUserId = parts[2];
                    String reservedRoom = parts[5];
                    String reservedDate = parts[6];
                    String reservedStart = parts[8];
                    String reservedEnd = parts[9];

                    if (reservedUserId.equals(request.getUserId())) {
                        continue;
                    }

                    if (reservedRoom.equals(request.getSelectedRoomName()) && reservedDate.equals(request.getDate())) {
                        Date reservedStartTime = sdf.parse(reservedStart);
                        Date reservedEndTime = sdf.parse(reservedEnd);

                        for (String timeSlot : request.getTimes()) {
                            String[] range = timeSlot.split("~");
                            if (range.length == 2) {
                                Date newStartTime = sdf.parse(range[0].trim());
                                Date newEndTime = sdf.parse(range[1].trim());

                                // 중복 조건: 시작 시간이 기존 예약의 끝 이전 && 끝 시간이 기존 예약의 시작 이후
                                if (newStartTime.before(reservedEndTime) && newEndTime.after(reservedStartTime)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("중복 시간 검사 오류: " + e.getMessage());
        }

        return true;
    }

    @Override
    protected String getErrorMessage() {
        return "선택한 시간대에 이미 예약이 존재합니다.";
    }

}
