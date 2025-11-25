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
 * (옵저버 패턴 알림 및 데이터 필터링 로직 검증 - 파일 격리 적용)
 */
public class AdminReservationModelTest {
    
    private Path tempFile;

    // 테스트용 가짜 옵저버
    class MockObserver implements ReservationObserver {
        boolean isNotified = false;
        List<Reservation> receivedData = null;

        @Override
        public void onReservationUpdated(List<Reservation> reservationList) {
            this.isNotified = true;
            this.receivedData = reservationList;
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        // 실제 파일 오염 방지를 위해 임시 파일 생성
        tempFile = Files.createTempFile("test_reservation_model", ".txt");
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    // 헬퍼 메서드: 모델 생성 후 파일 경로 교체
    private AdminReservationModel createSafeModel() {
        AdminReservationModel model = new AdminReservationModel();
        try {
            Field pathField = AdminReservationModel.class.getDeclaredField("FILE_PATH");
            pathField.setAccessible(true);
            pathField.set(null, tempFile.toString());
        } catch (Exception e) {
            System.err.println("경로 교체 실패: " + e.getMessage());
        }
        return model;
    }

    @Test
    public void testObserverNotification() {
        System.out.println("TEST: Observer Notification");
        
        // 1. 안전한 모델 생성
        AdminReservationModel model = createSafeModel();
        MockObserver observer = new MockObserver();

        // 2. 옵저버 등록
        model.addObserver(observer);

        // 3. 강제 알림 전송
        model.notifyObservers(new ArrayList<>());

        // 4. 검증
        assertTrue(observer.isNotified, "notifyObservers 호출 시 옵저버의 상태가 변경되어야 합니다.");
    }

    @Test
    public void testFilterData() throws Exception {
        System.out.println("TEST: Filter Data Logic");
        
        AdminReservationModel model = createSafeModel();
        MockObserver observer = new MockObserver();
        model.addObserver(observer);

        List<Reservation> dummyData = new ArrayList<>();
        dummyData.add(new Reservation("20201111", "학생", "김철수", "컴공", "실습실", "911", "2025-10-10", "월", "10:00", "12:00", "공부", "승인"));
        dummyData.add(new Reservation("20202222", "학생", "이영희", "전기", "회의실", "912", "2025-10-10", "월", "13:00", "15:00", "회의", "대기"));

        Field listField = AdminReservationModel.class.getDeclaredField("reservationList");
        listField.setAccessible(true);
        listField.set(model, dummyData);

        // 1. '이름' 검색
        model.filterData("철수", "이름");
        assertNotNull(observer.receivedData);
        assertEquals(1, observer.receivedData.size());
        assertEquals("김철수", observer.receivedData.get(0).getUserName());

        // 2. '학과' 검색
        model.filterData("전기", "학과");
        assertEquals(1, observer.receivedData.size());
        assertEquals("이영희", observer.receivedData.get(0).getUserName());
    }
}