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
    private final Map<Integer, Map<Integer, Map<Integer, Map<String, Integer>>>> dataStructure = new TreeMap<>();
    
    // 통합된 예약 파일 경로 (상대 경로)
    private final String fileName = "src/main/resources/RESERVATION.txt"; 

    public ReservationModel() {
        loadData();
    }

    // --- Getter 메서드들 (기존 유지) ---
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

    // --- 데이터 로딩 로직 ---
    private void loadData() {
        File file = new File(fileName); 
        
        // 프로젝트 루트에서도 찾아봄 (경로 호환성)
        if (!file.exists()) file = new File("RESERVATION.txt");

        System.out.println("📂 [시각화] 예약 데이터 로딩: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("⚠️ 예약 파일이 없습니다. (RESERVATION.txt)");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
            System.out.println("✅ 예약 데이터 로딩 및 통계 변환 완료!");
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        }
    }

    // ★ 핵심 수정: 새 포맷(12개 컬럼)에 맞춰 파싱
    private void parseLine(String line) {
        try {
            // 빈 줄이나 짧은 데이터 무시
            if (line == null || line.trim().isEmpty()) return;

            // 콤마로 분리
            String[] parts = line.split(",");
            
            // 데이터 구조: 
            // [0]ID, [1]구분, [2]학년, [3]반, [4]타입, [5]호실, [6]날짜, [7]요일, ...
            // 최소 7개는 있어야 날짜까지 읽음
            if (parts.length < 7) return; 

            String room = parts[5].trim();     // 911, 912 등
            String dateStr = parts[6].trim();  // 2024-03-05

            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
            
            int year = date.getYear();
            int month = date.getMonthValue();
            int week = date.get(WeekFields.of(Locale.KOREA).weekOfMonth());

            // 통계 구조에 집어넣기 (+1 카운트)
            dataStructure.computeIfAbsent(year, k -> new TreeMap<>())
                         .computeIfAbsent(month, k -> new TreeMap<>())
                         .computeIfAbsent(week, k -> new TreeMap<>())
                         .merge(room, 1, Integer::sum);

        } catch (Exception e) {
            // 날짜 형식이 틀리거나 숫자가 아닌 경우 등은 조용히 건너뜀
            // System.out.println("파싱 건너뜀: " + line); 
        }
    }
}