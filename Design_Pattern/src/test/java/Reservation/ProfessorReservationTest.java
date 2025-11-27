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
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author namw2
 */
public class ProfessorReservationTest {

    private ProfessorReservation instance;
    private MockReservationView mockView;

    // 파일 입출력 에러 방지를 위한 임시 파일 경로
    private final String BAN_LIST_PATH = "src/main/resources/banlist.txt";
    private final String RESERVATION_PATH = "src/main/resources/reservation.txt";

    public ProfessorReservationTest() {
    }

    @BeforeEach
    public void setUp() {
        // 1. 테스트 실행 전 임시 디렉토리 및 파일 생성 (FileNotFoundException 방지)
        System.out.println("setUp()");
        new File("src/main/resources").mkdirs();
        createFile(BAN_LIST_PATH, "");
        createFile(RESERVATION_PATH, "");

        // 2. 인스턴스 초기화
        instance = new ProfessorReservation();
        mockView = new MockReservationView();
        instance.view = mockView;
    }

    @AfterEach
    public void tearDown() {
        System.out.println("tearDown()");
        // 테스트 후 파일 정리
        new File(BAN_LIST_PATH).delete();
        new File(RESERVATION_PATH).delete();
    }

    // 파일 생성 헬퍼
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
        System.out.printf("%s: basic constructer test passed%n", instance.getClass().getSimpleName());
    }

    /**
     * Test of isUserBanned method.
     */
    @Test
    public void testIsUserBanned() {
        System.out.println("isUserBanned");

        String userId = "profLee";
        String userType = "교수";

        // 교수는 밴 리스트 검사를 하지 않고 항상 false를 반환해야 함
        boolean result = instance.isUserBanned(userId, userType);
        System.out.println("professor always return false =" + instance.isUserBanned(userId, userType));
        assertFalse(result, "교수는 차단 로직에서 항상 false여야 합니다.");
    }

    /**
     * Test of checkUserConstraints method.
     */
    @Test
    public void testCheckUserConstraints() {
        System.out.println("checkUserConstraints");

        String userId = "profLee";
        String date = "2024-05-05";
        // 아주 긴 시간을 예약하더라도 교수는 제한이 없어야 함
        List<String> times = Arrays.asList("09:00~12:00", "13:00~18:00");

        boolean result = instance.checkUserConstraints(userId, date, times);
        System.out.println("professor reservation always return True =" + instance.checkUserConstraints(userId, date, times));
        assertTrue(result, "교수는 사용자 제약 조건 검사에서 항상 true여야 합니다.");
    }

    /**
     * Test of confirmReservation method.
     */
    @Test
    public void testConfirmReservation() {
        System.out.println("confirmReservation");

        String expResult = "예약확정";
        String result = instance.confirmReservation();

        System.out.println(expResult + "=" + result);
        assertEquals(expResult, result, "교수의 예약 상태는 '예약확정'이어야 합니다.");
    }

    /**
     * Test of processTimeSlotConflict method.
     */
    @Test
    public void testProcessTimeSlotConflict() {
        System.out.println("processTimeSlotConflict");

        // [검증 전략]
        // processTimeSlotConflict는 내부적으로 'ahandleCancelConfirm()'을 호출합니다.
        // 하지만 ahandleCancelConfirm은 실제 파일 I/O와 부모 클래스의 필드(times 등)에 의존하므로
        // 단위 테스트 환경에서 그대로 실행하면 NullPointerException이 발생할 수 있습니다.
        // 따라서, 익명 클래스를 사용하여 ahandleCancelConfirm을 오버라이딩(Mocking/Spying)하여 호출 여부만 확인합니다.
        // 호출 여부를 저장할 배열 (익명 클래스 내부에서 접근 가능하도록)
        final boolean[] wasCancelCalled = {false};

        ProfessorReservation spyInstance = new ProfessorReservation() {
            @Override
            protected void ahandleCancelConfirm() {
                wasCancelCalled[0] = true; // 메서드가 호출되면 true로 변경
                System.out.println("[Test Log] ahandleCancelConfirm 메서드가 호출되었습니다 (Spy).");
            }
        };

        String userId = "profLee";
        String date = "2024-05-05";
        List<String> times = Arrays.asList("10:00~11:00");
        String roomName = "911";

        // 메서드 실행
        boolean result = spyInstance.processTimeSlotConflict(userId, date, times, roomName);

        // 검증 1: 결과값은 항상 true여야 함 (교수는 뺏을 수 있으므로 예약 진행)
        System.out.println("professor can take reservation return true =" + result);
        assertTrue(result, "충돌 발생 시 true를 반환해야 합니다.");

        // 검증 2: 취소 메서드가 실제로 호출되었는지 확인
        System.out.println( "canceled return true =" + wasCancelCalled[0]);
        assertTrue(wasCancelCalled[0], "충돌 처리를 위해 ahandleCancelConfirm()이 호출되어야 합니다.");
    }

}
