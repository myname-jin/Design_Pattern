package statistics;

import java.util.*;
import management.Reservation;

/**
 * [전략 패턴 통합 테스트]
 * 1. 똑같은 예약 데이터 리스트를 준비한다.
 * 2. 컨텍스트(StatsContext)에 '일별', '주별', '월별' 전략을 번갈아 끼운다.
 * 3. analyze()를 실행했을 때, 각각 다른 통계 결과가 나오는지 확인한다.
 */
public class TestStrategyPattern {

    public static void main(String[] args) {
        System.out.println("============== 전략 패턴 통합 테스트 시작 ==============");

        List<Reservation> data = new ArrayList<>();
        data.add(createRes("2025-05-28(수)")); // 5월 5주차
        data.add(createRes("2025-05-29(목)")); // 5월 5주차
        data.add(createRes("2025-05-30(금)")); // 5월 5주차
        data.add(createRes("2025-06-01(일)")); // 6월 1주차 (또는 5월 마지막주)
        data.add(createRes("2025-06-02(월)")); // 6월 1주차

        System.out.println(">> 준비된 데이터: " + data.size() + "건 (2025-05-28 ~ 2025-06-02)");

        StatsContext context = new StatsContext();

        // Case 1: 일별 전략 (DailyStrategy)
        System.out.println("\n[1] 전략 교체: 일별 전략 (DailyStrategy)");
        context.setStrategy(new DailyStrategy());
        
        Map<String, Integer> dailyResult = context.analyze(data);
        printResult(dailyResult);

        // 날짜가 그대로 키가 되어야 함 (5개 날짜가 모두 따로 나와야 함)
        if (dailyResult.size() == 5) {
            System.out.println(">>> 성공: 일별로 데이터가 잘 나뉘었습니다.");
        } else {
            System.out.println(">>> 실패: 데이터 개수가 맞지 않습니다.");
        }

        // Case 2: 주별 전략 (WeeklyStrategy)
        System.out.println("\n[2] 전략 교체: 주별 전략 (WeeklyStrategy)");
        context.setStrategy(new WeeklyStrategy());

        Map<String, Integer> weeklyResult = context.analyze(data);
        printResult(weeklyResult);

        // 5월 말과 6월 초가 주차별로 묶여야 함 (보통 2개 그룹)
        if (weeklyResult.size() >= 2) {
            System.out.println(">>> 성공: 주차별로 데이터가 그룹핑되었습니다.");
        } else {
            System.out.println(">>> 실패: 주차 계산이 이상합니다.");
        }

        // Case 3: 월별 전략 (MonthlyStrategy)
        System.out.println("\n[3] 전략 교체: 월별 전략 (MonthlyStrategy)");
        context.setStrategy(new MonthlyStrategy());

        Map<String, Integer> monthlyResult = context.analyze(data);
        printResult(monthlyResult);

        // 5월과 6월, 딱 2개의 그룹만 나와야 함
        if (monthlyResult.containsKey("2025-05") && monthlyResult.containsKey("2025-06") && monthlyResult.size() == 2) {
            System.out.println(">>> 성공: 월별로 데이터가 묶였습니다.");
        } else {
            System.out.println(">>> 실패: 월별 분류가 틀렸습니다.");
        }

        System.out.println("\n============== 전략 패턴 테스트 종료 ==============");
    }

    private static void printResult(Map<String, Integer> result) {
        System.out.println("   --- 분석 결과 ---");
        for (String key : result.keySet()) {
            System.out.println("   Key: [" + key + "] -> Count: " + result.get(key));
        }
    }

    private static Reservation createRes(String date) {
        // Reservation 생성자가 길어서 날짜만 중요하게 넣고 나머지는 더미값
        return new Reservation("testId", "student", "name", "dept", "roomType", "room", 
                               date, "day", "09:00", "10:00", "study", "approved");
    }
}