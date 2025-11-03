/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruleagreement;

/**
 *
 * @author adsd3
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RuleAgreementView extends JFrame {
    private final List<JCheckBox> checkBoxes = new ArrayList<>();
    private final JButton nextButton = new JButton("다음");

    public RuleAgreementView(List<String> rules) {
        setTitle("강의실 사용 규칙 동의");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));

        for (String rule : rules) {
            JCheckBox box = new JCheckBox(rule);
            checkBoxes.add(box);
            checkPanel.add(box);
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("모든 강의실 사용 규칙에 동의해주세요."), BorderLayout.NORTH);
        panel.add(new JScrollPane(checkPanel), BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.SOUTH);

        add(panel);
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public boolean allChecked() {
        return checkBoxes.stream().allMatch(AbstractButton::isSelected);
    }
    
}