package management;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AdminReservationModel 단위 테스트
 * (옵저버 패턴 동작 및 데이터 처리 로직 검증)
 */
public class AdminReservationModelTest {
    
    private Path tempResFile;  
    private Path tempNotiFile; 
    
    // 테스트용 가짜 옵저버
    class MockObserver implements ReservationObserver {
        boolean isNotified = false;
        List<Reservation> receivedData = null;

        @Override
        public void onReservationUpdated(List<Reservation> reservationList) {
            this.isNotified = true;
            this.receivedData = reservationList;
            System.out.println("[TestLog] 옵저버 알림 수신 완료. 데이터 건수: " + reservationList.size());
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        tempResFile = Files.createTempFile("test_reservation", ".txt");
        tempNotiFile = Files.createTempFile("test_notification", ".txt");
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempResFile);
        Files.deleteIfExists(tempNotiFile);
    }

    private AdminReservationModel createSafeModel() {
        AdminReservationModel model = new AdminReservationModel();
        try {
            Field resPathField = AdminReservationModel.class.getDeclaredField("FILE_PATH");
            resPathField.setAccessible(true);
            resPathField.set(null, tempResFile.toString());

            Field notiPathField = NotificationManager.class.getDeclaredField("FILE_PATH");
            notiPathField.setAccessible(true);
            notiPathField.set(null, tempNotiFile.toString());

        } catch (Exception e) {
            System.err.println("⚠️ 경로 교체 실패 (final 키워드 확인 필요): " + e.getMessage());
        }
        return model;
    }

    @Test
    public void testObserverNotification() {
        System.out.println("\n=== TEST 1: 옵저버 알림 수신 테스트 ===");
        
        AdminReservationModel model = createSafeModel();
        MockObserver observer = new MockObserver();

        // 1. 구독 (Subscribe)
        model.addObserver(observer);
        
        // 2. 알림 발송 (Notify)
        model.notifyObservers(new ArrayList<>());

        // 3. 검증 (Assert)
        assertTrue(observer.isNotified, "notifyObservers 호출 시 옵저버의 메서드가 실행되어야 한다.");
    }

    @Test
    public void testFilterData() throws Exception {
        System.out.println("\n=== TEST 2: 검색(Filter) 기능 테스트 ===");
        
        AdminReservationModel model = createSafeModel();
        MockObserver observer = new MockObserver();
        model.addObserver(observer);

        List<Reservation> dummyData = new ArrayList<>();
        dummyData.add(new Reservation("20201111", "학생", "김철수", "컴공", 
                "실습실", "911", "2025-10-10", "월", "10:00", "12:00", 
                "공부", "승인"));
        dummyData.add(new Reservation("20202222", "학생", "이영희", "전기", 
                "회의실", "912", "2025-10-10", "월", "13:00", "15:00", 
                "회의", "대기"));

        Field listField = AdminReservationModel.class.getDeclaredField("reservationList");
        listField.setAccessible(true);
        listField.set(model, dummyData);

        // 1. '이름'으로 검색
        model.filterData("철수", "이름");
        assertNotNull(observer.receivedData);
        assertEquals(1, observer.receivedData.size());
        assertEquals("김철수", observer.receivedData.get(0).getUserName());

        // 2. '학과'로 검색
        model.filterData("전기", "학과");
        assertEquals(1, observer.receivedData.size());
        assertEquals("이영희", observer.receivedData.get(0).getUserName());
    }

    @Test
    public void testUpdateStatusLogic() throws Exception {
        System.out.println("\n=== TEST 3: 상태 변경 및 저장 테스트 ===");

        AdminReservationModel model = createSafeModel();
        MockObserver observer = new MockObserver();
        model.addObserver(observer);

        // 초기 데이터
        List<Reservation> dummyData = new ArrayList<>();
        Reservation targetRes = new Reservation("20230001", "학생", "테스트", 
                "SW", "강의실", "911", "2025-12-25", "목", 
                "10:00", "12:00", "스터디", "예약대기");
        dummyData.add(targetRes);

        Field listField = AdminReservationModel.class.getDeclaredField("reservationList");
        listField.setAccessible(true);
        listField.set(model, dummyData);

        // Action: 상태 변경 (예약대기 -> 승인)
        model.updateStatus("20230001", "911", "2025-12-25", "10:00", "승인");

        // Assert 1: 옵저버가 받은 데이터가 '승인'으로 바뀌었는지
        assertNotNull(observer.receivedData);
        Reservation updatedRes = observer.receivedData.get(0);
        assertEquals("승인", updatedRes.getStatus(), "상태가 '승인'으로 변경되어야 합니다.");

        // Assert 2: 실제 파일에 저장되었는지 (파일 크기가 0보다 커야 함)
        assertTrue(Files.size(tempResFile) > 0, "데이터 변경 시 파일에 저장되어야 합니다.");
    }
}