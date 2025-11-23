package statistics;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import management.Reservation;

// 구체적인 전략들 (주별) [SFR-311] 주별 통계 전략
public class WeeklyStrategy implements StatsStrategy {
    
    @Override
    public Map<String, Integer> calculate(List<Reservation> data) {
        // 순서 보장을 위해 TreeMap 사용
        Map<String, Integer> stats = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (Reservation r : data) {
            try {
                // 날짜에서 요일 제거 ("2025-11-24(월)" -> "2025-11-24")
                String dateStr = r.getDate();
                if (dateStr.contains("(")) {
                    dateStr = dateStr.split("\\(")[0];
                }
                
                LocalDate date = LocalDate.parse(dateStr.trim(), formatter);
                
                // 해당 날짜가 그 해의 몇 번째 주인지 계산 (한국 기준)
                int weekOfYear = date.get(WeekFields.of(Locale.KOREA).weekOfYear());
                
                // 키 생성 (예: "2025년 48주차")
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