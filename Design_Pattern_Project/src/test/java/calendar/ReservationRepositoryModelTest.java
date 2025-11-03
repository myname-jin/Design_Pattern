/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package calendar;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationRepositoryModelTest {

    private ReservationRepositoryModel repository;
    private static final Path FILE_PATH = Paths.get("src", "main", "resources", "blocked_dates.txt");

    @BeforeEach
    void setUp() throws IOException {
        repository = new ReservationRepositoryModel();
        Files.deleteIfExists(FILE_PATH); // 초기화
        Files.createDirectories(FILE_PATH.getParent());
        Files.createFile(FILE_PATH);
    }

    @Test
    void testSaveRecordAndLoadAll() {
        repository.saveRecord("일부", "2025-05-18", "실습실", "913", "09:00");
        List<String> records = repository.loadAll();

        assertEquals(1, records.size());
        assertTrue(records.get(0).contains("2025-05-18"));
        assertEquals("일부,2025-05-18,실습실,913,09:00", records.get(0));
    }

    @Test
    void testLoadAllWhenFileDoesNotExist() throws IOException {
        Files.deleteIfExists(FILE_PATH);
        List<String> records = repository.loadAll();
        assertNotNull(records);
        assertTrue(records.isEmpty());
    }
}