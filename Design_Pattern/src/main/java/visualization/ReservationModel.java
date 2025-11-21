package visualization;

import java.io.*;
import java.nio.charset.StandardCharsets; // 인코딩 추가
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

public class ReservationModel {
    // 연도 -> 월 -> 주차 -> {강의실이름 : 예약횟수}
    private final Map<Integer, Map<Integer, Map<Integer, Map<String, Integer>>>> dataStructure = new TreeMap<>();
    
    // [수정] 파일명 소문자로 통일 (reservation.txt) 및 경로 설정
    private final String fileName = "src/main/resources/reservation.txt"; 

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

    // --- 데이터 로딩 로직 (UTF-8 적용) ---
    private void loadData() {
        File file = new File(fileName); 
        
        if (!file.exists()) {
            System.out.println("⚠️ 예약 파일이 없습니다: " + file.getAbsolutePath());
            return;
        }

        // [수정] 한글 깨짐 방지 (UTF-8)
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
            System.out.println("✅ [시각화] 데이터 로딩 완료 (" + fileName + ")");
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        }
    }

    // [수정] 12개 컬럼 구조에 맞춰 파싱
    private void parseLine(String line) {
        try {
            if (line == null || line.trim().isEmpty()) return;

            String[] parts = line.split(",");
            
            // [중요] 12개 컬럼 구조
            // [0]학번 [1]구분 [2]이름 [3]학과 [4]타입 [5]호실 [6]날짜 ...
            
            // 최소 7개 이상이어야 날짜까지 읽을 수 있음
            if (parts.length < 7) return; 

            String room = parts[5].trim();      // 6번째: 호실 (예: 911)
            String dateStr = parts[6].trim();   // 7번째: 날짜 (예: 2024-03-05)

            // 날짜 파싱 (YYYY-MM-DD)
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
            // 날짜 형식이 아니거나 깨진 데이터는 무시
        }
    }
}