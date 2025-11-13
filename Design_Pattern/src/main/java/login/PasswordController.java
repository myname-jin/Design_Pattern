/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;


import ServerClient.CommandProcessor;
import ServerClient.PasswordChangeCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * PasswordView를 제어하는 Controller
 * 커맨드 패턴의 'Client' 역할을 합니다.
 * @author adsd3
 */
public class PasswordController {
    
    private final PasswordView view;
    private final Socket socket;
    private final BufferedReader in;

    public PasswordController(PasswordView view, Socket socket, BufferedReader in) {
        this.view = view;
        this.socket = socket;
        this.in = in;
        
        this.view.getChangeButton().addActionListener(e -> handleChangePassword());
        this.view.getCancelButton().addActionListener(e -> handleCancel());
    }

    private void handleChangePassword() {
        String userId = view.getUserId();
        String oldPw = view.getOldPassword(); // 1. 기존 비밀번호 가져오기
        String newPw = view.getNewPassword();
        String confirmPw = view.getConfirmPassword();

        if (userId.isEmpty() || oldPw.isEmpty() || newPw.isEmpty()) { // 2. oldPw도 비었는지 검사
            JOptionPane.showMessageDialog(view, "모든 항목을 입력하세요.");
            return;
        }
        if (!newPw.equals(confirmPw)) {
            JOptionPane.showMessageDialog(view, "새 비밀번호가 일치하지 않습니다.");
            return;
        }

        try {
            // 3. 커맨드에 oldPw 전달
            CommandProcessor.getInstance().addCommand(
                new PasswordChangeCommand(userId, oldPw, newPw)
            );
            
            // 응답 읽기
            String response = in.readLine();
            
            if ("PW_CHANGE_SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(view, "비밀번호가 성공적으로 변경되었습니다. 로그인 화면으로 돌아갑니다.");
                handleCancel(); // 로그인 화면으로
            } else if ("PW_CHANGE_FAIL:NO_ID".equals(response)) {
                JOptionPane.showMessageDialog(view, "해당 ID를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            } else if ("PW_CHANGE_FAIL:WRONG_OLD_PW".equals(response)) {
                // 4. 기존 비밀번호 오류 처리
                JOptionPane.showMessageDialog(view, "기존 비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "비밀번호 변경에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "서버 통신 오류: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancel() {
        view.dispose();
        // 로그인 화면으로 돌아감
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView, socket, in);
            loginView.setLocationRelativeTo(null);
            loginView.setVisible(true);
        });
    }
}