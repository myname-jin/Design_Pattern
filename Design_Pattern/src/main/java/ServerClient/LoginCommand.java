/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * ConcreteCommand (로그인)
 * @author adsd3
 */
public class LoginCommand implements NetworkCommand {
    
    private final String userId;
    private final String password;
    private final String role;

    public LoginCommand(String userId, String password, String role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

    @Override
    public void execute(BufferedWriter out) throws IOException {
        out.write("LOGIN:" + userId + "," + password + "," + role);
        out.newLine();
        out.flush();
    }
}
