/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import org.junit.jupiter.api.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author suk22
 */
public class ClassroomControllerTest {

    private DefaultTableModel tableModel;
    private ClassroomController controller;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tableModel = new DefaultTableModel(new String[]{"Room", "Location", "Capacity", "Note"}, 0);
        tempFile = Files.createTempFile("classroom", ".txt");

        // 경로를 주입받는 생성자 사용
        controller = new ClassroomController(tableModel, tempFile.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testAddClassroom() {
        ClassroomModel classroom = new ClassroomModel("913", "9층", "30", "화이트보드 있음");

        controller.addClassroom(classroom);

        assertEquals(1, tableModel.getRowCount());
        assertEquals("913", tableModel.getValueAt(0, 0));

        List<ClassroomModel> list = controller.getClassroomList();
        assertEquals(1, list.size());
        assertEquals("913", list.get(0).getRoom());
    }

    @Test
    void testPreventDuplicateClassroom() {
        ClassroomModel classroom1 = new ClassroomModel("913", "9층", "30", "");
        ClassroomModel classroom2 = new ClassroomModel("913", "9층", "40", "빔 있음");

        controller.addClassroom(classroom1);
        controller.addClassroom(classroom2);

        List<ClassroomModel> list = controller.getClassroomList();
        assertEquals(1, list.size());
    }

    @Test
    void testUpdateClassroom() {
        ClassroomModel classroom = new ClassroomModel("202", "2층", "40", "");
        controller.addClassroom(classroom);

        ClassroomModel updated = new ClassroomModel("202", "2층", "50", "업데이트됨");
        controller.updateClassroom(updated);

        List<ClassroomModel> list = controller.getClassroomList();
        assertEquals(1, list.size());
        assertEquals("50", list.get(0).getCapacity());
        assertEquals("업데이트됨", list.get(0).getNote());
    }

    @Test
    void testDeleteClassroom() {
        ClassroomModel classroom = new ClassroomModel("303", "3층", "35", "");
        controller.addClassroom(classroom);

        controller.deleteClassroom("303");

        List<ClassroomModel> list = controller.getClassroomList();
        assertEquals(0, list.size());
        assertEquals(0, tableModel.getRowCount());
    }

    @Test
    void testDeleteNonExistentClassroom() {
        ClassroomModel classroom = new ClassroomModel("404", "4층", "40", "");
        controller.addClassroom(classroom);

        controller.deleteClassroom("999");

        List<ClassroomModel> list = controller.getClassroomList();
        assertEquals(1, list.size());
    }
}
