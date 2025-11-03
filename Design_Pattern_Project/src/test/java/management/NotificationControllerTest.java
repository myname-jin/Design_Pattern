/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author suk22
 */
public class NotificationControllerTest {

    private NotificationController controller;
    private MockNotificationModel mockModel;

    @BeforeEach
    void setUp() {
        mockModel = new MockNotificationModel();
        controller = new NotificationController();
        controller.setModel(mockModel);  // setter로 주입

        mockModel.setPendingReservations(Collections.emptyList());
        mockModel.setAllReservations(Collections.emptyList());
        controller.refreshNotifications();
    }

    @Test
    void testNewPendingReservationDetected() {
        // 새로운 대기 예약 추가
        mockModel.setPendingReservations(List.of("홍길동,2024-05-25 10:00,강의실 A"));
        mockModel.setAllReservations(List.of("홍길동,2024-05-25 10:00,강의실 A"));

        Map<String, List<String>> result = controller.detectNotificationChangesForTest();

        assertEquals(1, result.get("newPending").size());
        assertEquals("홍길동,2024-05-25 10:00,강의실 A", result.get("newPending").get(0));
        assertTrue(result.get("removed").isEmpty());
    }

    @Test
    void testRemovedReservationDetected() {
        // 먼저 예약이 있는 상태로 초기화
        mockModel.setPendingReservations(List.of("홍길동,2024-05-25 10:00,강의실 A"));
        mockModel.setAllReservations(List.of("홍길동,2024-05-25 10:00,강의실 A"));
        controller.refreshNotifications();

        // 이후 취소 상태로 전환
        mockModel.setPendingReservations(Collections.emptyList());
        mockModel.setAllReservations(Collections.emptyList());

        Map<String, List<String>> result = controller.detectNotificationChangesForTest();

        assertTrue(result.get("newPending").isEmpty());
        assertEquals(1, result.get("removed").size());
        assertEquals("홍길동,2024-05-25 10:00,강의실 A", result.get("removed").get(0));
    }

    @Test
    void testNoChangesDetected() {
        mockModel.setPendingReservations(Collections.emptyList());
        mockModel.setAllReservations(Collections.emptyList());

        Map<String, List<String>> result = controller.detectNotificationChangesForTest();

        assertTrue(result.get("newPending").isEmpty());
        assertTrue(result.get("removed").isEmpty());
    }

    private static class MockNotificationModel extends NotificationModel {

        private List<String> pendingReservations = new ArrayList<>();
        private List<String> allReservations = new ArrayList<>();

        @Override
        public List<String> getPendingReservations() {
            return pendingReservations;
        }

        @Override
        public List<String> getAllReservations() {
            return allReservations;
        }

        public void setPendingReservations(List<String> list) {
            this.pendingReservations = list;
        }

        public void setAllReservations(List<String> list) {
            this.allReservations = list;
        }
    }
}
