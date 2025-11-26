package Reservation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ReservationGUIControllerBuilderTest {

    // 유효한 builder 생성 테스트 (성공 케이스 ONLY)
    @Test
    void testBuildReservationSuccess() {
        // 준비
        String userId = "20230001";
        String userName = "홍길동";
        String userDept = "컴퓨터공학과";
        String roomName = "101호";

        String tomorrow = getTomorrowDate();
        List<String> times = Arrays.asList("10:00~12:00");
        String purpose = "스터디";

        // 실행
        ReservationInfo info = new ReservationInfoBuilder()
                .setUserInfo(userId, userName, userDept)
                .setRoomInfo(roomName)
                .setDateAndTimes(tomorrow, times)
                .setPurpose(purpose)
                .buildReservation();

        // 검증
        assertNotNull(info);
        assertEquals(userId, info.getUserId());
        assertEquals(userName, info.getUserName());
        assertEquals(userDept, info.getUserDept());
        assertEquals(roomName, info.getRoomName());
        assertEquals(tomorrow, info.getDate());
        assertEquals(times, info.getTimes());
        assertEquals(purpose, info.getPurpose());
    }

    // ---------------------------
    // 내일 날짜 문자열 생성 (yyyy-MM-dd)
    // ---------------------------
    private String getTomorrowDate() {
        java.time.LocalDate tomorrow = java.time.LocalDate.now().plusDays(1);
        return tomorrow.toString();
    }
}
