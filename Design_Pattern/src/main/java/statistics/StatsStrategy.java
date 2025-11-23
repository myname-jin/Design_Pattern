package statistics;

import java.util.List;
import java.util.Map;
import management.Reservation;

// 전략들의 공통 조상 (인터페이스)
public interface StatsStrategy {
    /**
     * 예약 리스트를 받아서, 기준에 따라 분류한 뒤 개수를 셈.
     * @param data 전체 예약 데이터
     * @return Map<기준(날짜/주/월), 횟수>
     */
    Map<String, Integer> calculate(List<Reservation> data);
}