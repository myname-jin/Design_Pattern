/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserNotification;

/**
 *
 * @author jms5310
 */
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class NotificationView extends JFrame {

    private NotificationController controller;
    private JList<NotificationModel.NotificationItem> notificationList;
    private DefaultListModel<NotificationModel.NotificationItem> listModel;
    private JTextPane detailPane;
    private JButton checkinButton;
    private JLabel titleLabel;

    public NotificationView(NotificationController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("알림 센터");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                handleLogout();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("알림 센터");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("뒤로가기");
        backButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        backButton.addActionListener(e -> handleBackButton());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        listModel = new DefaultListModel<>();
        notificationList = new JList<>(listModel);
        notificationList.setCellRenderer(new NotificationCellRenderer());
        notificationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        notificationList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                NotificationModel.NotificationItem selectedItem = notificationList.getSelectedValue();
                if (selectedItem != null) {
                    updateDetailView(selectedItem);
                    if (!selectedItem.isRead()) {
                        controller.markAsRead(selectedItem.getId());
                    }
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(notificationList);
        splitPane.setLeftComponent(listScrollPane);

        JPanel detailPanel = new JPanel(new BorderLayout(5, 5));
        detailPane = new JTextPane();
        detailPane.setContentType("text/html");
        detailPane.setEditable(false);

        JScrollPane detailScrollPane = new JScrollPane(detailPane);
        detailPanel.add(detailScrollPane, BorderLayout.CENTER);

        checkinButton = new JButton("입실 확인");
        checkinButton.setEnabled(false);
        checkinButton.addActionListener(e -> {
            NotificationModel.NotificationItem selectedItem = notificationList.getSelectedValue();
            if (selectedItem != null) {
                controller.checkIn(selectedItem.getId());
            }
        });

        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomButtonPanel.add(checkinButton);
        detailPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

        splitPane.setRightComponent(detailPanel);
        setContentPane(mainPanel);
    }

    private void handleBackButton() {
        dispose();
        new UserFunction.UserMainController(
            controller.getUserId(),
            controller.getUserType(), // ✅ userType 전달
            controller.getSocket(),
            controller.getIn(),
            controller.getOut()
        );
    }

    private void handleLogout() {
        try {
            if (controller.getSocket() != null && controller.getOut() != null) {
                controller.getOut().write("LOGOUT:" + controller.getUserId() + "\n");
                controller.getOut().flush();
                controller.getSocket().close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateNotificationList(List<NotificationModel.NotificationItem> notifications) {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for (NotificationModel.NotificationItem item : notifications) {
                listModel.addElement(item);
            }
            if (notificationList.getSelectedIndex() == -1 && listModel.size() > 0) {
                notificationList.setSelectedIndex(0);
            }
        });
    }

    public void updateUnreadCount(int count) {
        SwingUtilities.invokeLater(() -> {
            if (count > 0) {
                titleLabel.setText("알림 센터 (읽지 않은 알림: " + count + ")");
            } else {
                titleLabel.setText("알림 센터");
            }
        });
    }

    private void updateDetailView(NotificationModel.NotificationItem item) {
        detailPane.setText(controller.formatNotificationContent(item));
        boolean canCheckin = isCheckinAvailable(item);
        checkinButton.setEnabled(canCheckin);
        if (item.isCheckedIn()) {
            checkinButton.setText("입실 확인 완료");
            checkinButton.setEnabled(false);
        } else {
            checkinButton.setText("입실 확인");
        }
    }

    private boolean isCheckinAvailable(NotificationModel.NotificationItem item) {
        if (item.isCheckedIn()) return false;
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime reservationTime = item.getReservationTime();
        return now.isAfter(reservationTime) && now.isBefore(reservationTime.plusMinutes(10));
    }

    private class NotificationCellRenderer extends DefaultListCellRenderer {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof NotificationModel.NotificationItem item) {
                String displayText = item.getTitle() + " (" + item.getCreatedTime().format(formatter) + ")";
                label.setText(displayText);
                label.setFont(label.getFont().deriveFont(item.isRead() ? Font.PLAIN : Font.BOLD));
                if (isCheckinAvailable(item)) {
                    label.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
                } else if (item.isCheckedIn()) {
                    label.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
                } else {
                    label.setIcon(null);
                }
            }
            return label;
        }
    }
    public NotificationController getController() {
        return this.controller;
    }
}