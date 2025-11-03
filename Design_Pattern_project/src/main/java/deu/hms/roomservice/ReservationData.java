package deu.hms.roomservice;

import javax.swing.table.DefaultTableModel;

public class ReservationData {
    private DefaultTableModel reservationModel;
    private DefaultTableModel orderModel;
    private String year, month, day, hour, minute, room;
    
    // 생성자
    public ReservationData(DefaultTableModel reservationModel, DefaultTableModel orderModel,
                          String year, String month, String day, 
                          String hour, String minute, String room) {
        this.reservationModel = reservationModel;
        this.orderModel = orderModel;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.room = room;
    }
    
    // Getter 메소드들
    public DefaultTableModel getReservationModel() {
        return reservationModel;
    }
    
    public DefaultTableModel getOrderModel() {
        return orderModel;
    }
    
    public String getYear() {
        return year;
    }
    
    public String getMonth() {
        return month;
    }
    
    public String getDay() {
        return day;
    }
    
    public String getHour() {
        return hour;
    }
    
    public String getMinute() {
        return minute;
    }
    
    public String getRoom() {
        return room;
    }
}