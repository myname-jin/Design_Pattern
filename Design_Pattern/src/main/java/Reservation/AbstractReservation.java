/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    private Socket socket;
    private BufferedReader in; //
    private BufferedWriter out;

    String date;
    private List<String> times;
    private String time;
    private String purpose;
    private String selectedRoomName;
    private RoomModel selectedRoom;

    /*
     public final void doReservation(String userId, String userType, String userName, String userDept, String date, List<String> times, String purpose, String time, String selectedRoomName, ReservationView view) {
        this.view = view;
        loadRoomsFromExcel();
        getUserInfo(date, times, purpose, time, selectedRoomName);
        
        if (checkAllSelected(date, times, purpose, time) == false) {
            return;
        }
        
        if (isUserBanned(userId, userType) == true) {
            return;
        }
        if (isTimeSlotAlreadyReserved(selectedRoomName, date, times, userId)) { // 🚨 반환 값 확인
            view.showMessage("선택한 시간대에 이미 예약이 존재합니다."); // 메시지 표시
            return; // 🛑 중복 시 예약 중단
        }
        if (isUserTypeStudent(userType) == true) {
            if (!studentConstraints(userId, date, times)) { // 🚨 반환 값 확인
                return; // 🛑 학생 제약 조건 불충족 시 예약 중단
            }
        }
        saveReservationsForTimes(times, selectedRoomName, date, purpose, userName, userType, userId, userDept);

    }
     */
    public final void doReservation(String userId, String userType, String userName, String userDept, String date, List<String> times, String purpose, String time, String selectedRoomName, ReservationView view) {
        this.view = view;
        loadRoomsFromExcel();
        getUserInfo(date, times, purpose, time, selectedRoomName);

        // 1. 요청 객체 생성
        ReservationRequest request = new ReservationRequest(userId, userType, date, times, selectedRoomName, purpose);

        // 2. 책임 연쇄 구성 (순서대로 연결)
        ReservationCheckHandler chain = new CheckAllSelectedHandler();
        chain.setNext(new CheckUserBannedHandler())
                .setNext(new CheckTimeSlotReservedHandler())
                .setNext(new CheckStudentConstraintsHandler());

        try {
            // 3. 검증 체인 실행
            chain.check(request);

            // 4. 모든 검증 통과 시 예약 저장 수행
            saveReservationsForTimes(times, selectedRoomName, date, purpose, userName, userType, userId, userDept);

        } catch (Exception e) {
            // 5. 어느 핸들러에서든 실패하면 예외 메시지를 View에 표시하고 중단
            view.showMessage(e.getMessage());
        }
    }

    /*
    public final void doReservation(String userId, String userType, String userName, String userDept, String date, List<String> times, String purpose, String time, String selectedRoomName, ReservationView view) {
        this.view = view;// -> 이렇게 해도 되나?
        loadRoomsFromExcel();
        getUserInfo(date, times, purpose, time, selectedRoomName);
        /*
        if (checkAllSelected(date, times, purpose, time) == false) {
            return;
        }
     */
 /*
        if (isUserBanned(userId, userType) == true) {
            return;
        }
        
        // RoomCapacity 예약 가능 여부 먼저 확인
        for (String selectedTime : times) {
            String[] split = selectedTime.split("~");
            if (split.length != 2) continue;

            String startTime = split[0].trim();
            String endTime = split[1].trim();

            if (!RoomCapacity.getInstance().canReserve(this.selectedRoomName, date, startTime, endTime)) {
                view.showMessage("해당 시간에 예약이 가득 찼습니다.");
                return;
            }
        }
        
        if (isTimeSlotAlreadyReserved(selectedRoomName, date, times, userId)) { // 🚨 반환 값 확인
            view.showMessage("선택한 시간대에 이미 예약이 존재합니다."); // 메시지 표시
            return; // 🛑 중복 시 예약 중단
        }
        if (isUserTypeStudent(userType) == true) {
            if (!studentConstraints(userId, date, times)) { // 🚨 반환 값 확인
                return; // 🛑 학생 제약 조건 불충족 시 예약 중단
            }
        }
        
        // RoomCapacity 예약 가능 여부 및 등록
        for (String selectedTime : times) {
            String[] split = selectedTime.split("~");
            if (split.length != 2) continue;

            String startTime = split[0].trim();
            String endTime = split[1].trim();

            // 수용 인원 50% 체크
            if (!RoomCapacity.getInstance().canReserve(this.selectedRoomName, date, startTime, endTime)) {
                view.showMessage("해당 시간에 예약이 가득 찼습니다.");
                return;
            }

            // 예약 등록 (RoomCapacity 반영)
            RoomCapacity.getInstance().addReservation(this.selectedRoomName, date, startTime, endTime);
        }
        
        saveReservationsForTimes(times, this.selectedRoomName, date, purpose, userName, userType, userId, userDept);
        
         // 결과 뷰 표시
        viewReservationResult(userType);
    }
    
    public final void doReservation(String userId, String userType, String userName, String userDept, String date, List<String> times, String purpose, String time, String selectedRoomName, ReservationView view) {
        this.view = view;
        loadRoomsFromExcel();
        getUserInfo(date, times, purpose, time, selectedRoomName);

        // 입력 체크
        if (checkAllSelected(date, times, purpose, time) == false) {
            return;
        }

        // 금지 사용자 체크
        if (isUserBanned(userId, userType)) {
            return;
        }

        // 본인 예약 중복 체크
        for (String selectedTime : times) {
            String[] split = selectedTime.split("~");
            if (split.length != 2) continue;

            String startTime = split[0].trim();
            String endTime = split[1].trim();

            if (isTimeSlotAlreadyReservedForUser(selectedRoomName, date, startTime, endTime, userId)) {
                view.showMessage("이미 해당 시간에 예약이 존재합니다.");
                return;
            }
        }

        // 학생 제약 체크
        if (isUserTypeStudent(userType) && !studentConstraints(userId, date, times)) {
            return;
        }

        // RoomCapacity 체크 및 등록
        for (String selectedTime : times) {
            String[] split = selectedTime.split("~");
            if (split.length != 2) continue;

            String startTime = split[0].trim();
            String endTime = split[1].trim();

            if (!RoomCapacity.getInstance().canReserve(selectedRoomName, date, startTime, endTime)) {
                view.showMessage("해당 시간에 예약이 가득 찼습니다.");
                return;
            }

            // 예약 카운트 등록
            RoomCapacity.getInstance().addReservation(selectedRoomName, date, startTime, endTime);
        }

        // 파일에 예약 저장
        saveReservationsForTimes(times, selectedRoomName, date, purpose, userName, userType, userId, userDept);

        // 결과 뷰 표시
        viewReservationResult(userType);
    }

        // 본인 기준으로만 중복 체크
        private boolean isTimeSlotAlreadyReservedForUser(String roomName, String date, String startTime, String endTime, String userId) {
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

                    if (!reservedUserId.equals(userId)) continue;
                    if (!reservedRoom.equals(roomName) || !reservedDate.equals(date)) continue;

                    Date reservedStartTime = sdf.parse(reservedStart);
                    Date reservedEndTime = sdf.parse(reservedEnd);
                    Date newStartTime = sdf.parse(startTime);
                    Date newEndTime = sdf.parse(endTime);

                    if (newStartTime.before(reservedEndTime) && newEndTime.after(reservedStartTime)) {
                        return true;
                    }
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("중복 시간 검사 오류: " + e.getMessage());
        }

        return false;
    }*/

 /*
- getUserInfo
- checkAllSelected
- abstract isUserBanned(userId)
- isTimeSlotAlreadyReserved(String roomName, String date, List<String> newTimes)
- abstract studentConstraints
- isUserTypeStudent		(hook)
- abstract confirmReservation
- viewReservationResult
- getDayOfWeek(date);
- saveReservationsForTimes
- saveReservation - 이건 위에거(saveReservationsForTimes)에 포함되게 만들기 
     */
    protected abstract boolean isUserBanned(String userId, String userType);

    protected abstract boolean studentConstraints(String userId, String date, List<String> times);

    protected abstract String confirmReservation(String userType);

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
        this.time = time; // (주의: 아래 로직에서 사용되지 않는 불필요 변수로 보임)
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
                    if (parts[2].equals(userId) && parts[6].equals(date)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("예약 기록 읽기 실패: " + e.getMessage());
        }
        return false;
    }

    private boolean isTimeSlotAlreadyReserved(String roomName, String date, List<String> newTimes, String userId) {
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

    private boolean isRoomAvailable(String roomName, String date, List<String> times) {
        for (String timeSlot : times) {
            String[] split = timeSlot.split("~");
            if (split.length == 2) {
                String start = split[0].trim();
                String end = split[1].trim();
                if (!RoomCapacity.getInstance().canReserve(roomName, date, start, end)) {
                    return false;
                }
            }
        }
        return true;
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

    private void viewReservationResult(String userType) { //TODO 뷰를 어디서 받아오던가 해야함ㄴ
        if (userType.equals("교수")) {
            view.showMessage("예약이 확정되었습니다.");
        } else {
            view.showMessage("예약이 등록되었습니다. 관리자의 승인을 기다리는 중입니다.");
        }
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
        //saveResrevation();
        String dayOfWeek = getDayOfWeek(date);
        String status = confirmReservation(userType);
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
                //view.showMessage(status + "상태입니다.");

            }
        }
    }

    private void saveReservation(String name, String userType, String userId, String department,
            String roomType, String roomNumber,
            String date, String dayOfWeek, String startTime, String endTime,
            String purpose, String status) {
        String filePath = "src/main/resources/reservation.txt";
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), "UTF-8"))) {
            writer.write(String.join(",", name, userType, userId, department,
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
