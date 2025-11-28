package management;

import java.util.List;

public interface ReservationObserver {
    void onReservationUpdated(List<Reservation> reservationList);
}