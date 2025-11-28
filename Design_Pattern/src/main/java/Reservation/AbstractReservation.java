/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

import UserFunction.UserReservationCancelModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import management.AdminReservationModel;
import management.CancelCommand;
import management.NotificationManager;
import management.Reservation;
import management.ReservationCommand;
import management.ReservationObserver;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author namw2
 */
public abstract class AbstractReservation {

    protected ReservationView view; // 사용자 인터페이스 뷰 (GUI)
    private static final String EXCEL_PATH = "src/main/resources/available_rooms.xlsx";
    private static final List<String> LAB_ROOMS = Arrays.asList("911", "915", "916", "918");

    private List<RoomModel> allRooms = new ArrayList<>(); //로드된 강의실 목록
    public Workbook workbook; // 엑셀 파일 워크북 객체

    private BufferedReader in; //
    private BufferedWriter out;

    private String date;
    private List<String> times;
    private String startTime;
    private String purpose;
    private String time;
    private String selectedRoomName;
    private RoomModel selectedRoom;

    private UserReservationCancelModel model = new UserReservationCancelModel();

    private List<ReservationObserver> observers = new ArrayList<>();
    private NotificationManager notiManager = new NotificationManager();

    public final void doReservation(String userId, String userType, String userName, String userDept, String date, List<String> times, String purpose, String time, String selectedRoomName, ReservationView view) {
        this.view = view;
        loadRoomsFromExcel();
        getUserInfo(date, times, purpose, time, selectedRoomName);

        //선택 검사
        if (checkAllSelected(date, times, purpose, time) == false) {
            return;
        }

        //차단된 유저 검사
        if (isUserBanned(userId, userType) == true) {
            view.showMessage("제한된 사용자입니다.");
            return;
        }

        // 3. 시간표 충돌 및 처리 로직 (Hook) - 여기가 핵심 변경점!
        // 학생: 중복이면 실패 / 교수: 중복이면 기존 예약 취소 후 진행
        if (!processTimeSlotConflict(userId, date, times, selectedRoomName)) {
            return;
        }

        // 4. 사용자별 제약 조건 확인 (Hook)
        // 학생: 2시간 제한 등 / 교수: 제한 없음
        if (!checkUserConstraints(userId, date, times)) {
            return;
        }
        
        // 5. 예약 저장 실행 (우선 예약 로직 포함)
        saveReservationsForTimes(times, selectedRoomName, date, purpose, userName, userType, userId, userDept);

    }

    protected abstract boolean isUserBanned(String userId, String userType);

    protected abstract boolean processTimeSlotConflict(String userId, String date, List<String> times, String roomName);

    protected abstract boolean checkUserConstraints(String userId, String date, List<String> times);

    protected abstract String confirmReservation();

    // 교수님이 학생 예약을 밀어낼 때 호출되는 메서드
    protected void ahandleCancelConfirm() {
        for (String selectedTime : times) {
            String[] split = selectedTime.split("~");
            if (split.length == 2) {
                String startTime = split[0].trim();
                String canceledStudentId = cancelStudentReservation(date, selectedRoomName, startTime);

                if (canceledStudentId != null) {
                    String reason = "교수님 보강/세미나 우선 예약으로 인한 취소";
                    
                    model.saveCancelReason(canceledStudentId, reason);
                    String msg = String.format("[%s %s] %s", date, selectedRoomName, reason);
                    notiManager.sendNotification(canceledStudentId, msg);
                    
                    System.out.println("[Priority] 학생(" + canceledStudentId + ") 예약 취소 완료");
                }
            }
        }
    }

