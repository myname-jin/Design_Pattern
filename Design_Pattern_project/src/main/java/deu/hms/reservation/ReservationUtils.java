/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

/**
 *
 * @author adsd3
 */
import javax.swing.table.DefaultTableModel;
public class ReservationUtils {
    public static ReservationData createReservationData(
        String uniqueNumber,
        String name,
        String address,
        String phoneNumber,
        String checkInDate,
        String checkOutDate,
        String roomNumber,
        String guestCount,
        String stayCost,
        String paymentMethod,
        String status
        ) {
    return new ReservationData(
        uniqueNumber, name, address, phoneNumber, checkInDate, checkOutDate,
        roomNumber, guestCount, stayCost, paymentMethod,status
    );
}

     public static int addOrUpdateRow(DefaultTableModel model, ReservationData data) {
        // 새로운 행 데이터를 ReservationData 객체로 생성
         Object[] rowData = {
            data.getUniqueNumber(),
            data.getName(),
            data.getAddress(),
            data.getPhoneNumber(),
            data.getCheckInDate(),
            data.getCheckOutDate(),
            data.getRoomNumber(),
            data.getGuestCount(),
            data.getStayCost(),
            data.getPaymentMethod(),
            data.getStatus()
        };

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0) == null || model.getValueAt(i, 0).toString().trim().isEmpty()) {
                for (int j = 0; j < rowData.length; j++) {
                    model.setValueAt(rowData[j], i, j);
                }
                return i;
            }
        }
        // 빈 행이 없다면 새로운 행 추가
        model.addRow(rowData);
        return model.getRowCount() - 1; // 새로 추가된 행의 인덱스 반환
    }

}
