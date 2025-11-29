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

public class RegisterCommandTest {
    
    @Test
    public void testCommandStringFormat() throws IOException {
        System.out.println("[Unit Test] RegisterCommand 기능 검증");
        
        StringWriter sw = new StringWriter();
        BufferedWriter mockWriter = new BufferedWriter(sw);
        
        RegisterCommand cmd = new RegisterCommand(mockWriter, "Student", "newUser", "pass", "Kim", "CS");
        cmd.execute();
        mockWriter.flush();
        
        String result = sw.toString().trim();
        String expected = "REGISTER:Student:newUser:pass:Kim:CS";
        
        Assertions.assertEquals(expected, result, "회원가입 프로토콜 포맷이 일치해야 합니다.");
    }
}