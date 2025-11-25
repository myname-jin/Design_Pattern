package management;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UserStatsModel 단위 테스트
 * (통계 데이터 객체의 생성 및 조회 로직 검증)
 */
public class UserStatsModelTest {

    private UserStatsModel instance; 

    @BeforeEach
    public void setUp() {
        instance = new UserStatsModel("홍길동", "20230001", 10, 3, "개인 사정; 중복 예약");
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetName() {
        System.out.println("getName");
        assertEquals("홍길동", instance.getName());
    }

    @Test
    public void testGetUserId() {
        System.out.println("getUserId");
        assertEquals("20230001", instance.getUserId());
    }

    @Test
    public void testGetReservationCount() {
        System.out.println("getReservationCount");
        assertEquals(10, instance.getReservationCount());
    }

    @Test
    public void testGetCancelCount() {
        System.out.println("getCancelCount");
        assertEquals(3, instance.getCancelCount());
    }

    @Test
    public void testGetCancelReason() {
        System.out.println("getCancelReason");
        assertEquals("개인 사정; 중복 예약", instance.getCancelReason());
    }
}