/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Reservation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

/**
 *
 * @author scq37
 */
public class RoomModelTest {

    private RoomModel room;

    @BeforeEach
    public void setUp() {
        String[] times = {"09:00", "10:00"};
        room = new RoomModel("912호", "강의실", times);
    }

    // 매개변수 생성자 테스트
    @Test
    public void testConstructorWithParameters() {
        System.out.println("매개변수를 가진 생성자 테스트: " + room.getName() + ", " + room.getType());
        assertEquals("912호", room.getName());
        assertEquals("강의실", room.getType());
    }

    // 강의실 필터링 테스트
    @Test
    public void testFilterLectureRooms() {
        RoomModel[] allRooms = {
            new RoomModel("912호", "강의실", new String[]{"09:00"}),
            new RoomModel("915호", "실습실", new String[]{"10:00"}),
            new RoomModel("913호", "강의실", new String[]{"11:00"})
        };

        RoomModel[] filtered = Arrays.stream(allRooms)
            .filter(r -> r.getType().equals("강의실"))
            .toArray(RoomModel[]::new);

        System.out.println("\n[필터링된 강의실 목록]");
        for (RoomModel r : filtered) {
            System.out.println("- " + r.getName() + " (" + r.getType() + ")");
        }

        assertEquals(2, filtered.length);
        assertEquals("912호", filtered[0].getName());
        assertEquals("913호", filtered[1].getName());
    }

    // 실습실 필터링 테스트
    @Test
    public void testFilterPracticeRooms() {
        RoomModel[] allRooms = {
            new RoomModel("912호", "강의실", new String[]{"09:00"}),
            new RoomModel("915호", "실습실", new String[]{"10:00"}),
            new RoomModel("916호", "실습실", new String[]{"11:00"})
        };

        RoomModel[] filtered = Arrays.stream(allRooms)
            .filter(r -> r.getType().equals("실습실"))
            .toArray(RoomModel[]::new);

        System.out.println("\n[필터링된 실습실 목록]");
        for (RoomModel r : filtered) {
            System.out.println("- " + r.getName() + " (" + r.getType() + ")");
        }

        assertEquals(2, filtered.length);
        assertEquals("915호", filtered[0].getName());
        assertEquals("916호", filtered[1].getName());
    }

    // 유효하지 않은 유형 필터링 테스트
    @Test
    public void testFilterInvalidType() {
        RoomModel[] allRooms = {
            new RoomModel("912호", "강의실", new String[]{"09:00"}),
            new RoomModel("915호", "실습실", new String[]{"10:00"})
        };

        String invalidType = "회의실";
        RoomModel[] filtered = Arrays.stream(allRooms)
            .filter(r -> r.getType().equals(invalidType))
            .toArray(RoomModel[]::new);

        System.out.println("\n[유효하지 않은 유형 테스트]");
        System.out.println("입력된 유형: " + invalidType);
        if (filtered.length == 0) {
            System.out.println("❌ '" + invalidType + "' 유형은 존재하지 않습니다.");
        }
        System.out.println("→ 필터링된 강의실 개수: " + filtered.length);

        assertEquals(0, filtered.length);
    }
}