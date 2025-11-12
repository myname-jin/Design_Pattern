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
import java.io.BufferedWriter; // (BufferedWriter는 이제 불필요)
import java.io.IOException;    // (IOException도 이제 불필요)
import java.net.Socket;        // (Socket도 이제 불필요)

public class LogoutUtil {

    /**
     * 1. [커맨드 패턴 적용]
     * 'out'과 'socket' 매개변수를 제거합니다.
     * CommandProcessor는 싱글톤이므로 매개변수가 필요 없습니다.
     */
    public static void attach(JFrame frame, String userId) {
        if (frame == null || userId == null) {
            return;
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 2. [커맨드 패턴 적용]
                //    out.write(...) 대신 CommandProcessor에 명령 추가
                CommandProcessor.getInstance().addCommand(
                    new LogoutCommand(userId)
                );
                
               
            }
        });
    }
}