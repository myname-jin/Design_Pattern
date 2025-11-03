/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserNotification;

/**
 *
 * @author jms5310
 */
import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * 입실 확인을 위한 다이얼로그 UI
 */
public class CheckinDialog extends JDialog {

        private final NotificationModel.NotificationItem notification;
    private final NotificationView parentView;
    private JButton checkinButton;
    private JButton cancelButton;
    private JLabel countdownLabel;
    private Timer countdownTimer;
    private int remainingSeconds = 600; // 10분(600초)

    /**
     * 생성자
     * @param parent 부모 뷰
     * @param notification 알림 항목
     */
    public CheckinDialog(NotificationView parentView, NotificationModel.NotificationItem notification) {
        super(parentView, "입실 확인", true);
        this.parentView = parentView;
        this.notification = notification;
        
        initializeUI();
        startCountdownTimer();
    }

    /**
     * UI 초기화
     */
    private void initializeUI() {
        setSize(400, 300);
        setLocationRelativeTo(parentView);
        setResizable(false);
        
        // 메인 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // 타이틀
        JLabel titleLabel = new JLabel("입실 확인이 필요합니다");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // 예약 정보
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("예약 정보"));
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        infoPanel.add(new JLabel("강의실: " + notification.getRoomNumber()));
        infoPanel.add(new JLabel("날짜: " + notification.getReservationTime().format(dateFormatter)));
        infoPanel.add(new JLabel("시간: " + notification.getReservationTime().format(timeFormatter)));
        
        mainPanel.add(infoPanel);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // 카운트다운 타이머
        countdownLabel = new JLabel();
        countdownLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        countdownLabel.setForeground(Color.RED);
        countdownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateCountdownLabel();
        mainPanel.add(countdownLabel);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // 안내 메시지
        JLabel messageLabel = new JLabel("<html>예약 시간으로부터 10분 이내에 입실 확인을 하지 않으면<br>예약이 자동으로 취소됩니다.</html>");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(messageLabel);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        checkinButton = new JButton("입실 확인");
        cancelButton = new JButton("취소");
        
        // 입실 확인 버튼 클릭 이벤트
        checkinButton.addActionListener(e -> {
            if (countdownTimer != null) {
                countdownTimer.stop();
            }
            
            // 컨트롤러를 통해 입실 확인 처리
            NotificationController controller = parentView.getController();
            controller.checkIn(notification.getId());
            
            dispose();
        });
        
        // 취소 버튼 클릭 이벤트
        cancelButton.addActionListener(e -> {
            if (countdownTimer != null) {
                countdownTimer.stop();
            }
            dispose();
        });
        
        buttonPanel.add(checkinButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel);
        
        setContentPane(mainPanel);
    }
    
    /**
     * 카운트다운 타이머 시작
     */
    private void startCountdownTimer() {
        // 예약 시간부터 현재까지 경과 시간 계산
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.Duration elapsed = java.time.Duration.between(notification.getReservationTime(), now);
        
        // 600초(10분)에서 경과 시간 빼기
        remainingSeconds = 600 - (int)elapsed.getSeconds();
        if (remainingSeconds < 0) remainingSeconds = 0;
        
        // 타이머 시작
        countdownTimer = new Timer(1000, e -> {
            remainingSeconds--;
            updateCountdownLabel();
            
            if (remainingSeconds <= 0) {
                // 시간 초과 시 타이머 중지 및 메시지 표시
                countdownTimer.stop();
                JOptionPane.showMessageDialog(this, 
                    "입실 확인 시간이 초과되었습니다. 예약이 자동으로 취소됩니다.", 
                    "시간 초과", 
                    JOptionPane.WARNING_MESSAGE);
                dispose();
            }
        });
        countdownTimer.start();
    }
    
    /**
     * 카운트다운 레이블 업데이트
     */
    private void updateCountdownLabel() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        countdownLabel.setText(String.format("남은 시간: %02d:%02d", minutes, seconds));
    }
    
    /**
     * 다이얼로그 종료 시 타이머 정리
     */
    @Override
    public void dispose() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        super.dispose();
    }
}