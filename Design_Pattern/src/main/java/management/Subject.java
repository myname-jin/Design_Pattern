package management;

import java.util.ArrayList;
import java.util.List;

// 옵저버 패턴의 'Subject'  역할 - 추상 클래스
public abstract class Subject {
    
    // 관찰자 목록 (protected로 해서 자식이 접근 가능하게 하거나, private + getter 사용)
    private List<ReservationObserver> observers = new ArrayList<>();

    // 구독 신청 (Attach)
    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
    }

    // 구독 취소 (Detach)
    public void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
    }

    // 알림 보내기 (Notify)
    // 자식 클래스(ConcreteSubject)가 이 메서드를 호출해서 알림을 보냄
    public void notifyObservers(List<Reservation> data) {
        for (ReservationObserver observer : observers) {
            observer.onReservationUpdated(data);
        }
    }
}