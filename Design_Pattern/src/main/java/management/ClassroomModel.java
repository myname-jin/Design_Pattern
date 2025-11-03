/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

/**
 *
 * @author suk22
 */
public class ClassroomModel {

    private String room;
    private String location;
    private String capacity;
    private String note;

    public ClassroomModel(String room, String location, String capacity, String note) {
        this.room = room;
        this.location = location;
        this.capacity = capacity;
        this.note = note;
    }

    public String getRoom() {
        return room;
    }

    public String getLocation() {
        return location;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getNote() {
        return note;
    }

    public String toFileString() {
        return room + "," + location + "," + capacity + "," + note;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
