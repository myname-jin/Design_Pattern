/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author suk22
 */
public class NotificationController {

    private NotificationModel model;
    private NotificationView view;

    // 예약 상태 별로 이전 상태 저장
    protected Set<String> shownPending = new HashSet<>();
    protected Set<String> shownAll = new HashSet<>();

    private Timer timer;

    public NotificationController() {
        this.model = new NotificationModel();
    }

    public void setModel(NotificationModel model) {
        this.model = model;
    }

    public void startMonitoring() {
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> pendingList = model.getPendingReservations(); // 예약 대기 상태
                List<String> allList = model.getAllReservations();         // 전체 예약

                Set<String> currentPendingSet = new HashSet<>(pendingList);
                Set<String> currentAllSet = new HashSet<>(allList);

                List<String> newPendingMessages = new ArrayList<>();
                List<String> removedReservations = new ArrayList<>();

                // 예약 대기 추가 감지
                for (String msg : pendingList) {
                    if (!shownPending.contains(msg)) {
                        newPendingMessages.add(msg);
                    }
                }

                // 예약 취소 감지
                for (String old : shownAll) {
                    String oldName = old.split(",")[0];
                    boolean stillExists = currentAllSet.stream()
                            .anyMatch(newLine -> newLine.split(",")[0].equals(oldName));

                    if (!stillExists) {
                        removedReservations.add(old);

                    }
                }

                // 상태 갱신
                shownPending = currentPendingSet;
                shownAll = currentAllSet;

                // 알림 표시
                if (!newPendingMessages.isEmpty() || !removedReservations.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        StringBuilder sb = new StringBuilder();

                        if (!newPendingMessages.isEmpty()) {
                            sb.append("📌 새로운 예약 대기 ").append(newPendingMessages.size()).append("건\n");
                        }

                        for (String removed : removedReservations) {
                            String name = removed.split(",")[0];
                            sb.append("❌ ").append(name).append("님의 예약이 취소되었습니다.\n");
                        }

                        JOptionPane.showMessageDialog(null, sb.toString(), "알림", JOptionPane.INFORMATION_MESSAGE);
                    });
                }
            }
        }, 0, 5_000);
    }

    // 예약 상태 즉시 갱신용 공개 메서드
    public void refreshNotifications() {
        List<String> pendingList = model.getPendingReservations();
        shownPending = new HashSet<>(pendingList); // 상태만 초기화

        List<String> allList = model.getAllReservations();
        shownAll = new HashSet<>(allList);
    }

    public void showNotificationView() {
        List<String> pendingList = model.getPendingReservations();
        if (pendingList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "현재 대기 중인 예약이 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(() -> {
            view = new NotificationView(pendingList);
            view.setVisible(true);
        });
    }

    public void stopMonitoring() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public Map<String, List<String>> detectNotificationChangesForTest() {
        List<String> pendingList = model.getPendingReservations(); // 예약 대기 상태
        List<String> allList = model.getAllReservations();         // 전체 예약

        Set<String> currentPendingSet = new HashSet<>(pendingList);
        Set<String> currentAllSet = new HashSet<>(allList);

        List<String> newPendingMessages = new ArrayList<>();
        List<String> removedReservations = new ArrayList<>();

        for (String msg : pendingList) {
            if (!shownPending.contains(msg)) {
                newPendingMessages.add(msg);
            }
        }

        for (String old : shownAll) {
            if (!currentAllSet.contains(old)) {
                removedReservations.add(old);
            }
        }

        // 상태 갱신
        shownPending = currentPendingSet;
        shownAll = currentAllSet;

        Map<String, List<String>> result = new HashMap<>();
        result.put("newPending", newPendingMessages);
        result.put("removed", removedReservations);

        return result;
    }
}
