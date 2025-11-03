/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MainView extends JFrame {
    private final ReservationModel model;
    private final ReservationController controller;
    private final JButton backButton;
    private final JPanel chartPanel;

    public MainView(ReservationModel model, ReservationController controller) {
        this.model = model;
        this.controller = controller;

        setTitle("강의실 예약 통계");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // 버튼 생성 및 위치
        backButton = new JButton("뒤로");
        backButton.setBounds(700, 10, 80, 30);
        add(backButton);

        // 컨트롤러에 이 View 넘겨서 버튼 연결
        controller.setView(this);

        // 차트 영역 생성
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g);
            }
        };
        chartPanel.setBounds(0, 0, 800, 500);
        chartPanel.setOpaque(false);
        chartPanel.addMouseListener(controller);
        add(chartPanel);

        setVisible(true);
    }

    private void drawChart(Graphics g) {
        Map<String, Integer> data = model.getRoomTotals();
        int startX = 80, baseY = 400, barWidth = 50, spacing = 40, maxHeight = 200;
        int max = data.values().stream().max(Integer::compareTo).orElse(1);

        int i = 0;
        for (String room : data.keySet()) {
            int count = data.get(room);
            int height = (int) (count / (double) max * maxHeight);
            int x = startX + i * (barWidth + spacing);
            int y = baseY - height;

            g.setColor(new Color(100, 150, 255));
            g.fillRect(x, y, barWidth, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, barWidth, height);
            g.drawString(room, x + 10, baseY + 15);
            g.drawString(String.valueOf(count), x + 15, y - 5);

            controller.setClickArea(i, x, room);
            i++;
        }

        g.drawString("강의실 예약 총합", 320, 80);
    }

    public JButton getBackButton() {
        return backButton;
    }
}