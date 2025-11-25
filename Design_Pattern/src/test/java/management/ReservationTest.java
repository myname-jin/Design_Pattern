package management;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Reservation 클래스 단위 테스트
 * (데이터 저장, 조회, 테이블용 배열 변환 로직 검증)
 */
public class ReservationTest {

    private Reservation instance; 

    public ReservationTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        instance = new Reservation(
            "20230001", // studentId
            "학생",       // userType
            "홍길동",     // userName
            "컴퓨터공학", // department
            "세미나실",   // roomType
            "911",      // roomName
            "2025-11-25", // date
            "화",        // dayOfWeek
            "10:00",    // startTime
            "12:00",    // endTime
            "스터디",     // purpose
            "예약대기"    // status
        );
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetStudentId() {
        System.out.println("getStudentId");
        assertEquals("20230001", instance.getStudentId());
    }

    @Test
    public void testGetUserType() {
        System.out.println("getUserType");
        assertEquals("학생", instance.getUserType());
    }

    @Test
    public void testGetUserName() {
        System.out.println("getUserName");
        assertEquals("홍길동", instance.getUserName());
    }

    @Test
    public void testGetDepartment() {
        System.out.println("getDepartment");
        assertEquals("컴퓨터공학", instance.getDepartment());
    }

    @Test
    public void testGetRoomType() {
        System.out.println("getRoomType");
        assertEquals("세미나실", instance.getRoomType());
    }

    @Test
    public void testGetRoomName() {
        System.out.println("getRoomName");
        assertEquals("911", instance.getRoomName());
    }

    @Test
    public void testGetDate() {
        System.out.println("getDate");
        assertEquals("2025-11-25", instance.getDate());
    }

    @Test
    public void testGetDayOfWeek() {
        System.out.println("getDayOfWeek");
        assertEquals("화", instance.getDayOfWeek());
    }

    @Test
    public void testGetStartTime() {
        System.out.println("getStartTime");
        assertEquals("10:00", instance.getStartTime());
    }

    @Test
    public void testGetEndTime() {
        System.out.println("getEndTime");
        assertEquals("12:00", instance.getEndTime());
    }

    @Test
    public void testGetPurpose() {
        System.out.println("getPurpose");
        assertEquals("스터디", instance.getPurpose());
    }

    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        assertEquals("예약대기", instance.getStatus());
    }

    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        instance.setStatus("승인");
        assertEquals("승인", instance.getStatus());
    }

    /**
     * toArray() 테스트
     * 테이블에 표시하기 위해 데이터 순서와 포맷을 변경하는 로직을 검증
     */
    @Test
    public void testToArray() {
        System.out.println("toArray");
        
        Object[] result = instance.toArray();
        
        // Reservation.java에 정의된 순서와 포맷 확인
        // [0:학번, 1:학과, 2:이름, 3:구분, 4:강의실, 5:날짜(요일), 6:시간~시간, 7:목적, 8:상태]
        
        assertEquals("20230001", result[0]); // 학번
        assertEquals("컴퓨터공학", result[1]); // 학과
        assertEquals("홍길동", result[2]);     // 이름
        assertEquals("학생", result[3]);       // 구분
        assertEquals("911", result[4]);      // 호실
        assertEquals("2025-11-25(화)", result[5]); // 날짜 포맷 결합 확인
        assertEquals("10:00~12:00", result[6]);    // 시간 포맷 결합 확인
        assertEquals("스터디", result[7]);     // 목적
        assertEquals("예약대기", result[8]);    // 상태
    }
}