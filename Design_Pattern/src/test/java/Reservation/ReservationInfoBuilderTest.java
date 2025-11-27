package Reservation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ReservationInfoBuilderTest {

    // 항상 내일 날짜를 반환
    private String getTomorrowDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    @Test
    void testBuildReservationInfoSuccessUsingWrapperBuilder() {
        String tomorrow = getTomorrowDate();
        List<String> times = Arrays.asList("10:00~11:00");

        ReservationInfoBuilder builder = new ReservationInfoBuilder();

        ReservationInfo info = builder
                .setUserInfo("20201234", "홍길동", "컴퓨터공학과")
                .setRoomInfo("101호")
                .setDateAndTimes(tomorrow, times)
                .setPurpose("스터디")
                .buildReservation();

        assertNotNull(info);
        assertEquals("20201234", info.getUserId());
        assertEquals("홍길동", info.getUserName());
        assertEquals("컴퓨터공학과", info.getUserDept());
        assertEquals("101호", info.getRoomName());
        assertEquals(tomorrow, info.getDate());
        assertEquals(times, info.getTimes());
        assertEquals("스터디", info.getPurpose());
    }

    @Test
    void testBuilderChainingWorksCorrectly() {
        String tomorrow = getTomorrowDate();
        List<String> times = Arrays.asList("09:00~10:00");

        ReservationInfoBuilder builder = new ReservationInfoBuilder();

        // 체이닝이 문제없이 작동하는지만 확인
        builder.setUserInfo("20209999", "김철수", "기계공학과")
               .setRoomInfo("202호")
               .setDateAndTimes(tomorrow, times)
               .setPurpose("회의");

        ReservationInfo info = builder.buildReservation();

        assertEquals("김철수", info.getUserName());
        assertEquals("202호", info.getRoomName());
        assertEquals("회의", info.getPurpose());
    }

    @Test
    void testMultipleTimesSuccess() {
        String tomorrow = getTomorrowDate();
        List<String> times = Arrays.asList("13:00~14:00", "14:00~15:00"); // 총 2시간

        ReservationInfo info = new ReservationInfoBuilder()
                .setUserInfo("20201111", "박영희", "산업디자인과")
                .setRoomInfo("303호")
                .setDateAndTimes(tomorrow, times)
                .setPurpose("팀플")
                .buildReservation();

        assertEquals(times, info.getTimes());
        assertEquals("303호", info.getRoomName());
    }
}
