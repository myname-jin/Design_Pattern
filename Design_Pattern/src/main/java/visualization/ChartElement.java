/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

/**
 *
 * @author adsd3
 */
import java.awt.Graphics;

public abstract class ChartElement {
    public abstract void draw(Graphics g, int x, int y, int width, int height, int count, String label);
}