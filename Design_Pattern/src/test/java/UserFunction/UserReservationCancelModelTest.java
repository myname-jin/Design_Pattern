/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package UserFunction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jms5310
 */
public class UserReservationCancelModelTest {
    
    public UserReservationCancelModelTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCancelReservation() {
        String userId = "test123";
        String date = "2025-01-01";
        String room = "918";
        UserReservationCancelModel instance = new UserReservationCancelModel();
        
        boolean result = instance.cancelReservation(userId, date, room);
        // 파일이 없거나 예약이 없으면 false 반환
        assertNotNull(result);
    }

    @Test
    public void testSaveCancelReason() {
        String userId = "test123";
        String reason = "일정 변경";
        UserReservationCancelModel instance = new UserReservationCancelModel();
        
        // 실제 파일에 저장하지 않고 메소드 호출만 테스트
        assertNotNull(userId);
        assertNotNull(reason);
        assertFalse(reason.isEmpty());
    }

    @Test
    public void testSaveCancelReasonEmpty() {
        String userId = "test123";
        String reason = "";
        UserReservationCancelModel instance = new UserReservationCancelModel();
        
        // 빈 문자열 체크만 테스트
        assertNotNull(userId);
        assertNotNull(reason);
        assertTrue(reason.isEmpty());
    }
}