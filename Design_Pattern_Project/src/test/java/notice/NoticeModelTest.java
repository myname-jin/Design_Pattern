/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package notice;

/**
 *
 * @author adsd3
 */

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class NoticeModelTest {

    private static final String FILE = "test_model.txt";
    private NoticeModel model;

    @BeforeEach
    public void setup() {
        NoticeRepository repo = new NoticeRepository(FILE);
        model = new NoticeModel(repo);
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
    public void testAddNotice() {
        model.addNotice("새 공지", "중요");
        assertEquals(1, model.getNotices().size());
    }

    @Test
    public void testUpdateNotice() {
        model.addNotice("초기", "일반");
        model.updateNotice(0, "수정됨", "중요");

        Notice updated = model.getNotices().get(0);
        assertEquals("수정됨", updated.getContent());
        assertEquals("중요", updated.getCategory());
    }

    @Test
    public void testDeleteNotice() {
        model.addNotice("삭제할 공지", "일반");
        model.deleteNotice(0);

        assertTrue(model.getNotices().isEmpty());
    }

    @Test
    public void testToggleRead() {
        model.addNotice("읽기 테스트", "일반");

        model.toggleRead(0);
        assertTrue(model.getNotices().get(0).isRead());

        model.toggleRead(0);
        assertFalse(model.getNotices().get(0).isRead());
    }
}