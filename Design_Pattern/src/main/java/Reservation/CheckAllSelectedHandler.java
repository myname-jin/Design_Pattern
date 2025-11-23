/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

/**
 *
 * @author namw2
 */
public class CheckAllSelectedHandler extends ReservationCheckHandler{

    @Override
    protected boolean validate(ReservationRequest request) {
        // 기존 checkAllSelected 로직 이동
        if (request.getDate().isEmpty() || request.getPurpose().isEmpty() || request.getSelectedRoomName() == null) {
        return false;
        }
        else return true;
    }

    @Override
    protected String getErrorMessage() {
        return "모든 항목을 입력해주세요.";
    }
    
}
