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

public class LabDecoratorTest extends JFrame {
    public LabDecoratorTest() {
        setTitle("[Unit Test] LabDecorator Only");
        setSize(300, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawString("실습실 장식 (주황+아이콘)", 50, 50);
                
                // 단위 테스트 대상: LabDecorator
                ChartElement lab = new LabDecorator(new BasicBar());
                lab.draw(g, 100, 200, 50, 100, 10, "911호");
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) { new LabDecoratorTest(); }
}
