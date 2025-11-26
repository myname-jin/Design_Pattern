package management;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationService {
    
    private static final String NOTI_FILE = "src/main/resources/personal_notifications.txt";

    public void sendNotification(String studentId, String message) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        // 파일에 저장 형식: [받는사람ID, 메시지, 시간, 읽음여부(FALSE)]
        String line = String.format("%s,%s,%s,FALSE", studentId, message, time);
        
        File file = new File(NOTI_FILE);
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            
            writer.write(line);
            writer.newLine();
            System.out.println("[알림발송] " + studentId + "에게 저장됨: " + message);
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[알림실패] 파일 저장 중 오류 발생");
        }
    }
}