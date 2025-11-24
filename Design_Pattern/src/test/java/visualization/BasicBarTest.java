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
import visualization.BasicBar;

public class BasicBarTest extends JFrame {
    public BasicBarTest() {
        setTitle("[Unit Test] BasicBar Only");
        setSize(300, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawString("기본 막대 (회색)", 50, 50);
                
                // 단위 테스트 대상: BasicBar
                BasicBar bar = new BasicBar();
                bar.draw(g, 100, 200, 50, 100, 5, "900호");
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) { new BasicBarTest(); }
}
