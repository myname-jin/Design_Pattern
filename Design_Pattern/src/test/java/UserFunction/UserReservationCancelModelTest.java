package UserFunction;

import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserReservationCancelModelTest { 
    
    private Path tempResFile;
    private Path tempCancelFile;
    private UserReservationCancelModel model;

    @BeforeEach
    public void setUp() throws Exception {
        tempResFile = Files.createTempFile("test_reservation", ".txt");
        tempCancelFile = Files.createTempFile("test_cancel", ".txt");

        try (FileWriter writer = new FileWriter(tempResFile.toFile())) {
            writer.write("test1234,학생,홍길동,컴공,강의실,911,2025-11-30,금,10:00,12:00,스터디,승인\n");
        }

        model = new UserReservationCancelModel();

        setPrivateStaticField(UserReservationCancelModel.class, "RESERVATION_FILE", tempResFile.toString());
        setPrivateStaticField(UserReservationCancelModel.class, "CANCEL_FILE", tempCancelFile.toString());
    }

    @AfterEach
    public void tearDown() throws Exception {
        Files.deleteIfExists(tempResFile);
        Files.deleteIfExists(tempCancelFile);
    }

    @Test
    public void testCancelReservation_Success() throws Exception {
        boolean result = model.cancelReservation("test1234", "2025-11-30", "911", "10:00");
        assertTrue(result);

        List<String> lines = Files.readAllLines(tempResFile);
        boolean isCanceled = false;
        for (String line : lines) {
            if (line.startsWith("test1234") && line.contains("취소")) {
                isCanceled = true;
                break;
            }
        }
        assertTrue(isCanceled);
    }
    
    @Test
    public void testCancelReservation_Fail_WrongInfo() {
        boolean result = model.cancelReservation("test1234", "2025-11-30", "911", "15:00");
        assertFalse(result);
    }

    @Test
    public void testSaveCancelReason() throws Exception {
        String userId = "test1234";
        String reason = "취소 사유 테스트";

        boolean result = model.saveCancelReason(userId, reason);
        assertTrue(result);

        List<String> lines = Files.readAllLines(tempCancelFile);
        assertFalse(lines.isEmpty());
        assertTrue(lines.get(0).contains(userId));
        assertTrue(lines.get(0).contains(reason));
    }

    private void setPrivateStaticField(Class<?> clazz, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (Exception e) {
            System.err.println("필드 교체 실패 (final 제거 필요): " + e.getMessage());
        }
    }
}