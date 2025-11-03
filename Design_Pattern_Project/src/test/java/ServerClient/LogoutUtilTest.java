/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerClient;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class LogoutUtilTest {

    /** 
     * Socket을 상속해 getOutputStream()만 오버라이드한 더미 소켓 
     * close()는 noop
     */
    static class DummySocket extends Socket {
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        @Override
        public java.io.OutputStream getOutputStream() {
            return baos;
        }

        @Override
        public void close() {
            // noop
        }

        String getSentData() {
            return baos.toString();
        }
    }

    @Test
    public void testLogoutMessageSentOnWindowClosing() throws Exception {
        // 1) 준비: 더미 소켓과 BufferedWriter, 그리고 JFrame 생성
        DummySocket socket = new DummySocket();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        JFrame frame = new JFrame();
        // (윈도우 닫힘으로 종료되지 않도록 DO_NOTHING 설정)
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // 2) attach 호출: 윈도우가 닫힐 때 로그아웃 메시지 전송하도록 설정
        LogoutUtil.attach(frame, "user123", socket, out);

        // 3) 이벤트 강제 발생: 등록된 WindowListener 에게 windowClosing 호출
        WindowEvent evt = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
        for (WindowListener wl : frame.getWindowListeners()) {
            wl.windowClosing(evt);
        }

        // 4) 플러시된 데이터 읽기
        String sent = socket.getSentData();

        // 5) 검증: 정확히 "LOGOUT:user123\n" 메시지가 나갔는지 확인
        assertEquals("LOGOUT:user123\n", sent);
    }
}