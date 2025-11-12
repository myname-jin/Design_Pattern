/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * ConcreteCommand (로그아웃)
 * @author adsd3
 */
public class LogoutCommand implements NetworkCommand {
    
    private final String userId;

    public LogoutCommand(String userId) {
        this.userId = userId;
    }

    @Override
    public void execute(BufferedWriter out) throws IOException {
        // LogoutUtil에서 가져온 프로토콜
        out.write("LOGOUT:" + userId + "\n");
        out.flush();
    }
}
