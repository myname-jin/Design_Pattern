/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

import java.util.List;

public class ReservationInfoBuilder extends AbstractReservation {

    private ReservationInfo.Builder builder;

    public ReservationInfoBuilder() {
        this.builder = new ReservationInfo.Builder();
    }

    public ReservationInfoBuilder setUserInfo(String id, String name, String dept) {
        builder.setUserInfo(id, name, dept);
        return this;
    }

    public ReservationInfoBuilder setRoomInfo(String roomName) {
        builder.setRoomInfo(roomName);
        return this;
    }

    public ReservationInfoBuilder setDateAndTimes(String date, List<String> times) {
        builder.setDateAndTimes(date, times);
        return this;
    }

    public ReservationInfoBuilder setPurpose(String purpose) {
        builder.setPurpose(purpose);
        return this;
    }

    public ReservationInfo buildReservation() {
        return builder.build();
    }

    // AbstractReservation 구현
    @Override
    protected boolean isUserBanned(String userId, String userType) {
        // 학생 차단 여부 체크
        if (isUserTypeStudent(userType)) {
            // 기존 StudentReservation 로직 재사용 가능
            return false; // 예제에서는 단순 false
        }
        return false;
    }

    @Override
    protected boolean checkUserConstraints(String userId, String date, List<String> times) {
        // 이미 빌더에서 하루 전, 2시간 이하 체크를 했으므로 그대로 true 반환
        return true;
    }

    @Override
    protected String confirmReservation() {
        return "예약대기";
    }

    @Override
    protected boolean processTimeSlotConflict(String userId, String date, List<String> times, String roomName) {
        return true; // 충돌 없으면 진행
    }
}