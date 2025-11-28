package management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;

public class ReservationMgmtController {

    private static String FILE_PATH = "src/main/resources/reservation.txt";
    private static String BAN_LIST_FILE = "src/main/resources/banlist.txt";
    
    // 알림 매니저 (사용자에게 메시지 보내기용)
    private NotificationManager notiManager = new NotificationManager();

    // Command 패턴용 멤버 변수
    private AdminReservationModel model;
    private ReservationInvoker invoker;

    public ReservationMgmtController() {
        this.model = new AdminReservationModel();
        this.invoker = new ReservationInvoker();
    }

    // View가 Model에 접근할 수 있도록 getter 제공 (Dependency 관계용)
    public AdminReservationModel getModel() {
        return this.model;
    }

    // Command 패턴 실행 메서드들 (Client 역할)
    public void processApprove(String studentId, String roomName, String date, String startTime) {
        ReservationCommand command = new ApproveCommand(model, studentId, roomName, date, startTime);
        invoker.setCommand(command);
        invoker.buttonPressed();
    }

    public void processReject(String studentId, String roomName, String date, String startTime) {
        ReservationCommand command = new RejectCommand(model, studentId, roomName, date, startTime);
        invoker.setCommand(command);
        invoker.buttonPressed();
    }

    public void processCancel(String studentId, String roomName, String date, String startTime) {
        ReservationCommand command = new CancelCommand(model, studentId, roomName, date, startTime);
        invoker.setCommand(command);
        invoker.buttonPressed();
    }
    
    public void processUpdateStatus(String studentId, String roomName, String date, String startTime, String newStatus) {
        model.updateStatus(studentId, roomName, date, startTime, newStatus);
    }

    // --- 기존 레거시 메서드 유지 ---

    public List<ReservationMgmtModel> getAllReservations() {
        List<ReservationMgmtModel> reservations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 12) {
                    reservations.add(new ReservationMgmtModel(
                            data[0], // name
                            data[2], // studentId
                            data[3], // department
                            data[4], // room
                            data[6], // date
                            data[8] + "~" + data[9], // time
                            data[11] // approved
                    ));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public void updateApprovalStatus(String studentId, String newStatus) {
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 12 && data[2].equals(studentId)) {
                    data[11] = newStatus;
                    updatedLines.add(String.join(",", data));
                } else {
                    updatedLines.add(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null,
                "학번 " + studentId + "의 승인 여부가 '" + newStatus + "'(으)로 변경되었습니다.",
                "승인 결과",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public List<String> getBannedUsers() {
        List<String> bannedUsers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BAN_LIST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bannedUsers.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bannedUsers;
    }

    public void banUser(String studentId, String name, String dept, String userType) {
        List<String> bannedLines = getBannedLines();
        
        // 이미 제한된 사용자인지 학번으로 체크
        boolean alreadyBanned = false;
        for(String line : bannedLines) {
            if(line.startsWith(studentId + ",")) {
                alreadyBanned = true;
                break;
            }
        }

        if (!alreadyBanned) {
            String newBanRecord = String.format("%s,%s,%s,%s", studentId, name, dept, userType);
            bannedLines.add(newBanRecord);
            
            saveBannedLines(bannedLines);
            
            // 알림 전송
            String msg = "관리자에 의해 예약 권한이 '제한'되었습니다.\n예약 기능 사용이 불가능합니다.";
            notiManager.sendNotification(studentId, msg);
        }
    }
    
    // [해제] 파일에서 삭제 + 알림 전송
    public void unbanUser(String studentId) {
        List<String> bannedLines = getBannedLines();
        boolean removed = false;

        // 리스트에서 해당 학번을 포함한 줄 삭제
        Iterator<String> iterator = bannedLines.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith(studentId + ",")) {
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (removed) {
            saveBannedLines(bannedLines);
            
            String msg = "예약 권한 제한이 '해제'되었습니다.\n다시 예약이 가능합니다.";
            notiManager.sendNotification(studentId, msg);

            JOptionPane.showMessageDialog(null, "제한이 해제되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "제한 목록에 없는 사용자입니다.", "알림", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // 파일 내용을 통째로 읽어오는 메서드
    private List<String> getBannedLines() {
        List<String> lines = new ArrayList<>();
        File file = new File(BAN_LIST_FILE);
        if (!file.exists()) return lines;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) lines.add(line.trim());
            }
        } catch (IOException e) { e.printStackTrace(); }
        return lines;
    }
    
    private void saveBannedLines(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BAN_LIST_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserBanned(String studentId) {
        List<String> bannedLines = getBannedUsers();
        for (String line : bannedLines) {
            // 쉼표(,)로 잘라서 첫 번째(학번)가 일치하는지 확인
            String[] parts = line.split(",");
            if (parts.length > 0 && parts[0].trim().equals(studentId)) {
                return true; // 차단된 사용자임.
            }
        }
        return false; // 끝까지 찾아도 없음.
    }

    public List<ReservationMgmtModel> searchReservations(String name, String studentId, String room) {
        List<ReservationMgmtModel> allReservations = getAllReservations();
        List<ReservationMgmtModel> filtered = new ArrayList<>();

        for (ReservationMgmtModel r : allReservations) {
            boolean match = true;

            if (name != null && !name.isEmpty() && !r.getName().contains(name)) {
                match = false;
            }
            if (studentId != null && !studentId.isEmpty() && !r.getStudentId().contains(studentId)) {
                match = false;
            }
            if (room != null && !room.isEmpty() && !r.getRoom().contains(room)) {
                match = false;
            }

            if (match) {
                filtered.add(r);
            }
        }

        return filtered;
    }
}