/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package deu.hms.roomManagement;

/**
 *
 * @author Jimin
 */

public class Room {
    private final int floor; // 층 번호
    private final int roomNumber; // 방 번호
    private int price; // 방 가격
    private String grade; // 방 등급 (Standard, Deluxe, Suite)
    private int capacity; // 방 수용 인원

    public Room(int floor, int roomNumber, int price, String grade, int capacity) {
        this.floor = floor;
        this.roomNumber = roomNumber;
        setPrice(price);
        setGrade(grade);
        setCapacity(capacity);
    }

    public int getFloor() {
        return floor;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getPrice() {
        return price;
    }

    public String getGrade() {
        return grade;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setPrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("가격은 음수일 수 없습니다.");
        }
        this.price = price;
    }

    public void setGrade(String grade) {
        if (grade == null || grade.isEmpty()) {
            throw new IllegalArgumentException("등급은 비어 있을 수 없습니다.");
        }
        this.grade = grade;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("수용 인원은 0보다 커야 합니다.");
        }
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return floor + "," + roomNumber + "," + price + "," + grade + "," + capacity;
    }
}
