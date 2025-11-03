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

    public UserMainController(String userId, String userType, Socket socket, BufferedReader in, BufferedWriter out) {
    this.socket = socket;
    this.in = in;
    this.out = out;

    String userName = "알수없음";
    String userDept = "-";

    // ✅ 서버로부터 사용자 이름, 학과 요청
    try {
        out.write("INFO_REQUEST:" + userId + "\n");
        out.flush();

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
    view.setWelcomeMessage(userName); //  서버에서 받은 이름 사용

    initializeNotificationSystem();
    initListeners();

    if (socket != null && out != null) {
        LogoutUtil.attach(view, userId, socket, out);
    }

    view.setVisible(true);
}

    private void initializeNotificationSystem() {
        try {
            notificationController = NotificationController.getInstance(
    model.getUserId(),
    model.getUserType(),  //  여기 추가
    model.getSocket(),
    model.getIn(),
    model.getOut()
);
            notificationButton = new NotificationButton(
                model.getUserId(), model.getUserType(),  model.getSocket(), model.getIn(), model.getOut()
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
        new UserReservationListController(model.getUserId(), model.getUserType(),  model.getSocket(), model.getIn(), model.getOut());
    }

    private void openReservationSystem() {
        try {
            view.dispose();
            shutdownNotificationSystem();
            view.showMessage("강의실 예약 시스템으로 연결됩니다", "안내", JOptionPane.INFORMATION_MESSAGE);
            new ReservationGUIController(model.getUserId(), model.getUserName(), model.getUserDept(),
                                         model.getUserType(), model.getSocket(), model.getIn(), model.getOut());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "예약 시스템 연결 중 오류: " + e.getMessage(),
                                          "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openNoticeSystem() {
        try {
            view.dispose();
            new UserNoticeController(model.getUserId(), model.getSocket(), model.getIn(), model.getOut());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "공지사항 시스템 연결 중 오류: " + e.getMessage(),
                                          "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(view, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);

    if (result == JOptionPane.YES_OPTION) {
        // 🔽 1. 서버에 로그아웃 메시지 전송
        try {
            out.write("LOGOUT\n");
            out.flush();
            socket.close();  // 소켓 종료
        } catch (IOException e) {
            System.err.println("로그아웃 중 오류 발생: " + e.getMessage());
        }

        // 🔽 2. 알림 시스템 정리 + 화면 전환
        shutdownNotificationSystem();
        view.dispose(); // 현재 화면 닫기

        // 🔁 3. 서버 재연결 화면(ConnectView)으로 이동 → 새 소켓 생성됨
        new login.ConnectView();  // ← 여기에 IP 입력 화면 있음
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
