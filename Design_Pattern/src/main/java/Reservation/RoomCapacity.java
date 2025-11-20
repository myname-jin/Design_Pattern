/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

import java.util.HashMap;
import java.util.Map;

public class RoomCapacity {

    // 🔥 reordering 방지 + thread-safe single instance 보장
    private static volatile RoomCapacity instance;

    private final Map<String, Integer> roomCapacity;
    private final Map<String, Integer> currentReservations;

    private RoomCapacity() {
        roomCapacity = new HashMap<>();
        currentReservations = new HashMap<>();

        roomCapacity.put("911", 40);
        roomCapacity.put("915", 40);
        roomCapacity.put("916", 40);
        roomCapacity.put("918", 40);
    }

    // 🔥 Double-checked locking
    public static RoomCapacity getInstance() {
        if (instance == null) {  // 1st check (lock 없이)
            synchronized (RoomCapacity.class) {
                if (instance == null) {  // 2nd check (lock 안에서)
                    instance = new RoomCapacity();
                }
            }
        }
        return instance;
    }

    private String makeKey(String roomName, String date, String startTime, String endTime) {
        return roomName + "|" + date + "|" + startTime + "|" + endTime;
    }

    // 예약 가능 여부 확인 (전체정원 50% 이하)
    public synchronized boolean canReserve(String roomName, String date, String startTime, String endTime) {
        String key = makeKey(roomName, date, startTime, endTime);

        int total = roomCapacity.getOrDefault(roomName, 40);
        int current = currentReservations.getOrDefault(key, 0);

        return current < (total / 2) + 1;
    }

    // 예약 카운트 증가
    public synchronized void addReservation(String roomName, String date, String startTime, String endTime) {
        String key = makeKey(roomName, date, startTime, endTime);
        int current = currentReservations.getOrDefault(key, 0);
        currentReservations.put(key, current + 1);
    }

    // 예약 취소 시 카운트 감소
    public synchronized void cancelReservation(String roomName, String date, String startTime, String endTime) {
        String key = makeKey(roomName, date, startTime, endTime);
        int current = currentReservations.getOrDefault(key, 0);

        if (current > 0) {
            currentReservations.put(key, current - 1);
        }
    }
}
