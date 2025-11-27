package management;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RejectCommandTest {

    private AdminReservationModel model;
    private Path tempResFile;
    private Path tempNotiFile;

    @BeforeEach
    public void setUp() throws Exception {
        tempResFile = Files.createTempFile("test_reservation_reject", ".txt");
        tempNotiFile = Files.createTempFile("test_notification_reject", ".txt");

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

        List<Reservation> data = new ArrayList<>();
        data.add(new Reservation("TestID", "학생", "테스터", "컴공", "실습실", "911", 
                                 "2025-01-01", "월", "10:00", "12:00", "공부", "예약대기"));
        
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
        System.out.println("RejectCommand execute Test");

        RejectCommand command = new RejectCommand(model, "TestID", "911", "2025-01-01", "10:00");
        command.execute();

        String status = model.getCurrentStatus("TestID", "911", "2025-01-01", "10:00");
        assertEquals("거절", status, "커맨드 실행 후 상태가 '거절'이어야 합니다.");
    }
}