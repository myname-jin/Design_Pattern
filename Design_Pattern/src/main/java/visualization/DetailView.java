/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

import javax.swing.*;
import java.awt.*;
import java.util.*;

// 선택된 강의실의 요일별 예약 통계를 별도의 창에서 그래프로 시각화하는 상세 뷰.

public class DetailView extends JFrame {
    public DetailView(String room, Map<String, Integer> dayData) {
        setTitle("강의실 " + room + " 요일별 예약 통계");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        add(new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int startX = 80, baseY = 300, barWidth = 50, spacing = 40, maxHeight = 150;
                int max = dayData.values().stream().max(Integer::compareTo).orElse(1);

                g.drawString("강의실 " + room + " 요일별 예약 수", 200, 60);

                int i = 0;
                for (String day : Arrays.asList("월", "화", "수", "목", "금")) {
                    int count = dayData.getOrDefault(day, 0);
                    int height = (int) (count / (double) max * maxHeight);
                    int x = startX + i * (barWidth + spacing);
                    int y = baseY - height;

                    g.setColor(new Color(255, 180, 100));
                    g.fillRect(x, y, barWidth, height);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, barWidth, height);
                    g.drawString(day, x + 15, baseY + 15);
                    g.drawString(String.valueOf(count), x + 15, y - 5);
                    i++;
                }
            }
        });
    }
}

