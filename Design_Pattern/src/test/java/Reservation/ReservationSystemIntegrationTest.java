/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author namw2
 */
public class ReservationSystemIntegrationTest {
    private MockReservationView mockView;

    // 실제 파일 경로 (src/main/resources) 사용
    private final String BAN_LIST_PATH = "src/main/resources/banlist.txt";
    private final String RESERVATION_PATH = "src/main/resources/reservation.txt";
    private final String CANCEL_PATH = "src/main/resources/cancel.txt";

    @BeforeEach
    public void setUp() {
        System.out.println("[Integration Setup] 테스트 준비");
        // 폴더가 없으면 생성 (있으면 무시됨)
        new File("src/main/resources").mkdirs();
        
        mockView = new MockReservationView();
        
     
    }

    @AfterEach
    public void tearDown() {
        System.out.println("[Integration TearDown] 테스트 종료 (파일 보존)");
        
      
    }

    /**
     * 통합 테스트: 교수가 학생의 예약을 밀어내고 예약하는 전체 시나리오
     */
    @Test
    @DisplayName("통합: 학생 예약 후 교수가 같은 시간 예약 시 학생 취소 및 교수 등록")
    public void testProfessorOverrideStudentReservation() throws Exception {
        
        // -------------------------------------------------------
        // Step 1. 학생 예약 데이터 준비 (기존에 예약이 되어 있다고 가정)
        // -------------------------------------------------------
        String studentId = "student1";
        String date = "2024-05-05";
        String roomName = "911";
        String timeSlot = "09:00~10:00";
        
        // reservation.txt에 학생 예약 기록 추가 (append 모드이므로 기존 데이터 뒤에 붙음)
        // 포맷: 이름,타입,ID,학과,강의실타입,방번호,날짜,요일,시작,종료,목적,상태
        String existingStudentRes = String.format("%s,학생,홍길동,컴공,실습실,%s,%s,일,09:00,10:00,공부,예약대기", studentId, roomName, date);
        appendFile(RESERVATION_PATH, existingStudentRes);
        
        System.out.println("1. 학생 예약 데이터 준비 완료");

       
        ProfessorReservation profRes = new ProfessorReservation();
        profRes.view = mockView;
        
        // [중요] 엑셀 파일 없이 테스트하기 위해 Reflection으로 'allRooms'에 강의실 강제 주입
        injectRoomData(profRes, roomName);

        // 교수 예약 정보
        String profId = "profLee";
        List<String> times = Arrays.asList(timeSlot); // 09:00~10:00 (학생과 겹침)
        
        // 예약 실행 (여기서 내부적으로 학생 취소 -> 교수 저장이 일어남)
        profRes.doReservation(profId, "교수", "이교수", "컴공", date, times, "강의", "", roomName, mockView);
        
        System.out.println("2. 교수 예약 실행 완료");
        
        List<String> reservations = readFile(RESERVATION_PATH);
        
        boolean isStudentGone = reservations.stream().noneMatch(line -> line.contains(studentId));
        boolean isProfRegistered = reservations.stream().anyMatch(line -> line.contains(profId) && line.contains("예약확정"));
        
        System.out.println("Check: 학생 예약 삭제 여부 = " + isStudentGone);
        System.out.println("Check: 교수 예약 등록 여부 = " + isProfRegistered);

    
        // B. cancel.txt 확인 (취소 사유 저장 여부)
        List<String> cancels = readFile(CANCEL_PATH);
        boolean isCancelReasonSaved = cancels.stream().anyMatch(line -> line.trim().contains(studentId) && line.trim().contains(" 교수님 예약으로 인한 취소"));
        
        System.out.println("Check: 취소 사유 저장 여부 = " + isCancelReasonSaved);
        
    }
    

    // 리플렉션을 사용하여 private List<RoomModel> allRooms 필드에 데이터 주입
    private void injectRoomData(AbstractReservation instance, String roomName) throws Exception {
        // RoomModel 생성 (생성자 시그니처에 맞게 더미 데이터 넣기)
        RoomModel dummyRoom = new RoomModel(roomName, "실습실", new String[]{});
        List<RoomModel> roomList = new ArrayList<>();
        roomList.add(dummyRoom);
        
        // 부모 클래스(AbstractReservation)의 private 필드 'allRooms' 접근
        Field allRoomsField = AbstractReservation.class.getDeclaredField("allRooms");
        allRoomsField.setAccessible(true);
        allRoomsField.set(instance, roomList);
    }

    private void createFile(String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"))) {
            writer.write(content);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void appendFile(String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8"))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    private List<String> readFile(String path) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) lines.add(line);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return lines;
    }
}