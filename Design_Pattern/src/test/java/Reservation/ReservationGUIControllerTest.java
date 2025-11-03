package Reservation;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ReservationGUIController의 핵심 기능(메서드)들을 테스트하는 단위 테스트 클래스
 * - Excel 파일 처리, 날짜 처리, 예약 제한 로직 등을 검증
 * - 실제 소켓/서버 연결 대신, Mock 객체로 대체하여 테스트
 */
class ReservationGUIControllerTest {

    private ReservationGUIController controller;

    // Mock 객체 (실제 서버/파일 연결 대신 테스트용 더미 객체)
    private Socket mockSocket;
    private BufferedReader mockIn;
    private BufferedWriter mockOut;

    @BeforeEach
    void setUp() throws Exception {
        // 더미 소켓/버퍼 객체 생성
        mockSocket = mock(Socket.class);
        mockIn = mock(BufferedReader.class);
        mockOut = mock(BufferedWriter.class);

        // 서버 응답 역할을 하는 더미 데이터 설정
        when(mockIn.readLine()).thenReturn("INFO_RESPONSE:홍길동,컴퓨터소프트웨어공학,학생");

        // 테스트용 ReservationGUIController 인스턴스 생성
        controller = new ReservationGUIController("20230001", "홍길동", "컴퓨터소프트웨어공학", "학생",
                mockSocket, mockIn, mockOut);

        // 테스트용 Excel Workbook 생성 및 기본 시트 추가
        controller.workbook = new XSSFWorkbook();
        Sheet sheet = controller.workbook.createSheet("901");
        sheet.createRow(0).createCell(0).setCellValue("시간");
        sheet.createRow(1).createCell(0).setCellValue("09:00~10:00");
        sheet.getRow(1).createCell(1).setCellValue("비어있음");
    }

    /**
     * getDayOfWeek() 메서드 테스트
     * - 입력된 날짜 문자열로부터 올바른 요일(한글)을 반환하는지 확인
     */
    @Test
    void testGetDayOfWeek() {
        System.out.println("[테스트] 날짜 문자열로 요일 반환 테스트 실행");
        String day = controller.getDayOfWeek("2025-05-28");
        assertEquals("수", day);  // 2025-05-28은 수요일
    }

    /**
     * calculateTotalDuration() 메서드 테스트
     * - 시간대 리스트를 입력하면 총 예약 시간이 올바르게 계산되는지 확인
     */
    @Test
    void testCalculateTotalDuration() {
        System.out.println("[테스트] 선택한 시간대 총 예약 시간 계산 테스트 실행");
        List<String> times = Arrays.asList("10:00~11:00", "11:00~12:00");
        int total = controller.calculateTotalDuration(times);
        assertEquals(120, total);  // 총 120분 (2시간)
    }

    /**
     * getAvailableTimesByDay() 메서드 테스트
     * - Excel 시트에서 비어있는 시간대를 올바르게 찾아오는지 확인
     */
    @Test
    void testGetAvailableTimesByDay() {
        System.out.println("[테스트] Excel 시트에서 비어있는 시간대 추출 테스트 실행");
        List<String> times = controller.getAvailableTimesByDay(controller.workbook.getSheet("901"), 1);
        assertFalse(times.isEmpty());
        assertTrue(times.contains("09:00~10:00"));
    }

    /**
     * getRoomInfo() 메서드 테스트
     * - classroom.txt 파일에서 강의실 정보를 올바르게 불러오는지 확인
     */
    @Test
    void testGetRoomInfoReturnsCorrectly() throws IOException {
        System.out.println("[테스트] classroom.txt에서 강의실 정보 읽기 테스트 실행");
        // 테스트용 classroom.txt 파일 생성
        String filePath = "src/main/resources/classroom.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("901,5층,50명,빔프로젝터\n");
        }

        String info = controller.getRoomInfo("901");
        assertTrue(info.contains("5층"));  // 위치 정보가 포함되어 있는지 확인
        // 선택: 테스트 후 파일 삭제
        // new File(filePath).delete();
    }

    /**
     * isUserAlreadyReserved() 메서드 테스트
     * - 특정 사용자(userId)가 해당 날짜에 이미 예약을 했는지 확인
     */
    @Test
    void testIsUserAlreadyReserved() throws IOException {
        System.out.println("[테스트] 사용자 중복 예약 여부 확인 테스트 실행");
        // 테스트용 reservation.txt 파일 생성 (예약 기록)
        String filePath = "src/main/resources/reservation.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("홍길동,학생,20230001,컴퓨터소프트웨어공학,강의실,901,2025-05-28,수,10:00,11:00,스터디,예약대기\n");
        }

        boolean result = controller.isUserAlreadyReserved("20230001", "2025-05-28");
        assertTrue(result);  // 예약 기록이 있으므로 true
    }

    /**
     * isTimeSlotAlreadyReserved() 메서드 테스트
     * - 특정 강의실/날짜의 시간대에 예약 중복이 있는지 확인
     */
    @Test
    void testIsTimeSlotAlreadyReserved() throws IOException {
        System.out.println("[테스트] 특정 시간대 중복 예약 여부 확인 테스트 실행");
        // 테스트용 reservation.txt 파일 생성 (예약 기록)
        String filePath = "src/main/resources/reservation.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("홍길동,학생,20230001,컴퓨터소프트웨어공학,강의실,901,2025-05-28,수,10:00,11:00,스터디,예약대기\n");
        }

        List<String> newTimes = Arrays.asList("10:30~11:30");
        boolean result = controller.isTimeSlotAlreadyReserved("901", "2025-05-28", newTimes);
        assertTrue(result);  // 시간이 겹치므로 true
    }
}
