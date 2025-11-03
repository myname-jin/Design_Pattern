/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author suk22
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

    @Test
    void testConstructorAndGetters() {
        assertEquals("홍길동", model.getName());
        assertEquals("20231234", model.getStudentId());
        assertEquals("컴퓨터소프트웨어공학과", model.getDepartment());
        assertEquals("912", model.getRoom());
        assertEquals("2025-06-01", model.getDate());
        assertEquals("09:00~11:00", model.getTime());
        assertEquals("예약 대기", model.getApproved());
    }

    @Test
    void testSetApproved() {
        model.setApproved("승인");
        assertEquals("승인", model.getApproved());
    }
}
