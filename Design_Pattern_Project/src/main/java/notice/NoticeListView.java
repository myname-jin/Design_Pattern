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
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class NoticeListView extends JFrame {
    private final DefaultListModel<Notice> listModel;
    private final JList<Notice> noticeList;
    private final JButton addButton, editButton, deleteButton, toggleReadButton, backButton;
    private final JTextField searchField;
    private final JComboBox<String> filterCombo;

    public NoticeListView() {
        setTitle("공지사항 관리");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 검색/필터/읽음/뒤로가기 상단 패널
        searchField = new JTextField(12);
        filterCombo = new JComboBox<>(new String[]{"전체", "일반", "긴급", "시스템", "읽음", "안읽음"});
        toggleReadButton = new JButton("읽음/안읽음");
        backButton = new JButton("뒤로가기");

        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.add(new JLabel("검색:"));
        topLeftPanel.add(searchField);
        topLeftPanel.add(new JLabel("카테고리:"));
        topLeftPanel.add(filterCombo);

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.add(toggleReadButton);
        topRightPanel.add(backButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topRightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 공지 목록 중앙
        listModel = new DefaultListModel<>();
        noticeList = new JList<>(listModel);
        noticeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(noticeList), BorderLayout.CENTER);

        // 하단 버튼
        addButton = new JButton("추가");
        editButton = new JButton("수정");
        deleteButton = new JButton("삭제");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addButton);
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 목록 표시
    public void setNotices(List<Notice> notices) {
        listModel.clear();
        for (Notice n : notices) listModel.addElement(n);
    }

    public int getSelectedIndex() {
        return noticeList.getSelectedIndex();
    }

    public String getSearchText() {
        return searchField.getText().trim();
    }

    public String getFilterCategory() {
        return (String) filterCombo.getSelectedItem();
    }

    public JButton getBackButton() {
        return backButton;
    }

    // 리스너 등록
    public void addSearchListener(DocumentListener l) {
        searchField.getDocument().addDocumentListener(l);
    }

    public void addFilterListener(ActionListener l) {
        filterCombo.addActionListener(l);
    }

    public void addToggleReadListener(ActionListener l) {
        toggleReadButton.addActionListener(l);
    }

    public void addAddListener(ActionListener l) {
        addButton.addActionListener(l);
    }

    public void addEditListener(ActionListener l) {
        editButton.addActionListener(l);
    }

    public void addDeleteListener(ActionListener l) {
        deleteButton.addActionListener(l);
    }
}