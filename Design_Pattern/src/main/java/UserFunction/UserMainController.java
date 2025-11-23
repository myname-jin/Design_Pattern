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
import Reservation.ReservationCheckView;
import java.awt.event.ActionEvent; // [추가] 이벤트 처리를 위해 필요
import java.awt.event.ActionListener; // [추가] 이벤트 처리를 위해 필요

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

        // [충돌 해결 1] writer(_out)를 저장해야 커맨드 패턴이 작동함
        this.out = _out;

        String userName = "알수없음";
        String userDept = "-";

        // ✅ 서버로부터 사용자 이름, 학과 요청
        try {
            // [충돌 해결 2] InfoRequestCommand에 out 주입
            ServerClient.CommandProcessor.getInstance().addCommand(
                    new ServerClient.InfoRequestCommand(out, userId)
            );

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
            // [충돌 해결 3] LogoutUtil에 out 전달
            LogoutUtil.attach(view, userId, out);
        }

        // [충돌 해결 4] 알림 감시자 시작 코드는 살려둠
        // ===============================================================
        // [핵심] 알림 감시자 시작 (3초마다 파일 체크)
        // ===============================================================
        notiManager.startMonitoring(userId);
        // ===============================================================

        view.setVisible(true);
    }

    private void initializeNotificationSystem() {
        try {
            // [충돌 해결 5] NotificationController에도 out 전달 (일관성 유지)
            notificationController = NotificationController.getInstance(
                    model.getUserId(),
                    model.getUserType(),
                    model.getSocket(),
                    model.getIn(),
                    model.getOut()
            );

            notificationButton = new NotificationButton(
                    model.getUserId(),
                    model.getUserType(),
                    model.getSocket(),
                    model.getIn(),
                    model.getOut()
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
        view.addReservationCheckListener(e -> openReservationCheckSystem());
    }

    private void openReservationCheckSystem() {
        view.setVisible(false);

        ReservationCheckView checkview = new ReservationCheckView();
        new ReservationCheckController(checkview);
        checkview.setVisible(true);

        checkview.addGoBackListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkview.dispose(); // 현황 조회 창 닫기
                view.setVisible(true); // 메인 화면 다시 보이기
            }
        });
    }

    private void openReservationList() {
        view.dispose();
        shutdownNotificationSystem();
        // [충돌 해결 6] null 대신 out 전달
        new UserReservationListController(model.getUserId(), model.getUserType(), model.getSocket(), model.getIn(), out);
    }

    private void openReservationSystem() {
        try {
            view.dispose();
            shutdownNotificationSystem();
            view.showMessage("강의실 예약 시스템으로 연결됩니다", "안내", JOptionPane.INFORMATION_MESSAGE);
            // [충돌 해결 7] null 대신 out 전달
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
            // [충돌 해결 8] null 대신 out 전달
            new UserNoticeController(model.getUserId(), model.getSocket(), model.getIn(), out);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "공지사항 시스템 연결 중 오류: " + e.getMessage(),
                    "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(view, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            // [충돌 해결 9] 알림 감시자 중단 (ad4a9... 변경 사항)
            notiManager.stopMonitoring();

            // 🔽 1. 서버에 로그아웃 메시지 전송 (HEAD 변경 사항: writer 주입)
            try {
                ServerClient.CommandProcessor.getInstance().addCommand(
                        new ServerClient.LogoutCommand(out, model.getUserId())
                );

                // ⭐️ [핵심 수정] 메시지가 전송될 때까지 0.5초만 기다려줍니다.
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }

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
            if (notificationController != null) {
                notificationController.shutdown();
            }
            if (notificationButton != null) {
                notificationButton.shutdown();
            }
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
