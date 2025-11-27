// 파일: ReservationMonitor.java
package Reservation;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReservationMonitor extends Thread {

    private final String userId;
    private final long pollIntervalMillis;
    private final File reservationFile;
    private long lastModified = 0L;
    // 이미 알람 보낸 예약 키(중복 알림 방지)
    private final Set<String> notifiedSet = ConcurrentHashMap.newKeySet();

    public ReservationMonitor(String userId) {
        this(userId, 5000L); // 기본 5초 폴링
    }

    public ReservationMonitor(String userId, long pollIntervalMillis) {
        this.userId = userId;
        this.pollIntervalMillis = pollIntervalMillis;
        this.reservationFile = new File("src/main/resources/reservation.txt");
        setDaemon(true);
        System.out.println("Monitoring file: " + reservationFile.getAbsolutePath());

    }

    @Override
    public void run() {
        if (reservationFile.exists()) {
            lastModified = reservationFile.lastModified();
        } else {
            lastModified = 0L;
        }

        while (true) {
            try {
                Thread.sleep(pollIntervalMillis);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }

            long lm = reservationFile.exists() ? reservationFile.lastModified() : 0L;
            if (lm != lastModified) {
                lastModified = lm;
                scanAndNotify();
            } else {
                // 변경 없더라도 정기적으로 10분 전 알림 체크 필요
                scanAndNotify();
            }
        }
    }

    private void scanAndNotify() {
        List<String> lines;
        try {
            lines = ReservationFileUtil.readAllLines();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");

        Date now = new Date();

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] parts = line.split(",", -1);
            if (parts.length < 12) continue;

            String rUserId = parts[0];
            String roomNumber = parts[5];
            String date = parts[6]; // yyyy-MM-dd
            String start = parts[8]; // HH:mm
            String status = parts[11];

            if (!rUserId.equals(userId)) continue;
            if (!"승인".equals(status)) continue;

            String key = roomNumber + "|" + date + "|" + start;
            if (notifiedSet.contains(key)) continue;

            try {
                Date startDateTime = dateFmt.parse(date + " " + start);
                long diffMillis = startDateTime.getTime() - now.getTime();

                // 조건: 시작 10분 전(<= 10분 and > 0)
                if (diffMillis <= 10 * 60 * 1000L && diffMillis > 0) {
                    // schedule notifier: we want a dialog now, and cancellation should happen at (start + 10min)
                    long millisUntilCancel = (startDateTime.getTime() + 10 * 60 * 1000L) - now.getTime();
                    if (millisUntilCancel < 0) millisUntilCancel = 0;

                    // show dialog on EDT
                    final long scheduleMillis = millisUntilCancel;
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        NotificationDialog dlg = new NotificationDialog(userId, roomNumber, date, start, scheduleMillis);
                    });

                    notifiedSet.add(key);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

