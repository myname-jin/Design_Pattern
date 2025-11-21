/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 * ConcreteCommand (로그아웃)
 * @author adsd3
 */
import java.io.BufferedWriter;
import java.io.IOException;

public class LogoutCommand implements NetworkCommand {
    
    private final BufferedWriter writer;
    private final String userId;

    public LogoutCommand(BufferedWriter writer, String userId) {
        this.writer = writer;
        this.userId = userId;
    }

    @Override
    public void execute() throws IOException {
        writer.write("LOGOUT:" + userId + "\n");
        writer.flush();
    }
}
