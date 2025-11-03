/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author adsd3
 */
import java.time.LocalDate;

public class ReservationModel {
    private String type;
    private LocalDate date;
    private String roomType;
    private String roomNumber;
    private int slot;

    public ReservationModel(String type, LocalDate date, String roomType, String roomNumber, int slot) {
        this.type = type;
        this.date = date;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.slot = slot;
    }

    public String getType() { return type; }
    public LocalDate getDate() { return date; }
    public String getRoomType() { return roomType; }
    public String getRoomNumber() { return roomNumber; }
    public int getSlot() { return slot; }
}