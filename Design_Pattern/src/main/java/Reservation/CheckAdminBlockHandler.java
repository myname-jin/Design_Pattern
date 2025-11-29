/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

/**
 *
 * @author adsd3
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 관리자가 차단한 날짜/시간인지 검사하는 핸들러
 */
public class CheckAdminBlockHandler extends ReservationCheckHandler {

    private static final String BLOCK_FILE = "src/main/resources/blocked_dates.txt";

    @Override
    protected boolean validate(ReservationRequest request) {
        String reqDate = request.getDate();         // 요청 날짜 (yyyy-MM-dd)
        String reqRoom = request.getSelectedRoomName(); // 요청 강의실 (예: 911)
        List<String> reqTimes = request.getTimes(); // 요청 시간대 목록 (예: ["09:00~10:00", ...])

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(BLOCK_FILE), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 파일 포맷: 타입(0),날짜(1),방타입(2),호수(3),시간대(4)
                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                String type = parts[0];
                String blockDate = parts[1];

                // 1. 날짜가 다르면 패스
                if (!blockDate.equals(reqDate)) {
                    continue;
                }

                // 2. [모두 차단]인 경우 -> 무조건 실패
                if ("모두".equals(type)) {
                    return false; 
                }

                // 3. [일부 차단]인 경우 -> 호수와 시간이 겹치는지 확인
                if ("일부".equals(type) && parts.length >= 5) {
                    String blockRoom = parts[3]; // 차단된 호수 (예: 911)
                    String blockTime = parts[4]; // 차단된 교시 (예: 1)

                    // 호수가 일치하는지 확인
                    if (blockRoom.equals(reqRoom)) {
                        // 시간대가 겹치는지 확인
                        for (String timeRange : reqTimes) {
                            // 요청 시간("09:00~10:00")을 교시("1")로 변환하여 비교하거나,
                            // 또는 파일에 저장된 방식에 맞춰 비교해야 함.
                            // 여기서는 간단히 시작 시간이나 교시 인덱스 매핑 로직이 필요함.
                            
                            // (팁: 만약 파일에 "1"로 저장되고, reqTimes가 "09:00~10:00"이라면 변환 필요)
                            // 예시: 9시 시작이면 1교시로 간주
                            String reqStartHour = timeRange.split(":")[0]; // "09"
                            int hour = Integer.parseInt(reqStartHour);
                            int slotIndex = hour - 8; // 9시 -> 1교시
                            
                            if (String.valueOf(slotIndex).equals(blockTime)) {
                                return false; // 차단된 시간과 겹침
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("차단 파일 검사 중 오류: " + e.getMessage());
            // 파일이 없거나 읽기 실패 시, 일단 예약 허용(true) 또는 불허(false) 정책 결정
        }

        return true; // 걸리는 게 없으면 통과
    }

    @Override
    protected String getErrorMessage() {
        return "관리자에 의해 예약이 차단된 날짜 또는 시간입니다.";
    }
}