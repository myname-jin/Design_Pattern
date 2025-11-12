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
import java.io.IOException;
import java.net.Socket; // 1. Socket 임포트
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * 회원가입 화면의 Controller
 */
public class SignupController {
    private final SignupView view;
    private final BufferedReader in;
    private final Socket socket; // 2. Socket 필드 추가

    /**
     * LoginController로부터 Socket과 BufferedReader(in)를 주입받는 새 생성자
     */
    public SignupController(SignupView view, Socket socket, BufferedReader in) { // 3. Socket 파라미터 추가
        this.view = view;
        this.socket = socket; // 4. Socket 필드 초기화
        this.in = in;

        view.btnRegister.addActionListener(this::sendRegister);

        view.btnBack.addActionListener(e -> {
            view.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginView loginView  = new LoginView();
                // 5. LoginController 호출 시 socket 전달
                new LoginController(loginView, socket, in); 
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
            CommandProcessor.getInstance().addCommand(
                new RegisterCommand(role, id, pw, name, dept)
            );

            String response = in.readLine();
            
            if ("REGISTER_SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(view, "회원가입 되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();

                SwingUtilities.invokeLater(() -> {
                    LoginView loginView = new LoginView();
                    // 6. LoginController 호출 시 socket 전달
                    new LoginController(loginView, socket, in); 
                    loginView.setLocationRelativeTo(null);
                    loginView.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(view, "회원가입에 실패하였습니다.", "에러", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "서버 통신 오류: " + ex.getMessage(), "에러", JOptionPane.ERROR_MESSAGE);
        }
    }
}