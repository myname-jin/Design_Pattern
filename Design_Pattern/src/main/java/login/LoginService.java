/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

/**
 *
 * @author adsd3
 */
import ruleagreement.RuleAgreementController;
import management.ReservationMgmtView;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class LoginService {
    private final LoginModel model;
    private final LoginView view;
    private final Socket socket;
    private final BufferedWriter out;
    private final BufferedReader in;

    public LoginService(LoginModel model, LoginView view, Socket socket) throws IOException {
        this.model = model;
        this.view = view;
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void attemptLogin() {
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
                String userType = role; // ✅ 역할을 userType으로 저장

                try {
                    if ("admin".equalsIgnoreCase(role)) {
                        new ReservationMgmtView(userId).setVisible(true);
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
                while ((response = in.readLine()) != null) {
                    if ("LOGIN_SUCCESS".equals(response)) {
                        JOptionPane.showMessageDialog(view, userId + "님 자동 로그인 성공");
                        String userType = role; // ✅ 대기 후 로그인에도 동일하게 선언

                        try {
                            if ("admin".equalsIgnoreCase(role)) {
                                new ReservationMgmtView(userId).setVisible(true);
                            } else {
                                new RuleAgreementController(userId, userType, socket, out);
                            }
                            view.dispose();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(view, "화면 전환 오류: " + ex.getMessage());
                        }
                        break;
                    }
                }

            } else {
                JOptionPane.showMessageDialog(view, "로그인 실패");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "서버 연결 실패: " + ex.getMessage());
        }
    }
}