/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 * ConcreteCommand (비밀번호 변경)
 * @author adsd3
 */
import java.io.BufferedWriter;
import java.io.IOException;

public class PasswordChangeCommand implements NetworkCommand {
    
    private final BufferedWriter writer;
    private final String userId;
    private final String oldPassword;
    private final String newPassword;

    public PasswordChangeCommand(BufferedWriter writer, String userId, String oldPassword, String newPassword) {
        this.writer = writer;
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    @Override
    public void execute() throws IOException {
        writer.write("PW_CHANGE:" + userId + "," + oldPassword + "," + newPassword);
        writer.newLine();
        writer.flush();
    }
}