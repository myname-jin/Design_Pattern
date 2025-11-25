/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */
import ServerClient.RegisterCommand;
import java.io.*;

public class RegisterCommandTest {
    public static void main(String[] args) throws IOException {
        System.out.println("[Unit Test] RegisterCommand 기능 검증");
        
        StringWriter sw = new StringWriter();
        BufferedWriter mockWriter = new BufferedWriter(sw);
        
        RegisterCommand cmd = new RegisterCommand(mockWriter, "Student", "newUser", "pass", "Kim", "CS");
        cmd.execute();
        mockWriter.flush();
        
        String result = sw.toString().trim();
        String expected = "REGISTER:Student:newUser:pass:Kim:CS";
        
        if (expected.equals(result)) System.out.println("✅ Pass");
        else System.err.println(" Fail: " + result);
    }
}