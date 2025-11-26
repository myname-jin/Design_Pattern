package statistics;

import java.util.*;
import management.Reservation;


//구체적인 전략들 (월별)
public class MonthlyStrategy implements StatsStrategy {
    @Override
    public Map<String, Integer> calculate(List<Reservation> data) {
        Map<String, Integer> stats = new TreeMap<>();
        
        for (Reservation r : data) {
            String dateKey = r.getDate();
            if (dateKey.length() >= 7) {
                String monthKey = dateKey.substring(0, 7); 
                stats.put(monthKey, stats.getOrDefault(monthKey, 0) + 1);
            }
        }
        return stats;
    }
}