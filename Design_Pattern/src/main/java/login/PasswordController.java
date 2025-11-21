/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;


import ServerClient.CommandProcessor;
import ServerClient.PasswordChangeCommand;
import java.io.BufferedReader;
import java.io.BufferedWriter; // 추가
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class PasswordController {
    
    private final PasswordView view;
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter writer; // [추가]

    // [수정] 생성자 파라미터 writer 추가
    public PasswordController(PasswordView view, Socket socket, BufferedReader in, BufferedWriter writer) {
        this.view = view;
        this.socket = socket;
        this.in = in;
        this.writer = writer; // 저장
        
        this.view.getChangeButton().addActionListener(e -> handleChangePassword());
        this.view.getCancelButton().addActionListener(e -> handleCancel());
    }

    private void handleChangePassword() {
        String userId = view.getUserId();
        String oldPw = view.getOldPassword();
        String newPw = view.getNewPassword();
        String confirmPw = view.getConfirmPassword();

        if (userId.isEmpty() || oldPw.isEmpty() || newPw.isEmpty()) {
            JOptionPane.showMessageDialog(view, "모든 항목을 입력하세요.");
            return;
        }
        if (!newPw.equals(confirmPw)) {
            JOptionPane.showMessageDialog(view, "새 비밀번호가 일치하지 않습니다.");
            return;
        }

        try {
            // [수정] 커맨드 생성 시 writer 주입
            CommandProcessor.getInstance().addCommand(
                new PasswordChangeCommand(writer, userId, oldPw, newPw)
            );
            
            // 응답 읽기
            String response = in.readLine();
            
            if ("PW_CHANGE_SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(view, "비밀번호가 성공적으로 변경되었습니다. 로그인 화면으로 돌아갑니다.");
                handleCancel();
            } else if ("PW_CHANGE_FAIL:NO_ID".equals(response)) {
                JOptionPane.showMessageDialog(view, "해당 ID를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            } else if ("PW_CHANGE_FAIL:WRONG_OLD_PW".equals(response)) {
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
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            // [수정] 뒤로 갈 때 writer 전달
            new LoginController(loginView, socket, in, writer);
            loginView.setLocationRelativeTo(null);
            loginView.setVisible(true);
        });
    }
}