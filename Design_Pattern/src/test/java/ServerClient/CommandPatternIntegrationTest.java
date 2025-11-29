/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.*;

public class CommandPatternIntegrationTest {
    
    @Test
    public void testFullPipelineFlow() throws InterruptedException {
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

        // 순서대로 데이터가 모두 포함되어 있는지 확인
        Assertions.assertTrue(result.contains("LOGIN:userA") && 
            result.contains("INFO_REQUEST:userA") && 
            result.contains("LOGOUT:userA"), "모든 명령이 리시버에 도달하고 순서가 유지되어야 합니다.");
    }
}