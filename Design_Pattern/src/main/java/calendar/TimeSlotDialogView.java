/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author adsd3
 */

// 시간대(1~8) 선택 3×3 그리드 UI

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TimeSlotDialogView {
    public interface Handler {
        void onSelect(String timeSlot);  // 시간대 선택
        void onCancel();                 // 취소
    }

    private JDialog dialog;
    private Handler handler;
    private final List<String> blockedSlots;

    /** 생성 시점에 이미 차단된 슬롯 리스트를 전달 */
    public TimeSlotDialogView(List<String> blockedSlots) {
        this.blockedSlots = blockedSlots;
    }

    public void setHandler(Handler h) {
        this.handler = h;
    }

    public void show() {
        dialog = new JDialog((Frame)null, "시간대 선택", true);
        dialog.setLayout(new GridLayout(3, 3));  // 3×3 그리드

        String[] slots = {
            "1 (9-10시)", "2 (10-11시)", "3 (11-12시)",
            "4 (12-13시)", "5 (13-14시)", "6 (14-15시)",
            "7 (15-16시)", "8 (16-17시)"
        };

        for (String s : slots) {
            String num = s.split(" ")[0];
            JButton btn = new JButton(s);
            if (blockedSlots.contains(num)) {
                btn.setEnabled(false);
                btn.setToolTipText("이미 차단된 시간대입니다.");
            }
            btn.addActionListener(e -> handler.onSelect(num));
            dialog.add(btn);
        }

        JButton cancelBtn = new JButton("취소");
        cancelBtn.addActionListener(e -> handler.onCancel());
        dialog.add(cancelBtn);  // 3×3 그리드의 마지막 셀에 추가

        dialog.setSize(500, 350);           // 창 크기 확대
        dialog.setLocationRelativeTo(null); // 화면 중앙
        dialog.setVisible(true);
    }

    public void close() {
        dialog.dispose();
    }
}