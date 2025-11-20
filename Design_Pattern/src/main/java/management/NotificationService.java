package management;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationService {
    
    // 개인 알림 저장용 파일
    private static final String NOTI_FILE = "src/main/resources/personal_notifications.txt";

    // 알림 보내기 (파일에 저장 -> 나중에 사용자가 로그인하면 읽음)
    public void sendNotification(String studentId, String message) {
        // 현재 시간 구하기
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        // 파일에 저장 형식: [받는사람ID, 메시지, 시간, 읽음여부(FALSE)]
        String line = String.format("%s,%s,%s,FALSE", studentId, message, time);
        
        File file = new File(NOTI_FILE);
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            
            writer.write(line);
            writer.newLine();
            System.out.println("[알림발송] " + studentId + "에게 저장됨: " + message);
            
            // (확장 포인트) 만약 소켓 기능이 있다면 여기서 userSocket.send(message)를 호출하면 실시간 푸시가 됨.
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[알림실패] 파일 저장 중 오류 발생");
        }
    }
}