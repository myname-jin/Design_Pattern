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

public class UserMainController {
    private UserMainModel model;
    private UserMainView view;

    private NotificationController notificationController;
    private NotificationButton notificationButton;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public UserMainController(String userId, String userType, Socket socket, BufferedReader in, BufferedWriter _out) {
        this.socket = socket;
        this.in = in;
        this.out = _out; // [수정] 여기서 null로 초기화하던 것을 _out으로 변경

        String userName = "알수없음";
        String userDept = "-";

        // ✅ 서버로부터 사용자 이름, 학과 요청
        try {
            // [수정] InfoRequestCommand에 out 주입
            ServerClient.CommandProcessor.getInstance().addCommand(
                new ServerClient.InfoRequestCommand(out, userId));

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

        // 모델 생성 시 out 전달
        this.model = new UserMainModel(userId, userType, socket, in, out);
        this.view = new UserMainView();
        view.setWelcomeMessage(userName);

        initializeNotificationSystem();
        initListeners();

        if (socket != null && out != null) {
            // LogoutUtil 수정 필요함 (아래 참조)
            LogoutUtil.attach(view, userId, out); 
        }

        view.setVisible(true);
    }

    private void initializeNotificationSystem() {
        try {
            // NotificationController에도 out이 필요할 수 있음. 일단 기존 유지.
            notificationController = NotificationController.getInstance(
                model.getUserId(),
                model.getUserType(),
                model.getSocket(),
                model.getIn(),
                model.getOut() // null 대신 out 전달
            );
            notificationButton = new NotificationButton(
                model.getUserId(), model.getUserType(), model.getSocket(), model.getIn(), model.getOut()
            );
            view.setNotificationButton(notificationButton);
        } catch (Exception e) {
            System.err.println("알림 시스템 초기화 실패: " + e.getMessage());
            JOptionPane.showMessageDialog(view, "알림 시스템 초기화에 실패했습니다. 기본 기능은 정상 작동합니다.",
                                          "경고", JOptionPane.WARNING_MESSAGE);
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
        shutdownNotificationSystem();
        // [수정] null 대신 out 전달
        new UserReservationListController(model.getUserId(), model.getUserType(), model.getSocket(), model.getIn(), out);
    }

    private void openReservationSystem() {
        try {
            view.dispose();
            shutdownNotificationSystem();
            view.showMessage("강의실 예약 시스템으로 연결됩니다", "안내", JOptionPane.INFORMATION_MESSAGE);
            // [수정] null 대신 out 전달
            new ReservationGUIController(model.getUserId(), model.getUserName(), model.getUserDept(),
                                         model.getUserType(), model.getSocket(), model.getIn(), out);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "예약 시스템 연결 중 오류: " + e.getMessage(),
                                          "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openNoticeSystem() {
        try {
            view.dispose();
            // [수정] null 대신 out 전달
            new UserNoticeController(model.getUserId(), model.getSocket(), model.getIn(), out);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "공지사항 시스템 연결 중 오류: " + e.getMessage(),
                                          "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(view, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            // 🔽 1. 서버에 로그아웃 메시지 전송 (큐에 등록)
            try {
                ServerClient.CommandProcessor.getInstance().addCommand(
                    new ServerClient.LogoutCommand(out, model.getUserId()) 
                );
                
                // ⭐️ [핵심 수정] 메시지가 전송될 때까지 0.5초만 기다려줍니다.
                try { Thread.sleep(500); } catch (InterruptedException e) {} 

                socket.close();  // 소켓 종료
            } catch (IOException e) {
                System.err.println("로그아웃 중 오류 발생: " + e.getMessage());
            }

            // 🔽 2. 알림 시스템 정리 + 화면 전환
            shutdownNotificationSystem();
            ServerClient.CommandProcessor.resetInstance(); 
            view.dispose(); 

            // 🔁 3. 서버 재연결 화면으로 이동
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