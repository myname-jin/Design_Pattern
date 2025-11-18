package management;

import java.util.List;

public interface ReservationObserver {
    // 데이터가 변경되었을 때 호출되는 메서드
    void onReservationUpdated(List<Reservation> reservationList);
}