package management;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UserStatsController 단위 테스트
 * (예약 및 취소 통계 집계 로직 검증)
 */
public class UserStatsControllerTest {

    private Path tempReservationFile;
    private Path tempCancelFile;
    private UserStatsController controller;

    @BeforeEach
    void setUp() throws IOException {
        tempReservationFile = Files.createTempFile("reservation", ".txt");
        tempCancelFile = Files.createTempFile("cancel", ".txt");

        // 임시 예약 데이터 기록
        List<String> reservationData = Arrays.asList(
                "홍길동,학생,user01,컴공,강의실1", // 2건
                "홍길동,학생,user01,컴공,강의실2",
                "김철수,학생,user02,전기,강의실1"  // 1건
        );
        Files.write(tempReservationFile, reservationData);

        // 임시 취소 데이터 기록 (형식: 학번, 사유)
        List<String> cancelData = Arrays.asList(
                "user01,개인 사정", // 2건
                "user01,중복 예약",
                "user02,사정 변경"  // 1건
        );
        Files.write(tempCancelFile, cancelData);

        controller = new UserStatsController(tempReservationFile.toString(), tempCancelFile.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempReservationFile);
        Files.deleteIfExists(tempCancelFile);
    }

    @Test
    void testLoadUserStats() {
        List<UserStatsModel> stats = controller.loadUserStats();

        // 2명의 통계가 나와야 함
        assertEquals(2, stats.size());

        // user01 (홍길동) 검증
        UserStatsModel user1 = stats.stream()
                .filter(s -> s.getUserId().equals("user01"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(user1);
        assertEquals("홍길동", user1.getName());
        assertEquals(2, user1.getReservationCount()); // 예약 2건
        assertEquals(2, user1.getCancelCount());      // 취소 2건
        assertTrue(user1.getCancelReason().contains("개인 사정"));
        assertTrue(user1.getCancelReason().contains("중복 예약"));

        // user02 (김철수) 검증
        UserStatsModel user2 = stats.stream()
                .filter(s -> s.getUserId().equals("user02"))
                .findFirst()
                .orElse(null);

        assertNotNull(user2);
        assertEquals("김철수", user2.getName());
        assertEquals(1, user2.getReservationCount()); // 예약 1건
        assertEquals(1, user2.getCancelCount());      // 취소 1건
        assertTrue(user2.getCancelReason().contains("사정 변경"));
    }
}