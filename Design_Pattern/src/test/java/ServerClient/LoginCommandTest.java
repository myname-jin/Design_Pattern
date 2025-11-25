/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */
import ServerClient.LoginCommand;
import java.io.*;

public class LoginCommandTest {
    public static void main(String[] args) throws IOException {
        System.out.println("[Unit Test] LoginCommand 기능 검증");
        
        StringWriter sw = new StringWriter();
        BufferedWriter mockWriter = new BufferedWriter(sw);
        
        // 테스트 실행
        LoginCommand cmd = new LoginCommand(mockWriter, "testUser", "1234", "Student");
        cmd.execute();
        mockWriter.flush();
        
        // 결과 확인
        String result = sw.toString().trim();
        String expected = "LOGIN:testUser,1234,Student";
        
        if (expected.equals(result)) System.out.println(" Pass");
        else System.err.println(" Fail: " + result);
    }
}
