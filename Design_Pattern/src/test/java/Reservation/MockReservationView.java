/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

/**
 *
 * @author namw2
 */

//출력을 위한 가짜 뷰 클래스 
public class MockReservationView extends ReservationView{
    public String lastMessage = "";

    @Override
    public void showMessage(String message) {
        this.lastMessage = message;
        System.out.println("[Test View Log]: " + message);
    }
}
