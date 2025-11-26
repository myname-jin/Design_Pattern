package management;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ReservationMgmtModel 단위 테스트
 * (데이터 객체의 생성 및 조회, 수정 검증)
 */
public class ReservationMgmtModelTest {

    private ReservationMgmtModel model;

    @BeforeEach
    void setUp() {
        model = new ReservationMgmtModel(
                "홍길동",
                "20231234",
                "컴퓨터소프트웨어공학과",
                "912",
                "2025-06-01",
                "09:00~11:00",
                "예약 대기"
        );
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void testGetName() {
        assertEquals("홍길동", model.getName());
    }

    @Test
    void testGetStudentId() {
        assertEquals("20231234", model.getStudentId());
    }

    @Test
    void testGetDepartment() {
        assertEquals("컴퓨터소프트웨어공학과", model.getDepartment());
    }

    @Test
    void testGetRoom() {
        assertEquals("912", model.getRoom());
    }

    @Test
    void testGetDate() {
        assertEquals("2025-06-01", model.getDate());
    }

    @Test
    void testGetTime() {
        assertEquals("09:00~11:00", model.getTime());
    }

    @Test
    void testGetApproved() {
        assertEquals("예약 대기", model.getApproved());
    }

    @Test
    void testSetApproved() {
        model.setApproved("승인");
        assertEquals("승인", model.getApproved());
    }
}