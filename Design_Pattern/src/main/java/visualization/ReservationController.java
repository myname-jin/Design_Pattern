/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

import java.util.*;

public class ReservationController {
    private final ReservationModel model;
    private MainView view;
    
    private int currentYear;
    private int currentMonth;

    public ReservationController(ReservationModel model) {
        this.model = model;
    }

    public void setView(MainView view) {
        this.view = view;
        loadYears(); 
    }

    private void loadYears() {
        Map<Integer, Integer> yearData = new TreeMap<>();
        for(int y : model.getYears()) yearData.put(y, model.getYearTotal(y));
        view.yearPanel.setData(yearData, "년도별 예약 현황 (클릭하여 상세조회)");
        view.showCard("YEAR");
    }

    public void handleChartClick(ChartPanel.Type type, int mouseX, Map<Integer, Object> clickMap) {
        Object selectedKey = null;
        for (int x : clickMap.keySet()) {
            if (mouseX >= x && mouseX <= x + 60) { 
                selectedKey = clickMap.get(x);
                break;
            }
        }
        if (selectedKey == null) return;

        switch (type) {
            case YEAR:
                this.currentYear = (int) selectedKey;
                view.monthPanel.setData(model.getMonths(currentYear), currentYear + "년 월별 통계");
                view.showCard("MONTH");
                break;
            case MONTH:
                this.currentMonth = (int) selectedKey;
                view.weekPanel.setData(model.getWeeks(currentYear, currentMonth), currentMonth + "월 주차별 통계");
                view.showCard("WEEK");
                break;
            case WEEK:
                int week = (int) selectedKey;
                Map<String, Integer> roomData = model.getRoomStats(currentYear, currentMonth, week);
                view.roomPanel.setData(roomData, week + "주차 강의실별 상세 (데코레이터 적용)");
                view.showCard("ROOM");
                break;
            case ROOM:
                break;
        }
    }

    public void handleBack() {
        loadYears(); 
    }
}