package Reservation;

import ServerClient.LogoutUtil;
import UserFunction.UserMainController;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.awt.event.*;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * ReservationGUIController
 * 강의실 예약 시스템의 GUI Controller 클래스
 * - 사용자 정보 처리
 * - 엑셀/텍스트 파일 기반 강의실 및 예약 관리
 * - View(ReservationView)와 연결하여 사용자와 상호작용
 * - 서버와의 통신(Socket, BufferedReader/Writer) 연동
 */

public class ReservationGUIController {

    private ReservationView view; // 사용자 인터페이스 뷰 (GUI)
    private static final String EXCEL_PATH = "src/main/resources/available_rooms.xlsx";
    private static final List<String> LAB_ROOMS = Arrays.asList("911", "915", "916", "918");

    private List<RoomModel> allRooms = new ArrayList<>(); //로드된 강의실 목록
    public Workbook workbook; // 엑셀 파일 워크북 객체

    private String userName;  //사용자 이름
    private String userId;  //사용자id
    private String userDept;    //사용자 학과
    private String userType; // "학생" 또는 "교수"

    private Socket socket;
    private BufferedReader in; //

    private BufferedWriter out;

//클라이언트-서버 연결 코드(로그인과 사용자 페이지 연결되면 주석 해제)
    public ReservationGUIController(String userId, String name, String dept, String type,
            Socket socket, BufferedReader in, BufferedWriter out) {
        this.userId = userId;
        this.userName = name;
        this.userDept = dept;
        this.userType = type;
        this.socket = socket;
        this.in = in;
        this.out = out;

        view = new ReservationView();
        
        // 서버에서 사용자 정보 불러오기
        initializeUserInfoFromServer();
        System.out.println("최종 유저 정보 - 이름: " + userName + ", 학과: " + userDept);

        view.setUserInfo(userName, userId, userDept);

        LogoutUtil.attach(view, userId, socket, out);

        initializeReservationFeatures();

        view.setVisible(true);

    }
    
    /**
     * [기본 생성자] - 테스트용
     * 서버 연결 없이 테스트 또는 임시 실행
     */
    
    public ReservationGUIController() {


        view = new ReservationView();
        view.setUserInfo(userName, userId, userDept);

        // userName이나 userDept가 비어 있으면 서버에 사용자 정보 요청
        if ((userName == null || userName.isEmpty()) || (userDept == null || userDept.isEmpty())) { 
            try { // 추가
                out.write("INFO_REQUEST:" + userId + "\n"); 
                out.flush(); // 추가
                String response = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine(); 
                if (response != null && response.startsWith("INFO_RESPONSE:")) { // 추가
                    String[] parts = response.substring("INFO_RESPONSE:".length()).split(","); 
                    if (parts.length >= 3) { 
                        this.userName = parts[1]; 
                        this.userDept = parts[2]; 
                        view.setUserInfo(this.userName, userId, this.userDept); // 추가
                    } // 추가
                } // 추가
            } catch (IOException e) { // 추가
                System.out.println(" 사용자 정보 요청 오류: " + e.getMessage()); // 추가
            } // 추가
        } // 추가
        initializeReservationFeatures();
        view.setVisible(true);
    }
    
      /**
     * 예약 기능 초기화:
     * - 강의실 목록, 버튼 이벤트, 시간대 표시, UI 구성 등
     */
    
