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

/**
 */
public class LectureDecoratorTest {
    private JFrame frame;

    @BeforeEach
    public void setUp() {
        frame = new JFrame();
        frame.setTitle("[Unit Test] LectureDecorator Only");
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    @Test
    public void testDecoratorVisuals() throws InterruptedException {
        // [Test Execution]
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawString("강의실 장식 (파랑)", 50, 50);
                
                // 단위 테스트 대상: LectureDecorator
                ChartElement lecture = new LectureDecorator(new BasicBar());
                lecture.draw(g, 100, 200, 50, 100, 15, "912호");
            }
        };
        
        frame.add(panel);
        frame.setVisible(true);
        System.out.println(">>> [LectureDecorator Test] 파란색 채우기를 확인하세요.");
        Thread.sleep(2000); 
    }

    @AfterEach
    public void tearDown() {
        if (frame != null) {
            frame.dispose();
        }
    }
}