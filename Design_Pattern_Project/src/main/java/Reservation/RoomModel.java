/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

/**
 *
 * @author scq37
 */
public class RoomModel {
      private String name;    //강의실 이름
    private String type;            //강의실 번호
    private String[] availableTimes;        //예약가능 시간
    
    /*
    public Room() {
        this.name = "강의실";
        this.type
        this()
    }
    */
    public RoomModel(String name, String type, String[] availableTimes) {
        this.name = name;
        this.type = type;
        this.availableTimes = availableTimes;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String[] getAvailableTimes() {
        return availableTimes;
    }
}
