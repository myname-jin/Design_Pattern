/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package visualization;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationModelTest {

    private ReservationModel model;

    @BeforeEach
    public void setUp() {
        model = new ReservationModel();
    }

    @Test
    public void testGetRoomTotals() {
        Map<String, Integer> totals = model.getRoomTotals();

        assertNotNull(totals);
        assertEquals(8, totals.size()); // 911~918 총 8개

        assertEquals(6, totals.get("911")); // ← 수정됨
        assertEquals(2, totals.get("912"));
        assertEquals(1, totals.get("913"));
        assertEquals(1, totals.get("914"));
        assertEquals(1, totals.get("915"));
    }

    @Test
    public void testGetRoomByDay() {
        Map<String, Integer> byDay911 = model.getRoomByDay("911");

        assertNotNull(byDay911);
        assertEquals(5, byDay911.size()); // 월~금

        assertEquals(1, byDay911.get("월"));
        assertEquals(2, byDay911.get("화"));
        assertEquals(1, byDay911.get("수"));
        assertEquals(1, byDay911.get("목"));
        assertEquals(1, byDay911.get("금"));
    }

    @Test
    public void testGetRoomByDayEmptyRoom() {
        Map<String, Integer> byDay = model.getRoomByDay("999"); // 존재하지 않는 방

        assertNotNull(byDay);
        assertTrue(byDay.isEmpty());
    }
}