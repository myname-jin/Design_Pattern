package management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AdminReservationModel {
    
    // 파일 경로 (상대 경로)
    private static final String FILE_PATH = "src/main/resources/reservation.txt"; 
    
    private List<ReservationObserver> observers = new ArrayList<>();
    private List<Reservation> reservationList = new ArrayList<>();
    
    // 옛날 컨트롤러 제거
    // private ReservationMgmtController realDataController = new ReservationMgmtController();

    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(List<Reservation> dataToShow) {
        for (ReservationObserver observer : observers) {
            observer.onReservationUpdated(dataToShow);
        }
    }

    // --- 비즈니스 로직 ---

    // [수정됨] 옛날 코드에 의존하지 않고, 직접 파일을 읽어옵니다.
    public void loadData() {
        reservationList.clear();
        File file = new File(FILE_PATH);

        // 파일이 없으면 그냥 빈 리스트로 알림 보내고 종료
        if (!file.exists()) {
            System.out.println("[Model] 파일이 없습니다: " + FILE_PATH);
            notifyObservers(reservationList);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 빈 줄은 건너뜀
                if (line.trim().isEmpty()) continue;

                // 콤마(,)로 분리: 이름,학과,학번,강의실,날짜,시간,승인여부
                String[] parts = line.split(",");
                
                // 데이터 형식이 맞는지 확인 (최소 7개 항목)
                if (parts.length >= 7) {
                    Reservation res = new Reservation(
                        parts[0].trim(), // 이름
                        parts[1].trim(), // 학과
                        parts[2].trim(), // 학번
                        parts[3].trim(), // 강의실
                        parts[4].trim(), // 날짜
                        parts[5].trim(), // 시간
                        parts[6].trim()  // 승인여부
                    );
                    reservationList.add(res);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[Model] 파일 읽기 실패!");
        }
        
        // 화면 갱신 알림 발사!
        notifyObservers(reservationList); 
        System.out.println("[Model] 데이터 직접 로드 완료: " + reservationList.size() + "건");
    }

    // 상태 변경 (승인/거절)
    public void updateStatus(String studentId, String roomName, String date, String time, String newStatus) {
        boolean isUpdated = false;

        for (Reservation res : reservationList) {
            if (res.getStudentId().equals(studentId) &&
                res.getRoomName().equals(roomName) &&
                res.getDate().equals(date) &&
                res.getTime().equals(time)) {
                
                res.setStatus(newStatus);
                isUpdated = true;
                break; 
            }
        }
        
        if (isUpdated) {
            saveToFile(); // 변경사항 파일 저장
        }
        
        notifyObservers(reservationList); 
    }

    // 파일 저장
    private void saveToFile() {
        File file = new File(FILE_PATH);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Reservation res : reservationList) {
                // 읽어올 때와 똑같은 포맷으로 저장 (CSV)
                String line = String.format("%s,%s,%s,%s,%s,%s,%s",
                        res.getUserName(),
                        res.getDepartment(),
                        res.getStudentId(),
                        res.getRoomName(),
                        res.getDate(),
                        res.getTime(),
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

    // 검색 기능
    public void filterData(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            notifyObservers(reservationList);
            return;
        }

        List<Reservation> filteredList = reservationList.stream()
                .filter(r -> r.getUserName().contains(keyword) ||
                             r.getDepartment().contains(keyword) ||
                             r.getStudentId().contains(keyword) ||
                             r.getRoomName().contains(keyword))
                .collect(Collectors.toList());

        notifyObservers(filteredList);
    }
    
    public List<Reservation> getAllReservations() {
        return reservationList;
    }
}