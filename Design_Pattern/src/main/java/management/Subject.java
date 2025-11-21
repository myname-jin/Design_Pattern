package management;

import java.util.List;

// 옵저버 패턴의 주체(Subject) - 추상 클래스
// 구현은 자식(ConcreteSubject)에게 위임함
public abstract class Subject {
    
    // 추상 메서드: 자식이 반드시 구현해야 함
    public abstract void addObserver(ReservationObserver observer);
    public abstract void removeObserver(ReservationObserver observer);
    public abstract void notifyObservers(List<Reservation> data);
}