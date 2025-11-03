/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;

/**
 *
 * @author jms5310
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import UserNotification.NotificationButton;

public class UserMainView extends JFrame {
    private JLabel welcomeLabel;
    private JLabel dateLabel;
    private JButton viewReservationsButton;
    private JButton createReservationButton;
    private JButton noticeButton;
    private JButton logoutButton;
    private NotificationButton notificationButton;

    public UserMainView() {
        // 기본 프레임 설정
        setTitle("강의실 예약 시스템 - 사용자 메뉴");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true); // 이 줄이 꼭 있어야 창이 뜹니다!

        // UI 초기화
        initUI();
    }
    
    private void initUI() {
        // 상단 패널 - 환영 메시지
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // 중앙 패널 - 기능 버튼들
        JPanel centerPanel = createButtonPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // 하단 패널 - 로그아웃 버튼
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 메인 환영 메시지
        welcomeLabel = new JLabel("환영합니다", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);
        
        // 현재 시간 정보 
        dateLabel = new JLabel(new java.util.Date().toString(), SwingConstants.RIGHT);
        dateLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        panel.add(dateLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // 예약 조회 버튼
        viewReservationsButton = createFunctionButton("내 예약 조회/취소", "예약 목록을 확인하고 취소할 수 있습니다.");
        panel.add(viewReservationsButton);
        
        // 강의실 예약 버튼
        createReservationButton = createFunctionButton("강의실 예약하기", "새로운 강의실을 예약합니다.");
        panel.add(createReservationButton);
        
        // 공지사항 버튼
        noticeButton = createFunctionButton("공지사항 확인하기", "관리자가 등록한 공지사항을 확인합니다.");
        panel.add(noticeButton);
        
        return panel;
    }
    
    private JButton createFunctionButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        button.setBackground(new Color(240, 240, 240));
        button.setPreferredSize(new Dimension(200, 60));
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
         JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // notificationButton은 나중에 setNotificationButton()으로 설정
        leftPanel.add(new JLabel("알림: ")); // 플레이스홀더
        
       JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButton = new JButton("로그아웃");
        logoutButton.setFocusPainted(false);
        rightPanel.add(logoutButton);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
     public void setNotificationButton(NotificationButton button) {
        this.notificationButton = button;
        
        // 기존 플레이스홀더 제거 후 실제 버튼 추가
        JPanel footerPanel = (JPanel) getContentPane().getComponent(2); // 하단 패널
        JPanel leftPanel = (JPanel) footerPanel.getComponent(0); // 왼쪽 패널
        
        leftPanel.removeAll(); // 기존 내용 제거
        leftPanel.add(new JLabel("알림: "));
        leftPanel.add(notificationButton);
        
        // 화면 갱신
        leftPanel.revalidate();
        leftPanel.repaint();
    }
    
    // 사용자 정보 설정
    public void setWelcomeMessage(String userId) {
        welcomeLabel.setText("환영합니다, " + userId + "님");
    }
    
    // 이벤트 리스너 등록 메서드
    public void addViewReservationsListener(ActionListener listener) {
        viewReservationsButton.addActionListener(listener);
    }
    
    public void addCreateReservationListener(ActionListener listener) {
        createReservationButton.addActionListener(listener);
    }
    
    public void addNoticeListener(ActionListener listener) {
        noticeButton.addActionListener(listener);
    }
    
    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
    
    // 메시지 표시
    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    //알림버튼
      public NotificationButton getNotificationButton() {
        return notificationButton;
    }
}