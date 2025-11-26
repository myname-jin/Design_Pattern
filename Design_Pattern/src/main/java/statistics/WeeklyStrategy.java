package statistics;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import management.Reservation;

// 구체적인 전략들 (주별) 
public class WeeklyStrategy implements StatsStrategy {
    
    @Override
    public Map<String, Integer> calculate(List<Reservation> data) {
        Map<String, Integer> stats = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (Reservation r : data) {
            try {
                String dateStr = r.getDate();
                if (dateStr.contains("(")) {
                    dateStr = dateStr.split("\\(")[0];
                }
                
                LocalDate date = LocalDate.parse(dateStr.trim(), formatter);
                
                int weekOfYear = date.get(WeekFields.of(Locale.KOREA).weekOfYear());
                
                String key = String.format("%d년 %02d주차", date.getYear(), weekOfYear);
                
                stats.put(key, stats.getOrDefault(key, 0) + 1);
                
            } catch (Exception e) {
                // 날짜 파싱 실패 시 무시
                // System.out.println("날짜 파싱 오류: " + r.getDate());
            }
        }
        return stats;
    }
}