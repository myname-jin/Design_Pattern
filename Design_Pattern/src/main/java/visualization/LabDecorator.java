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

public class LabDecorator extends BarDecorator {
    
    public LabDecorator(ChartElement element) {
        super(element);
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height, int count, String label) {
        // 1. 부모(BasicBar)가 먼저 기본 형태를 그림
        this.wrappedElement.draw(g, x, y, width, height, count, label);
        
        // 2. 실습실 색상 (주황색) 덮어쓰기
        g.setColor(new Color(255, 165, 0)); 
        g.fillRect(x, y, width, height);
        
        // 3. 테두리 및 텍스트 복구
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(label, x + (width/2) - 15, y + height + 15);
        g.drawString(String.valueOf(count), x + (width/2) - 5, y - 5);
        
        // ★ "실습실" 텍스트 추가 (수정된 부분)
        // 한글이 깨지지 않도록 폰트 지정 ("맑은 고딕" 등)
        g.setFont(new Font("맑은 고딕", Font.BOLD, 11)); 
        
        // 위치를 x + 5 정도로 조정 (한글이라 약간 왼쪽으로)
        g.drawString("★실습실", x + 5, y + height - 10);
        
        // 다음 그리기를 위해 폰트 원상복구
        g.setFont(new Font("Dialog", Font.PLAIN, 12)); 
    }
}