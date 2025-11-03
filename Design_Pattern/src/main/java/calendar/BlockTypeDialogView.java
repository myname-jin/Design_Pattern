/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author adsd3
 */
// 전체/일부/해제/취소 블록 타입 선택 UI

import javax.swing.*;
import java.awt.*;

public class BlockTypeDialogView {
    public interface Handler {
        void onBlockFull();      // 모두 차단
        void onBlockPartial();   // 일부 차단
        void onUnblock();        // 차단 취소
        void onCancel();         // 취소
    }

    private JDialog dialog;
    private Handler handler;
    private boolean disablePartial = false;

    /** 일부 차단 버튼 비활성화 여부 설정 */
    public void setDisablePartialBlock(boolean disable) {
        this.disablePartial = disable;
    }

    public void setHandler(Handler h) {
        this.handler = h;
    }

    public void show() {
        dialog = new JDialog((Frame)null, "예약 차단 선택", true);
        dialog.setLayout(new GridLayout(4, 1));

        JButton fullBtn    = new JButton("모두 차단");
        JButton partialBtn = new JButton("일부 차단");
        JButton unblockBtn = new JButton("차단 취소");
        JButton cancelBtn  = new JButton("취소");

        // 전체 차단 상태면 일부 차단 비활성화
        partialBtn.setEnabled(!disablePartial);
        if (disablePartial) {
            partialBtn.setToolTipText("이미 전체 차단된 날짜입니다.");
        }

        fullBtn.addActionListener(e -> handler.onBlockFull());
        partialBtn.addActionListener(e -> handler.onBlockPartial());
        unblockBtn.addActionListener(e -> handler.onUnblock());
        cancelBtn.addActionListener(e -> handler.onCancel());

        dialog.add(fullBtn);
        dialog.add(partialBtn);
        dialog.add(unblockBtn);
        dialog.add(cancelBtn);

        dialog.setSize(400, 300);           // 창 크기 확대
        dialog.setLocationRelativeTo(null); // 화면 중앙
        dialog.setVisible(true);
    }

    public void close() {
        dialog.dispose();
    }
}