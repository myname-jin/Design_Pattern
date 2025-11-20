/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public CardLayout cardLayout = new CardLayout();
    public JPanel mainPanel = new JPanel(cardLayout);
    
    public ChartPanel yearPanel;
    public ChartPanel monthPanel;
    public ChartPanel weekPanel;
    public ChartPanel roomPanel;

    public MainView(ReservationModel model, ReservationController controller) {
        setTitle("강의실 예약 통계 시스템");
        setSize(800, 600);
        
        // ★수정됨: 창을 닫아도 메인 프로그램은 유지되도록 DISPOSE로 변경
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        yearPanel = new ChartPanel("년도별 통계", controller, ChartPanel.Type.YEAR);
        monthPanel = new ChartPanel("월별 통계", controller, ChartPanel.Type.MONTH);
        weekPanel = new ChartPanel("주차별 통계", controller, ChartPanel.Type.WEEK);
        roomPanel = new ChartPanel("강의실별 통계", controller, ChartPanel.Type.ROOM);

        mainPanel.add(yearPanel, "YEAR");
        mainPanel.add(monthPanel, "MONTH");
        mainPanel.add(weekPanel, "WEEK");
        mainPanel.add(roomPanel, "ROOM");

        add(mainPanel, BorderLayout.CENTER);
        
        JButton backBtn = new JButton("처음으로 / 뒤로가기");
        backBtn.addActionListener(e -> controller.handleBack());
        add(backBtn, BorderLayout.NORTH);

        setVisible(true);
    }
    
    public void showCard(String name) {
        cardLayout.show(mainPanel, name);
    }
}