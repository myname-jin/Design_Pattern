/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author adsd3
 */
// blocked_dates.txt 파일 입출력 관리

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ReservationRepositoryModel {
    /** 프로젝트 루트 기준 상대경로 */
    private static final Path FILE_PATH = Paths.get("src", "main", "resources", "blocked_dates.txt");

    /** 한 줄 레코드 저장 */
    public void saveRecord(String type, String date, String roomType, String roomNumber, String timeSlot) {
        try {
            // 파일이 없으면 생성
            if (Files.notExists(FILE_PATH.getParent())) {
                Files.createDirectories(FILE_PATH.getParent());
            }
            if (Files.notExists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
            }
            try (BufferedWriter w = Files.newBufferedWriter(FILE_PATH, StandardOpenOption.APPEND)) {
                w.write(String.join(",", type, date, roomType, roomNumber, timeSlot));
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 전체 레코드 읽기 */
    public List<String> loadAll() {
        try {
            if (Files.notExists(FILE_PATH)) {
                return new ArrayList<>();
            }
            return Files.readAllLines(FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}