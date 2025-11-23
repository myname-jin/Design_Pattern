package statistics;

import java.util.*;
import management.Reservation;


// 구체적인 전략들 (일별)
public class DailyStrategy implements StatsStrategy {
    @Override
    public Map<String, Integer> calculate(List<Reservation> data) {
        // 날짜순 정렬을 위해 TreeMap 사용
        Map<String, Integer> stats = new TreeMap<>();
        
        for (Reservation r : data) {
            // 날짜 형식: "2025-11-24(월)" -> "2025-11-24" 추출
            String dateKey = r.getDate();
            if (dateKey.contains("(")) {
                dateKey = dateKey.split("\\(")[0];
            }
            
            // 해당 날짜의 카운트 + 1
            stats.put(dateKey, stats.getOrDefault(dateKey, 0) + 1);
        }
        return stats;
    }
}