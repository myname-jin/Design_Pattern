/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rulemanagement;

/**
 *
 * @author adsd3
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 관리용 UI. 규칙 목록에 체크박스를 표시하고, 새 규칙 입력 및 삭제 기능 버튼을 제공합니다.
 */
public class RuleManagementView extends JFrame {

    private final JPanel checkPanel = new JPanel();
    private final JTextField newRuleField = new JTextField(20);
    private final JButton addButton = new JButton("추가");
    private final JButton deleteButton = new JButton("삭제");
    private final JButton backButton = new JButton("뒤로");

    public RuleManagementView(List<String> rules) {
        setTitle("강의실 사용 규칙 관리");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 체크리스트 구성
        checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
        updateRules(rules);
        JScrollPane scrollPane = new JScrollPane(checkPanel);

        // 입력창 구성
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("새 규칙:"));
        inputPanel.add(newRuleField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        // 상단 영역에 버튼 추가
        JPanel topPanel = new JPanel(null); // null layout for manual positioning
        topPanel.setPreferredSize(new Dimension(500, 30));
        backButton.setBounds(400, 10, 80, 20);
        topPanel.add(backButton);

        // 전체 구성
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * 체크박스로 표시된 규칙을 갱신
     */
    public void updateRules(List<String> rules) {
        checkPanel.removeAll();
        for (String rule : rules) {
            JCheckBox box = new JCheckBox(rule);
            checkPanel.add(box);
        }
        checkPanel.revalidate();
        checkPanel.repaint();
    }

    /**
     * 현재 표시된 JCheckBox들을 반환
     */
    public List<JCheckBox> getRuleCheckBoxes() {
        List<JCheckBox> list = new ArrayList<>();
        for (Component c : checkPanel.getComponents()) {
            if (c instanceof JCheckBox) {
                list.add((JCheckBox) c);
            }
        }
        return list;
    }

    public String getNewRuleText() {
        return newRuleField.getText().trim();
    }

    public void clearNewRuleField() {
        newRuleField.setText("");
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    Object getNewRuleField() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
