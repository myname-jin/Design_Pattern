/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * ConcreteCommand (회원가입)
 * @author adsd3
 */
public class RegisterCommand implements NetworkCommand {
    
    private final String msg;

    public RegisterCommand(String role, String id, String pw, String name, String dept) {
        this.msg = String.join(":", "REGISTER", role, id, pw, name, dept);
    }

    @Override
    public void execute(BufferedWriter out) throws IOException {
        out.write(msg);
        out.newLine();
        out.flush();
    }
}