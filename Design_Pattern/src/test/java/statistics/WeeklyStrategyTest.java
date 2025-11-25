package statistics;

import java.util.*;
import management.Reservation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeeklyStrategyTest {

    @Test
    public void testCalculate() {
        System.out.println("WeeklyStrategy: 주별 통계 계산 테스트");

        List<Reservation> data = new ArrayList<>();
        // 2025년 5월 21일, 22일 -> 같은 주차
        data.add(createRes("2025-05-21(수)"));
        data.add(createRes("2025-05-22(목)"));
        // 2025년 6월 1일 -> 다른 주차
        data.add(createRes("2025-06-01(일)"));

        WeeklyStrategy strategy = new WeeklyStrategy();

        // When
        Map<String, Integer> result = strategy.calculate(data);

        // 키값이 정확히 몇 주차인지 계산하기보다, 그룹이 2개로 나뉘었는지 확인
        assertEquals(2, result.size()); 
        
        // 날짜 파싱 오류가 있는 데이터 처리 확인
        data.add(createRes("잘못된날짜"));
        Map<String, Integer> errorResult = strategy.calculate(data);
        assertEquals(2, errorResult.size(), "잘못된 날짜는 무시되어야 합니다.");
    }

    private Reservation createRes(String date) {
        return new Reservation("id", "type", "name", "dept", "roomType", "room", 
                               date, "day", "time", "time", "purpose", "status");
    }
}