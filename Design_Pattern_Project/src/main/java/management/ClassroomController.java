/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

/**
 *
 * @author suk22
 */
public class ClassroomController {

    private DefaultTableModel tableModel;
    private String filePath;
    private static final String DEFAULT_FILE_PATH = "src/main/resources/classroom.txt";

    public ClassroomController(DefaultTableModel tableModel) {
        this(tableModel, DEFAULT_FILE_PATH);
    }

    public ClassroomController(DefaultTableModel tableModel, String filePath) {
        this.tableModel = tableModel;
        this.filePath = filePath;
    }

    public String addClassroom(ClassroomModel classroom) {
        List<ClassroomModel> existing = getClassroomList();
        for (ClassroomModel c : existing) {
            if (c.getRoom().equals(classroom.getRoom())) {
                return "이미 존재하는 강의실입니다: " + classroom.getRoom();
            }
        }

        tableModel.addRow(new Object[]{
            classroom.getRoom(),
            classroom.getLocation(),
            classroom.getCapacity(),
            classroom.getNote()
        });

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(classroom.toFileString());
            writer.newLine();
        } catch (IOException e) {
            return "파일 저장 중 오류 발생: " + e.getMessage();
        }

        return "강의실이 추가되었습니다: " + classroom.getRoom();
    }

    public List<ClassroomModel> getClassroomList() {
        List<ClassroomModel> classrooms = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 4) {
                    classrooms.add(new ClassroomModel(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 개발 중 로그용
        }

        return classrooms;
    }

    public String updateClassroom(ClassroomModel updatedClassroom) {
        List<ClassroomModel> classrooms = getClassroomList();
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (ClassroomModel c : classrooms) {
                if (c.getRoom().equals(updatedClassroom.getRoom())) {
                    writer.write(updatedClassroom.toFileString());
                    found = true;
                } else {
                    writer.write(c.toFileString());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            return "파일 저장 중 오류 발생: " + e.getMessage();
        }

        return found ? "강의실이 수정되었습니다: " + updatedClassroom.getRoom()
                : "수정할 강의실을 찾을 수 없습니다: " + updatedClassroom.getRoom();
    }

    public String deleteClassroom(String room) {
        List<ClassroomModel> classrooms = getClassroomList();
        boolean found = false;

        List<ClassroomModel> updatedClassrooms = new ArrayList<>();
        for (ClassroomModel c : classrooms) {
            if (c.getRoom().equals(room)) {
                found = true;
            } else {
                updatedClassrooms.add(c);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (ClassroomModel c : updatedClassrooms) {
                writer.write(c.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            return "파일 저장 중 오류 발생: " + e.getMessage();
        }

        if (found) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(room)) {
                    tableModel.removeRow(i);
                    break;
                }
            }
            return "강의실이 삭제되었습니다: " + room;
        } else {
            return "삭제할 강의실을 찾을 수 없습니다: " + room;
        }
    }
}
