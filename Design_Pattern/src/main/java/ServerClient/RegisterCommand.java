/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 * ConcreteCommand (회원가입)
 * @author adsd3
 */
import java.io.BufferedWriter;
import java.io.IOException;

public class RegisterCommand implements NetworkCommand {
    
    private final BufferedWriter writer;
    private final String msg;

    public RegisterCommand(BufferedWriter writer, String role, String id, String pw, String name, String dept) {
        this.writer = writer;
        this.msg = String.join(":", "REGISTER", role, id, pw, name, dept);
    }

    @Override
    public void execute() throws IOException {
        writer.write(msg);
        writer.newLine();
        writer.flush();
    }
}