/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

/**
 *
 * @author adsd3
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class ReservationStatusScheduler {

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // 상태 업데이트를 예약하는 메서드
    public void scheduleStatusUpdate(String checkInDate, int rowIndex, DefaultTableModel model) {
        try {
            // 현재 시간과 체크인 날짜의 18시를 계산
            LocalDateTime now = LocalDateTime.now();
            LocalDate checkInDay;

            // checkInDate를 LocalDate로 변환
            try {
                checkInDay = LocalDate.parse(checkInDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                System.err.println("체크인 날짜 형식이 잘못되었습니다: " + checkInDate);
                return;
            }

            LocalDateTime targetTime = LocalDateTime.now().plusSeconds(10);
            //LocalDateTime targetTime = checkInDay.atTime(18, 0); // 오후 6시 기준
            long delay = Duration.between(now, targetTime).toMillis();

            if (delay < 0) {
                System.out.println("체크인 날짜가 이미 지났습니다. 즉시 상태를 업데이트합니다.");
                delay = 0; // 이미 지난 경우 즉시 실행
            }

            // 스케줄러를 통해 상태 업데이트 예약
            scheduler.schedule(() -> {
                SwingUtilities.invokeLater(() -> {
                    try {
                        // 상태 열에서 현재 상태 확인 및 업데이트
                        String cardStatus = (String) model.getValueAt(rowIndex, 10); // 상태 열 가져오기
                        String newStatus = "카드등록".equals(cardStatus) ? "예약확정" : "예약취소";
                        model.setValueAt(newStatus, rowIndex, 10); // 테이블에서 상태 변경
                        System.out.println("테이블 상태 업데이트: " + newStatus);

                        // 파일 상태 업데이트
                        updateFileStatus(model, rowIndex, newStatus);

                    } catch (Exception e) {
                        System.err.println("상태 업데이트 중 오류 발생: " + e.getMessage());
                    }
                });
            }, delay, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            System.err.println("스케줄링 중 오류 발생: " + e.getMessage());
        }
    }

    private void updateFileStatus(DefaultTableModel model, int rowIndex, String newStatus) {
        try {
            String uniqueNumber = (String) model.getValueAt(rowIndex, 0); // 고유번호 가져오기
            System.out.println("파일 업데이트: 고유번호=" + uniqueNumber + ", 새 상태=" + newStatus);

            // 상태 업데이트
            FileManager.updateStatus(uniqueNumber, newStatus, "Reservation.txt");

        } catch (IOException e) {
            System.err.println("파일 상태 업데이트 중 오류 발생: " + e.getMessage());
        }
    }

    // 서비스 종료 메서드
    public void shutdown() {
        scheduler.shutdown();
    }
}