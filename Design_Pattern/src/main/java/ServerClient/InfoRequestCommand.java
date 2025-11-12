/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * ConcreteCommand (사용자 정보 요청)
 * @author adsd3
 */
public class InfoRequestCommand implements NetworkCommand {
    
    private final String userId;

    public InfoRequestCommand(String userId) {
        this.userId = userId;
    }

    @Override
    public void execute(BufferedWriter out) throws IOException {
        out.write("INFO_REQUEST:" + userId + "\n");
        out.flush();
    }
}
