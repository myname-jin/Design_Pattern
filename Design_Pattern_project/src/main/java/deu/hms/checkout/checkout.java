package deu.hms.checkout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;

public class checkout extends JFrame {

    private JTable reservationListTable;
    private JTextField searchTextField, roomPriceField;
    private JTextArea feedbackTextArea;
    private JRadioButton onsitePaymentRadio, cardRegistrationRadio;
    private DefaultTableModel tableModel;
    private ButtonGroup paymentGroup;

    public checkout() {
        setTitle("체크아웃");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeUI();
        loadReservationData();

        setLocationRelativeTo(null); // 화면 중앙에 배치
        setVisible(true);

        // 종료 시 데이터 저장
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveReservationData();
                System.exit(0);
            }
        });
    }

    private void initializeUI() {
        JPanel topPanel = createTopPanel(); // 뒤로가기 버튼 포함
        JPanel searchPanel = createSearchPanel();
        JScrollPane tableScrollPane = createTableScrollPane();
        JPanel bottomPanel = createBottomPanel();

        add(topPanel, BorderLayout.NORTH); // 뒤로가기 버튼을 포함한 상단 패널 추가
        add(searchPanel, BorderLayout.CENTER); // 검색 패널
        add(tableScrollPane, BorderLayout.CENTER); // 예약자 목록 테이블
        add(bottomPanel, BorderLayout.SOUTH); // 하단 패널 추가

        pack();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("이전 페이지로");
        backButton.addActionListener(e -> dispose()); // 현재 창을 닫습니다.
        topPanel.add(backButton, BorderLayout.WEST); // 뒤로가기 버튼을 왼쪽에 배치
        return topPanel;
    }


    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        String[] searchOptions = {"성이름", "고유번호", "객실 번호"};
        JComboBox<String> searchComboBox = new JComboBox<>(searchOptions);
        searchTextField = new JTextField(15);

        JButton searchButton = new JButton("검색");
        searchButton.addActionListener(e -> performSearch(searchComboBox.getSelectedItem().toString()));

        JButton refreshButton = new JButton("새로고침");
        refreshButton.addActionListener(e -> refreshTable());

        searchPanel.add(searchComboBox);
        searchPanel.add(searchTextField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        return searchPanel;
    }

    private JScrollPane createTableScrollPane() {
        String[] columnNames = {"고유 번호", "이름", "전화 번호", "방 번호", "객실 금액", "결제 수단", "상태", "총 금액"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reservationListTable = new JTable(tableModel);
        reservationListTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) updateCheckoutDetails();
        });

        return new JScrollPane(reservationListTable);
    }

    private JPanel createBottomPanel() {
        JPanel feedbackPanel = createFeedbackPanel();
        JPanel paymentPanel = createPaymentPanel();

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(feedbackPanel, BorderLayout.CENTER);
        bottomPanel.add(paymentPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    private JPanel createFeedbackPanel() {
        JPanel feedbackPanel = new JPanel(new BorderLayout());
        JLabel feedbackLabel = new JLabel("FeedBack:");
        feedbackTextArea = new JTextArea(3, 20);
        feedbackPanel.add(feedbackLabel, BorderLayout.NORTH);
        feedbackPanel.add(new JScrollPane(feedbackTextArea), BorderLayout.CENTER);
        return feedbackPanel;
    }

    private JPanel createPaymentPanel() {
        JPanel paymentPanel = new JPanel();
        paymentGroup = new ButtonGroup();

        onsitePaymentRadio = new JRadioButton("현장 결제");
        cardRegistrationRadio = new JRadioButton("카드 결제");

        paymentGroup.add(onsitePaymentRadio);
        paymentGroup.add(cardRegistrationRadio);

        JButton checkoutButton = new JButton("체크아웃");
        checkoutButton.addActionListener(e -> performCheckout());

        roomPriceField = new JTextField(10);
        roomPriceField.setEditable(false);

        paymentPanel.add(onsitePaymentRadio);
        paymentPanel.add(cardRegistrationRadio);
        paymentPanel.add(new JLabel("총 금액:"));
        paymentPanel.add(roomPriceField);
        paymentPanel.add(checkoutButton);

        return paymentPanel;
    }

    private void loadReservationData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("CheckInData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 11) {
                    tableModel.addRow(new Object[]{
                            data[0], data[1], data[2], data[5],
                            data[7], data[8], data[9], data[10]
                    });
                }
            }
            JOptionPane.showMessageDialog(this, "예약자 데이터를 성공적으로 불러왔습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "데이터 파일을 찾을 수 없습니다. 새로 생성됩니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "예약자 데이터를 불러오는 중 오류가 발생했습니다!", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveReservationData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("CheckInData.txt"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write(String.join(",",
                        tableModel.getValueAt(i, 0).toString(),
                        tableModel.getValueAt(i, 1).toString(),
                        "-",
                        tableModel.getValueAt(i, 2).toString(),
                        "-",
                        "-",
                        tableModel.getValueAt(i, 3).toString(),
                        "-",
                        tableModel.getValueAt(i, 4).toString(),
                        tableModel.getValueAt(i, 5).toString(),
                        tableModel.getValueAt(i, 6).toString()));
                writer.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "CheckInData.txt 저장 중 오류가 발생했습니다!", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performCheckout() {
        int selectedRow = reservationListTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "체크아웃할 항목을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String uniqueId = tableModel.getValueAt(selectedRow, 0).toString();
        String name = tableModel.getValueAt(selectedRow, 1).toString();
        String roomNumber = tableModel.getValueAt(selectedRow, 3).toString();
        int basePrice = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString().replace(",", ""));
        String paymentMethod = tableModel.getValueAt(selectedRow, 5).toString();

        int additionalAmount = getAdditionalAmountFromFile(roomNumber);
        int lateFee = calculateLateCheckoutFee();
        int totalPrice = basePrice + additionalAmount + lateFee;

        saveFeedback(name, feedbackTextArea.getText().trim());
        saveToResultText(uniqueId, name, roomNumber, totalPrice, paymentMethod);

        updateCheckInData(uniqueId, "체크아웃 완료");
        tableModel.setValueAt("체크아웃 완료", selectedRow, 6);

        JOptionPane.showMessageDialog(this, "체크아웃 완료! 추가 요금: " + lateFee, "성공", JOptionPane.INFORMATION_MESSAGE);
        feedbackTextArea.setText("");
        roomPriceField.setText("");
    }

    private int getAdditionalAmountFromFile(String roomNumber) {
        int additionalAmount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("ServiceList.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 8 && data[4].trim().equals(roomNumber)) {
                    additionalAmount += Integer.parseInt(data[7].trim());
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "ServiceList.txt 처리 중 오류 발생!", "오류", JOptionPane.ERROR_MESSAGE);
        }
        return additionalAmount;
    }

    private int calculateLateCheckoutFee() {
        java.time.LocalTime now = java.time.LocalTime.now();
        java.time.LocalTime checkoutLimit = java.time.LocalTime.of(11, 0);
        return now.isAfter(checkoutLimit) ? Integer.parseInt(roomPriceField.getText().replace(",", "")) : 0;
    }

    private void saveFeedback(String name, String feedback) {
        if (feedback.isEmpty()) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("FeedBack.txt", true))) {
            writer.write("이름: " + name + ", 피드백: " + feedback);
            writer.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "피드백 저장 중 오류 발생!", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToResultText(String id, String name, String room, int price, String payment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultText.txt", true))) {
            writer.write(String.join(", ", id, name, room, String.valueOf(price), payment, "체크아웃 완료"));
            writer.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "resultText.txt 저장 중 오류 발생!", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCheckInData(String uniqueId, String newStatus) {
        StringBuilder updatedContent = new StringBuilder();
        File inputFile = new File("CheckInData.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 10 && data[0].equals(uniqueId)) {
                    data[9] = newStatus;
                }
                updatedContent.append(String.join(",", data)).append(System.lineSeparator());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "CheckInData.txt 읽기 오류!", "오류", JOptionPane.ERROR_MESSAGE);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
            writer.write(updatedContent.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "CheckInData.txt 저장 오류!", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCheckoutDetails() {
        int selectedRow = reservationListTable.getSelectedRow();
        if (selectedRow == -1) return;

        String roomNumber = tableModel.getValueAt(selectedRow, 3).toString();
        int basePrice = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString().replace(",", ""));
        int additionalAmount = getAdditionalAmountFromFile(roomNumber);
        int totalPrice = basePrice + additionalAmount;

        roomPriceField.setText(String.format("%,d", totalPrice));
        String paymentMethod = tableModel.getValueAt(selectedRow, 5).toString();

        if ("현장 결제".equals(paymentMethod)) onsitePaymentRadio.setSelected(true);
        else if ("카드 결제".equals(paymentMethod)) cardRegistrationRadio.setSelected(true);
    }

    private void performSearch(String searchType) {
        String searchInput = searchTextField.getText().trim();
        if (searchInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "검색어를 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel filteredModel = new DefaultTableModel(
                new Object[]{"고유 번호", "이름", "전화 번호", "방 번호", "객실 금액", "결제 수단", "상태", "총 금액"}, 0
        );

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String value = "";
            if ("성이름".equals(searchType)) value = tableModel.getValueAt(i, 1).toString();
            else if ("고유번호".equals(searchType)) value = tableModel.getValueAt(i, 0).toString();
            else if ("객실 번호".equals(searchType)) value = tableModel.getValueAt(i, 3).toString();

            if (value.contains(searchInput)) {
                filteredModel.addRow(new Object[]{
                        tableModel.getValueAt(i, 0), tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2), tableModel.getValueAt(i, 3),
                        tableModel.getValueAt(i, 4), tableModel.getValueAt(i, 5),
                        tableModel.getValueAt(i, 6), tableModel.getValueAt(i, 7)
                });
            }
        }
        reservationListTable.setModel(filteredModel);
    }

    private void refreshTable() {
        reservationListTable.setModel(tableModel);
        tableModel.setRowCount(0);
        loadReservationData();
        JOptionPane.showMessageDialog(this, "예약자 명단이 새로고침되었습니다.", "새로고침 완료", JOptionPane.INFORMATION_MESSAGE);
    }
}
