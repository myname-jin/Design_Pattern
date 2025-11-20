    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

public class ReservationModel {
    // 데이터 구조: 년 -> 월 -> 주 -> 호실 -> 횟수
    private final Map<Integer, Map<Integer, Map<Integer, Map<String, Integer>>>> dataStructure = new TreeMap<>();
    
    private final String fileName = "src/main/resources/visualization.txt";

    public ReservationModel() {
        loadData();
    }

    // --- Getter 메서드들 (그대로 유지) ---
    public Set<Integer> getYears() { return dataStructure.keySet(); }
    
    public Map<Integer, Integer> getMonths(int year) {
        Map<Integer, Integer> result = new TreeMap<>();
        if (dataStructure.containsKey(year)) {
            for (var entry : dataStructure.get(year).entrySet()) {
                int month = entry.getKey();
                int count = entry.getValue().values().stream()
                        .flatMap(w -> w.values().stream())
                        .mapToInt(Integer::intValue).sum();
                result.put(month, count);
            }
        }
        return result;
    }

    public Map<Integer, Integer> getWeeks(int year, int month) {
        Map<Integer, Integer> result = new TreeMap<>();
        if (dataStructure.containsKey(year) && dataStructure.get(year).containsKey(month)) {
            for (var entry : dataStructure.get(year).get(month).entrySet()) {
                int week = entry.getKey();
                int count = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
                result.put(week, count);
            }
        }
        return result;
    }

    public Map<String, Integer> getRoomStats(int year, int month, int week) {
        if (dataStructure.containsKey(year) && dataStructure.get(year).containsKey(month) 
            && dataStructure.get(year).get(month).containsKey(week)) {
            return dataStructure.get(year).get(month).get(week);
        }
        return new HashMap<>();
    }

    public int getYearTotal(int year) {
        if (!dataStructure.containsKey(year)) return 0;
        return dataStructure.get(year).values().stream()
                .flatMap(m -> m.values().stream())
                .flatMap(w -> w.values().stream())
                .mapToInt(Integer::intValue).sum();
    }
    // ---------------------------------------

    // ★ 핵심: 로컬 파일 읽기 로직으로 복구됨
    private void loadData() {
        // 프로젝트 루트 폴더에서 visualization.txt를 찾습니다.
        File file = new File(fileName); 

        System.out.println(" [Client] 시각화 데이터 파일 로딩 시도: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("️ 파일이 없어서 기본(테스트) 데이터를 사용합니다.");
            initializeWithDefaultData();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
            System.out.println(" [Client] 로컬 데이터 파일 로딩 성공!");
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
            initializeWithDefaultData();
        }
    }

    private void parseLine(String line) {
        try {
            String[] parts = line.split(",");
            LocalDate date = LocalDate.parse(parts[0].trim(), DateTimeFormatter.ISO_DATE);
            String room = parts[1].trim();

            int year = date.getYear();
            int month = date.getMonthValue();
            int week = date.get(WeekFields.of(Locale.KOREA).weekOfMonth());

            dataStructure.computeIfAbsent(year, k -> new TreeMap<>())
                         .computeIfAbsent(month, k -> new TreeMap<>())
                         .computeIfAbsent(week, k -> new TreeMap<>())
                         .merge(room, 1, Integer::sum);
        } catch (Exception ignored) {}
    }

    private void initializeWithDefaultData() {
        // 비상용 더미 데이터
        String[] dummy = {
            "2024-03-05,911", "2024-03-06,912", "2024-03-12,913", "2024-04-01,915",
            "2025-01-10,918", "2025-01-12,916"
        };
        for (String s : dummy) parseLine(s);
    }
}