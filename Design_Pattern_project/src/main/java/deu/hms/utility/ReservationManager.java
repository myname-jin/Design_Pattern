/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.utility;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jimin
 */
public class ReservationManager {
    private final List<HotelFloor> floors;
    private static final String FILE_NAME = "roomInfo.txt";

    // 생성자: 층 수와 층당 객실 수를 받아 초기화
    public ReservationManager(int numFloors, int roomsPerFloor) {
        floors = new ArrayList<>();
        for (int i = 0; i < numFloors; i++) {
            floors.add(new HotelFloor(roomsPerFloor));
        }
        loadReservations();
    }

    // 특정 층의 특정 방 예약 가능 여부 확인
    public boolean isRoomAvailable(int floor, int room, LocalDate checkIn, LocalDate checkOut) {
        return floors.get(floor).isRoomAvailable(room, checkIn, checkOut);
    }

    // 특정 층의 특정 방 예약 처리
    public boolean reserveRoom(int floor, int room, LocalDate checkIn, LocalDate checkOut) {
        boolean success = floors.get(floor).reserveRoom(room, checkIn, checkOut);
        if (success) saveReservations();
        return success;
    }

    // 특정 층 가져오기
    public HotelFloor getFloor(int floorIndex) {
        if (floorIndex >= 0 && floorIndex < floors.size()) {
            return floors.get(floorIndex);
        }
        throw new IllegalArgumentException("Invalid floor index: " + floorIndex);
    }
    
    // 층 리스트를 반환하는 메서드 추가
    public List<HotelFloor> getFloors() {
        return floors;
    }
    
    // 특정 방의 가격, 등급, 수용 인원을 설정하는 메서드 추가
    public void setRoomInfo(int floor, int room, int price, String grade, int capacity) {
        if (floor >= 0 && floor < floors.size()) {
            HotelFloor currentFloor = floors.get(floor);
            if (room >= 0 && room < currentFloor.getRooms().size()) {
                System.out.println("Setting room info: floor=" + (floor + 1) + ", room=" + (room + 1) + ", price=" + price + ", grade=" + grade + ", capacity=" + capacity);
                currentFloor.setRoomInfo(room, price, grade, capacity);
            }
        } 
    }

    // 예약 정보 저장 메서드
    private void saveReservations() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(floors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 예약 정보 로드 메서드
    @SuppressWarnings("unchecked")
    private void loadReservations() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            List<HotelFloor> loadedFloors = (List<HotelFloor>) in.readObject();
            for (int i = 0; i < floors.size(); i++) {
                floors.set(i, loadedFloors.get(i));
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous reservations found. Starting fresh.");
        }
    }
}