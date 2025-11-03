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
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.net.Socket;

/**
 * 메인 UI에 추가할 알림 버튼 컴포넌트 (싱글톤 패턴 적용)
 */
public class NotificationButton extends JButton {
    
    private NotificationController controller;
    private Timer blinkTimer;
    private boolean isBlinking = false;
    private Color originalBackground;
    private Color blinkColor = new Color(255, 100, 100);
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String userId;
    private String userType; 


    /**
     * 생성자 (싱글톤 패턴 사용)
     * @param userId 사용자 ID
     */
    public NotificationButton(String userId, String userType, Socket socket, BufferedReader in, BufferedWriter out)
 {
        this.userId = userId;
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.userType = userType;

        setText("알림");
        setFocusPainted(false);
        originalBackground = getBackground();
        
        // ✅ 싱글톤 인스턴스 사용
        try {
        controller = NotificationController.getInstance(userId, userType, socket, in, out);

        } catch (Exception e) {
            System.err.println("❌ NotificationButton: 컨트롤러 초기화 실패 - " + e.getMessage());
            e.printStackTrace();
            controller = null;
        }
        
        // 클릭 이벤트
        addActionListener(e -> {
            stopBlinking();
            if (controller != null) {
                // ✅ 메인화면 닫기 추가
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (parentFrame != null) {
                    parentFrame.dispose();
                }
                
                controller.showNotificationView();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "알림 시스템을 사용할 수 없습니다.", 
                    "오류", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        updateButtonState();
    }
    
    /**
     * 버튼 상태 업데이트 (읽지 않은 알림 수에 따라)
     */
    public void updateButtonState() {
        if (controller == null) {
            setText("알림");
            return;
        }
        
        try {
            int unreadCount = controller.getModel().getUnreadCount();
            setText(controller.getNotificationButtonText());
            
            if (unreadCount > 0 && !isBlinking) {
                startBlinking();
            } else if (unreadCount == 0 && isBlinking) {
                stopBlinking();
            }
        } catch (Exception e) {
            System.err.println("❌ NotificationButton: 버튼 상태 업데이트 실패 - " + e.getMessage());
            setText("알림");
        }
    }
    
    /**
     * 깜빡임 효과 시작
     */
    private void startBlinking() {
        if (blinkTimer != null) {
            blinkTimer.stop();
        }
        
        isBlinking = true;
        final boolean[] blink = {false};
        
        blinkTimer = new Timer(500, e -> {
            if (blink[0]) {
                setBackground(originalBackground);
            } else {
                setBackground(blinkColor);
            }
            blink[0] = !blink[0];
            repaint();
        });
        
        blinkTimer.start();
    }
    
    /**
     * 깜빡임 효과 중지
     */
    private void stopBlinking() {
        if (blinkTimer != null) {
            blinkTimer.stop();
            blinkTimer = null;
        }
        
        isBlinking = false;
        setBackground(originalBackground);
        repaint();
    }
    
    /**
     * 컨트롤러 반환
     */
    public NotificationController getController() {
        return controller;
    }
    
    /**
     *  컴포넌트 종료 시 정리 작업 (싱글톤이므로 인스턴스 제거하지 않음)
     */
    public void shutdown() {
        stopBlinking();
        //  싱글톤 인스턴스는 제거하지 않음 (다른 곳에서도 사용할 수 있음)
    }
}