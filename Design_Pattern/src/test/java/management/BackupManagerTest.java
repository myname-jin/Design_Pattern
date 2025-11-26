package management;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * BackupManager 단위 테스트
 * (백업 파일 생성 로직 검증)
 */
public class BackupManagerTest {

    private BackupManager instance;
    private Path tempOriginFile;
    private Path tempBackupDir;

    @BeforeEach
    public void setUp() throws Exception {
        instance = new BackupManager();

        // 임시 원본 파일과 임시 백업 폴더 생성
        tempOriginFile = Files.createTempFile("reservation_origin", ".txt");
        Files.write(tempOriginFile, List.of("데이터1", "데이터2"));
        tempBackupDir = Files.createTempDirectory("backup_test_dir");

        // 경로 교체 시도
        try {
            setPrivateField("ORIGINAL_FILE", tempOriginFile.toString());
            setPrivateField("BACKUP_DIR", tempBackupDir.toString() + File.separator); 
        } catch (Exception e) {
            fail("❌ 경로 교체 실패: BackupManager의 필드가 final인지 확인하세요. 테스트를 중단합니다.");
        }
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = BackupManager.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        try {
            field.set(null, value);
        } catch (Exception e) {
            System.err.println("필드 교체 실패 (final 문제일 수 있음): " + fieldName);
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempOriginFile);
        // 임시 백업 폴더 정리 
        if (Files.exists(tempBackupDir)) {
            Files.walk(tempBackupDir)
                 .sorted((a, b) -> b.compareTo(a)) 
                 .map(Path::toFile)
                 .forEach(File::delete);
        }
    }

    @Test
    public void testPerformBackup() {
        System.out.println("performBackup Test");
        String result = instance.performBackup();
        
        assertNotNull(result);
        assertTrue(result.startsWith("백업 완료"));
        
        // 실제 테스트용 임시 폴더만 확인
        File backupDir = tempBackupDir.toFile();
        File[] files = backupDir.listFiles();
        assertNotNull(files);
        assertTrue(files.length > 0, "백업 파일이 생성되어야 합니다.");
    }
}