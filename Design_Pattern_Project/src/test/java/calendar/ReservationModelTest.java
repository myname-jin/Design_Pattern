/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package calendar;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationModelTest {

    @Test
    void testConstructorAndGetters() {
        String type = "일부";
        LocalDate date = LocalDate.of(2025, 5, 18);
        String roomType = "실습실";
        String roomNumber = "913";
        int slot = 3;

        ReservationModel model = new ReservationModel(type, date, roomType, roomNumber, slot);

        assertEquals("일부", model.getType());
        assertEquals(date, model.getDate());
        assertEquals("실습실", model.getRoomType());
        assertEquals("913", model.getRoomNumber());
        assertEquals(3, model.getSlot());
    }
}