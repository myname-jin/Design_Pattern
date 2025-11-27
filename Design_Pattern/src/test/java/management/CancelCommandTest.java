package management;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CancelCommandTest {

    private AdminReservationModel model;
    private Path tempResFile;
    private Path tempNotiFile;

    @BeforeEach
    public void setUp() throws Exception {
        tempResFile = Files.createTempFile("test_reservation_cancel", ".txt");
        tempNotiFile = Files.createTempFile("test_notification_cancel", ".txt");

        model = new AdminReservationModel();
        try {
            Field resPathField = AdminReservationModel.class.getDeclaredField("FILE_PATH");
            resPathField.setAccessible(true);
            resPathField.set(null, tempResFile.toString());
            
            Field notiPathField = NotificationManager.class.getDeclaredField("FILE_PATH");
            notiPathField.setAccessible(true);
            notiPathField.set(null, tempNotiFile.toString());
        } catch (Exception e) {
            System.err.println("경로 교체 실패: " + e.getMessage());
        }
        
        // 이미 승인된 상태의 데이터 주입
        List<Reservation> data = new ArrayList<>();
        data.add(new Reservation("TestID", "학생", "테스터", "컴공", "실습실", "911", 
                                 "2025-01-01", "월", "10:00", "12:00", "공부", "승인")); 
        
        Field listField = AdminReservationModel.class.getDeclaredField("reservationList");
        listField.setAccessible(true);
        listField.set(model, data);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempResFile);
        Files.deleteIfExists(tempNotiFile);
    }

    @Test
    public void testExecute() {
        System.out.println("CancelCommand execute Test");

        CancelCommand command = new CancelCommand(model, "TestID", "911", "2025-01-01", "10:00");
        command.execute();

        String status = model.getCurrentStatus("TestID", "911", "2025-01-01", "10:00");
        assertEquals("취소", status, "커맨드 실행 후 상태가 '취소'여야 합니다.");
    }
}