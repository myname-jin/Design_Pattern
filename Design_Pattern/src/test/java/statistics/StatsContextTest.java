package statistics;

import java.util.*;
import management.Reservation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StatsContextTest {

    @Test
    public void testStrategySwitching() {
        System.out.println("StatsContext: 전략 교체 테스트");

        StatsContext context = new StatsContext();
        List<Reservation> data = new ArrayList<>();
        data.add(new Reservation("id", "type", "name", "dept", "roomType", "room", 
                                 "2025-05-05(월)", "day", "time", "time", "purpose", "status"));

        context.setStrategy(new DailyStrategy());
        Map<String, Integer> dailyResult = context.analyze(data);
        assertTrue(dailyResult.containsKey("2025-05-05"), "일별 전략은 날짜 전체를 키로 사용해야 합니다.");

        context.setStrategy(new MonthlyStrategy());
        Map<String, Integer> monthlyResult = context.analyze(data);
        assertTrue(monthlyResult.containsKey("2025-05"), "월별 전략은 월(Month)까지만 키로 사용해야 합니다.");
    }
    
    @Test
    public void testNoStrategy() {
        // 전략을 설정하지 않았을 때 안전하게 빈 Map을 반환하는지
        StatsContext context = new StatsContext();
        Map<String, Integer> result = context.analyze(new ArrayList<>());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}