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
import java.io.IOException;
import java.net.Socket;

public class LogoutUtil {

    public static void attach(JFrame frame, String userId, Socket socket, BufferedWriter out) {
        if (frame == null || userId == null || socket == null || out == null) {
            return;
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    // 서버에 LOGOUT 신호는 보냄
                    out.write("LOGOUT:" + userId + "\n");
                    out.flush();

                    //  소켓은 닫지 않음 (FileWatcher 등이 계속 사용 중)

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}