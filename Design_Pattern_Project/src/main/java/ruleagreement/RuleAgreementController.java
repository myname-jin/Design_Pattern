/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruleagreement;

/**
 *
 * @author adsd3
 */

import ServerClient.LogoutUtil;
import UserFunction.UserMainController;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class RuleAgreementController {
    private final RuleAgreementModel model;
    private final RuleAgreementView view;

    public RuleAgreementController(String userId, String userType, Socket socket, BufferedWriter out) throws Exception {
        String absolutePath = "src/main/resources/rules.txt";
        this.model = new RuleAgreementModel(absolutePath);
        this.view = new RuleAgreementView(model.getRules());

        //  로그아웃 처리
        LogoutUtil.attach(view, userId, socket, out);

        view.getNextButton().addActionListener(e -> {
            if (view.allChecked()) {
                JOptionPane.showMessageDialog(view, "모든 규칙에 동의하셨습니다. 다음 단계로 이동합니다.");
                view.dispose();

                try {
                    // in 스트림 생성
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    //  Main 화면 진입
                    new UserMainController(userId, userType, socket, in, out);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view, "메인 화면 연결 실패: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(view, "모든 규칙에 동의해야 합니다.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        view.setVisible(true);
    }
    public void showView() {
    SwingUtilities.invokeLater(() -> {
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    });
}
}