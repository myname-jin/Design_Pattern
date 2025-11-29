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

public class PasswordChangeCommandTest {
    
    @Test
    public void testCommandStringFormat() throws IOException {
        System.out.println("[Unit Test] PasswordChangeCommand 기능 검증");
        
        StringWriter sw = new StringWriter();
        BufferedWriter mockWriter = new BufferedWriter(sw);
        
        PasswordChangeCommand cmd = new PasswordChangeCommand(mockWriter, "user1", "old", "new");
        cmd.execute();
        mockWriter.flush();
        
        String result = sw.toString().trim();
        String expected = "PW_CHANGE:user1,old,new";
        
        Assertions.assertEquals(expected, result, "비밀번호 변경 프로토콜 포맷이 일치해야 합니다.");
    }
}