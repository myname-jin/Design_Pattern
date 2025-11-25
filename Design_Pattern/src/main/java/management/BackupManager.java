package management;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BackupManager {

    private static String ORIGINAL_FILE = "src/main/resources/reservation.txt";
    private static String BACKUP_DIR = "backups";

    public BackupManager() {
        File dir = new File(BACKUP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // [백업 기능]
    public String performBackup() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String backupFileName = "reservation_" + timestamp + ".txt";
            
            Path source = Paths.get(ORIGINAL_FILE);
            Path target = Paths.get(BACKUP_DIR, backupFileName);

            if (!Files.exists(source)) {
                return "백업 실패: 원본 파일이 없습니다.";
            }

            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            
            return "백업 완료: " + backupFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "백업 실패: " + e.getMessage();
        }
    }

    // 복구 기능 (반환 타입: String)
    public String performRestore() {
        JFileChooser fileChooser = new JFileChooser(BACKUP_DIR);
        fileChooser.setDialogTitle("복구할 백업 파일을 선택하세요");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File backupFile = fileChooser.getSelectedFile();
            
            try {
                Path source = backupFile.toPath();
                Path target = Paths.get(ORIGINAL_FILE);

                // 백업 파일을 원본 위치로 덮어쓰기
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                
                return "성공: 데이터가 복구되었습니다."; 
                
            } catch (IOException e) {
                e.printStackTrace();
                return "실패: " + e.getMessage(); 
            }
        }
        
        // 사용자가 취소 눌렀을 때
        return null; 
    }
}