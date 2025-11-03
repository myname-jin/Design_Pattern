/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
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
        super(parent, "예약 취소", true); // true는 modal 다이얼로그를 의미
        initComponents();
    }
    
    private void initComponents() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // 예약 정보 영역
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("취소할 예약 정보"));
        
        reservationInfoArea = new JTextArea(6, 30);
        reservationInfoArea.setEditable(false);
        reservationInfoArea.setLineWrap(true);
        infoPanel.add(new JScrollPane(reservationInfoArea), BorderLayout.CENTER);
        
        // 취소 사유 입력 영역
        JPanel reasonPanel = new JPanel(new BorderLayout());
        reasonPanel.setBorder(BorderFactory.createTitledBorder("취소 사유"));
        
        reasonField = new JTextField(30);
        reasonPanel.add(reasonField, BorderLayout.CENTER);
        
        // 버튼 영역
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confirmButton = new JButton("확인");
        cancelButton = new JButton("취소");
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        // 전체 레이아웃 구성
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(reasonPanel);
        
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // 기본 취소 버튼 동작
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    public void setReservationInfo(String name, String userId, String room, String date, String startTime, String endTime) {
        String info = String.format(
            "이름: %s\n학번: %s\n강의실: %s\n날짜: %s\n시간: %s ~ %s",
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
