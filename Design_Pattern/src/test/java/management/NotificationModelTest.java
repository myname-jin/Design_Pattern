/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author suk22
 */
public class NotificationModelTest {

    private Path tempFile;

    @BeforeEach
    public void setup() throws IOException {
        tempFile = Files.createTempFile("test_reservation", ".txt");

        List<String> testData = Arrays.asList(
                "홍길동,학생,2023001,컴공,강의실,101,2025-05-24,금,10:00,12:00,수업,예약대기",
                "김영희,학생,2023002,컴공,강의실,102,2025-05-25,토,14:00,16:00,스터디,예약승인"
        );
        Files.write(tempFile, testData);
    }

    @AfterEach
    public void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testGetPendingReservations() {
        NotificationModel model = new NotificationModel(tempFile.toString());
        List<String> result = model.getPendingReservations();

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("홍길동"));
        assertTrue(result.get(0).contains("예약 대기"));
    }

    @Test
    public void testGetAllReservations() {
        NotificationModel model = new NotificationModel(tempFile.toString());
        List<String> result = model.getAllReservations();

        assertEquals(2, result.size());
        assertTrue(result.get(0).contains("홍길동"));
        assertTrue(result.get(1).contains("김영희"));
    }
}
