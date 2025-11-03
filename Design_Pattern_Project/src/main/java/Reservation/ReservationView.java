package Reservation;

import org.jdatepicker.impl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReservationView extends JFrame {
    private JLabel nameLabel, idLabel, deptLabel;
    private JComboBox<String> roomTypeComboBox;
    private JLabel roomInfoLabel;
    private JComboBox<String> roomComboBox;
    private JPanel timeSlotPanel;
    private JDatePickerImpl datePicker;
    private JTextField selectedTimeField;
    private JLabel totalDurationLabel;
    private JPanel purposePanel;
    private JButton[] purposeButtons;
    private JButton reserveButton;
    private JButton backButton;
    private String selectedPurpose = "";
    private Set<String> selectedTimes = new TreeSet<>();

    public ReservationView() {
        setTitle("강의실 예약 시스템");
        setSize(720, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        nameLabel = new JLabel("이름: ");
        idLabel = new JLabel("학번: ");
        deptLabel = new JLabel("학과: ");
        infoPanel.add(nameLabel);
        infoPanel.add(idLabel);
        infoPanel.add(deptLabel);
        add(infoPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("강의실 유형:"));
        roomTypeComboBox = new JComboBox<>();
        typePanel.add(roomTypeComboBox);
        centerPanel.add(typePanel);

        // 강의실 선택
        JPanel roomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        roomPanel.add(new JLabel("강의실 선택:"));
        roomComboBox = new JComboBox<>();
        roomPanel.add(roomComboBox);
        centerPanel.add(roomPanel);
        
        roomInfoLabel = new JLabel(" ");  // 초기 빈 라벨
        roomPanel.add(roomInfoLabel);    // 강의실 선택 아래에 배치


        // 날짜 선택
        UtilDateModel model = new UtilDateModel();
        Calendar today = Calendar.getInstance();
        model.setDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        model.setSelected(true); // 오늘 날짜 선택
        Properties p = new Properties();
        p.put("text.today", "오늘");
        p.put("text.month", "월");
        p.put("text.year", "년");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JPanel datePanelUI = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanelUI.add(new JLabel("예약 날짜 선택:"));
        datePanelUI.add(datePicker);
        centerPanel.add(datePanelUI);

        // 시간대 영역
        JLabel timeLabel = new JLabel("예약 가능한 시간대 목록:", SwingConstants.CENTER);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(timeLabel);
        timeSlotPanel = new JPanel();
        timeSlotPanel.setLayout(new GridLayout(0, 2));
        JScrollPane scrollPane = new JScrollPane(timeSlotPanel);
        scrollPane.setPreferredSize(new Dimension(680, 130));
        centerPanel.add(scrollPane);

        // 선택된 시간
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(new JLabel("선택한 시간:"));
        selectedTimeField = new JTextField(30);
        timePanel.add(selectedTimeField);
        centerPanel.add(timePanel);
        
        //총 선택 시간
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        durationPanel.add(new JLabel("총 선택 시간:"));
        totalDurationLabel = new JLabel("0분");
        durationPanel.add(totalDurationLabel);
        centerPanel.add(durationPanel);

        // 목적 선택
        JLabel purposeLabel = new JLabel("예약 목적 선택:", SwingConstants.CENTER);
        purposeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(purposeLabel);
        
        purposePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[] purposes = {"수업", "시험", "스터디", "세미나", "기타"};
        purposeButtons = new JButton[purposes.length];
        
        Dimension fixedSize = new Dimension(80, 30);  // 원하는 크기
        Insets margin = new Insets(5, 10, 5, 10);  //내부 여백 고정
        
        for (int i = 0; i < purposes.length; i++) {
            final String purpose = purposes[i];
            JButton btn = new JButton(purpose);
             btn.setPreferredSize(fixedSize);           //  고정 크기
            btn.setMargin(margin);                     //  고정 여백
            btn.setFocusPainted(false);                //  클릭 테두리 제거
            btn.setBackground(null);                   // 초기 배경

            purposeButtons[i] = btn;
            
            btn.addActionListener(e -> {
               selectedPurpose = purpose;
                    
                for (JButton b : purposeButtons) {
                    b.setBackground(null);
                    b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
        
        btn.setBackground(new Color(200, 230, 255)); // 파스텔 블루
        btn.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // 강조
    });

   purposePanel.add(purposeButtons[i]);
}

centerPanel.add(purposePanel);


        add(centerPanel, BorderLayout.CENTER);

        reserveButton = new JButton("예약하기");
        backButton = new JButton("뒤로가기");
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        bottomPanel.add(reserveButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 사용자 정보 표시
    public void setUserInfo(String name, String id, String dept) {
        nameLabel.setText("이름: " + name);
        idLabel.setText("학번: " + id);
        deptLabel.setText("학과: " + dept);
    }
    
    
    public void setRoomTypeList(java.util.List<String> types) {
        roomTypeComboBox.removeAllItems();
        for (String type : types) {
            roomTypeComboBox.addItem(type);
        }
    }

    // 강의실 콤보박스 채우기

    /**
     *
      @param roomNames 강의실 이름 리스트
     */
    public void setRoomList(java.util.List<String> roomNames) {
        roomComboBox.removeAllItems();
        for (String name : roomNames) {
            roomComboBox.addItem(name);
        }
    }
    
    public String getSelectedRoomType() {
    return (String) roomTypeComboBox.getSelectedItem();
    }

    public String getSelectedRoom() {
        return (String) roomComboBox.getSelectedItem();
    }
    
    public void addRoomTypeSelectionListener(ActionListener listener) {
    roomTypeComboBox.addActionListener(listener);
    }

    public void addRoomSelectionListener(ActionListener listener) {
        roomComboBox.addActionListener(listener);
    }

    public String getSelectedDate() {
        return datePicker.getJFormattedTextField().getText().trim();
    }

    public void addDateSelectionListener(ActionListener listener) {
        datePicker.addActionListener(listener);
    }

    public void addTimeSlot(String time, ActionListener listener) {
        JButton timeButton = new JButton(time);
        timeButton.setBackground(Color.LIGHT_GRAY);
        timeButton.addActionListener(listener);
        timeButton.addActionListener(e -> {
            if (selectedTimes.contains(time)) {
                selectedTimes.remove(time);
                timeButton.setBackground(Color.LIGHT_GRAY);
            } else {
                selectedTimes.add(time);
                timeButton.setBackground(new Color(180, 220, 250));
            }
            updateSelectedTimeField();
        });
        timeSlotPanel.add(timeButton);
        timeSlotPanel.revalidate();
        timeSlotPanel.repaint();
    }
    
     private void updateSelectedTimeField() {
        selectedTimeField.setText(String.join(", ", selectedTimes));
    }

    public java.util.List<String> getSelectedTimes() {
        return new ArrayList<>(selectedTimes);
    }

    public void clearTimeSlots() {
        selectedTimes.clear();
        selectedTimeField.setText("");
        totalDurationLabel.setText("0분");
        timeSlotPanel.removeAll();
        timeSlotPanel.revalidate();
        timeSlotPanel.repaint();
    }
    
    public void setTotalDuration(String durationText) {
        totalDurationLabel.setText(durationText);
    }
    
    public String getSelectedPurpose() {
        return selectedPurpose;
    }
     
    public void addReserveButtonListener(ActionListener listener) {
        reserveButton.addActionListener(listener);
    }
    
    public void addBackButtonListener(ActionListener listener) {
    backButton.addActionListener(listener);
    }


    public String getSelectedTime() {
        return selectedTimeField.getText().trim();
    }

    public void setSelectedTime(String time) {
        selectedTimeField.setText(time);
    }
    
    public JButton getBackButton() {
    return backButton;
    }
    
    public void setRoomInfoText(String info) {
    roomInfoLabel.setText(info);
}
    
    //교수용 설정. 일단은 호출만. 필요시 기능 추가
    public void enableProfessorMode() {
//    JPanel professorPanel = new JPanel();
//    professorPanel.setLayout(new BoxLayout(professorPanel, BoxLayout.X_AXIS));
//    professorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // 목적 버튼 외부 설정 메서드
    public void setPurposeOptions(java.util.List<String> options) {
        purposePanel.removeAll();
        purposeButtons = new JButton[options.size()];

        Dimension fixedSize = new Dimension(80, 30);
        Insets margin = new Insets(5, 10, 5, 10);

        for (int i = 0; i < options.size(); i++) {
            final String purpose = options.get(i);
            JButton btn = new JButton(purpose);
            btn.setPreferredSize(fixedSize);
            btn.setMargin(margin);
            btn.setFocusPainted(false);
            btn.setBackground(null);

            btn.addActionListener(e -> {
                selectedPurpose = purpose;
                for (JButton b : purposeButtons) {
                    b.setBackground(null);
                    b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                }
                btn.setBackground(new Color(200, 230, 255));
                btn.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            });

            purposeButtons[i] = btn;
            purposePanel.add(btn);
        }

        purposePanel.revalidate();
        purposePanel.repaint();

        if (purposeButtons.length > 0) {
            purposeButtons[0].doClick(); // 기본 선택
        }
    }

   public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parse(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}
