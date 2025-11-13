/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import ServerClient.CommandProcessor;
import ServerClient.FileWatcher;
import ServerClient.FileSyncClient;
import ServerClient.InfoRequestCommand;
import ServerClient.LoginCommand;
import ruleagreement.RuleAgreementController;
import management.ReservationMgmtView;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class LoginController {

    private final LoginView view;
    private final Socket socket; 
    private final BufferedReader in;

    public LoginController(LoginView view, Socket socket, BufferedReader in) {
        this.view = view;
        this.socket = socket;
        this.in = in;
        setupListeners();
    }

    private void setupListeners() {
        view.getLoginButton().addActionListener(e -> attemptLogin());
        view.getRegisterButton().addActionListener(e -> handleSignup());
        
        // 1. [비밀번호 변경] 버튼 리스너 추가
        view.getFindPasswordButton().addActionListener(e -> handlePasswordChange());
    }

    private void attemptLogin() {
        // ... (기존 attemptLogin 메서드 내용은 그대로 둡니다) ...
        String userId = view.getUserId();
        String password = view.getPassword();
        String role = view.getRole();

        try {
            CommandProcessor.getInstance().addCommand(
                new LoginCommand(userId, password, role)
            );

            String response = in.readLine();

            if ("LOGIN_SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(view, userId + "님 로그인 성공");

                FileWatcher fileWatcher = new FileWatcher();
                FileSyncClient fileSyncClient = new FileSyncClient();
                fileWatcher.addObserver(fileSyncClient);
                fileWatcher.start();

                CommandProcessor.getInstance().addCommand(
                    new InfoRequestCommand(userId)
                );

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
                        new RuleAgreementController(userId, userType, socket, null);
                    }
                    view.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view, "화면 전환 오류: " + ex.getMessage());
                }

            } else if ("LOGIN_LOCKED".equals(response)) {
                JOptionPane.showMessageDialog(view, "로그인 5회 실패로 5분간 계정이 잠겼습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            
            } else if ("WAIT".equals(response)) {
                JOptionPane.showMessageDialog(view, "현재 접속 인원 초과로 대기 중입니다.");
                
                String line;
                while ((line = in.readLine()) != null) {
                    if ("LOGIN_SUCCESS".equals(line)) {
                        JOptionPane.showMessageDialog(view, userId + "님 자동 로그인 성공");

                        FileWatcher fileWatcher = new FileWatcher();
                        FileSyncClient fileSyncClient = new FileSyncClient();
                        fileWatcher.addObserver(fileSyncClient);
                        fileWatcher.start();

                        CommandProcessor.getInstance().addCommand(
                            new InfoRequestCommand(userId)
                        );
                        
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
                        
                        final String finalUserType = userType;
                        final String finalUserId = userId;
                        
                        SwingUtilities.invokeLater(() -> {
                            try {
                                RuleAgreementController rac
                                        = new RuleAgreementController(finalUserId, finalUserType, socket, null);
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
                
            } else if (response != null && response.startsWith("FAIL:")) {
                String count = response.substring("FAIL:".length());
                JOptionPane.showMessageDialog(view,
                    "로그인 실패 (" + count + "/5)\n5회 실패 시 5분간 계정이 잠깁니다.",
                    "로그인 실패", JOptionPane.ERROR_MESSAGE);
                    
            } else {
                JOptionPane.showMessageDialog(view, "로그인 실패", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "서버 통신 오류: " + ex.getMessage());
        }
    }

    public void handleSignup() {
        view.dispose();
        SignupView signupView = new SignupView();
        new SignupController(signupView, socket, in);
        signupView.setVisible(true);
    }
    
    /**
     * 2. [비밀번호 변경] 버튼 처리 메서드 (신규 추가)
     */
    private void handlePasswordChange() {
        view.dispose();
        PasswordView pwView = new PasswordView();
        new PasswordController(pwView, socket, in);
        pwView.setVisible(true);
    }
}