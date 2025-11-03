/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

/**
 *
 * @author suk22
 */
public class UserStatsModel {

    private String name;
    private String userId;
    private int reservationCount;
    private int cancelCount;
    private String cancelReason;

    public UserStatsModel(String name, String userId, int reservationCount, int cancelCount, String cancelReason) {
        this.name = name;
        this.userId = userId;
        this.reservationCount = reservationCount;
        this.cancelCount = cancelCount;
        this.cancelReason = cancelReason;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public int getCancelCount() {
        return cancelCount;
    }

    public String getCancelReason() {
        return cancelReason;
    }
}
