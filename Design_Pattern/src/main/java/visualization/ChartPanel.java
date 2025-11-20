/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

/**
 *
 * @author adsd3
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class ChartPanel extends JPanel {
    public enum Type { YEAR, MONTH, WEEK, ROOM }
    
    private String title;
    private final Type type;
    private Map<Object, Integer> data;
    private final ReservationController controller;
    private final Map<Integer, Object> clickMap = new HashMap<>();

    public ChartPanel(String title, ReservationController controller, Type type) {
        this.title = title;
        this.controller = controller;
        this.type = type;
        this.setBackground(Color.WHITE);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleChartClick(type, e.getX(), clickMap);
            }
        });
    }

    public void setData(Map<?, Integer> newData, String subTitle) {
        this.data = (Map<Object, Integer>) newData;
        this.title = subTitle;
        this.clickMap.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) {
            g.drawString("데이터가 없습니다.", 350, 250);
            return;
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        g.drawString(title, 250, 50);
        g.setFont(new Font("Dialog", Font.PLAIN, 12));

        int startX = 100, baseY = 450, barWidth = 60, spacing = 40, maxHeight = 300;
        int maxVal = data.values().stream().max(Integer::compareTo).orElse(1);

        int idx = 0;
        for (Object key : new TreeMap<>(data).keySet()) {
            int count = data.get(key);
            int height = (int) ((double)count / maxVal * maxHeight);
            int x = startX + idx * (barWidth + spacing);
            int y = baseY - height;

            clickMap.put(x, key);

            // --- 데코레이터 패턴 적용 (ROOM 타입일 때만) ---
            ChartElement bar = new BasicBar(); 

            if (type == Type.ROOM) {
                String roomName = key.toString();
                if (Arrays.asList("911", "915", "916", "918").contains(roomName)) {
                    bar = new LabDecorator(bar);     
                } else {
                    bar = new LectureDecorator(bar); 
                }
            }

            String label = key.toString();
            if (type == Type.MONTH) label += "월";
            else if (type == Type.WEEK) label += "주차";

            bar.draw(g, x, y, barWidth, height, count, label);
            
            idx++;
        }
    }
}
