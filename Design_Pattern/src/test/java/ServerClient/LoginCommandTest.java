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

public class LoginCommandTest {
    
    @Test
    public void testCommandStringFormat() throws IOException {
        System.out.println("[Unit Test] LoginCommand 기능 검증");
        
        StringWriter sw = new StringWriter();
        BufferedWriter mockWriter = new BufferedWriter(sw);
        
        // 테스트 실행
        LoginCommand cmd = new LoginCommand(mockWriter, "testUser", "1234", "Student");
        cmd.execute();
        mockWriter.flush();
        
        // 결과 확인 및 검증
        String result = sw.toString().trim();
        String expected = "LOGIN:testUser,1234,Student";
        
        Assertions.assertEquals(expected, result, "로그인 프로토콜 포맷이 일치해야 합니다.");
    }
}