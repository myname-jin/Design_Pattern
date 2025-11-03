/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

/**
 *
 * @author msj02
 */
import javax.swing.*;
import java.awt.*;

public class SignupView extends JFrame {
    JTextField txtId;
    JPasswordField txtPw;
    private JTextField txtName;
    private JTextField txtDept;
    public JButton btnRegister;
    public JButton btnBack; // 뒤로가기 버튼
    JComboBox<String> cmbRole;

    public SignupView() {
        super("회원가입");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        txtId = new JTextField();
        txtPw = new JPasswordField();
        txtName = new JTextField();
        txtDept = new JTextField();
        cmbRole = new JComboBox<>(new String[]{"admin", "학생", "교수"});
        btnRegister = new JButton("등록");
        btnBack = new JButton("뒤로가기");

        // 입력 부분
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("ID:"));
        panel.add(txtId);
        panel.add(new JLabel("PW:"));        
        panel.add(txtPw);
        panel.add(new JLabel("이름:"));        
        panel.add(txtName);
        panel.add(new JLabel("학과:"));        
        panel.add(txtDept);
        panel.add(new JLabel("역할:"));        
        panel.add(cmbRole);
        
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonpanel.add(btnRegister);
        buttonpanel.add(btnBack);

        // 프레임에 붙이기
        add(panel, BorderLayout.CENTER);
        add(buttonpanel, BorderLayout.SOUTH);
    }
    
    public String getId() {
        return txtId.getText().trim();
    }

    public String getPw() {
        return new String(txtPw.getPassword()).trim();
    }
    // 이름 입력값을 반환
    public String getName() {
        return txtName.getText().trim();
    }
    // 학과 입력값을 반환
    public String getDept() {
        return txtDept.getText().trim();
    }

    public String getRole() {
        return (String) cmbRole.getSelectedItem();
    }

    public JButton getBtnRegister() {
        return btnRegister;
    }
}