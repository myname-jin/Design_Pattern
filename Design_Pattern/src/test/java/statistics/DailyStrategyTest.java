package statistics;

import java.util.*;
import management.Reservation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DailyStrategyTest {

    @Test
    public void testCalculate() {
        System.out.println("DailyStrategy: 일별 통계 계산 테스트");
        
        List<Reservation> data = new ArrayList<>();
        // 11월 24일 데이터 2개, 11월 25일 데이터 1개
        data.add(createRes("2025-11-24(월)"));
        data.add(createRes("2025-11-24(월)"));
        data.add(createRes("2025-11-25(화)"));

        DailyStrategy strategy = new DailyStrategy();

        Map<String, Integer> result = strategy.calculate(data);

        assertEquals(2, result.size()); // 날짜는 2개여야 함
        assertEquals(2, result.get("2025-11-24")); // 24일은 2건
        assertEquals(1, result.get("2025-11-25")); // 25일은 1건
    }
    
    private Reservation createRes(String date) {
        return new Reservation("id", "type", "name", "dept", "roomType", "room", 
                               date, "day", "time", "time", "purpose", "status");
    }
}