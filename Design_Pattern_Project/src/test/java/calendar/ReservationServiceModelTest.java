/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package calendar;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceModelTest {

    private ReservationServiceModel service;
    private static final Path FILE_PATH = Paths.get("src", "main", "resources", "blocked_dates.txt");

    @BeforeEach
    void setUp() throws Exception {
        Files.deleteIfExists(FILE_PATH);
        Files.createDirectories(FILE_PATH.getParent());
        Files.createFile(FILE_PATH);

        ReservationRepositoryModel repo = new ReservationRepositoryModel();
        service = new ReservationServiceModel(repo);
    }

    @Test
    void testBlockAllAndIsFullyBlocked() {
        service.blockAll("2025-05-18");

        assertTrue(service.isFullyBlocked("2025-05-18"));
        assertFalse(service.isFullyBlocked("2025-05-19"));
    }

    @Test
    void testBlockPartialAndGetBlockedTimeSlots() {
        service.blockPartial("2025-05-18", "강의실", "911", "11:00");
        List<String> slots = service.getBlockedTimeSlots("2025-05-18", "강의실", "911");

        assertEquals(1, slots.size());
        assertEquals("11:00", slots.get(0));
    }

    @Test
    void testUnblockRemovesEntries() {
        service.blockAll("2025-05-18");
        assertTrue(service.isFullyBlocked("2025-05-18"));

        service.unblock("2025-05-18");
        assertFalse(service.isFullyBlocked("2025-05-18"));
    }

    @Test
    void testGetBlockedDates() {
        service.blockPartial("2025-05-18", "실습실", "914", "10:00");
        service.blockPartial("2025-05-19", "실습실", "914", "11:00");

        List<String> dates = service.getBlockedDates();
        assertTrue(dates.contains("2025-05-18"));
        assertTrue(dates.contains("2025-05-19"));
    }

    @Test
    void testStaticGetBlockedDates() {
        ReservationServiceModel.staticGetBlockedDates(); // 실행만 정상적으로 되는지 확인
    }
}