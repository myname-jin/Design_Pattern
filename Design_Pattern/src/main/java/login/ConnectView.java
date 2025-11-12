/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

/**
 *
 * @author adsd3
 */

import ServerClient.CommandProcessor;
import ServerClient.SocketManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ConnectView extends javax.swing.JFrame {

    private JTextField ipField;
    private JButton connectButton;

    public ConnectView() {
        setTitle("서버 연결");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("서버 IP:");
        ipField = new JTextField("127.0.0.1", 15);
        connectButton = new JButton("서버 연결");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(label);
        panel.add(ipField);

        add(panel, BorderLayout.CENTER);
        add(connectButton, BorderLayout.SOUTH);

        connectButton.addActionListener((ActionEvent e) -> {
            String ip = ipField.getText().trim();
            int port = 5000; 
            try {
                Socket socket = new Socket(ip, port);
                SocketManager.setSocket(socket);
                System.out.println("[ConnectView] 서버와 연결되었습니다: " + ip + ":" + port);

                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                CommandProcessor.getInstance().setWriter(out);
                CommandProcessor.getInstance().start();

                JOptionPane.showMessageDialog(this, "서버 연결 성공");

                SwingUtilities.invokeLater(() -> {
                    LoginView  loginView  = new LoginView();
                    // LoginModel loginModel = new LoginModel(); // 1. 모델(Model) 생성 코드 삭제
                    new LoginController(loginView, socket, in); // 2. LoginController 호출 시 Model 제거
                    loginView.setLocationRelativeTo(null);
                    loginView.setVisible(true);
                });

                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "서버 연결 실패: " + ex.getMessage(), "연결 오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}