/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

import java.util.UUID; // UUID를 사용하기 위해 추가
import java.io.IOException; 
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author adsd3
 */

public class ReservationData {
    private String uniqueNumber; // String 타입으로 변경
    private String name;
    private String address;
    private String phoneNumber;
    private String checkInDate;
    private String checkOutDate;
    private String roomNumber;
    private String guestCount;
    private String stayCost;
    private String paymentMethod;
    private String status;

public ReservationData(String name, String address, String phoneNumber,
                       String checkInDate, String checkOutDate, String roomNumber,
                       String guestCount, String stayCost, String paymentMethod,
                      String status) {
    this.uniqueNumber = UUID.randomUUID().toString(); // 고유번호 생성
    this.name = name;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.roomNumber = roomNumber;
    this.guestCount = guestCount;
    this.stayCost = stayCost;
    this.paymentMethod = paymentMethod;
    this.status = status;
}

// 기존 데이터 로드용 생성자
public ReservationData(String uniqueNumber, String name, String address, String phoneNumber,
                       String checkInDate, String checkOutDate, String roomNumber,
                       String guestCount, String stayCost, String paymentMethod,
                       String status ) {
    this.uniqueNumber = uniqueNumber; // 기존 고유번호 유지
    this.name = name;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.roomNumber = roomNumber;
    this.guestCount = guestCount;
    this.stayCost = stayCost;
    this.paymentMethod = paymentMethod;
    this.status = status;

}

// txt파일 수정할 때 사용하는 코드 데이터를 csv형태로 변환
    public String toCSV() {
    return uniqueNumber + "," +
           name + "," +
           address + "," +
           phoneNumber + "," +
           checkInDate + "," +
           checkOutDate + "," +
           roomNumber + "," +
           guestCount + "," +
           stayCost + "," +
           paymentMethod + "," +
           status;
         
}
private void updateFileStatus(DefaultTableModel model, int rowIndex, String newStatus) {
    try {
        String uniqueNumber = (String) model.getValueAt(rowIndex, 0); // 고유번호 가져오기

        // rowData를 ReservationData 객체로 변환
        ReservationData data = new ReservationData(
            uniqueNumber,
            (String) model.getValueAt(rowIndex, 1),
            (String) model.getValueAt(rowIndex, 2),
            (String) model.getValueAt(rowIndex, 3),
            (String) model.getValueAt(rowIndex, 4),
            (String) model.getValueAt(rowIndex, 5),
            (String) model.getValueAt(rowIndex, 6),
            (String) model.getValueAt(rowIndex, 7),
            (String) model.getValueAt(rowIndex, 8),
            (String) model.getValueAt(rowIndex, 9),
            newStatus // 상태 업데이트
        );
        
        // 수정된 데이터를 파일에 반영
        FileManager.updateInFile(data, "Reservation.txt");
    } catch (IOException e) {
        System.err.println("파일 업데이트 중 오류 발생: " + e.getMessage());
    }
}

public ReservationData() { //초기화과정
    // 필요한 경우 기본값 설정
    this.uniqueNumber = "";
    this.name = "";
    this.address = "";
    this.phoneNumber = "";
    this.checkInDate = "";
    this.checkOutDate = "";
    this.roomNumber = "";
    this.guestCount = "";
    this.stayCost = "";
    this.paymentMethod = "";
    this.status = "";
}
public void updateDates(String checkInDate, String checkOutDate) {
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
}





    // Getters and setters (필수)
    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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

    public void setStatus(String status) {
    this.status = status;
    }
    
    public void setUniqueNumber(String uniqueNumber) { //고유번호 setter
        this.uniqueNumber = uniqueNumber;
    }
}
