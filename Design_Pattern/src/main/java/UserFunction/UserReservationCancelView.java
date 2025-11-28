package UserFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 *
 * @author jms5310
 */
public class UserReservationCancelView extends JDialog {
    private JTextArea reservationInfoArea;
    private JTextField reasonField;
    private JButton confirmButton;
    private JButton cancelButton;
    
    public UserReservationCancelView(Frame parent) {
        super(parent, "예약 취소", true); // Modal = true
        initComponents();
    }
    
    private void initComponents() {
        setSize(400, 350);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // 1. 예약 정보 영역
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("취소할 예약 정보"));
        
        reservationInfoArea = new JTextArea(5, 30);
        reservationInfoArea.setEditable(false);
        reservationInfoArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        infoPanel.add(new JScrollPane(reservationInfoArea), BorderLayout.CENTER);
        
        // 2. 취소 사유 입력 영역
        JPanel reasonPanel = new JPanel(new BorderLayout());
        reasonPanel.setBorder(BorderFactory.createTitledBorder("취소 사유 입력"));
        
        reasonField = new JTextField();
        reasonField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        reasonPanel.add(reasonField, BorderLayout.CENTER);
        
        // 전체 내용 패널
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(reasonPanel);
        
        add(contentPanel, BorderLayout.CENTER);

        // 3. 버튼 영역
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confirmButton = new JButton("취소 확정");
        cancelButton = new JButton("닫기");
        
        // 닫기 버튼 동작
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void setReservationInfo(String name, String userId, String room, String date, String startTime, String endTime) {
        String info = String.format(
            " 이름: %s\n 학번: %s\n 강의실: %s\n 날짜: %s\n 시간: %s ~ %s",
            name, userId, room, date, startTime, endTime
        );
        reservationInfoArea.setText(info);
    }
    
    public String getCancelReason() {
        return reasonField.getText().trim();
    }
    
    public void addConfirmListener(ActionListener listener) {
        confirmButton.addActionListener(listener);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
}