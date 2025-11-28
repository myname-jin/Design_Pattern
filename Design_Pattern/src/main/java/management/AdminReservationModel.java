package management;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// ConcreteSubject: 실제 기능을 구현하는 클래스 (겸 Receiver)
public class AdminReservationModel extends Subject {
    
    private static String FILE_PATH = "src/main/resources/reservation.txt"; 
    private NotificationManager notiManager = new NotificationManager();
    private List<Reservation> reservationList = new ArrayList<>();
    
    public AdminReservationModel() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void addObserver(ReservationObserver observer) {
        if (observers != null) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(ReservationObserver observer) {
        if (observers != null) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(List<Reservation> dataToShow) {
        if (observers != null) {
            for (ReservationObserver observer : observers) {
                observer.onReservationUpdated(dataToShow);
            }
        }
    }
    
    public List<Reservation> getAllReservations() {
        return reservationList;
    }

    // --- 커맨드 패턴을 위한 실제 행동(Action) 메서드들 ---
    
    // 1. 승인 처리
    public void approveReservation(String studentId, String roomName, String date, String startTime) {
        updateStatus(studentId, roomName, date, startTime, "승인");
        String msg = String.format("[%s] %s 예약이 '승인'되었습니다.", date, roomName);
        notiManager.sendNotification(studentId, msg);
    }

    // 2. 거절 처리
    public void rejectReservation(String studentId, String roomName, String date, String startTime) {
        updateStatus(studentId, roomName, date, startTime, "거절");
        String msg = String.format("[%s] %s 예약이 '거절'되었습니다.", date, roomName);
        notiManager.sendNotification(studentId, msg);
    }

    // 3. 강제 취소 처리
    public void cancelReservation(String studentId, String roomName, String date, String startTime) {
        updateStatus(studentId, roomName, date, startTime, "취소");
        String msg = String.format("관리자 사정으로 [%s %s] 예약이 '취소'되었습니다.", date, roomName);
        notiManager.sendNotification(studentId, msg);
    }

    // --- 비즈니스 로직 ---

    public void loadData() {
        reservationList.clear();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("[Model] 파일이 없습니다: " + FILE_PATH);
            notifyObservers(reservationList);
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    Reservation res = new Reservation(
                        parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), 
                        parts[4].trim(), parts[5].trim(), parts[6].trim(), parts[7].trim(), 
                        parts[8].trim(), parts[9].trim(), parts[10].trim(), parts[11].trim()
                    );
                    reservationList.add(res);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[Model] 파일 읽기 실패!");
        }
        
        checkAndExpireReservations();
        notifyObservers(reservationList); 
    }

    private void checkAndExpireReservations() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        boolean isUpdated = false;

        for (Reservation res : reservationList) {
            if ("승인".equals(res.getStatus())) {
                try {
                    LocalDate resDate = LocalDate.parse(res.getDate(), dateFormatter);
                    LocalTime endTime = LocalTime.parse(res.getEndTime(), timeFormatter);
                    LocalDateTime endDateTime = LocalDateTime.of(resDate, endTime);
                    
                    if (now.isAfter(endDateTime)) {
                        res.setStatus("예약확정"); 
                        isUpdated = true;
                    }
                } catch (Exception e) { }
            }
        }
        if (isUpdated) saveToFile();
    }

    // 내부 상태 업데이트 로직 
    public void updateStatus(String studentId, String roomName, String date,
            String startTime, String newStatus) {
        boolean isUpdated = false;
        for (Reservation res : reservationList) {
            if (res.getStudentId().equals(studentId) &&
                res.getRoomName().equals(roomName) &&
                res.getDate().equals(date) &&
                res.getStartTime().equals(startTime)) {
                
                res.setStatus(newStatus);
                isUpdated = true;
                break; 
            }
        }
        if (isUpdated) saveToFile(); 
        notifyObservers(reservationList); 
    }


    public String getCurrentStatus(String studentId, String roomName, 
            String date, String startTime) {
        for (Reservation res : reservationList) {
            if (res.getStudentId().equals(studentId) &&
                res.getRoomName().equals(roomName) &&
                res.getDate().equals(date) &&
                res.getStartTime().equals(startTime)) {
                return res.getStatus();
            }
        }
        return null;
    }

    private void saveToFile() {
        File file = new File(FILE_PATH);
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            
            for (Reservation res : reservationList) {
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        res.getStudentId(), res.getUserType(), res.getUserName(), res.getDepartment(),
                        res.getRoomType(), res.getRoomName(), res.getDate(), res.getDayOfWeek(),
                        res.getStartTime(), res.getEndTime(), res.getPurpose(), res.getStatus()
                );
                writer.write(line);
                writer.newLine(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void filterData(String keyword, String searchType) {
        if (keyword == null || keyword.trim().isEmpty()) {
            notifyObservers(reservationList);
            return;
        }
        List<Reservation> filteredList = reservationList.stream()
                .filter(r -> {
                    switch (searchType) {
                        case "이름": return r.getUserName().contains(keyword);
                        case "학과": return r.getDepartment().contains(keyword);
                        case "학번": return r.getStudentId().contains(keyword);
                        case "구분": return r.getUserType().contains(keyword);
                        case "강의실": return r.getRoomName().contains(keyword);
                        case "날짜": return r.getDate().contains(keyword);
                        case "상태": return r.getStatus().contains(keyword);
                        case "전체": default:
                            return r.getUserName().contains(keyword) ||
                                   r.getDepartment().contains(keyword) ||
                                   r.getStudentId().contains(keyword) ||
                                   r.getRoomName().contains(keyword) ||
                                   r.getDate().contains(keyword) ||
                                   r.getStatus().contains(keyword);
                    }
                })
                .collect(Collectors.toList());
        notifyObservers(filteredList);
    }
}