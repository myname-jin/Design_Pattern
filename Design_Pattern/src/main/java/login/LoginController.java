/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;


import Reservation.ReservationMonitor;
import ServerClient.*;
import ruleagreement.RuleAgreementController;
import management.ReservationMgmtView;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class LoginController {

    private final LoginView view;
    private final Socket socket; 
    private final BufferedReader in;
    
    // [수정] 리시버 (커맨드에게 넘겨주기 위해 필요)
    private final BufferedWriter writer; 

    // [수정] 생성자에서 writer를 받음
    public LoginController(LoginView view, Socket socket, BufferedReader in, BufferedWriter writer) {
        this.view = view;
        this.socket = socket;
        this.in = in;
        this.writer = writer; // 저장
        setupListeners();
    }

    private void setupListeners() {
        view.getLoginButton().addActionListener(e -> attemptLogin());
        view.getRegisterButton().addActionListener(e -> handleSignup());
        view.getFindPasswordButton().addActionListener(e -> handlePasswordChange());
    }

    private void attemptLogin() {
        String userId = view.getUserId();
        String password = view.getPassword();
        String role = view.getRole();

        try {
            // [수정] 커맨드 생성 시 writer 주입
            CommandProcessor.getInstance().addCommand(
                new LoginCommand(writer, userId, password, role)
            );

            String response = in.readLine();

            if ("LOGIN_SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(view, userId + "님 로그인 성공");

                // [수정] FileSyncClient 생성 시 writer 전달
                FileWatcher fileWatcher = new FileWatcher();
                FileSyncClient fileSyncClient = new FileSyncClient(writer); 
                fileWatcher.addObserver(fileSyncClient);
                fileWatcher.start();
                
                new ReservationMonitor(userId).start();

                // [수정] InfoRequestCommand에 writer 주입
                CommandProcessor.getInstance().addCommand(
                    new InfoRequestCommand(writer, userId)
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
                        // [수정] RuleAgreementController에 writer 전달 (null -> writer)
                        new RuleAgreementController(userId, userType, socket, writer);
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
                // 대기열 루프: 서버에서 LOGIN_SUCCESS 줄 때까지 대기
                while ((line = in.readLine()) != null) {
                    if ("LOGIN_SUCCESS".equals(line)) {
                        JOptionPane.showMessageDialog(view, userId + "님 자동 로그인 성공");

                        // [수정] 대기 후 로그인 시에도 FileSyncClient에 writer 전달
                        FileWatcher fileWatcher = new FileWatcher();
                        FileSyncClient fileSyncClient = new FileSyncClient(writer);
                        fileWatcher.addObserver(fileSyncClient);
                        fileWatcher.start();
                        
                        new ReservationMonitor(userId).start();

                        // [수정] InfoRequestCommand에 writer 주입
                        CommandProcessor.getInstance().addCommand(
                            new InfoRequestCommand(writer, userId)
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
                                // [수정] 대기 후 화면 전환 시에도 writer 전달 (null -> writer)
                                RuleAgreementController rac
                                        = new RuleAgreementController(finalUserId, finalUserType, socket, writer);
                                rac.showView();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(view,
                                        "이용 동의 화면 오류: " + ex.getMessage());
                            }
                            view.dispose();
                        });
                        break; // 로그인 성공했으므로 루프 종료
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
        // [수정] SignupController 생성 시 writer 전달
        new SignupController(signupView, socket, in, writer); 
        signupView.setVisible(true);
    }
    
    private void handlePasswordChange() {
        view.dispose();
        PasswordView pwView = new PasswordView();
        // [수정] PasswordController 생성 시 writer 전달
        new PasswordController(pwView, socket, in, writer);
        pwView.setVisible(true);
    }
}