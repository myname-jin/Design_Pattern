/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author adsd3
 */
// 호수(911~918) 선택 3×3 그리드 UI
import javax.swing.*;
import java.awt.*;

public class RoomNumberDialogView {

    public interface Handler {
        void onSelect(String roomNumber);
        void onCancel();
    }

    private JDialog dialog;
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void show(String roomType) {
        dialog = new JDialog((Frame) null, "호수 선택", true);
        JPanel panel = new JPanel();

        int[] rooms;
        int rows, cols;

        if ("강의실".equals(roomType)) {
            rooms = new int[]{912, 913, 914};
            rows = 2;
            cols = 2;
        } else if ("실습실".equals(roomType)) {
            rooms = new int[]{911, 915, 916, 918};
            rows = 2;
            cols = 3;
        } else {
            rooms = new int[]{911, 912, 913, 914, 915, 916, 917, 918};
            rows = 3;
            cols = 3;
        }

        panel.setLayout(new GridLayout(rows, cols, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int room : rooms) {
            String num = String.valueOf(room);
            JButton btn = new JButton(num);
            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 16f));
            btn.addActionListener(e -> {
                if (handler != null) handler.onSelect(num);
                dialog.dispose();
            });
            panel.add(btn);
        }

        // 빈 셀 채우기 (버튼 수가 부족할 경우)
        int totalCells = rows * cols;
        if (rooms.length < totalCells - 1) {
            for (int i = 0; i < (totalCells - 1 - rooms.length); i++) {
                panel.add(new JLabel(""));
            }
        }

        // 마지막에 취소 버튼 추가
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setFont(cancelBtn.getFont().deriveFont(Font.PLAIN, 14f));
        cancelBtn.addActionListener(e -> {
            if (handler != null) handler.onCancel();
            dialog.dispose();
        });
        panel.add(cancelBtn);

        dialog.setContentPane(panel);
        dialog.setSize(420, 280);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public void close() {
        if (dialog != null) {
            dialog.dispose();
        }
    }
}