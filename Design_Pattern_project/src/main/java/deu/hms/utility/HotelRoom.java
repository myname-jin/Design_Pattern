/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.utility;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jimin
 */

public class HotelRoom implements Serializable {
    private final Map<LocalDate, Boolean> reservations = new HashMap<>();
    private int capacity;
    private int price;
    private String grade;

    // 수용 인원, 가격, 등급을 기본 값으로 설정
    public HotelRoom() {
        this.capacity = 0;
        this.price = 0;
        this.grade = "empty room";
    }

    // 생성자: 수용 인원, 가격, 등급을 초기화
    public HotelRoom(int capacity, int price, String grade) {
        this.capacity = capacity;
        this.price = price;
        this.grade = grade;
    }

    // 예약 가능한지 확인하는 메서드
    public boolean isAvailable(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            return false; // 날짜가 null인 경우 예약 불가
        }

        LocalDate date = checkIn;
        while (date.isBefore(checkOut)) {
            if (reservations.getOrDefault(date, false)) {
                return false; // 이미 예약된 날짜가 있는 경우 예약 불가
            }
            date = date.plusDays(1);
        }
        return true;
    }

    // 예약 처리 메서드
    public boolean reserve(LocalDate checkIn, LocalDate checkOut) {
        if (!isAvailable(checkIn, checkOut)) return false; // 예약 가능 여부 확인

        LocalDate date = checkIn;
        while (date.isBefore(checkOut)) {
            reservations.put(date, true); // 해당 날짜 범위 예약 처리
            date = date.plusDays(1);
        }
        return true; // 예약 성공
    }

    // 체크아웃 처리 메서드
    public void checkout(LocalDate checkIn, LocalDate checkOut) {
        LocalDate date = checkIn;
        while (date.isBefore(checkOut)) {
            reservations.put(date, false); // 체크아웃 시 예약 해제
            date = date.plusDays(1);
        }
    }
    
    // 수용 인원 반환 및 설정 메서드
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    // 가격 반환 및 설정 메서드
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    // 등급 반환 및 설정 메서드
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}