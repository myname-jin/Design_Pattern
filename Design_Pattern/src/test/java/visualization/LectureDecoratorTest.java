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
import visualization.*;

public class LectureDecoratorTest extends JFrame {
    public LectureDecoratorTest() {
        setTitle("[Unit Test] LectureDecorator Only");
        setSize(300, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawString("강의실 장식 (파랑)", 50, 50);
                
                // 단위 테스트 대상: LectureDecorator
                ChartElement lecture = new LectureDecorator(new BasicBar());
                lecture.draw(g, 100, 200, 50, 100, 15, "912호");
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) { new LectureDecoratorTest(); }
}
