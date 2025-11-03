/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author adsd3
 */
// 예약 차단·해제 및 조회 비즈니스 로직
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class ReservationServiceModel {
    private final ReservationRepositoryModel repo;

    public ReservationServiceModel(ReservationRepositoryModel repo) {
        this.repo = repo;
    }

    /** 전체 차단 */
    public void blockAll(String date) {
        repo.saveRecord("모두", date, "", "", "");
    }

    /** 일부 차단 */
    public void blockPartial(String date, String roomType, String roomNumber, String timeSlot) {
        repo.saveRecord("일부", date, roomType, roomNumber, timeSlot);
    }

    /** 차단 해제 */
    public void unblock(String date) {
        List<String> keep = repo.loadAll().stream()
            .filter(l -> !l.startsWith("모두," + date + ",") && !l.contains("," + date + ","))
            .collect(Collectors.toList());

        Path path = Paths.get("src", "main", "resources", "blocked_dates.txt");
        try {
            // 덮어쓰기 모드
            if (Files.notExists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, keep, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 차단된 날짜 목록 반환 */
    public List<String> getBlockedDates() {
        return repo.loadAll().stream()
            .map(l -> l.split(",")[1])
            .distinct()
            .collect(Collectors.toList());
    }

    /** 특정 강의실·호수의 차단된 시간대 반환 */
    public List<String> getBlockedTimeSlots(String date, String roomType, String roomNumber) {
        return repo.loadAll().stream()
            .filter(l -> l.contains(date + "," + roomType + "," + roomNumber + ","))
            .map(l -> l.split(",")[4])
            .distinct()
            .collect(Collectors.toList());
    }

    /** 전체 차단 여부 반환 */
    public boolean isFullyBlocked(String date) {
        return repo.loadAll().stream()
            .anyMatch(l -> l.startsWith("모두," + date + ","));
    }

    /** static 헬퍼 (뷰에서 바로 호출용) */
    public static List<String> staticGetBlockedDates() {
        return new ReservationServiceModel(new ReservationRepositoryModel()).getBlockedDates();
    }
}