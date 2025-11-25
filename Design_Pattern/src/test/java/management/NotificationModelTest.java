package management;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * NotificationModel 단위 테스트
 * (파일 읽기 및 예약 상태별 알림 메시지 생성 로직 검증)
 */
public class NotificationModelTest {

    private Path tempFile;

    @BeforeEach
    public void setup() throws IOException {
        tempFile = Files.createTempFile("test_reservation", ".txt");

        List<String> testData = Arrays.asList(
            "홍길동,학생,2023001,컴공,강의실,101,2025-05-24,금,10:00,12:00,수업,예약대기",
            "김영희,학생,2023002,컴공,강의실,102,2025-05-25,토,14:00,16:00,스터디,예약승인"
        );
        Files.write(tempFile, testData);
    }

    @AfterEach
    public void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testGetPendingReservations() {
        NotificationModel model = new NotificationModel(tempFile.toString());

        //대기 중인 예약 조회
        List<String> result = model.getPendingReservations();

        // '예약대기' 상태인 홍길동 데이터만 반환되어야 함
        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("홍길동"));
        assertTrue(result.get(0).contains("예약 대기"));
    }

    @Test
    public void testGetAllReservations() {
        NotificationModel model = new NotificationModel(tempFile.toString());

        // 전체 예약 조회
        List<String> result = model.getAllReservations();

        //상태 상관없이 모든 데이터(2건) 반환 확인
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(line -> line.contains("홍길동")));
        assertTrue(result.stream().anyMatch(line -> line.contains("김영희")));
    }
}