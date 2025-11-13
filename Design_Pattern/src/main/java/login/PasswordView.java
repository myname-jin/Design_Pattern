/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import javax.swing.*;
import java.awt.*;

/**
 * 비밀번호 변경을 위한 View (JFrame)
 * @author adsd3
 */
public class PasswordView extends JFrame {

    private JTextField userIdField = new JTextField(15);
    private JPasswordField oldPasswordField = new JPasswordField(15); // 1. 기존 비밀번호 칸 추가
    private JPasswordField newPasswordField = new JPasswordField(15);
    private JPasswordField confirmPasswordField = new JPasswordField(15);
    private JButton changeButton = new JButton("변경하기");
    private JButton cancelButton = new JButton("취소 (로그인 화면)");

    public PasswordView() {
        setTitle("비밀번호 변경");
        setSize(350, 300); // 2. 세로 크기 증가 (250 -> 300)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5)); // 3. 레이아웃 3x2 -> 4x2
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("ID:"));
        panel.add(userIdField);
        
        panel.add(new JLabel("기존 비밀번호:")); // 4. 기존 비밀번호 라벨 추가
        panel.add(oldPasswordField); // 5. 기존 비밀번호 필드 추가
        
        panel.add(new JLabel("새 비밀번호:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("새 비밀번호 확인:"));
        panel.add(confirmPasswordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(changeButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getUserId() {
        return userIdField.getText().trim();
    }
    
    // 6. 기존 비밀번호 Getter 추가
    public String getOldPassword() {
        return new String(oldPasswordField.getPassword()).trim();
    }

    public String getNewPassword() {
        return new String(newPasswordField.getPassword()).trim();
    }
    
    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword()).trim();
    }

    public JButton getChangeButton() {
        return changeButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}