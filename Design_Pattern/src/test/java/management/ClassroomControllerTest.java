package management;

import org.junit.jupiter.api.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassroomController 단위 테스트
 * (강의실 추가, 중복 방지, 수정, 삭제 로직 검증)
 */
public class ClassroomControllerTest {

    private DefaultTableModel tableModel;
    private ClassroomController controller;
    private Path tempFile;

    @BeforeEach
   void setUp() throws Exception {
        // 1. 임시 파일 생성
        tempFile = Files.createTempFile("test_classroom", ".txt");

        tableModel = new DefaultTableModel(new String[]{"Room", "Info"}, 0);
        controller = new ClassroomController(tableModel);

        // 2. 파일 경로 강제 교체
        Field field = ClassroomController.class.getDeclaredField("FILE_PATH");
        field.setAccessible(true);
        
        try {
            field.set(null, tempFile.toString());
        } catch (Exception e) {
            System.err.println("❌ 파일 경로 교체 실패! 실제 파일이 덮어씌워질 위험이 있습니다.");
            // 교체 실패하면 아예 테스트가 실패하도록 막아야 데이터가 안전함
            fail("테스트 환경 설정 실패: ClassroomController의 FILE_PATH를 변경할 수 없습니다. (final 키워드를 제거하세요)");
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testAddClassroom() {
        ClassroomModel classroom = new ClassroomModel("913", "9층 / 30명 / 화이트보드");
        String result = controller.addClassroom(classroom);
        assertNull(result, "강의실 추가는 성공해야 합니다.");
        List<ClassroomModel> list = controller.getClassroomList();
        assertEquals(1, list.size());
        assertEquals("913", list.get(0).getRoom());
        assertEquals("9층 / 30명 / 화이트보드", list.get(0).getInfo());
    }

    @Test
    void testPreventDuplicateClassroom() {
        ClassroomModel classroom1 = new ClassroomModel("913", "기본 정보");
        ClassroomModel classroom2 = new ClassroomModel("913", "중복된 호실 정보");

        controller.addClassroom(classroom1);
        String errorMsg = controller.addClassroom(classroom2); // 중복 추가 시도

        assertNotNull(errorMsg, "중복된 강의실 추가 시 에러 메시지가 반환되어야 합니다.");
        
        List<ClassroomModel> list = controller.getClassroomList();
        assertEquals(1, list.size(), "중복된 데이터는 저장되지 않아야 합니다.");
        assertEquals("기본 정보", list.get(0).getInfo(), "처음 저장된 데이터가 유지되어야 합니다.");
    }

    @Test
    void testUpdateClassroom() {
        ClassroomModel classroom = new ClassroomModel("202", "초기 정보");
        controller.addClassroom(classroom);

        ClassroomModel updated = new ClassroomModel("202", "수정된 정보");
        controller.updateClassroom(updated);

        List<ClassroomModel> list = controller.getClassroomList();
        assertEquals(1, list.size());
        assertEquals("수정된 정보", list.get(0).getInfo(), "정보가 업데이트되어야 합니다.");
    }

    @Test
    void testDeleteClassroom() {
        controller.addClassroom(new ClassroomModel("303", "3층"));
        controller.deleteClassroom("303");

        List<ClassroomModel> list = controller.getClassroomList();
        assertEquals(0, list.size(), "삭제 후 리스트는 비어있어야 합니다.");
    }

    @Test
    void testDeleteNonExistentClassroom() {
        controller.addClassroom(new ClassroomModel("404", "4층"));

        // 없는 강의실 삭제 시도
        controller.deleteClassroom("999");

        // 기존 데이터 유지 확인
        List<ClassroomModel> list = controller.getClassroomList();
        assertEquals(1, list.size());
        assertEquals("404", list.get(0).getRoom());
    }
}