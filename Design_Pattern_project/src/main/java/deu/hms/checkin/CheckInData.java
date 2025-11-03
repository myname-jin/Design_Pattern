    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package deu.hms.checkin;

/**
 *
 * @author Jimin
 */

public class CheckInData {
    private String uniqueNumber;
    private String name;
    private String phoneNumber;
    private String checkInDate;
    private String checkOutDate;
    private String roomNumber;
    private String guestCount;
    private String stayCost;
    private String paymentMethod;
    private String status;
    private String requestDetails;

    public CheckInData(String uniqueNumber, String name, String phoneNumber, String checkInDate, String checkOutDate, 
                    String roomNumber, String guestCount, String stayCost, String paymentMethod, String status, String requestDetails) {
        this.uniqueNumber = uniqueNumber;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomNumber = roomNumber;
        this.guestCount = guestCount;
        this.stayCost = stayCost;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.requestDetails = requestDetails;
    }

    public String toCSV() {
        return uniqueNumber + "," + name + "," + phoneNumber + "," + checkInDate + "," + checkOutDate + "," + roomNumber + ","
                + guestCount + "," + stayCost + "," + paymentMethod + "," + status + "," + requestDetails;
    }

    // getter 메서드들
    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCheckInDate() {
        return checkInDate; 
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }

    public String getGuestCount() {
        return guestCount;
    }

    public String getStayCost() {
        return stayCost;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestDetails() {
        return requestDetails;
    }
}
