package management;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ClassroomController {

    private DefaultTableModel tableModel;
    private static String FILE_PATH = "src/main/resources/classroom.txt";

    public ClassroomController(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    // 1. 목록 불러오기
    public List<ClassroomModel> getClassroomList() {
        List<ClassroomModel> classrooms = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return classrooms;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2); 
                if (parts.length >= 2) {
                    classrooms.add(new ClassroomModel(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classrooms;
    }

    // 2. 저장하기
    private void saveToFile(List<ClassroomModel> list) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            for (ClassroomModel c : list) {
                writer.write(c.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 3. 추가
    public String addClassroom(ClassroomModel newRoom) {
        List<ClassroomModel> list = getClassroomList();
        for (ClassroomModel c : list) {
            if (c.getRoom().equals(newRoom.getRoom())) {
                return "강의실 정보가 이미 존재합니다: " + newRoom.getRoom();
            }
        }
        list.add(newRoom);
        saveToFile(list);
        return null; 
    }

    // 4. 수정
    public void updateClassroom(ClassroomModel updatedRoom) {
        List<ClassroomModel> list = getClassroomList();
        boolean found = false;
        
        for (ClassroomModel c : list) {
            if (c.getRoom().equals(updatedRoom.getRoom())) {
                c.setInfo(updatedRoom.getInfo());
                found = true;
                break;
            }
        }
        
        if (found) {
            saveToFile(list);
            JOptionPane.showMessageDialog(null, "수정되었습니다.");
        }
    }

    // 5. 삭제
    public void deleteClassroom(String roomName) {
        List<ClassroomModel> list = getClassroomList();
        boolean removed = list.removeIf(c -> c.getRoom().equals(roomName));
        
        if (removed) {
            saveToFile(list);
            JOptionPane.showMessageDialog(null, "삭제되었습니다.");
        }
    }
}