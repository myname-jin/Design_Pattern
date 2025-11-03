/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package deu.hms.roomManagement;

/**
 *
 * @author Jimin
 */

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RoomRepository {
    private final String FILE_NAME = "roomInfo.txt";
    private final List<Room> roomList;

    public RoomRepository() {
        this.roomList = new ArrayList<>();
        loadRoomInfoFromFile();
    }

    public List<Room> getRoomList() {
        return Collections.unmodifiableList(roomList);
    }

    public Room findRoom(int floor, int roomNumber) {
        return roomList.stream()
                .filter(room -> room.getFloor() == floor && room.getRoomNumber() == roomNumber)
                .findFirst()
                .orElse(null);
    }

    public void addRoom(Room room) {
        if (findRoom(room.getFloor(), room.getRoomNumber()) != null) {
            throw new IllegalArgumentException("이미 존재하는 객실입니다.");
        }
        roomList.add(room);
        saveRoomInfoToFile();
    }

    public void deleteRoom(int floor, int roomNumber) {
        Room room = findRoom(floor, roomNumber);
        if (room != null) {
            roomList.remove(room);
            saveRoomInfoToFile();
        } else {
            throw new IllegalArgumentException("삭제할 객실을 찾을 수 없습니다.");
        }
    }

    public void updateRoom(Room room, int newPrice, String newGrade, int newCapacity) {
        Room targetRoom = findRoom(room.getFloor(), room.getRoomNumber());
        if (targetRoom != null) {
            targetRoom.setPrice(newPrice);
            targetRoom.setGrade(newGrade);
            targetRoom.setCapacity(newCapacity);
            saveRoomInfoToFile();
        } else {
            throw new IllegalArgumentException("수정할 객실을 찾을 수 없습니다.");
        }
    }

    public void saveRoomInfoToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Room room : roomList) {
                writer.write(room.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("객실 정보 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void loadRoomDataToTable(javax.swing.JTable roomTable) {
        DefaultTableModel model = (DefaultTableModel) roomTable.getModel();
        model.setRowCount(0);
        for (Room room : roomList) {
            model.addRow(new Object[]{room.getFloor(), room.getRoomNumber(), room.getPrice(), room.getGrade(), room.getCapacity()});
        }
    }

    private void loadRoomInfoFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("객실 정보 파일이 존재하지 않습니다. 새 파일을 생성합니다.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    int floor = Integer.parseInt(data[0].trim());
                    int roomNumber = Integer.parseInt(data[1].trim());
                    int price = Integer.parseInt(data[2].trim());
                    String grade = data[3].trim();
                    int capacity = Integer.parseInt(data[4].trim());
                    roomList.add(new Room(floor, roomNumber, price, grade, capacity));
                } else {
                    System.out.println("잘못된 데이터 형식: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 문제가 발생했습니다: " + e.getMessage());
        }
    }
        public void saveTableDataToFile(DefaultTableModel model) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            // 테이블의 각 행 데이터를 파일에 작성
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder rowBuilder = new StringBuilder();
                
                for (int j = 0; j < model.getColumnCount(); j++) {
                    rowBuilder.append(model.getValueAt(i, j).toString()); // 각 셀의 값을 가져오기
                    if (j < model.getColumnCount() - 1) {
                        rowBuilder.append(","); // 쉼표로 구분
                    }
                }
                
                bufferedWriter.write(rowBuilder.toString()); // 행 데이터를 파일에 작성
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "파일 저장 중 오류가 발생했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
