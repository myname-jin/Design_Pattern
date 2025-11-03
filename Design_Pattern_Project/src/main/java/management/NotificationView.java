/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 * @author suk22
 */
public class NotificationView extends JFrame {

    private JTextArea textArea;
    private JButton btnClose;

    public NotificationView(List<String> notifications) {
        setTitle("예약 대기 알림");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        textArea.setMargin(new Insets(10, 10, 10, 10)); // 여백 추가

        for (String line : notifications) {
            textArea.append(line + "\n");
        }

        btnClose = new JButton("닫기");
        btnClose.addActionListener(e -> dispose());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
        add(btnClose, BorderLayout.SOUTH);
    }
}
