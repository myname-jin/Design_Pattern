/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;

/**
 *
 * @author jms5310
 */
import UserNotification.*;
import ServerClient.LogoutUtil;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import Reservation.ReservationGUIController;
import Reservation.ReservationView;

// [추가] 알림 감시자 매니저
import management.NotificationManager; 

public class UserMainController {
    private UserMainModel model;
    private UserMainView view;

    private NotificationController notificationController;
    private NotificationButton notificationButton;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    
    // [추가] 알림 감시자 인스턴스
    private NotificationManager notiManager = new NotificationManager();

    public UserMainController(String userId, String userType, Socket socket, BufferedReader in, BufferedWriter _out) {
        this.socket = socket;
        this.in = in;
        this.out = null; 

        String userName = "알수없음";
        String userDept = "-";

        try {
            ServerClient.CommandProcessor.getInstance().addCommand(
                new ServerClient.InfoRequestCommand(userId));

            String response = in.readLine();
            if (response != null && response.startsWith("INFO_RESPONSE:")) {
                String[] parts = response.substring("INFO_RESPONSE:".length()).split(",");
                if (parts.length >= 4) {
                    userName = parts[1].trim();
                    userDept = parts[2].trim();
                }
            }
        } catch (IOException e) {
            System.out.println(" 사용자 정보 수신 실패: " + e.getMessage());
        }

        this.model = new UserMainModel(userId, userType, socket, in, out);
        this.view = new UserMainView();
        view.setWelcomeMessage(userName); 

        initializeNotificationSystem();
        initListeners();

        if (socket != null && out != null) {
            LogoutUtil.attach(view, userId);
        }
        
        // ===============================================================
        // [핵심] 알림 감시자 시작 (3초마다 파일 체크)
        // ===============================================================
        notiManager.startMonitoring(userId); 
        // ===============================================================

        view.setVisible(true);
    }

    private void initializeNotificationSystem() {
        try {
            // 1. NotificationController 생성 (싱글톤 getInstance 호출)
            notificationController = NotificationController.getInstance(
                model.getUserId(),
                model.getUserType(),
                model.getSocket(),
                model.getIn(),
                null 
            );

            // 2. NotificationButton 생성
            notificationButton = new NotificationButton(
                model.getUserId(), 
                model.getUserType(),  
                model.getSocket(), 
                model.getIn(), 
                null
            );
            
            view.setNotificationButton(notificationButton);
            
        } catch (Exception e) {
            System.err.println("알림 시스템 초기화 실패: " + e.getMessage());
        }
    }

    private void initListeners() {
        view.addViewReservationsListener(e -> openReservationList());
        view.addCreateReservationListener(e -> openReservationSystem());
        view.addNoticeListener(e -> openNoticeSystem());
        view.addLogoutListener(e -> handleLogout());
    }

    private void openReservationList() {
        view.dispose();
        // shutdownNotificationSystem(); 
        new UserReservationListController(model.getUserId(), model.getUserType(),  model.getSocket(), model.getIn(), null);
    }

    private void openReservationSystem() {
        try {
            view.dispose();
            // shutdownNotificationSystem();
            view.showMessage("강의실 예약 시스템으로 연결됩니다", "안내", JOptionPane.INFORMATION_MESSAGE);
            new ReservationGUIController(model.getUserId(), model.getUserName(), model.getUserDept(),
                                             model.getUserType(), model.getSocket(), model.getIn(), null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "예약 시스템 연결 중 오류: " + e.getMessage(),
                                          "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openNoticeSystem() {
        try {
            view.dispose();
            new UserNoticeController(model.getUserId(), model.getSocket(), model.getIn(), null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "공지사항 시스템 연결 중 오류: " + e.getMessage(),
                                          "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(view, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            // [핵심] 감시자 중단
            notiManager.stopMonitoring();
            
            try {
                ServerClient.CommandProcessor.getInstance().addCommand(
                new ServerClient.LogoutCommand(model.getUserId()) );
                
                socket.close(); 
            } catch (IOException e) {
                System.err.println("로그아웃 중 오류 발생: " + e.getMessage());
            }

            shutdownNotificationSystem();
            ServerClient.CommandProcessor.resetInstance(); 
            view.dispose(); 

            new login.ConnectView(); 
        }
    }

    private void shutdownNotificationSystem() {
        try {
             if (notificationController != null) notificationController.shutdown();
             if (notificationButton != null) notificationButton.shutdown();
        } catch (Exception e) {
            System.err.println("알림 시스템 종료 오류: " + e.getMessage());
        }
    }

    public NotificationController getNotificationController() {
        return notificationController;
    }

    public NotificationButton getNotificationButton() {
        return notificationButton;
    }
}