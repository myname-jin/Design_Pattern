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

public class BasicBar extends ChartElement {
    @Override
    public void draw(Graphics g, int x, int y, int width, int height, int count, String label) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, width, height);
        
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        
        g.drawString(label, x + (width/2) - 15, y + height + 15);
        g.drawString(String.valueOf(count), x + (width/2) - 5, y - 5);
    }
}