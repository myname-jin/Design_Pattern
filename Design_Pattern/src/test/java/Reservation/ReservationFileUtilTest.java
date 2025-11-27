package Reservation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class ReservationFileUtilTest {

    private static final Path TEST_PATH = Paths.get("src/main/resources/reservation.txt");

    @BeforeEach
    void setup() throws IOException {
        // 테스트용 파일 데이터 준비
        String initialData =
                "0,0,user01,홍길동,컴퓨터공학과,101,2025-05-20,2,10:00,12:00,스터디,승인\n" +
                "0,0,user02,이순신,전자공학과,102,2025-05-21,2,09:00,11:00,팀플,승인\n";

        Files.write(TEST_PATH, initialData.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    void testUpdateReservationStatus_success() throws IOException {

        boolean updated = ReservationFileUtil.updateReservationStatus(
                "user01",
                "101",
                "2025-05-20",
                "10:00",
                "승인",
                "취소"
        );

        assertTrue(updated, "예약 상태가 변경되었어야 한다.");

        List<String> lines = Files.readAllLines(TEST_PATH, StandardCharsets.UTF_8);

        String[] parts = lines.get(0).split(",");
        assertEquals("취소", parts[11], "첫 번째 예약의 상태가 취소로 변경되어야 한다.");
    }

    @Test
    void testUpdateReservationStatus_noMatch() throws IOException {

        boolean updated = ReservationFileUtil.updateReservationStatus(
                "userXX",       // 존재하지 않는 유저
                "999",
                "2030-01-01",
                "00:00",
                "승인",
                "취소"
        );

        assertFalse(updated, "일치하는 예약이 없으므로 false가 되어야 한다.");

        // 파일 변경 없어야 함
        List<String> lines = Files.readAllLines(TEST_PATH, StandardCharsets.UTF_8);
        String[] parts = lines.get(0).split(",");

        assertEquals("승인", parts[11], "상태는 그대로 승인이어야 한다.");
    }
}
