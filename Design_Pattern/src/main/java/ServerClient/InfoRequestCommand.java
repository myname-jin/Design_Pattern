/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 * ConcreteCommand (사용자 정보 요청)
 * @author adsd3
 */
import java.io.BufferedWriter;
import java.io.IOException;

public class InfoRequestCommand implements NetworkCommand {
    
    private final BufferedWriter writer;
    private final String userId;

    public InfoRequestCommand(BufferedWriter writer, String userId) {
        this.writer = writer;
        this.userId = userId;
    }

    @Override
    public void execute() throws IOException {
        writer.write("INFO_REQUEST:" + userId + "\n");
        writer.flush();
    }
}