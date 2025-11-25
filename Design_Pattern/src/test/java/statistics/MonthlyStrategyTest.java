package statistics;

import java.util.*;
import management.Reservation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MonthlyStrategyTest {

    @Test
    public void testCalculate() {
        System.out.println("MonthlyStrategy: 월별 통계 계산 테스트");

        // Given
        List<Reservation> data = new ArrayList<>();
        data.add(createRes("2025-05-01(목)"));
        data.add(createRes("2025-05-31(토)")); // 같은 5월
        data.add(createRes("2025-06-01(일)")); // 다른 6월

        MonthlyStrategy strategy = new MonthlyStrategy();

        // When
        Map<String, Integer> result = strategy.calculate(data);

        // Then
        assertEquals(2, result.size());
        assertEquals(2, result.get("2025-05")); // 5월은 2건
        assertEquals(1, result.get("2025-06")); // 6월은 1건
    }

    private Reservation createRes(String date) {
        return new Reservation("id", "type", "name", "dept", "roomType", "room", 
                               date, "day", "time", "time", "purpose", "status");
    }
}