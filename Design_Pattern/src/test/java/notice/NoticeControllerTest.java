/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package notice;

import org.junit.jupiter.api.*;
import java.util.*;
import javax.swing.*;
import static org.junit.jupiter.api.Assertions.*;

public class NoticeControllerTest {

    private NoticeController controller;
    private MockListView listView;
    private MockEditorView editorView;
    private NoticeModel model;

    @BeforeEach
    public void setup() {
        listView = new MockListView();
        editorView = new MockEditorView();

        NoticeRepository repo = new NoticeRepository("test_ctrl.txt");
        model = new NoticeModel(repo);
        model.addNotice("중요공지", "중요");
        model.addNotice("일반공지", "일반");
        model.addNotice("읽은공지", "일반");
        model.toggleRead(2);  // 3번째 공지를 읽음으로 표시

        controller = new NoticeController(model, listView, editorView);
    }

    @AfterEach
    public void cleanup() {
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("test_ctrl.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFilterCategory_all() {
        listView.setFilter("전체");
        listView.setSearchText("");
        controller.refreshList();

        assertEquals(3, listView.getRendered().size());
    }

    @Test
    public void testFilterCategory_importantOnly() {
        listView.setFilter("중요");
        listView.setSearchText("");
        controller.refreshList();

        List<Notice> rendered = listView.getRendered();
        assertEquals(1, rendered.size());
        assertEquals("중요공지", rendered.get(0).getContent());
    }

    @Test
    public void testFilterCategory_unreadOnly() {
        listView.setFilter("안읽음");
        listView.setSearchText("");
        controller.refreshList();

        List<Notice> rendered = listView.getRendered();
        assertEquals(2, rendered.size()); // 1개는 읽음 처리되어 제외
    }

    @Test
    public void testSearchKeyword() {
        listView.setFilter("전체");
        listView.setSearchText("일반");
        controller.refreshList();

        List<Notice> rendered = listView.getRendered();
        assertEquals(1, rendered.size()); // ✅ 2 → 1로 수정
        for (Notice n : rendered) {
            assertTrue(n.getContent().contains("일반"));
        }
    }

    // ---------------- Mock 클래스 ----------------
    static class MockListView extends NoticeListView {

        private String search = "";
        private String filter = "전체";
        private List<Notice> rendered = new ArrayList<>();

        public void setSearchText(String text) {
            this.search = text;
        }

        public void setFilter(String cat) {
            this.filter = cat;
        }

        @Override
        public String getSearchText() {
            return search;
        }

        @Override
        public String getFilterCategory() {
            return filter;
        }

        @Override
        public void setNotices(List<Notice> notices) {
            this.rendered = notices;
        }

        public List<Notice> getRendered() {
            return rendered;
        }
    }

    static class MockEditorView extends NoticeEditorView {

        public MockEditorView() {
            super(new JFrame());  // 필수: 컴파일 오류 방지
        }

        @Override
        public void showEditor(String content, String category) {
            // 테스트 목적이므로 아무것도 하지 않음
        }
    }
}
