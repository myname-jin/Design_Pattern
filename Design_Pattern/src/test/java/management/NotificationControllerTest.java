package management;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * NotificationController 단위 테스트
 * (새로운 예약 대기 및 취소 감지 로직 검증)
 */
public class NotificationControllerTest {

    private NotificationController controller;
    private MockNotificationModel mockModel;

    // 테스트용 Mock 모델 내부 클래스
    private static class MockNotificationModel extends NotificationModel {
        private List<String> pendingReservations = new ArrayList<>();
        private List<String> allReservations = new ArrayList<>();

        @Override
        public List<String> getPendingReservations() { return pendingReservations; }

        @Override
        public List<String> getAllReservations() { return allReservations; }

        public void setPendingReservations(List<String> list) { this.pendingReservations = list; }
        public void setAllReservations(List<String> list) { this.allReservations = list; }
    }

    @BeforeEach
    void setUp() {
        mockModel = new MockNotificationModel();
        controller = new NotificationController();
        controller.setModel(mockModel); 

        // 초기 상태: 아무것도 없음
        mockModel.setPendingReservations(Collections.emptyList());
        mockModel.setAllReservations(Collections.emptyList());
        controller.refreshNotifications(); 
    }

    @Test
    void testNewPendingReservationDetected() {
        // 새로운 예약 대기 데이터 추가
        String newReservation = "홍길동,2024-05-25 10:00,강의실 A";
        mockModel.setPendingReservations(List.of(newReservation));
        mockModel.setAllReservations(List.of(newReservation));

        // 변경 사항 감지
        Map<String, List<String>> result = controller.detectNotificationChangesForTest();

        // 'newPending'에 잡혀야 함
        assertEquals(1, result.get("newPending").size());
        assertEquals(newReservation, result.get("newPending").get(0));
        assertTrue(result.get("removed").isEmpty());
    }

    @Test
    void testRemovedReservationDetected() {
        String oldReservation = "홍길동,2024-05-25 10:00,강의실 A";
        mockModel.setPendingReservations(List.of(oldReservation));
        mockModel.setAllReservations(List.of(oldReservation));
        controller.refreshNotifications(); // 현재 상태를 '기존'으로 저장

        // 예약이 사라짐 (취소됨)
        mockModel.setPendingReservations(Collections.emptyList());
        mockModel.setAllReservations(Collections.emptyList());

        Map<String, List<String>> result = controller.detectNotificationChangesForTest();

        // 'removed'에 잡혀야 함
        assertTrue(result.get("newPending").isEmpty());
        assertEquals(1, result.get("removed").size());
        assertEquals(oldReservation, result.get("removed").get(0));
    }

    @Test
    void testNoChangesDetected() {
        // 아무 변화 없음
        Map<String, List<String>> result = controller.detectNotificationChangesForTest();
        assertTrue(result.get("newPending").isEmpty());
        assertTrue(result.get("removed").isEmpty());
    }
}