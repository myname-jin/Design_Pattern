/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

/**
 *
 * @author adsd3
 */
import java.awt.*;

public class LectureDecorator extends BarDecorator {
    public LectureDecorator(ChartElement element) {
        super(element);
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height, int count, String label) {
        super.draw(g, x, y, width, height, count, label);
        
        g.setColor(new Color(100, 149, 237)); 
        g.fillRect(x, y, width, height);
        
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(label, x + (width/2) - 15, y + height + 15);
        g.drawString(String.valueOf(count), x + (width/2) - 5, y - 5);
    }
}