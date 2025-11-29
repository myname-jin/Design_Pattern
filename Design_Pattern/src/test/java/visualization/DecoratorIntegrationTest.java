/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

/**
 *
 * @author adsd3
 */
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * [Integration Test] 데코레이터 패턴 전체 검증
 */
public class DecoratorIntegrationTest {
    private JFrame frame;

    @BeforeEach
    public void setUp() {
        frame = new JFrame();
        frame.setTitle("[Integration Test] 데코레이터 패턴 전체 검증");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    @Test
    public void testAutomaticDecorationLogic() throws InterruptedException {
        // [Test Execution]
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(new Font("맑은 고딕", Font.BOLD, 14));
                g.drawString("통합 테스트: 조건에 따른 자동 장식 확인", 150, 50);
                
                String[] testRooms = {"911", "912", "915", "913"}; // 실습실(Lab), 강의실(Lecture) 섞음
                int startX = 50;

                for (String room : testRooms) {
                    ChartElement bar = new BasicBar();
                    
                    // [핵심 로직] 조건에 따른 동적 조립 (Integration Logic)
                    if (Arrays.asList("911", "915", "916", "918").contains(room)) {
                        bar = new LabDecorator(bar); // 실습실 조건
                    } else {
                        bar = new LectureDecorator(bar); // 강의실 조건
                    }
                    
                    // 그리기
                    bar.draw(g, startX, 250, 50, 150, 99, room);
                    startX += 100;
                }
            }
        };
        
        frame.add(panel);
        frame.setVisible(true);
        System.out.println(">>> [Decorator Integration Test] 911, 915는 주황+아이콘, 912, 913은 파랑색인지 확인하세요.");
        Thread.sleep(3000); 
    }

    @AfterEach
    public void tearDown() {
        if (frame != null) {
            frame.dispose();
        }
    }
}