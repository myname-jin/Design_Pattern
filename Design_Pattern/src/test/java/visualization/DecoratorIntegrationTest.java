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
import java.util.Arrays;
import visualization.*;

public class DecoratorIntegrationTest extends JFrame {
    public DecoratorIntegrationTest() {
        setTitle("[Integration Test] 데코레이터 패턴 전체 검증");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(new Font("맑은 고딕", Font.BOLD, 14));
                g.drawString("통합 테스트: 조건에 따른 자동 장식 확인", 150, 50);
                
                String[] testRooms = {"911", "912", "915", "913"}; // 실습실, 강의실 섞음
                int startX = 50;

                for (String room : testRooms) {
                    // 1. 기본 생성
                    ChartElement bar = new BasicBar();
                    
                    // 2. [핵심 로직] 조건에 따른 동적 조립
                    if (Arrays.asList("911", "915", "916", "918").contains(room)) {
                        bar = new LabDecorator(bar); // 실습실
                    } else {
                        bar = new LectureDecorator(bar); // 강의실
                    }
                    
                    // 3. 그리기
                    bar.draw(g, startX, 250, 50, 150, 99, room);
                    startX += 100;
                }
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) { new DecoratorIntegrationTest(); }
}
