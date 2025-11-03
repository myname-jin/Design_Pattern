/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package notice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NoticeTest {

    @Test
    public void testSerializationAndDeserialization() {
        Notice original = new Notice("내용입니다", "중요", true);
        String serialized = original.serialize();

        Notice result = Notice.deserialize(serialized);
        assertEquals("내용입니다", result.getContent());
        assertEquals("중요", result.getCategory());
        assertTrue(result.isRead());
    }

    @Test
    public void testToStringFormat() {
        Notice unread = new Notice("안읽은 공지", "일반", false);
        assertTrue(unread.toString().contains("[안읽음]"));

        Notice read = new Notice("읽은 공지", "일반", true);
        assertFalse(read.toString().contains("[안읽음]"));
    }
}