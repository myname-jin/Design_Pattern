/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

/**
 *
 * @author adsd3
 */
import ServerClient.CommandProcessor;
import ServerClient.RegisterCommand;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter; // 추가
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SignupController {
    private final SignupView view;
    private final BufferedReader in;
    private final Socket socket;
    private final BufferedWriter writer; // [추가]

    // [수정] 생성자 파라미터에 writer 추가
    public SignupController(SignupView view, Socket socket, BufferedReader in, BufferedWriter writer) {
        this.view = view;
        this.socket = socket;
        this.in = in;
        this.writer = writer; // 저장

        view.btnRegister.addActionListener(this::sendRegister);

        view.btnBack.addActionListener(e -> {
            view.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginView loginView  = new LoginView();
                // [수정] 뒤로 갈 때도 writer 전달
                new LoginController(loginView, socket, in, writer); 
                loginView.setLocationRelativeTo(null);
                loginView.setVisible(true);
            });
        });
    }

    private void sendRegister(ActionEvent e) {
        String id   = view.getId();
        String pw   = view.getPw();
        String name = view.getName();
        String dept = view.getDept();
        String role = view.getRole();

        if (id.isEmpty() || pw.isEmpty() || name.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(view, "모든 항목을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // [수정] RegisterCommand에 writer 주입
            CommandProcessor.getInstance().addCommand(
                new RegisterCommand(writer, role, id, pw, name, dept)
            );

            String response = in.readLine();
            
            if ("REGISTER_SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(view, "회원가입 되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();

                SwingUtilities.invokeLater(() -> {
                    LoginView loginView = new LoginView();
                    // [수정] writer 전달
                    new LoginController(loginView, socket, in, writer); 
                    loginView.setLocationRelativeTo(null);
                    loginView.setVisible(true);
                });
            } else if ("REGISTER_FAIL:DUPLICATE_ID".equals(response)) {
                JOptionPane.showMessageDialog(view, "이미 사용 중인 ID입니다.", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
                
            } else {
                JOptionPane.showMessageDialog(view, "회원가입에 실패하였습니다.", "에러", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "서버 통신 오류: " + ex.getMessage(), "에러", JOptionPane.ERROR_MESSAGE);
        }
    }
}