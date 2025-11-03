/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package deu.hms.roomManagement;

/**
 *
 * @author Jimin
 */

public class RoomService {
    private final RoomRepository roomRepository;

    // 생성자
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // 객실 추가
    public void addRoom(Room room) {
        roomRepository.addRoom(room);
    }

    // 객실 삭제
    public void deleteRoom(int floor, int roomNumber) {
        roomRepository.deleteRoom(floor, roomNumber);
    }

    // 객실 수정
    public void updateRoom(Room room, int newPrice, String newGrade, int newCapacity) {
        roomRepository.updateRoom(room, newPrice, newGrade, newCapacity);
    }

    // RoomRepository 반환
    public RoomRepository getRoomRepository() {
        return roomRepository;
    }
}
