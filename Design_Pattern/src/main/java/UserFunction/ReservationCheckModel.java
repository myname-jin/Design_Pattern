/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;

/**
 *
 * @author namw2
 */
public class ReservationCheckModel {
    private String Year;
    private String Month;
    private String Day;
    private String RoomType;
    private String RoomNum;

    public ReservationCheckModel(String Year, String Month, String Day, String RoomType, String RoomNum) {
        this.Year = Year;
        this.Month = Month;
        this.Day = Day;
        this.RoomType = RoomType;
        this.RoomNum = RoomNum;
    }

    public String getYear() {
        return Year;
    }

    public String getMonth() {
        return Month;
    }

    public String getDay() {
        return Day;
    }

    public String getRoomType() {
        return RoomType;
    }

    public String getRoomNum() {
        return RoomNum;
    }
    
    @Override
    public String toString() {
        return Year + "," + Month + "," + Day + "," + RoomType + "," + RoomNum;
    }
            
            
            
            
}
