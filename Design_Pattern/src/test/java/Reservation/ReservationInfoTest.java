package Reservation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ReservationInfoTest {

    // 항상 "내일 날짜"를 반환하는 헬퍼 함수
    private String getTomorrowDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    @Test
    void testBuildReservationInfoSuccess() {
        String tomorrow = getTomorrowDate();
        List<String> times = Arrays.asList("10:00~11:00");

        ReservationInfo info = new ReservationInfo.Builder()
                .setUserInfo("20201234", "홍길동", "컴퓨터공학과")
                .setRoomInfo("101호")
                .setDateAndTimes(tomorrow, times)
                .setPurpose("스터디")
                .build();

        assertEquals("20201234", info.getUserId());
        assertEquals("홍길동", info.getUserName());
        assertEquals("컴퓨터공학과", info.getUserDept());
        assertEquals("101호", info.getRoomName());
        assertEquals(tomorrow, info.getDate());
        assertEquals(times, info.getTimes());
        assertEquals("스터디", info.getPurpose());
    }

    @Test
    void testMultipleTimesUnderTwoHoursSuccess() {
        String tomorrow = getTomorrowDate();
        List<String> times = Arrays.asList("09:00~10:00", "10:00~11:00"); // 총 2시간

        ReservationInfo info = new ReservationInfo.Builder()
                .setUserInfo("20204567", "이순신", "전자공학과")
                .setRoomInfo("201호")
                .setDateAndTimes(tomorrow, times)
                .setPurpose("회의")
                .build();

        assertEquals("20204567", info.getUserId());
        assertEquals("이순신", info.getUserName());
        assertEquals("전자공학과", info.getUserDept());
        assertEquals("201호", info.getRoomName());
        assertEquals(tomorrow, info.getDate());
        assertEquals(times, info.getTimes());
        assertEquals("회의", info.getPurpose());
    }

    @Test
    void testDifferentUserSuccess() {
        String tomorrow = getTomorrowDate();
        List<String> times = Arrays.asList("13:00~14:00");

        ReservationInfo info = new ReservationInfo.Builder()
                .setUserInfo("20209999", "김철수", "기계공학과")
                .setRoomInfo("303호")
                .setDateAndTimes(tomorrow, times)
                .setPurpose("프로젝트 회의")
                .build();

        assertEquals("김철수", info.getUserName());
        assertEquals("303호", info.getRoomName());
    }
}
