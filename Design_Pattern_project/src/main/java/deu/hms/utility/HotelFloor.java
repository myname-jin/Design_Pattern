/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.utility;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jimin
 */
public class HotelFloor implements Serializable {
    private final List<HotelRoom> rooms;

    // 생성자: 층당 방의 수를 받아 초기화
    public HotelFloor(int roomsPerFloor) {
        rooms = new ArrayList<>();
        for (int i = 0; i < roomsPerFloor; i++) {
            rooms.add(new HotelRoom());
        }
    }

    // 특정 방 가져오기
    public HotelRoom getRoom(int roomIndex) {
        if (roomIndex >= 0 && roomIndex < rooms.size()) {
            return rooms.get(roomIndex);
        }
        throw new IllegalArgumentException("Invalid room index: " + roomIndex);
    }

    // 특정 방 예약 가능 여부 확인
    public boolean isRoomAvailable(int roomIndex, LocalDate checkIn, LocalDate checkOut) {
        return getRoom(roomIndex).isAvailable(checkIn, checkOut);
    }

    // 특정 방 예약 처리
    public boolean reserveRoom(int roomIndex, LocalDate checkIn, LocalDate checkOut) {
        return getRoom(roomIndex).reserve(checkIn, checkOut);
    }

    // 특정 방의 가격, 등급, 수용 인원을 설정하는 메서드
    public void setRoomInfo(int roomIndex, int price, String grade, int capacity) {
        HotelRoom room = getRoom(roomIndex);
        room.setPrice(price);
        room.setGrade(grade);
        room.setCapacity(capacity);
    }

    // 방 리스트 반환 메서드
    public List<HotelRoom> getRooms() {
        return rooms;
    }
}
