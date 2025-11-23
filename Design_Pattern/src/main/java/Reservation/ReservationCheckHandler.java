/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

/**
 *
 * @author namw2
 */
public abstract class ReservationCheckHandler {
    protected ReservationCheckHandler nextHandler;

    public ReservationCheckHandler setNext(ReservationCheckHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler; // 체이닝을 위해 리턴
    }

    // 템플릿 메서드 패턴 적용: 검증 로직 수행 후 다음 단계로 넘김
    public void check(ReservationRequest request) throws Exception {
        if (!validate(request)) {
            // 검증 실패 시 예외를 던져 처리를 중단하고 메시지를 전달
            throw new Exception(getErrorMessage());
        }
        
        // 다음 핸들러가 있다면 계속 진행
        if (nextHandler != null) {
            nextHandler.check(request);
        }
    }

    // 각 구체 클래스가 구현할 검증 로직
    protected abstract boolean validate(ReservationRequest request);
    
    // 실패 시 보여줄 메시지
    protected abstract String getErrorMessage();
}
