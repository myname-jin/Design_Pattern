/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * ConcreteCommand (비밀번호 변경)
 * @author adsd3
 */
public class PasswordChangeCommand implements NetworkCommand {
    
    private final String userId;
    private final String oldPassword; // 1. 기존 비밀번호 필드 추가
    private final String newPassword;

    public PasswordChangeCommand(String userId, String oldPassword, String newPassword) { // 2. 생성자 수정 (인자 3개)
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    @Override
    public void execute(BufferedWriter out) throws IOException {
        // 3. 프로토콜 변경 (id,oldPw,newPw)
        out.write("PW_CHANGE:" + userId + "," + oldPassword + "," + newPassword);
        out.newLine();
        out.flush();
    }
}