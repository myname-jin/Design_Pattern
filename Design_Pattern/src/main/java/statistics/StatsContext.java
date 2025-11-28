package statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import management.Reservation;

// [Context] 전략을 보유하고 실행하는 객체
public class StatsContext {
    
    private StatsStrategy strategy; // 현재 사용 중인 전략 

    // 전략 교체 (Setter)
    public void setStrategy(StatsStrategy strategy) {
        this.strategy = strategy;
    }

    // 전략 실행 (Delegate)
    public Map<String, Integer> analyze(List<Reservation> data) {
        if (strategy == null) {
            return new HashMap<>();
        }
        // 구체적인 알고리즘은 전략 객체에게 위임함
        return strategy.calculate(data);
    }
}