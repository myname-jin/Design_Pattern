/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package ServerClient;

import ServerClient.CommandProcessor;
import ServerClient.LogoutUtil;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

public class LogoutUtilTest {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("[Integration Test] LogoutUtil (Window Closing Event) 검증");

        // 1. 가짜 리시버 준비
        StringWriter sw = new StringWriter();
        BufferedWriter mockWriter = new BufferedWriter(sw);

        // 2. 가짜 윈도우(JFrame) 생성 (화면에는 안 보이게 설정)
        JFrame dummyFrame = new JFrame();
        String userId = "TestUser";

        // 3. [핵심] LogoutUtil 연결 (리스너 부착)
        LogoutUtil.attach(dummyFrame, userId, mockWriter);

        // 4. [이벤트 시뮬레이션] 강제로 '창 닫기' 이벤트를 발생시킴!
        System.out.println(" 시스템 이벤트 발생: WINDOW_CLOSING 강제 호출");
        dummyFrame.dispatchEvent(new WindowEvent(dummyFrame, WindowEvent.WINDOW_CLOSING));

        // 5. 비동기 대기
        // LogoutUtil 내부에는 0.5초 대기 로직이 있고,
        // Invoker가 스레드로 동작하므로 넉넉히 1.5초 기다려줍니다.
        Thread.sleep(1500);

        // 6. 검증 (리시버에 LOGOUT 메시지가 도착했는지 확인)
        String result = sw.toString().trim();
        String expected = "LOGOUT:TestUser";

        System.out.println("--------------------------------------------------");
        System.out.println("결과 확인: " + result);
        System.out.println("--------------------------------------------------");

        if (result.contains(expected)) {
            System.out.println(" 테스트 성공: 윈도우 종료 시 로그아웃 커맨드가 자동 실행되었습니다.");
        } else {
            System.err.println(" 테스트 실패: 로그아웃 메시지가 전송되지 않았습니다.");
        }
        
        // 테스트용 프레임 메모리 해제
        dummyFrame.dispose();
        System.exit(0); // 스레드 강제 종료
    }
}