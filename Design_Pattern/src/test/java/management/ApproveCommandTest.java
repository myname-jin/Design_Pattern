package management;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ApproveCommandTest {

    private AdminReservationModel model;
    private Path tempFile; 

    @BeforeEach
    public void setUp() throws Exception {
        tempFile = Files.createTempFile("test_reservation", ".txt");
        
        model = new AdminReservationModel();

        try {
            Field pathField = AdminReservationModel.class.getDeclaredField("FILE_PATH");
            pathField.setAccessible(true);
            // static 필드라면 첫 인자에 null, 인스턴스 필드라면 model을 넣으세요.
            // 보통 상수로 쓰면 static일 확률이 높습니다. 에러나면 model로 바꿔보세요.
            pathField.set(null, tempFile.toString()); 
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
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testExecute() {
        System.out.println("ApproveCommand execute Test");

        ApproveCommand command = new ApproveCommand(model, "TestID", "911", "2025-01-01", "10:00");
        command.execute();

        String status = model.getCurrentStatus("TestID", "911", "2025-01-01", "10:00");
        assertEquals("승인", status, "커맨드 실행 후 상태가 '승인'이어야 합니다.");
    }
}