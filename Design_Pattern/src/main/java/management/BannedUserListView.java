package management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BannedUserListView extends JDialog {

    private ReservationMgmtController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    public BannedUserListView(JFrame parent, ReservationMgmtController controller) {
        super(parent, "제한된 사용자 관리", true);
        this.controller = controller;
        
        initComponents();
        loadBannedUserData();
        
        setSize(600, 400);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("제한된 사용자 목록");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"학번", "이름", "학과", "구분"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton unbanButton = new JButton("제한 해제");
        JButton closeButton = new JButton("닫기");

        unbanButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "해제할 사용자를 선택해주세요.");
                return;
            }
            String studentId = (String) table.getValueAt(selectedRow, 0);
            String name = (String) table.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(this, 
                    "[" + name + "] 님의 제한을 해제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                controller.unbanUser(studentId);
                loadBannedUserData(); // 목록 새로고침
            }
        });

        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(unbanButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadBannedUserData() {
        tableModel.setRowCount(0);
        File file = new File("src/main/resources/banlist.txt");
        
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 파일 형식: 학번,이름,학과,구분
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    tableModel.addRow(parts); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}