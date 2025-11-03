/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;


import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author suk22
 */
public class UserStatsControllerTest {

    private Path tempReservationFile;
    private Path tempCancelFile;
    private UserStatsController controller;

    @BeforeEach
    void setUp() throws IOException {
        // 임시 파일 생성
        tempReservationFile = Files.createTempFile("reservation", ".txt");
        tempCancelFile = Files.createTempFile("cancel", ".txt");

        // 임시 예약 데이터 기록
        List<String> reservationData = Arrays.asList(
                "홍길동,강의실1,user01,2024-01-01",
                "홍길동,강의실2,user01,2024-01-02",
                "김철수,강의실1,user02,2024-01-03"
        );
        Files.write(tempReservationFile, reservationData);

        // 임시 취소 데이터 기록
        List<String> cancelData = Arrays.asList(
                "user01,개인 사정",
                "user01,중복 예약",
                "user02,사정 변경"
        );
        Files.write(tempCancelFile, cancelData);

        // 컨트롤러에 임시 파일 경로 주입
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

        assertEquals(2, stats.size());

        UserStatsModel user1 = stats.stream()
                .filter(s -> s.getUserId().equals("user01"))
                .findFirst()
                .orElse(null);
        assertNotNull(user1);
        assertEquals("홍길동", user1.getName());
        assertEquals(2, user1.getReservationCount());
        assertEquals(2, user1.getCancelCount());
        assertTrue(user1.getCancelReason().contains("개인 사정"));
        assertTrue(user1.getCancelReason().contains("중복 예약"));

        UserStatsModel user2 = stats.stream()
                .filter(s -> s.getUserId().equals("user02"))
                .findFirst()
                .orElse(null);
        assertNotNull(user2);
        assertEquals("김철수", user2.getName());
        assertEquals(1, user2.getReservationCount());
        assertEquals(1, user2.getCancelCount());
        assertTrue(user2.getCancelReason().contains("사정 변경"));
    }
}
