/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author suk22
 */
public class ReservationMgmtDataModel {

    private List<ReservationMgmtModel> reservations;

    public ReservationMgmtDataModel() {
        reservations = new ArrayList<>();
    }

    public void addReservation(ReservationMgmtModel r) {
        reservations.add(r);
    }

    public List<ReservationMgmtModel> getAllReservations() {
        return reservations;
    }

    // 이름, 학번, 강의실로 필터 검색
    public List<ReservationMgmtModel> searchReservations(String name, String studentId, String room) {
        List<ReservationMgmtModel> result = new ArrayList<>();

        for (ReservationMgmtModel r : reservations) {
            boolean match = true;

            if (name != null && !name.isEmpty() && !r.getName().contains(name)) {
                match = false;
            }
            if (studentId != null && !studentId.isEmpty() && !r.getStudentId().contains(studentId)) {
                match = false;
            }
            if (room != null && !room.isEmpty() && !r.getRoom().contains(room)) {
                match = false;
            }

            if (match) {
                result.add(r);
            }
        }

        return result;
    }
}
