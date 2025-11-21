/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;
/**
 *
 * @author adsd3
 */
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;

public class LogoutUtil {

    public static void attach(JFrame frame, String userId, BufferedWriter writer) {
        if (frame == null || userId == null) {
            return;
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 로그아웃 명령 큐에 추가
                CommandProcessor.getInstance().addCommand(
                    new LogoutCommand(writer, userId)
                );
                
                //  [핵심 수정] 메시지가 날아갈 시간을 벌어줍니다 (0.5초)
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                
                // 이후 프레임이 닫히고 프로그램 종료됨
            }
        });
    }
}