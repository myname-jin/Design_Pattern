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

// ConcreteSubject: 실제 기능을 구현하는 클래스
public class AdminReservationModel extends Subject {
    
    // 파일 경로
    private static final String FILE_PATH = "src/main/resources/reservation.txt"; 
    
    // [직접 구현 1] 관찰자 목록을 자식 클래스가 직접 관리함
    private List<ReservationObserver> observers = new ArrayList<>();
    
    private List<Reservation> reservationList = new ArrayList<>();

    // [직접 구현 2] 부모의 추상 메서드 구현 (@Override)
    @Override
    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(List<Reservation> dataToShow) {
        for (ReservationObserver observer : observers) {
            observer.onReservationUpdated(dataToShow);
        }
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
                
                // 12개 컬럼 확인
                if (parts.length >= 12) {
                    Reservation res = new Reservation(
                        parts[0].trim(), // 1.ID (학번)
                        parts[1].trim(), // 2.구분
                        parts[2].trim(), // 3.이름
                        parts[3].trim(), // 4.학과
                        parts[4].trim(), // 5.타입
                        parts[5].trim(), // 6.호실
                        parts[6].trim(), // 7.날짜
                        parts[7].trim(), // 8.요일
                        parts[8].trim(), // 9.시작
                        parts[9].trim(), // 10.종료
                        parts[10].trim(),// 11.목적
                        parts[11].trim() // 12.상태
                    );
                    reservationList.add(res);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[Model] 파일 읽기 실패!");
        }
        
        // 데이터를 불러온 직후, 지난 예약이 있는지 체크하여 상태 업데이트
        checkAndExpireReservations();
        
        notifyObservers(reservationList); 
        System.out.println("[Model] 데이터 로드 완료: " + reservationList.size() + "건");
    }

    // 시간이 지난 '승인' 예약을 '예약확정'으로 자동 변경
    private void checkAndExpireReservations() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        boolean isUpdated = false;

        for (Reservation res : reservationList) {
            // 이미 '승인' 상태인 예약만 체크
            if ("승인".equals(res.getStatus())) {
                try {
                    LocalDate resDate = LocalDate.parse(res.getDate(), dateFormatter);
                    LocalTime endTime = LocalTime.parse(res.getEndTime(), timeFormatter);
                    LocalDateTime endDateTime = LocalDateTime.of(resDate, endTime);
                    
                    // 현재 시간이 예약 종료 시간을 지났다면?
                    if (now.isAfter(endDateTime)) {
                        res.setStatus("예약확정"); 
                        isUpdated = true;
                        System.out.println("[Auto] 지난 예약 확정 처리: " + res.getStudentId() + " / " + res.getDate());
                    }
                } catch (Exception e) {
                    // 날짜 형식이 잘못된 경우 무시
                }
            }
        }

        // 변경된 사항이 있으면 파일에 저장
        if (isUpdated) {
            saveToFile();
        }
    }

    // 상태 변경 (관리자 수동 조작)
    public void updateStatus(String studentId, String roomName, String date, String startTime, String newStatus) {
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
        
        if (isUpdated) {
            saveToFile(); 
        }
        
        notifyObservers(reservationList); 
    }

    // 현재 상태 조회 (수정 방지용)
    public String getCurrentStatus(String studentId, String roomName, String date, String startTime) {
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
                        res.getStudentId(),
                        res.getUserType(),
                        res.getUserName(),
                        res.getDepartment(),
                        res.getRoomType(),
                        res.getRoomName(),
                        res.getDate(),
                        res.getDayOfWeek(),
                        res.getStartTime(),
                        res.getEndTime(),
                        res.getPurpose(),
                        res.getStatus()
                );
                
                writer.write(line);
                writer.newLine(); 
            }
            System.out.println("[Model] 저장 완료: " + FILE_PATH);
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[Model] 저장 실패!");
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