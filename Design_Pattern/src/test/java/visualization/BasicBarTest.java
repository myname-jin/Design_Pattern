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
public class BasicBarTest {
    private JFrame frame;

    @BeforeEach
    public void setUp() {
        frame = new JFrame();
        frame.setTitle("[Unit Test] BasicBar Only");
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    @Test
    public void testBasicBarDraw() throws InterruptedException {
        // [Test Execution]
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawString("기본 막대 (회색)", 50, 50);
                
                // 단위 테스트 대상: BasicBar
                BasicBar bar = new BasicBar();
                // (Graphics, startX, startY, width, height, value, roomID)
                bar.draw(g, 100, 200, 50, 100, 5, "900호");
            }
        };
        
        frame.add(panel);
        frame.setVisible(true);
        System.out.println(">>> [BasicBar Test] 창을 확인하고 닫아주세요.");
        Thread.sleep(2000); // 시각적 검증 시간 부여
    }

    @AfterEach
    public void tearDown() {
        if (frame != null) {
            frame.dispose();
        }
    }
}