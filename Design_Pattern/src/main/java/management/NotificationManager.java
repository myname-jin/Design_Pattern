package management;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class NotificationManager {

    private static final String FILE_PATH = "src/main/resources/personal_notifications.txt";
    private boolean isRunning = false; 

    // --- [관리자용] 알림 보내기 ---
    public void sendNotification(String studentId, String message) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 포맷: 학번,메시지,시간,읽음여부(FALSE)
        String line = String.format("%s,%s,%s,FALSE", studentId, message, time);

        File file = new File(FILE_PATH);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            writer.write(line);
            writer.newLine();
            System.out.println("[Admin] 알림 파일 저장 완료: " + line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- [사용자용] 실시간 감시 시작 ---
    public void startMonitoring(String myStudentId) {
        if (isRunning) return;
        isRunning = true;

        System.out.println(">>> [User] 알림 감시자 시작됨! (대상 ID: " + myStudentId + ")");

        new Thread(() -> {
            while (isRunning) {
                checkAndPopup(myStudentId);
                try {
                    Thread.sleep(3000); // 3초 대기
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    public void stopMonitoring() {
        isRunning = false;
        System.out.println("<<< [User] 알림 감시자 종료");
    }

    // [핵심] 디버깅 로그가 추가된 확인 로직
    private synchronized void checkAndPopup(String myId) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("[Debug] 알림 파일이 없습니다.");
            return;
        }

        List<String> allLines = new ArrayList<>();
        boolean foundNew = false;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                // 구조: [0]학번, [1]메시지, [2]시간, [3]읽음여부
                if (parts.length >= 4) {
                    String fileId = parts[0].trim();
                    String isRead = parts[3].trim();

                    // [디버깅 로그] 이게 콘솔에 떠야 합니다!
                    // System.out.println("[Check] 파일ID: " + fileId + " / 내ID: " + myId + " / 읽음: " + isRead);

                    // 조건: 아이디가 같고(equals) + 안 읽었으면(FALSE)
                    if (fileId.equals(myId) && "FALSE".equals(isRead)) {
                        
                        System.out.println("🎉 [성공] 새 알림 발견! 팝업을 띄웁니다.");
                        
                        String msg = parts[1];
                        String time = parts[2];

                        // 팝업 띄우기
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(null, 
                                msg, 
                                "새로운 알림 도착 🔔", 
                                JOptionPane.INFORMATION_MESSAGE);
                        });
                        
                        // 읽음 처리
                        line = fileId + "," + parts[1] + "," + parts[2] + ",TRUE";
                        foundNew = true;
                    }
                }
                allLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 변경 사항 저장
        if (foundNew) {
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                for (String str : allLines) {
                    writer.write(str);
                    writer.newLine();
                }
                System.out.println("[User] 알림 읽음 처리 완료 (FALSE -> TRUE)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}