    private void initializeReservationFeatures() {

        if (userType.equals("교수")) {
            view.enableProfessorMode(); // View 내부에서 교수 전용 UI 구역 활성화
            view.setPurposeOptions(List.of("강의 전용", "세미나", "기타"));  // 교수 전용
        } else {
            view.setPurposeOptions(List.of("스터디", "동아리 활동", "면담", "팀 회의"));  // 학생용
        }

        loadRoomsFromExcel();
        // 강의실 유형 선택시 → 해당 방 목록 표시
        view.setRoomTypeList(Arrays.asList("강의실", "실습실"));

        view.addRoomTypeSelectionListener(e -> {
            String selectedType = view.getSelectedRoomType();
            List<String> filtered = allRooms.stream()
                    .filter(r -> r.getType().equals(selectedType))
                    .map(RoomModel::getName)
                    .collect(Collectors.toList());
            view.setRoomList(filtered);
        });

        // 날짜 or 강의실 선택 변경 시 시간대 갱신
        ActionListener timeUpdateListener = e -> {
            updateAvailableTimes();  // 예약 가능한 시간대 갱신
            String selectedRoom = view.getSelectedRoom();
            if (selectedRoom != null && !selectedRoom.isEmpty()) {
                String roomInfo = getRoomInfo(selectedRoom);  // 강의실 정보 가져오기
                view.setRoomInfoText(roomInfo);               // View에 표시
            }
        };
        view.addRoomSelectionListener(timeUpdateListener);
        view.addDateSelectionListener(timeUpdateListener);

        // 예약 버튼 이벤트 처리
        view.addReserveButtonListener(e -> {
            String date = view.getSelectedDate();
            List<String> times = view.getSelectedTimes();
            String time = view.getSelectedTime();
            String purpose = view.getSelectedPurpose();
            String selectedRoomName = view.getSelectedRoom();
            RoomModel selectedRoom = getRoomByName(selectedRoomName);

            if (date.isEmpty() || time.isEmpty() || purpose.isEmpty() || selectedRoom == null) {
                view.showMessage("모든 항목을 입력해주세요.");
                return;
            }

            // 예약 제한 사용자 체크 추가 부분
            ReservationController controller = new ReservationController();
            if (controller.isUserBanned(userId)) {
                view.showMessage("해당 사용자는 예약이 제한되어 있습니다. 관리자에게 문의하세요.");
                return;
            }

            // ✅ 시간 중복 체크 (교수/학생 공통)
            if (isTimeSlotAlreadyReserved(selectedRoomName, date, times)) {
                view.showMessage("선택한 시간대에 이미 예약이 존재합니다.");
                return;
            }

            // ✅ 학생만 예약 제한 적용
            if (userType.equals("학생")) {
                if (isUserAlreadyReserved(userId, date)) {
                    view.showMessage("학생은 하루 1회만 예약할 수 있습니다.");
                    return;
                }

                int totalMinutes = calculateTotalDuration(times);
                if (totalMinutes > 120) {
                    view.showMessage("총 예약 시간이 2시간(120분)을 초과할 수 없습니다.");
                    return;
                }
            }

            String status = userType.equals("교수") ? "예약확정" : "예약대기";

            if (userType.equals("교수")) {
                view.showMessage("예약이 확정되었습니다.");
            } else {
                view.showMessage("예약이 등록되었습니다. 관리자의 승인을 기다리는 중입니다.");
            }

            String dayOfWeek = getDayOfWeek(date);

            for (String selectedTime : times) {
                String[] split = selectedTime.split("~");
                if (split.length == 2) {
                    String startTime = split[0].trim();
                    String endTime = split[1].trim();

                    saveReservation(userName, userType, userId, userDept,
                            selectedRoom.getType(), selectedRoom.getName(),
                            date, dayOfWeek, startTime, endTime, purpose, status);
                }
            }
        });

        view.addBackButtonListener(e -> {
            view.dispose();  // 현재 ReservationView 닫기

            // UserMainController 생성 (기존 로그인 정보 전달)
            new UserMainController(userId, userType, socket, in, out);
        });

        view.setVisible(true);
    }
    
    /**
     * classroom.txt에서 강의실 정보 가져오기 (위치, 인원, 비고)
     */
    
