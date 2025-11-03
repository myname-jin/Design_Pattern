/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

/**
 *
 * @author adsd3
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import ServerClient.FileWatcher;
import ServerClient.SocketManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * 최초 “서버 연결” 화면
 */
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

        // “서버 연결” 버튼 클릭 리스너
        connectButton.addActionListener((ActionEvent e) -> {
            String ip = ipField.getText().trim();
            int port = 5000; 
            try {
                // 1) 실제 서버에 Socket 연결
                Socket socket = new Socket(ip, port);
                
                // 2) SocketManager에 set → 전역에서 재사용 가능
                SocketManager.setSocket(socket);
                System.out.println("[ConnectView] 서버와 연결되었습니다: " + ip + ":" + port);

                // 3) FileWatcher 시작 (예: 파일 동기화가 필요하다면)
                new FileWatcher().start();

                // 4) 로그인 화면으로 넘어가기 (IP 입력 칸 없음)
                JOptionPane.showMessageDialog(this, "서버 연결 성공");

                SwingUtilities.invokeLater(() -> {
                    LoginView  loginView  = new LoginView();
                    LoginModel loginModel = new LoginModel();
                    // SocketManager를 썼기 때문에, LoginController에서는 따로 socket을 받을 필요 없음
                    new LoginController(loginView, loginModel);
                    loginView.setLocationRelativeTo(null);
                    loginView.setVisible(true);
                });

                // 5) 현재 ConnectView 창 닫기
                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "서버 연결 실패: " + ex.getMessage(), "연결 오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
