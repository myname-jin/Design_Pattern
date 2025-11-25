package management;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ReservationMgmtController 단위 테스트
 * (예약 조회, 승인 변경, 사용자 차단 로직 검증 - Reflection 사용)
 */
public class ReservationMgmtControllerTest {

    private Path tempReservationFile;
    private Path tempBanListFile;
    private ReservationMgmtController controller;

    @BeforeEach
    void setUp() throws Exception {
        tempReservationFile = Files.createTempFile("reservation", ".txt");
        tempBanListFile = Files.createTempFile("banlist", ".txt");

        String data1 = "홍길동,X,20231234,컴퓨터공학,913,X,2025-05-21,X,09:00,10:00,X,승인";
        String data2 = "김철수,X,20231235,전자공학,912,X,2025-05-22,X,11:00,12:00,X,승인";
        Files.write(tempReservationFile, List.of(data1, data2));

        controller = new ReservationMgmtController();

        // Reflection으로 private static final FILE_PATH 교체
        replaceFilePath("FILE_PATH", tempReservationFile.toString());
        replaceFilePath("BAN_LIST_FILE", tempBanListFile.toString());
    }

    // Reflection을 이용한 필드 값 변경 헬퍼 메서드
    private void replaceFilePath(String fieldName, String newPath) {
        try {
            Field field = ReservationMgmtController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, newPath); 
        } catch (Exception e) {
            // final 키워드 때문에 변경이 안 될 수도 있음 
            System.err.println("경고: " + fieldName + " 경로 교체 실패. (소스 코드에서 final 제거 권장)");
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempReservationFile);
        Files.deleteIfExists(tempBanListFile);
    }

    @Test
    void testGetAllReservations() {
        List<ReservationMgmtModel> list = controller.getAllReservations();
        
        // 데이터가 제대로 읽혔는지 확인
        // (만약 0이 나온다면 ReservationMgmtModel 파싱 로직이나 인덱스를 확인해야 함)
        assertEquals(2, list.size());
        assertEquals("홍길동", list.get(0).getName());
        assertEquals("김철수", list.get(1).getName());
    }

    @Test
    void testUpdateApprovalStatus() {
        // "20231234" 학번의 승인 상태를 "거절"로 변경
        controller.updateApprovalStatus("20231234", "거절");

        List<ReservationMgmtModel> updatedList = controller.getAllReservations();
        // 홍길동(0번 인덱스)의 상태가 바뀌었는지 확인
        assertEquals("거절", updatedList.get(0).getApproved());
    }

    @Test
    void testBanAndUnbanUser() {
        String studentId = "20231234";

        // 처음엔 차단 안 됨
        assertFalse(controller.isUserBanned(studentId));

        // 차단 실행 (이름, 학과 등은 더미 데이터)
        controller.banUser(studentId, "홍길동", "컴공", "학생");
        assertTrue(controller.isUserBanned(studentId));

        // 차단 해제 실행
        controller.unbanUser(studentId);
        assertFalse(controller.isUserBanned(studentId));
    }

    @Test
    void testSearchReservations() {
        // 이름 검색
        List<ReservationMgmtModel> result = controller.searchReservations("김철수", null, null);
        assertEquals(1, result.size());
        assertEquals("김철수", result.get(0).getName());

        // 없는 데이터 검색
        List<ReservationMgmtModel> none = controller.searchReservations(null, "999999", null);
        assertTrue(none.isEmpty());
    }
}