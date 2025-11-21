/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

import java.io.BufferedWriter;
import java.io.IOException;

public class LoginCommand implements NetworkCommand {
    
    private final BufferedWriter writer; // 리시버
    private final String userId;
    private final String password;
    private final String role;

    public LoginCommand(BufferedWriter writer, String userId, String password, String role) {
        this.writer = writer;
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

    @Override
    public void execute() throws IOException {
        // 내 안에 있는 writer 사용
        writer.write("LOGIN:" + userId + "," + password + "," + role);
        writer.newLine();
        writer.flush();
    }
}