/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

import java.util.List;

/**
 *
 * @author namw2
 */
public class ReservationRequest {
    private String userId;
    private String userType;
    private String date;
    private List<String> times;
    private String selectedRoomName;
    private String purpose;
    // ... 생성자 및 Getter 메서드 ...
    
    public ReservationRequest(String userId, String date, String selectedRoomName, List<String> times) {
        this.userId = userId;
        this.date = date;
        this.selectedRoomName = selectedRoomName;
        this.times = times;
    }
    public ReservationRequest(String userId, String userType, String date, List<String> times, String selectedRoomName, String purpose) {
        this.userId = userId;
        this.userType = userType;
        this.date = date;
        this.times = times;
        this.selectedRoomName = selectedRoomName;
        this.purpose = purpose;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getDate() {
        return date;
    }

    public List<String> getTimes() {
        return times;
    }

    public String getSelectedRoomName() {
        return selectedRoomName;
    }

    public String getPurpose() {
        return purpose;
    }
    
    
    
}
