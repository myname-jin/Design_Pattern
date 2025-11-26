package statistics;

import java.util.*;
import management.Reservation;


// 구체적인 전략들 (일별)
public class DailyStrategy implements StatsStrategy {
    @Override
    public Map<String, Integer> calculate(List<Reservation> data) {
        Map<String, Integer> stats = new TreeMap<>();
        
        for (Reservation r : data) {
            String dateKey = r.getDate();
            if (dateKey.contains("(")) {
                dateKey = dateKey.split("\\(")[0];
            }
            
            stats.put(dateKey, stats.getOrDefault(dateKey, 0) + 1);
        }
        return stats;
    }
}