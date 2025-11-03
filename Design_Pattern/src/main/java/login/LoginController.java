/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import ServerClient.FileWatcher;
import ServerClient.SocketManager;
import ruleagreement.RuleAgreementController;
import management.ReservationMgmtView;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class LoginController {

    private final LoginView view;
    private final LoginModel model;
    private final Socket socket;
    private final BufferedWriter out;
    private final BufferedReader in;

    public LoginController(LoginView view, LoginModel model) {
        this.view = view;
        this.model = model;

        Socket tempSocket = SocketManager.getSocket();   // 수정됨: 새로 생성하지 않고 전역 소켓을 가져온다.
        BufferedWriter tempOut = null;
        BufferedReader tempIn = null;

        if (tempSocket == null || tempSocket.isClosed()) {
            // SocketManager에 저장된 소켓이 없거나 이미 닫혔으면 오류 메시지만 띄우고 리턴
            JOptionPane.showMessageDialog(view, "서버에 연결되어 있지 않습니다.", "연결 오류", JOptionPane.ERROR_MESSAGE);
            // 여기서 return 또는 예외 처리 후 생성자 종료
            this.socket = null;
            this.out = null;
            this.in = null;
            return;
        }

        try {
            // 수정됨: SocketManager에서 꺼내온 tempSocket으로 스트림 생성
            tempOut = new BufferedWriter(new OutputStreamWriter(tempSocket.getOutputStream()));
            tempIn = new BufferedReader(new InputStreamReader(tempSocket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "스트림 생성 실패: " + e.getMessage(), "스트림 오류", JOptionPane.ERROR_MESSAGE);
            // 실패 시 소켓 닫아도 되지만, 필요하다면 SocketManager.close() 호출
            SocketManager.close();
            this.socket = null;
            this.out = null;
            this.in = null;
            return;
        }

// 여기까지 오면 tempSocket, tempOut, tempIn 모두 유효
        this.socket = tempSocket;
        this.out = tempOut;
        this.in = tempIn;
        setupListeners();
    }

    public LoginController(LoginView view, LoginModel model, Socket socket) throws IOException {
        this.view = view;
        this.model = model;
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        setupListeners();
    }

    private void setupListeners() {
        view.getLoginButton().addActionListener(e -> attemptLogin());
        view.getRegisterButton().addActionListener(e -> handleSignup());
    }

    private void attemptLogin() {
        String userId = view.getUserId();
        String password = view.getPassword();
        String role = view.getRole(); // "학생", "교수", "admin"

        try {
            out.write("LOGIN:" + userId + "," + password + "," + role);
            out.newLine();
            out.flush();

            String response = in.readLine();

            if ("LOGIN_SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(view, userId + "님 로그인 성공");

                SocketManager.setSocket(socket);  // ← 이 줄을 꼭 먼저 추가

                new FileWatcher().start();

                // 🔽 서버에 유저 정보 요청
                out.write("INFO_REQUEST:" + userId + "\n");
                out.flush();

                String userInfoResponse = in.readLine();
                String name = "알수없음";
                String dept = "미지정";
                String userType = role;

                if (userInfoResponse != null && userInfoResponse.startsWith("INFO_RESPONSE:")) {
                    String[] parts = userInfoResponse.substring("INFO_RESPONSE:".length()).split(",");
                    if (parts.length >= 4) {
                        name = parts[1];
                        dept = parts[2];
                        userType = parts[3];
                    }
                }

                try {
                    if ("admin".equalsIgnoreCase(role)) {
                        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                        new ReservationMgmtView().setVisible(true);
                    } else {
                        new RuleAgreementController(userId, userType, socket, out);
                    }
                    view.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view, "화면 전환 오류: " + ex.getMessage());
                }

            } else if ("WAIT".equals(response)) {
                JOptionPane.showMessageDialog(view, "현재 접속 인원 초과로 대기 중입니다.");

                String line;
                while ((line = in.readLine()) != null) {
                    if ("LOGIN_SUCCESS".equals(line)) {
                        JOptionPane.showMessageDialog(view, userId + "님 자동 로그인 성공");

                        SocketManager.setSocket(socket);  // ← 이 줄을 꼭 먼저 추가

                        new FileWatcher().start();
                        // 서버에 정보 요청
                        out.write("INFO_REQUEST:" + userId);
                        out.newLine();
                        out.flush();
                        String userInfoResponse = in.readLine();
                        String name = "알수없음";
                        String dept = "미지정";
                        String userType = role;

                        // ✅ 여기서 EDT로 새 창 띄우고 기존 창 닫기
                        SwingUtilities.invokeLater(() -> {
                            try {
                                RuleAgreementController rac
                                        = new RuleAgreementController(userId, userType, socket, out);
                                rac.showView();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(view,
                                        "이용 동의 화면 오류: " + ex.getMessage());
                            }
                            view.dispose();
                        });
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(view, "로그인 실패");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "서버 통신 오류: " + ex.getMessage());
        }
    }

    public void handleSignup() {
        view.dispose();
        SignupView signupView = new SignupView();
        SignupModel signupModel = new SignupModel();
        new SignupController(signupView, signupModel);
        signupView.setVisible(true);
    }

}
