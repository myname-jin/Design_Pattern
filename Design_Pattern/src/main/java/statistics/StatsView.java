package statistics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import management.AdminReservationModel;
import management.Reservation;

public class StatsView extends JFrame {
    
    private StatsContext context; 
    private AdminReservationModel dataModel; 
    
    private JComboBox<String> yearCombo;
    private JComboBox<String> semesterCombo;
    private JComboBox<String> typeCombo; 
    private JTable statsTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public StatsView() {
        context = new StatsContext();
        dataModel = new AdminReservationModel();
        dataModel.loadData(); 
        
        initComponents();
        
        // 기본값 설정
        context.setStrategy(new DailyStrategy());
        updateStats();
    }

    private void initComponents() {
        setTitle("예약 통계 현황");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 1. 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 검색 조건
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        yearCombo = new JComboBox<>(new String[]{"2024", "2025", "2026"});
        yearCombo.setSelectedItem("2025");
        
        semesterCombo = new JComboBox<>(new String[]{"1학기", "하계", "2학기", "동계", "전체"});
        
        typeCombo = new JComboBox<>(new String[]{"일별 현황", "주별 현황", "월별 현황"});
        typeCombo.addActionListener(e -> handleStrategyChange());

        JButton searchBtn = new JButton("조회");
        searchBtn.addActionListener(e -> updateStats());

        leftPanel.add(new JLabel("연도:"));
        leftPanel.add(yearCombo);
        leftPanel.add(new JLabel("학기:"));
        leftPanel.add(semesterCombo);
        leftPanel.add(new JLabel("분석 기준:"));
        leftPanel.add(typeCombo);
        leftPanel.add(searchBtn);
        
        // 시각화 버튼
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton visualBtn = new JButton("시각화 자료 >");
        visualBtn.setBackground(new Color(200, 230, 255));
        
        visualBtn.addActionListener(e -> {
            try {
                visualization.ReservationModel vModel = new visualization.ReservationModel();
                visualization.ReservationController vController = new visualization.ReservationController(vModel);
                visualization.MainView vView = new visualization.MainView(vModel, vController);
                vController.setView(vView);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        rightPanel.add(visualBtn);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 2. 중앙 테이블
        String[] columns = {"구분 (기간)", "예약 건수"};
        tableModel = new DefaultTableModel(columns, 0);
        statsTable = new JTable(tableModel);
        statsTable.setRowHeight(25);
        statsTable.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        add(new JScrollPane(statsTable), BorderLayout.CENTER);

        // 3. 하단 합계
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel(" 총 예약 건수: 0건");
        totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton closeBtn = new JButton("닫기");
        closeBtn.addActionListener(e -> dispose());
        
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(closeBtn, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // [전략 패턴] 전략 교체
    private void handleStrategyChange() {
        String selected = (String) typeCombo.getSelectedItem();
        
        if ("일별 현황".equals(selected)) {
            context.setStrategy(new DailyStrategy());
        } else if ("주별 현황".equals(selected)) {
            context.setStrategy(new WeeklyStrategy()); // [추가됨]
        } else {
            context.setStrategy(new MonthlyStrategy());
        }
        
        updateStats();
    }

    private void updateStats() {
        List<Reservation> allData = dataModel.getAllReservations();
        
        String year = (String) yearCombo.getSelectedItem();
        String semester = (String) semesterCombo.getSelectedItem();
        
        // 학기 필터링 실행
        List<Reservation> filteredData = filterData(allData, year, semester);

        // 전략 실행 (통계 계산)
        Map<String, Integer> result = context.analyze(filteredData);

        // 결과 출력
        tableModel.setRowCount(0);
        int totalCount = 0;
        
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue() + "건"});
            totalCount += entry.getValue();
        }
        
        totalLabel.setText(" 총 예약 건수: " + totalCount + "건");
    }
    
    // 학기별 날짜 필터링 로직
    private List<Reservation> filterData(List<Reservation> data, String year, String semester) {
        List<Reservation> result = new ArrayList<>();
        
        for (Reservation r : data) {
            String date = r.getDate(); // ex) "2025-05-20"
            if (date == null || date.length() < 7) continue;

            // 1. 연도 체크
            if (!date.startsWith(year)) continue;

            // 2. 월(Month) 추출
            int month = Integer.parseInt(date.substring(5, 7));
            boolean match = false;

            // 3. 학기별 월 범위 설정
            switch (semester) {
                case "1학기": // 3, 4, 5, 6월
                    if (month >= 3 && month <= 6) match = true;
                    break;
                case "하계": // 7, 8월
                    if (month >= 7 && month <= 8) match = true;
                    break;
                case "2학기": // 9, 10, 11, 12월
                    if (month >= 9 && month <= 12) match = true;
                    break;
                case "동계": // 1, 2월
                    if (month >= 1 && month <= 2) match = true;
                    break;
                case "전체":
                    match = true;
                    break;
            }
            
            if (match) result.add(r);
        }
        return result;
    }
}