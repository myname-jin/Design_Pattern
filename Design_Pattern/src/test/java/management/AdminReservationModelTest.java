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
 * (옵저버 패턴, 데이터 필터링, 그리고 리팩토링된 상태 변경 로직 검증)
 */
public class AdminReservationModelTest {
    
    private Path tempResFile;  // 예약 데이터용 임시 파일
    private Path tempNotiFile; // 알림 데이터용 임시 파일
    
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
        // 테스트 실행 시 실제 파일을 건드리지 않도록 임시 파일 생성
        tempResFile = Files.createTempFile("test_reservation", ".txt");
        tempNotiFile = Files.createTempFile("test_notification", ".txt");
    }

    @AfterEach
    public void tearDown() throws IOException {
        // 테스트 종료 후 임시 파일 삭제
        Files.deleteIfExists(tempResFile);
        Files.deleteIfExists(tempNotiFile);
    }

    // 모델 생성 시 파일 경로들을 임시 파일로 교체 (Reflection 사용)
    private AdminReservationModel createSafeModel() {
        AdminReservationModel model = new AdminReservationModel();
        try {
            // 1. AdminReservationModel의 파일 경로 교체
            Field resPathField = AdminReservationModel.class.getDeclaredField("FILE_PATH");
            resPathField.setAccessible(true);
            resPathField.set(null, tempResFile.toString());

            // 2. NotificationManager의 파일 경로도 교체 (알림 파일 오염 방지)
            Field notiPathField = NotificationManager.class.getDeclaredField("FILE_PATH");
            notiPathField.setAccessible(true);
            notiPathField.set(null, tempNotiFile.toString());

        } catch (Exception e) {
            System.err.println("경로 교체 실패: " + e.getMessage());
        }
        return model;
    }

    @Test
    public void testObserverNotification() {
        System.out.println("TEST: Observer Notification");
        
        AdminReservationModel model = createSafeModel();
        MockObserver observer = new MockObserver();

        model.addObserver(observer);
        
        // 강제 알림 (빈 리스트 전송)
        model.notifyObservers(new ArrayList<>());

        assertTrue(observer.isNotified, "notifyObservers 호출 시 옵저버의 상태가 변경되어야 합니다.");
    }

    @Test
    public void testFilterData() throws Exception {
        System.out.println("TEST: Filter Data Logic");
        
        AdminReservationModel model = createSafeModel();
        MockObserver observer = new MockObserver();
        model.addObserver(observer);

        // 더미 데이터 주입
        List<Reservation> dummyData = new ArrayList<>();
        dummyData.add(new Reservation("20201111", "학생", "김철수", "컴공", "실습실", "911", "2025-10-10", "월", "10:00", "12:00", "공부", "승인"));
        dummyData.add(new Reservation("20202222", "학생", "이영희", "전기", "회의실", "912", "2025-10-10", "월", "13:00", "15:00", "회의", "대기"));

        // Reflection으로 private 리스트에 데이터 설정
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

    @Test
    public void testApproveLogic() throws Exception {
        System.out.println("TEST: Approve Logic (State Change + Notification)");

        AdminReservationModel model = createSafeModel();
        MockObserver observer = new MockObserver();
        model.addObserver(observer);

        // 테스트 데이터 준비 (초기 상태: 예약대기)
        List<Reservation> dummyData = new ArrayList<>();
        // Reservation 생성자: (학번, 구분, 이름, 학과, 룸타입, 룸이름, 날짜, 요일, 시작, 종료, 목적, 상태)
        Reservation targetRes = new Reservation("20230001", "학생", "테스트", "SW", "강의실", "911", "2025-12-25", "목", "10:00", "12:00", "스터디", "예약대기");
        dummyData.add(targetRes);

        // 데이터 주입
        Field listField = AdminReservationModel.class.getDeclaredField("reservationList");
        listField.setAccessible(true);
        listField.set(model, dummyData);

        // Action: 모델의 승인 메서드 호출
        model.approveReservation("20230001", "911", "2025-12-25", "10:00");

        // Assert 1: 옵저버를 통해 받은 데이터의 상태가 '승인'으로 변했는지 확인
        assertNotNull(observer.receivedData);
        Reservation updatedRes = observer.receivedData.get(0);
        assertEquals("승인", updatedRes.getStatus(), "승인 메서드 호출 후 상태가 '승인'이어야 합니다.");

        // Assert 2: 알림 파일에 기록되었는지 확인
        List<String> notiLines = Files.readAllLines(tempNotiFile);
        assertFalse(notiLines.isEmpty(), "알림 파일에 데이터가 기록되어야 합니다.");
        
        // 파일 포맷: 학번,메시지,시간,읽음여부
        String savedLine = notiLines.get(0);
        assertTrue(savedLine.contains("20230001"), "알림에 학번이 포함되어야 합니다.");
        assertTrue(savedLine.contains("승인"), "알림 메시지에 '승인' 내용이 포함되어야 합니다.");
    }
}