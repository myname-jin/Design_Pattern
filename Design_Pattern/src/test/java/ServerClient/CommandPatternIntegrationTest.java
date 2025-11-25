/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */
import ServerClient.*;
import java.io.*;

public class CommandPatternIntegrationTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 커맨드 패턴 통합 테스트 (Integration Test) ===");

        StringWriter sw = new StringWriter();
        BufferedWriter mockWriter = new BufferedWriter(sw);
        CommandProcessor processor = CommandProcessor.getInstance();

        // Client Action
        System.out.println("Step 1: 클라이언트 명령 전달");
        processor.addCommand(new LoginCommand(mockWriter, "userA", "1234", "Student"));
        processor.addCommand(new InfoRequestCommand(mockWriter, "userA"));
        processor.addCommand(new LogoutCommand(mockWriter, "userA"));

        // Wait
        Thread.sleep(1000);

        // Verify
        System.out.println("Step 2: 리시버 데이터 확인");
        String result = sw.toString();
        System.out.println(result);

        if (result.contains("LOGIN:userA") && 
            result.contains("INFO_REQUEST:userA") && 
            result.contains("LOGOUT:userA")) {
            System.out.println(" 통합 테스트 성공");
        } else {
            System.err.println(" 통합 테스트 실패");
        }
    }
}
