/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author adsd3
 */
// 월간 달력 UI 생성 및 차단된 날짜 표시
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendarView extends JFrame {
    public interface DayDoubleClickHandler {
        void onDoubleClick(LocalDate date);
    }

    private DayDoubleClickHandler handler;
    private final JPanel calendarPanel = new JPanel(new GridLayout(6, 7));
    private YearMonth current = YearMonth.now();

    private JButton backButton;
    private JLabel monthLabel;
    private JButton next;

    public CalendarView() {
        setTitle("강의실 예약 차단 시스템");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 상단 헤더
        JPanel header = new JPanel(new BorderLayout());

        JButton prev = new JButton("<<");
        next = new JButton(">>");

        // 오른쪽: 뒤로가기 + 다음달 버튼
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        backButton = new JButton("뒤로가기");
        backButton.setFocusable(false);
        rightPanel.add(backButton);
        rightPanel.add(next);

        // 중앙: 월 표시 (2025년 5월) 정중앙 정렬
        monthLabel = new JLabel(getMonthLabel());
        monthLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(Box.createHorizontalStrut(100), BorderLayout.WEST);
        centerPanel.add(monthLabel, BorderLayout.CENTER);
        centerPanel.add(Box.createHorizontalStrut(100), BorderLayout.EAST);

        header.add(prev, BorderLayout.WEST);
        header.add(centerPanel, BorderLayout.CENTER);
        header.add(rightPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);

        // 월 이동 기능
        prev.addActionListener(e -> {
            current = current.minusMonths(1);
            monthLabel.setText(getMonthLabel());
            refresh();
        });

        next.addActionListener(e -> {
            current = current.plusMonths(1);
            monthLabel.setText(getMonthLabel());
            refresh();
        });

        refresh();
        setVisible(true);
    }

    public void setDayDoubleClickHandler(DayDoubleClickHandler h) {
        this.handler = h;
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    private String getMonthLabel() {
        return current.getYear() + "년 " +
               current.getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.KOREAN);
    }

    public void refresh() {
        calendarPanel.removeAll();
        int daysInMonth = current.lengthOfMonth();
        List<String> blocked = ReservationServiceModel.staticGetBlockedDates();

        for (int d = 1; d <= daysInMonth; d++) {
            LocalDate date = current.atDay(d);
            JButton btn = new JButton(String.valueOf(d));
            if (blocked.contains(date.toString())) {
                btn.setBackground(Color.RED);
                btn.setOpaque(true);
                btn.setForeground(Color.WHITE);
                btn.setToolTipText("차단된 날짜");
            }

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && handler != null) {
                        handler.onDoubleClick(date);
                    }
                }
            });

            calendarPanel.add(btn);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
}