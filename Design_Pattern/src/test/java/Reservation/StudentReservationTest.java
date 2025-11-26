/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Reservation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author namw2
 */
public class StudentReservationTest {
    
    private StudentReservation instance;
    private MockReservationView mockView;
    
    // 테스트용 파일 경로 (소스코드와 일치해야 함)
    private final String BAN_LIST_PATH = "src/main/resources/banlist.txt";
    private final String RESERVATION_PATH = "src/main/resources/reservation.txt";

    public StudentReservationTest() {
    }
    
    @BeforeEach
    public void setUp() {
        System.out.println("setUp()");
        // 1. 테스트 대상 객체 생성
        instance = new StudentReservation();
        
        // 2. 가짜 View 주입 (protected 필드 접근)
        mockView = new MockReservationView();
        instance.view = mockView;
        
        // 3. 테스트용 디렉토리 생성 (없을 경우)
        new File("src/main/resources").mkdirs();
        
        // 4. 깨끗한 상태로 파일 초기화
        createFile(BAN_LIST_PATH, "");
        createFile(RESERVATION_PATH, "");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("tearDown()");
        // 테스트 후 파일 삭제 (선택사항, 데이터 보존이 필요 없다면 삭제)
        new File(BAN_LIST_PATH).delete();
        new File(RESERVATION_PATH).delete();
    }

    // 파일 생성 헬퍼 메서드
    private void createFile(String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // 파일에 내용 추가 헬퍼 메서드
    private void appendFile(String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8"))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDefaultConstructer() {
        assertNotNull(instance);
        System.out.printf("%s: basic constructer test%n", instance.getClass().getSimpleName());
    }

    /**
     * Test of isUserBanned method.
     */
    @Test
    public void testIsUserBanned() {
        System.out.println("isUserBanned");
        
        // Given: 차단 목록 파일 생성
        createFile(BAN_LIST_PATH, "bannedUser\nanotherBadUser");

        // When & Then
        // 1. 차단된 사용자 (학생)
        System.out.println("banned user return True =" + instance.isUserBanned("bannedUser", "학생"));
        assertTrue(instance.isUserBanned("bannedUser", "학생"), "차단 목록에 있는 학생은 true여야 함");
        
        
        // 2. 일반 사용자 (학생)
        System.out.println("not banned user return false =" + instance.isUserBanned("goodUser", "학생"));
        assertFalse(instance.isUserBanned("goodUser", "학생"), "차단 목록에 없는 학생은 false여야 함");
        
        // 3. 교수는 차단 목록과 상관없이 통과되어야 하는지? 
        // (현재 StudentReservation 로직상 '학생'이 아니면 무조건 false 반환)
        System.out.println("professor always return false =" + instance.isUserBanned("bannedUser", "교수"));
        assertFalse(instance.isUserBanned("bannedUser", "교수"), "학생이 아닌 유저는 false여야 함");
    }

    /**
     * Test of checkUserConstraints method.
     */
    @Test
    public void testCheckUserConstraints() {
        System.out.println("checkUserConstraints");
        
        String userId = "testStudent";
        String date = "2024-05-05";
        
        // Case 1: 정상 케이스 (예약 없음, 2시간 이하)
        List<String> validTimes = Arrays.asList("09:00~10:00", "10:00~11:00"); // 120분
        System.out.println("nomal case return True =" +instance.checkUserConstraints(userId, date, validTimes));
        assertTrue(instance.checkUserConstraints(userId, date, validTimes));

        // Case 2: 시간 초과 (120분 초과)
        List<String> longTimes = Arrays.asList("09:00~10:00", "10:00~11:00", "11:00~11:01"); // 121분
        boolean resultTimeLimit = instance.checkUserConstraints(userId, date, longTimes);
        System.out.println("time over return false =" + resultTimeLimit);
        assertFalse(resultTimeLimit);
        assertEquals("총 예약 시간이 2시간(120분)을 초과할 수 없습니다.", mockView.lastMessage);

        // Case 3: 이미 당일 예약이 있는 경우 (1일 1회 제한)
        // 형식: ID,타입,이름,학과,강의실타입,방번호,날짜,요일,시작,종료,목적,상태
        String existingRecord = "testStudent,학생,홍길동,컴공,실습실,911,2024-05-05,일,09:00,10:00,공부,예약확정";
        appendFile(RESERVATION_PATH, existingRecord);
        
        boolean resultAlreadyReserved = instance.checkUserConstraints(userId, date, validTimes);
        System.out.println("already reserved return false =" + resultAlreadyReserved);
        assertFalse(resultAlreadyReserved);
        assertEquals("학생은 하루 1회만 예약할 수 있습니다.", mockView.lastMessage);
    }

    /**
     * Test of confirmReservation method.
     */
    @Test
    public void testConfirmReservation() {
        System.out.println("confirmReservation");
        
        String expResult = "예약대기";
        String result = instance.confirmReservation();
        
        System.out.println( expResult + "=" + result);
        assertEquals(expResult, result, "학생 예약은 '예약대기' 상태여야 합니다.");
    }

    /**
     * Test of processTimeSlotConflict method.
     */
    @Test
    public void testProcessTimeSlotConflict() {
        System.out.println("processTimeSlotConflict");
        
        String userId = "newStudent";
        String date = "2024-12-25";
        String roomName = "911";
        List<String> times = Arrays.asList("10:00~11:00");
        
        // Case 1: 충돌 없음 (파일이 비어있음)
        createFile(RESERVATION_PATH, "");
        boolean resultNoConflict = instance.processTimeSlotConflict(userId, date, times, roomName);
        System.out.println( "no conflict return true =" + instance.processTimeSlotConflict(userId, date, times, roomName));
        assertTrue(resultNoConflict, "충돌이 없으면 true를 반환해야 함");

        // Case 2: 충돌 발생
        // 기존 예약: 911호, 2024-12-25, 09:50~10:50 (10:00~11:00과 겹침)
        String conflictRecord = "oldUser,학생,기존,컴공,실습실,911,2024-12-25,수,09:50,10:50,공부,예약확정";
        appendFile(RESERVATION_PATH, conflictRecord);
        
        boolean resultConflict = instance.processTimeSlotConflict(userId, date, times, roomName);
        System.out.println( "conflict return false =" + instance.processTimeSlotConflict(userId, date, times, roomName));
        assertFalse(resultConflict, "시간이 겹치면 false를 반환해야 함");
        assertEquals("선택한 시간대에 이미 예약이 존재합니다.", mockView.lastMessage);
    }
}