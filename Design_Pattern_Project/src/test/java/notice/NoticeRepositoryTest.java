/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package notice;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class NoticeRepositoryTest {

    private static final String FILE = "test_repo.txt";
    private NoticeRepository repo;

    @BeforeEach
    public void setup() {
        repo = new NoticeRepository(FILE);
    }

    @AfterEach
    public void cleanup() {
        try {
            Files.deleteIfExists(Paths.get(FILE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveAndLoad() {
        List<Notice> input = Arrays.asList(
            new Notice("공지 A", "일반", false),
            new Notice("공지 B", "중요", true)
        );

        repo.saveNotices(input);
        List<Notice> loaded = repo.loadNotices();

        assertEquals(2, loaded.size());
        assertEquals("공지 A", loaded.get(0).getContent());
        assertTrue(loaded.get(1).isRead());
    }
}