    public String getRoomInfo(String roomName) {
        String filePath = "src/main/resources/classroom.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 4 && parts[0].equals(roomName)) {
                    return String.format("%s, %s, %s", parts[1], parts[2], parts[3]); // 위치, 인원, 비고
                }
            }
        } catch (IOException e) {
            System.out.println("강의실 정보 읽기 실패: " + e.getMessage());
        }
        return "정보 없음";
    }
    
    /**
     * 날짜/강의실 선택 시 → 선택 가능한 시간대 갱신
     */
    
    private void updateAvailableTimes() {
        String date = view.getSelectedDate();
        String roomName = view.getSelectedRoom();
        if (date == null || date.isEmpty() || roomName == null) {
            return;
        }

        int dayCol = getDayColumnIndex(date);
        

        Sheet sheet = workbook.getSheet(roomName);
        List<String> availableTimes = getAvailableTimesByDay(sheet, dayCol);

        view.clearTimeSlots();
        for (String time : availableTimes) {
            view.addTimeSlot(time, e -> {
                int total = calculateTotalDuration(view.getSelectedTimes());
                view.setTotalDuration(total + "분");
            });
        }
    }
    
    /**
     * 선택한 시간대들의 총 예약 시간 계산 (분 단위)
     */

    public int calculateTotalDuration(List<String> times) {
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
    
     /**
     * 사용자가 이미 예약했는지 확인 (하루 1회 제한) - 학생만
     */

    public boolean isUserAlreadyReserved(String userId, String date) {
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
    
    /**
     * 선택한 시간대에 중복 예약이 있는지 확인
     */

    public boolean isTimeSlotAlreadyReserved(String roomName, String date, List<String> newTimes) {
        String path = "src/main/resources/reservation.txt";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    String reservedRoom = parts[5];
                    String reservedDate = parts[6];
                    String reservedStart = parts[8];
                    String reservedEnd = parts[9];

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
     * 강의실 이름으로 RoomModel 객체 반환
     */
    
    private RoomModel getRoomByName(String name) {
        for (RoomModel r : allRooms) {
            if (r.getName().equals(name)) {
                return r;
            }
        }
        return null;
    }
    
    /**
     * Excel 파일에서 강의실 목록 로드 (available_rooms.xlsx)
     */
    
    public void loadRoomsFromExcel() {
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
    
    /**
     * 날짜 → 요일 열 인덱스 변환 (1~7)
     */
    
    public int getDayColumnIndex(String selectedDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(selectedDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return switch (dayOfWeek) {
                case Calendar.MONDAY ->
                    1;
                case Calendar.TUESDAY ->
                    2;
                case Calendar.WEDNESDAY ->
                    3;
                case Calendar.THURSDAY ->
                    4;
                case Calendar.FRIDAY ->
                    5;
                case Calendar.SATURDAY ->
                    6;
                case Calendar.SUNDAY ->
                    7;

                default ->
                    -1;
            };
        } catch (Exception e) {
            return -1;
        }
    }
    
      /**
     * Excel 시트에서 선택한 요일의 '비어있음' 시간대 반환
     */

    public List<String> getAvailableTimesByDay(Sheet sheet, int dayCol) {
        List<String> times = new ArrayList<>();
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
            Cell cell = row.getCell(dayCol);
            if (cell != null && "비어있음".equals(cell.getStringCellValue().trim())) {
                times.add(time);
            }
        }
        return times;
    }
    
     /**
     * 예약 정보를 파일에 저장 (reservation.txt)
     */
    
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
    
    /**
     * 날짜 문자열 → 한글 요일 반환
     */

    public String getDayOfWeek(String dateStr) {
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

    //서버에서 불러오기
    private void initializeUserInfoFromServer() {
    try {
        out.write("INFO_REQUEST:" + userId + "\n");
        out.flush();

        String response = in.readLine();
        System.out.println(" 서버 응답: " + response);

        if (response != null && response.startsWith("INFO_RESPONSE:")) {
            String[] parts = response.substring("INFO_RESPONSE:".length()).split(",");
            System.out.println("분해된 응답: " + Arrays.toString(parts));

            if (parts.length >= 4) {
    this.userName = parts[1];  //  이름
    this.userDept = parts[2];  //  학과
    this.userType = parts[3];  //  역할
    view.setUserInfo(this.userName, userId, this.userDept);
} else {
                System.out.println(" 응답 형식 오류: 5개 요소가 아님");
            }
        } else {
            System.out.println(" 서버 응답 없음 또는 형식 오류");
        }
    } catch (IOException e) {
        System.out.println(" 사용자 정보 요청 실패: " + e.getMessage());
    }
}
}
