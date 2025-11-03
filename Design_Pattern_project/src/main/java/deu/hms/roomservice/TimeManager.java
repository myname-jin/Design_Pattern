package deu.hms.roomservice;

import java.util.Calendar;
import javax.swing.JSpinner;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

public class TimeManager {
    // 멤버 변수 선언
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    
    // 생성자
    public TimeManager() {
        this.calendar = Calendar.getInstance();
        initializeTime();
    }
    
    // Getter/Setter 메소드
    public Calendar getCalendar() {
        return calendar;
    }
    
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        initializeTime();
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
    
    public int getDay() {
        return day;
    }
    
    public void setDay(int day) {
        this.day = day;
    }
    
    public int getHour() {
        return hour;
    }
    
    public void setHour(int hour) {
        this.hour = hour;
    }
    
    public int getMinute() {
        return minute;
    }
    
    public void setMinute(int minute) {
        this.minute = minute;
    }
    
    // 현재 날짜/시간 초기화 메소드
    public void initCurrentDateTime(JSpinner yearSpinner, JSpinner monthSpinner, 
                                  JSpinner daySpinner, JSpinner hourSpinner, 
                                  JSpinner minuteSpinner) {
        try {
            initializeSpinnerModels(yearSpinner, monthSpinner, daySpinner, 
                                  hourSpinner, minuteSpinner);
            setSpinnerValues(yearSpinner, monthSpinner, daySpinner, 
                           hourSpinner, minuteSpinner);
            setSpinnerEditors(yearSpinner);
        } catch (Exception e) {
            showError("날짜/시간 초기화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 내부 헬퍼 메소드들
    private void initializeTime() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }
    
    private void initializeSpinnerModels(JSpinner yearSpinner, JSpinner monthSpinner, 
                                       JSpinner daySpinner, JSpinner hourSpinner, 
                                       JSpinner minuteSpinner) {
        if (validateSpinners(yearSpinner, monthSpinner, daySpinner, 
                           hourSpinner, minuteSpinner)) {
            yearSpinner.setModel(new SpinnerNumberModel(year, 2000, 3000, 1));
            monthSpinner.setModel(new SpinnerNumberModel(month, 1, 12, 1));
            daySpinner.setModel(new SpinnerNumberModel(day, 1, 31, 1));
            hourSpinner.setModel(new SpinnerNumberModel(hour, 0, 23, 1));
            minuteSpinner.setModel(new SpinnerNumberModel(minute, 0, 59, 1));
        }
    }
    
    private void setSpinnerValues(JSpinner yearSpinner, JSpinner monthSpinner, 
                                JSpinner daySpinner, JSpinner hourSpinner, 
                                JSpinner minuteSpinner) {
        yearSpinner.setValue(year);
        monthSpinner.setValue(month);
        daySpinner.setValue(day);
        hourSpinner.setValue(hour);
        minuteSpinner.setValue(minute);
    }
    
    private void setSpinnerEditors(JSpinner yearSpinner) {
        JSpinner.NumberEditor yearEditor = new JSpinner.NumberEditor(yearSpinner, "#");
        yearSpinner.setEditor(yearEditor);
    }
    
    private boolean validateSpinners(JSpinner... spinners) {
        for (JSpinner spinner : spinners) {
            if (spinner == null) {
                showError("스피너가 초기화되지 않았습니다.");
                return false;
            }
        }
        return true;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "오류", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    // 날짜 유효성 검사 메소드
    public boolean isValidDate(int year, int month, int day) {
        try {
            calendar.setLenient(false);
            calendar.set(year, month - 1, day);
            calendar.getTime();
            return true;
        } catch (Exception e) {
            showError("유효하지 않은 날짜입니다.");
            return false;
        }
    }
    
    // 시간 유효성 검사 메소드
    public boolean isValidTime(int hour, int minute) {
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            showError("유효하지 않은 시간입니다.");
            return false;
        }
        return true;
    }
    
    // 현재 시간을 문자열로 반환하는 메소드
    public String getCurrentTimeAsString() {
        return String.format("%04d-%02d-%02d %02d:%02d", 
                           year, month, day, hour, minute);
    }
}