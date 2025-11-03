/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notice;

/**
 *
 * @author adsd3
 */
import javax.swing.*;
import java.awt.*;

public class NoticeEditorView extends JDialog {
    private final JTextArea textArea;
    private final JComboBox<String> categoryCombo;
    private final JButton saveButton, cancelButton;
    private boolean saved = false;

    public NoticeEditorView(JFrame parent) {
        super(parent, "공지사항 편집", true);
        setSize(350, 250);
        setLocationRelativeTo(parent);

        textArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(textArea);
        categoryCombo = new JComboBox<>(new String[]{"일반", "긴급", "시스템"});

        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel catPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        catPanel.add(new JLabel("카테고리:"));
        catPanel.add(categoryCombo);
        centerPanel.add(catPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        saveButton = new JButton("저장");
        cancelButton = new JButton("취소");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> { saved = true; setVisible(false); });
        cancelButton.addActionListener(e -> { saved = false; setVisible(false); });
    }

    public void showEditor(String initialText, String initialCategory) {
        textArea.setText(initialText);
        categoryCombo.setSelectedItem(initialCategory);
        saved = false;
        setVisible(true);
    }

    public boolean isSaved() { return saved; }
    public String getText() { return textArea.getText().trim(); }
    public String getSelectedCategory() { return categoryCombo.getSelectedItem().toString(); }
}
