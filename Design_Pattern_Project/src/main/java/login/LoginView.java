/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField userIdField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private JRadioButton userRadio = new JRadioButton("사용자", true);
    private JRadioButton adminRadio = new JRadioButton("관리자");
    private JButton loginButton = new JButton("로그인");
    private JButton registerButton = new JButton("회원가입");
    private JButton findPasswordButton = new JButton("비밀번호 찾기/변경");
    public JLabel labelId = new JLabel("ID:"); // ID: 라벨 생성
    public JLabel labelPw = new JLabel("PW:"); // PW: 라벨 생성

    public LoginView() {
        setTitle("로그인 화면");
        setSize(350, 270);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        userIdField.setBounds(80, 30, 180, 30);
        passwordField.setBounds(80, 70, 180, 30);
        loginButton.setBounds(80, 145, 80, 30);
        userRadio.setBounds(80, 110, 80, 20);
        adminRadio.setBounds(160, 110, 80, 20);
        registerButton.setBounds(170, 145, 85, 30);
        findPasswordButton.setBounds(80, 185, 175, 30);
        labelId.setBounds(50, 30, 80, 30); // ID: 라벨 위치 지정
        labelPw.setBounds(50, 70, 80, 30); // PW: 라벨 위치 지정

        ButtonGroup group = new ButtonGroup();
        group.add(userRadio);
        group.add(adminRadio);

        add(userIdField);
        add(passwordField);
        add(loginButton);
        add(userRadio);
        add(adminRadio);
        add(registerButton);
        add(findPasswordButton); // 비밀번호 찾기/변경 버튼 추가
        add(labelId); // ID: 라벨 추가
        add(labelPw); // PW: 라벨 추가  
    }

    public String getUserId() {
        return userIdField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }

    public String getRole() {
        return userRadio.isSelected() ? "user" : "admin";
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public JButton getFindPasswordButton() {
        return findPasswordButton;
    }
}
