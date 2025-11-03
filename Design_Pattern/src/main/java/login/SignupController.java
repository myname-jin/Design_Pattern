/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

/**
 *
 * @author msj02
 */
import ServerClient.SocketManager;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * 회원가입 화면의 Controller
 */
public class SignupController {
    private final SignupView view;
    private final SignupModel model;

    public SignupController(SignupView view, SignupModel model) {
        this.view = view;
        this.model = model;

        // “등록” 버튼 클릭 시 sendRegister() 실행
        view.btnRegister.addActionListener(this::sendRegister);

        // “뒤로가기” 버튼 클릭 시 로그인 화면으로 돌아가기
        view.btnBack.addActionListener(e -> {
            view.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginView loginView  = new LoginView();
                LoginModel loginModel = new LoginModel();
                new LoginController(loginView, loginModel);
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
        String role = view.getRole(); // 예: “USER” 또는 “ADMIN”

        // 빈칸 체크
        if (id.isEmpty() || pw.isEmpty() || name.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(view, "모든 항목을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1) SocketManager에서 저장된 소켓을 꺼내온다.
        Socket socket = SocketManager.getSocket();
        if (socket == null || socket.isClosed()) {
            // 연결된 소켓이 없거나 이미 닫힌 경우
            JOptionPane.showMessageDialog(view, "서버에 연결되어 있지 않습니다.", "에러", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 2) BufferedWriter/BufferedReader를 생성 (절대 try-with-resources로 감싸지 마세요!)
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 3) 프로토콜: “REGISTER:role:id:pw:name:dept”
            String msg = String.join(":", "REGISTER", role, id, pw, name, dept);
            out.write(msg);
            out.newLine();
            out.flush();

            // 4) 서버 응답 처리
            String response = in.readLine();
            if ("REGISTER_SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(view, "회원가입 되었습니다.", "메시지", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();

                // 회원가입이 완료되면 로그인 화면으로 돌아간다
                SwingUtilities.invokeLater(() -> {
                    LoginView loginView = new LoginView();
                    LoginModel loginModel = new LoginModel();
                    new LoginController(loginView, loginModel);
                    loginView.setLocationRelativeTo(null);
                    loginView.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(view, "회원가입에 실패하였습니다.", "에러", JOptionPane.ERROR_MESSAGE);
            }

            // **중요**: out.close()나 in.close()를 절대로 호출하면 안 됩니다.
            // 닫으면 내부적으로 소켓이 같이 닫혀 버립니다.
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "서버 통신 오류: " + ex.getMessage(), "에러", JOptionPane.ERROR_MESSAGE);
        }
       
    }
}