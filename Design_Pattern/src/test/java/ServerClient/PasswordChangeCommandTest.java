/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */
import ServerClient.PasswordChangeCommand;
import java.io.*;

public class PasswordChangeCommandTest {
    public static void main(String[] args) throws IOException {
        System.out.println("[Unit Test] PasswordChangeCommand 기능 검증");
        
        StringWriter sw = new StringWriter();
        BufferedWriter mockWriter = new BufferedWriter(sw);
        
        PasswordChangeCommand cmd = new PasswordChangeCommand(mockWriter, "user1", "old", "new");
        cmd.execute();
        mockWriter.flush();
        
        String result = sw.toString().trim();
        String expected = "PW_CHANGE:user1,old,new";
        
        if (expected.equals(result)) System.out.println(" Pass");
        else System.err.println(" Fail: " + result);
    }
}
