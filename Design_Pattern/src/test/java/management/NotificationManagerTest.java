package management;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * NotificationManager 단위 테스트
 * (알림 파일 저장 포맷 및 로직 검증 - 파일 격리 적용)
 */
public class NotificationManagerTest {
    
    private NotificationManager instance;
    private Path tempNotiFile;

    @BeforeEach
    public void setUp() throws Exception {
        // 1. 임시 파일 먼저 생성
        tempNotiFile = Files.createTempFile("test_personal_notifications", ".txt");

        // 2. 객체를 만들기 전에 리플렉션으로 파일 경로를 먼저 교체
        Field field = NotificationManager.class.getDeclaredField("FILE_PATH");
        field.setAccessible(true);
        try {
            field.set(null, tempNotiFile.toString());
        } catch (Exception e) {
            System.err.println("❌ 경로 교체 실패! 소스 코드의 FILE_PATH에서 'final'을 제거했는지 확인하세요.");
            fail("테스트 환경 설정 실패: NotificationManager의 FILE_PATH를 변경할 수 없습니다.");
        }

        // 3. 경로가 교체된 상태에서 객체 생성 (이제 임시 파일을 바라봄)
        instance = new NotificationManager();
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempNotiFile);
    }

    @Test
    public void testSendNotification() throws IOException {
        System.out.println("sendNotification");
        
        String studentId = "20231234";
        String message = "예약이\n승인되었습니다."; 
        
        instance.sendNotification(studentId, message);

        // 실제 파일이 아닌 임시 파일을 읽어서 검증
        List<String> lines = Files.readAllLines(tempNotiFile);
        assertFalse(lines.isEmpty(), "파일에 내용이 저장되어야 합니다.");
        
        String lastLine = lines.get(lines.size() - 1);
        String[] parts = lastLine.split(",");
        
        assertEquals(4, parts.length);
        assertEquals("20231234", parts[0]); 
        assertEquals("예약이 승인되었습니다.", parts[1]); 
        assertEquals("FALSE", parts[3]); 
    }

    @Test
    public void testStopMonitoring() {
        System.out.println("stopMonitoring");
        instance.stopMonitoring();
    }
}