package Reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NotificationDialog extends JFrame {

    private volatile boolean confirmed = false;
    private final String userId;
    private final String roomNumber;
    private final String date;       // yyyy-MM-dd
    private final String startTime;  // HH:mm
    private javax.swing.Timer cancelTimer;

    public NotificationDialog(String userId, String roomNumber, String date, String startTime, long millisUntilCancel) {
        super("예약 확인 알림");

        this.userId = userId;
        this.roomNumber = roomNumber;
        this.date = date;
        this.startTime = startTime;

        // ----------------------------
        // 기본 UI 설정
        // ----------------------------
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("<html>예약이 10분 후에 시작합니다.<br/>"
                + "입실 확인을 누르지 않으면 예약이 자동 취소됩니다.</html>");
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(label, BorderLayout.CENTER);

        // ----------------------------
        // 버튼 영역
        // ----------------------------
        JButton confirmBtn = new JButton("입실 확인");
        JButton cancelBtn = new JButton("예약 취소");

        // 입실 확인 버튼: 취소 타이머 중단 + 창 닫기
        confirmBtn.addActionListener(e -> {
            confirmed = true;
            if (cancelTimer != null) cancelTimer.stop();
            dispose();
        });

        // 예약 취소 버튼: 즉시 취소 처리
        cancelBtn.addActionListener(e -> {
            boolean ok = ReservationFileUtil.updateReservationStatus(
                    userId, roomNumber, date, startTime, "승인", "취소"
            );
            System.out.println("Manual cancel: " + ok);

            if (cancelTimer != null) cancelTimer.stop();
            dispose();
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(confirmBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // ----------------------------
        // 창 닫기(X 버튼) 눌러도 예약 취소
        // ----------------------------
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!confirmed) {  // 확인 눌렀으면 취소하지 않음
                    boolean ok = ReservationFileUtil.updateReservationStatus(
                            userId, roomNumber, date, startTime, "승인", "취소"
                    );
                    System.out.println("Cancel by window close: " + ok);
                }
            }
        });

        // ----------------------------
        // 자동 취소 타이머 (예약 시작 + 10분 뒤)
        // ----------------------------
        if (millisUntilCancel > 0) {
            cancelTimer = new javax.swing.Timer(
                    (int) Math.min(millisUntilCancel, Integer.MAX_VALUE),
                    ae -> {
                        if (!confirmed) {
                            boolean ok = ReservationFileUtil.updateReservationStatus(
                                    userId, roomNumber, date, startTime, "승인", "취소"
                            );
                            System.out.println("Auto-cancel: " + ok);
                        }
                        dispose();
                    }
            );
            cancelTimer.setRepeats(false);
            cancelTimer.start();
        }

        // ----------------------------
        // 창 표시
        // ----------------------------
        pack();
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