    // 학생 예약을 파일에서 찾아 '취소' 상태로 변경
    private String cancelStudentReservation(String date, String room, String startTime) {
        String filePath = "src/main/resources/reservation.txt";
        List<String> allLines = new ArrayList<>();
        String targetStudentId = null;
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    String rId = parts[0].trim();
                    String rType = parts[1].trim();
                    String rRoom = parts[5].trim();
                    String rDate = parts[6].trim();
                    String rStart = parts[8].trim();
                    String rStatus = parts[11].trim();

                    // 조건: 학생이고, 날짜/장소/시간 일치하고, 이미 취소된 게 아니어야 함
                    if ("학생".equals(rType) && rRoom.equals(room) && rDate.equals(date) && rStart.equals(startTime) 
                            && !"취소".equals(rStatus) && !"거절".equals(rStatus)) {
                        
                        targetStudentId = rId; // 학번 저장
                        parts[11] = "취소";    // 상태 변경
                        line = String.join(",", parts); // 줄 업데이트
                        found = true;
                    }
                }
                allLines.add(line);
            }
        } catch (IOException e) { return null; }

        // 파일 덮어쓰기
        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
                for (String line : allLines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) { return null; }
        }

        return targetStudentId; // 취소된 학생 학번 반환 (없으면 null)
    }

    protected int calculateTotalDuration(List<String> times) {
        int total = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (String time : times) {
            try {
                String[] parts = time.split("~");
                Date start = sdf.parse(parts[0]);
                Date end = sdf.parse(parts[1]);
                long diff = (end.getTime() - start.getTime()) / (1000 * 60);
                total += diff;
            } catch (ParseException e) {
                System.out.println("시간 파싱 오류: " + time);
            }
        }
        return total;
    }

    protected boolean isUserAlreadyReserved(String userId, String date) {
        String path = "src/main/resources/reservation.txt";
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    if (parts[0].equals(userId) && parts[6].equals(date)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("예약 기록 읽기 실패: " + e.getMessage());
        }
        return false;
    }

    protected boolean isTimeSlotAlreadyReserved(String roomName, String date, List<String> newTimes, String userId) {
        String path = "src/main/resources/reservation.txt";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    String reservedUserId = parts[2];
                    String reservedRoom = parts[5];
                    String reservedDate = parts[6];
                    String reservedStart = parts[8];
                    String reservedEnd = parts[9];

                    if (reservedUserId.equals(userId)) {
                        continue;
                    }

                    if (reservedRoom.equals(roomName) && reservedDate.equals(date)) {
                        Date reservedStartTime = sdf.parse(reservedStart);
                        Date reservedEndTime = sdf.parse(reservedEnd);

                        for (String timeSlot : newTimes) {
                            String[] range = timeSlot.split("~");
                            if (range.length == 2) {
                                Date newStartTime = sdf.parse(range[0].trim());
                                Date newEndTime = sdf.parse(range[1].trim());

                                // 중복 조건: 시작 시간이 기존 예약의 끝 이전 && 끝 시간이 기존 예약의 시작 이후
                                if (newStartTime.before(reservedEndTime) && newEndTime.after(reservedStartTime)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("중복 시간 검사 오류: " + e.getMessage());
        }

        return false;
    }

    /**
     * 학생 = true 나머지 = false
     *
     * @return
     */
    protected boolean isUserTypeStudent(String userType) {
        if (userType.equals("학생")) {
            return true;
        } else {
            return false;
        }
    }

    private void notifyObservers(List<Reservation> dataToShow) {
        for (ReservationObserver observer : observers) {
            observer.onReservationUpdated(dataToShow);
        }
    }

    private void loadRoomsFromExcel() {
        try (InputStream fis = new FileInputStream(EXCEL_PATH)) {
            workbook = new XSSFWorkbook(fis);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String roomName = sheet.getSheetName();
                RoomModel room = new RoomModel(roomName,
                        LAB_ROOMS.contains(roomName) ? "실습실" : "강의실",
                        new String[0]);
                allRooms.add(room);
            }
        } catch (IOException e) {
            System.out.println("엑셀 파일 읽기 오류: " + e.getMessage());
        }
    }

    private void getUserInfo(String date, List<String> times, String purpose, String time, String selectedRoomName) {
        this.date = date;
        this.times = times; // 선택된 모든 시간 슬롯 (리스트)
        this.time = time;
        this.purpose = purpose;
        this.selectedRoomName = selectedRoomName;
        this.selectedRoom = getRoomByName(selectedRoomName);
    }

    private boolean checkAllSelected(String date, List<String> times, String purpose, String time) {
        if (this.date.isEmpty() || this.purpose.isEmpty() || selectedRoom == null) {
            view.showMessage("모든 항목을 입력해주세요.");
            return false;
        }
        return true;
    }

    private String getDayOfWeek(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY ->
                    "일";
                case Calendar.MONDAY ->
                    "월";
                case Calendar.TUESDAY ->
                    "화";
                case Calendar.WEDNESDAY ->
                    "수";
                case Calendar.THURSDAY ->
                    "목";
                case Calendar.FRIDAY ->
                    "금";
                case Calendar.SATURDAY ->
                    "토";
                default ->
                    "";
            };
        } catch (Exception e) {
            return "";
        }
    }

    private void saveReservationsForTimes(List<String> times, String selectedRoomName, String date, String purpose, String userName, String userType, String userId, String userDept) {
        
        String dayOfWeek = getDayOfWeek(date);
        String status = confirmReservation();
        RoomModel selectedRoom = getRoomByName(selectedRoomName);

        for (String selectedTime : times) {
            String[] split = selectedTime.split("~");
            if (split.length == 2) {
                String startTime = split[0].trim();
                String endTime = split[1].trim();

                RoomCapacity.getInstance().addReservation(selectedRoomName, date, startTime, endTime);

                saveReservation(userId, userType, userName, userDept,
                        selectedRoom.getType(), selectedRoom.getName(),
                        date, dayOfWeek, startTime, endTime, purpose, status);

                if (status.equals("예약대기")) {
                    view.showMessage("예약이 등록되었습니다. 관리자의 승인을 기다리는 중입니다.");
                } else if (status.equals("예약확정")) {
                    view.showMessage("예약이 확정되었습니다.");
                }

            }
        }
    }

    private void saveReservation(String userId, String userType, String name, String department,
            String roomType, String roomNumber,
            String date, String dayOfWeek, String startTime, String endTime,
            String purpose, String status) {
        String filePath = "src/main/resources/reservation.txt";
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), "UTF-8"))) {
            writer.write(String.join(",", userId, userType,name , department,
                    roomType, roomNumber, date, dayOfWeek, startTime, endTime,
                    purpose, status));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("예약 저장 실패: " + e.getMessage());
        }
    }

    private RoomModel getRoomByName(String name) {
        for (RoomModel r : allRooms) {
            if (r.getName().equals(name)) {
                return r;
            }
        }
        return null;
    }
}
