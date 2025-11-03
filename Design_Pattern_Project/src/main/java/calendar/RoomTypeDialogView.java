/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author adsd3
 */
// 강의실/실습실 선택 UI
import javax.swing.*;
import java.awt.*;

public class RoomTypeDialogView {
    public interface Handler {
        void onSelectLecture();  // 강의실 선택
        void onSelectLab();      // 실습실 선택
        void onCancel();         // 취소
    }

    private JDialog dialog;
    private Handler handler;

    public void setHandler(Handler h) {
        this.handler = h;
    }

    public void show() {
        dialog = new JDialog((Frame)null, "강의실/실습실 선택", true);
        dialog.setLayout(new GridLayout(3, 1));

        JButton lec    = new JButton("강의실");
        JButton lab    = new JButton("실습실");
        JButton cancel = new JButton("취소");

        lec.addActionListener(e -> handler.onSelectLecture());
        lab.addActionListener(e -> handler.onSelectLab());
        cancel.addActionListener(e -> handler.onCancel());

        dialog.add(lec);
        dialog.add(lab);
        dialog.add(cancel);

        dialog.setSize(400, 300);           // 창 크기 확대
        dialog.setLocationRelativeTo(null); // 화면 중앙
        dialog.setVisible(true);
    }

    public void close() {
        dialog.dispose();
    }
}