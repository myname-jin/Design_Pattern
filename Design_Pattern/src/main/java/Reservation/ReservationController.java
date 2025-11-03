/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

//터미널 구현 파일(GUIController.java 파일로 볼 것)
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 *
 * @author scq37
 *
 */
public class ReservationController {  //예약 제어 클래스

    private static final String EXCEL_PATH = "src/main/resources/available_rooms.xlsx";
    private static final List<String> LAB_ROOMS = Arrays.asList("911", "915", "916");

    private List<RoomModel> allRooms = new ArrayList<>();
    private ConsoleView view = new ConsoleView(); //사용자 인터페이스 객체 생성

    public void start() {
        loadRoomsFromExcel();

        // 사용자 정보 입력
        String name = view.inputName();
        String userType = view.inputUserType();
        String userId = view.inputUserId();
        String department = view.inputDepartment();

        //1. 강의실 또는 실습실 중 하나 선택
        String type = view.selectRoomType().trim();
        System.out.println("선택한 유형: [" + type + "]");

        List<RoomModel> filteredRooms = new ArrayList<>();
        for (RoomModel room : allRooms) {
            if (type.equals("실습실") && LAB_ROOMS.contains(room.getName())) {
                filteredRooms.add(room);
            } else if (type.equals("강의실") && !LAB_ROOMS.contains(room.getName())) {
                filteredRooms.add(room);
            }
        }

        //System.out.println("엑셀 강의실 목록: " + filteredRooms);
        for (RoomModel r : filteredRooms) {
            System.out.println("- " + r.getName() + " (" + r.getType() + ")");
        }

        //3. 필터링된 강의실 없으면 종료
        if (filteredRooms.isEmpty()) {
            System.out.println("해당 유형에 대한 강의실이 없습니다.");
            return;
        }

        //강의실 목록 보여주기
        view.showRooms(filteredRooms.toArray(new RoomModel[0]));
        String selectedName = view.getInput();
        RoomModel selectedRoom = null;
        for (RoomModel room : filteredRooms) {
            if (room.getName().equalsIgnoreCase(selectedName.trim())) {
                selectedRoom = room;
                break;
            }
        }

        // 예약 상세 정보 입력
        view.showAvailableTimes(selectedRoom.getAvailableTimes());
        String date = view.inputDate();
        String startTime = view.inputStartTime();
        String endTime = view.inputEndTime();
        String purpose = view.inputPurpose();
        String status = "예약완료";

        // 결과 출력
        System.out.println("\n[예약 완료]");
        System.out.println("강의실: " + selectedRoom.getName());
        System.out.println("시간: " + startTime + " ~ " + endTime);

        // 파일 저장
        saveReservation(name, userType, userId, department,
                selectedRoom.getType(), selectedRoom.getName(),
                date, startTime, endTime, purpose, status);
    }

    private boolean isLabRoom(String roomNumber) {
        return roomNumber.equals("911")
                || roomNumber.equals("915")
                || roomNumber.equals("916")
                || roomNumber.equals("918"); // 새로 추가
    }

    private void loadRoomsFromExcel() {
        try (InputStream fis = new FileInputStream(EXCEL_PATH); Workbook workbook = new XSSFWorkbook(fis)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String roomName = sheet.getSheetName();
                List<String> availableTimes = new ArrayList<>();

                for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
                    Row row = sheet.getRow(rowIdx);
                    if (row == null) {
                        continue;
                    }

                    Cell timeCell = row.getCell(0);
                    if (timeCell == null || timeCell.getCellType() != CellType.STRING) {
                        continue;
                    }

                    String time = timeCell.getStringCellValue();
                    boolean isFree = false;

                    for (int col = 1; col < row.getLastCellNum(); col++) {
                        Cell cell = row.getCell(col);
                        if (cell != null && "비어있음".equals(cell.getStringCellValue().trim())) {
                            isFree = true;
                            break;
                        }
                    }

                    if (isFree) {
                        availableTimes.add(time);
                    }
                }

                if (!availableTimes.isEmpty()) {
                    RoomModel room = new RoomModel(roomName, LAB_ROOMS.contains(roomName) ? "실습실" : "강의실",
                            availableTimes.toArray(new String[0]));
                    allRooms.add(room);
                }
            }

        } catch (IOException e) {
            System.out.println("엑셀 파일 읽기 오류: " + e.getMessage());
        }
    }

    private void saveReservation(String name, String userType, String userId, String department,
            String roomType, String roomNumber,
            String date, String startTime, String endTime,
            String purpose, String status) {
        String filePath = "src/main/resources/reservation.txt";

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), "UTF-8"))) {

            writer.write(String.join(",", name, userType, userId, department,
                    roomType, roomNumber, date, startTime, endTime,
                    purpose, status));
            writer.newLine();

        } catch (IOException e) {
            System.out.println("예약 저장 실패: " + e.getMessage());
        }

    }

    public boolean isUserBanned(String studentId) {
        List<String> bannedUsers = getBannedUsers();
        return bannedUsers.contains(studentId);
    }
    
 private List<String> getBannedUsers() {
        List<String> bannedUsers = new ArrayList<>();
        String filePath = "src/main/resources/banlist.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                bannedUsers.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("제한 사용자 파일 읽기 실패: " + e.getMessage());
        }

        return bannedUsers;
    }
}
