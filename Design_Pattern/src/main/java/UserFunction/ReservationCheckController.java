/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;

/**
 *
 * @author namw2
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Reservation.ReservationCheckView;

public class ReservationCheckController {

    private ReservationCheckView view;
    private String filePath = "src/main/resources/reservation.txt"; // 파일 경로

    public ReservationCheckController(ReservationCheckView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        // 뷰의 콤보박스 변경 시 자동으로 테이블 업데이트
        view.addDateChangeListener(e -> loadReservationData());
        view.addRoomTypeChangeListener(e -> loadReservationData());
        view.addRoomNumChangeListener(e -> loadReservationData());

        // 처음 실행 시 한번 로드
        loadReservationData();
    }

    // 핵심 로직: 파일 읽어서 테이블 채우기
    public void loadReservationData() {
        // 1. 뷰에서 선택된 값 가져오기
        String selectedDate = view.getSelectedDate();     // 예: 2025-11-26
        String selectedType = view.getSelectedRoomType(); // 예: 실습실
        String selectedNum = view.getSelectedRoomNum();   // 예: 911

        //테스트용
        System.out.println(selectedDate + selectedNum + selectedType);

        if (selectedNum == null) {
            return; // 방 번호가 아직 로드 안됐으면 중단
        }
        // 2. 테이블 초기화 (데이터 쌓임 방지)
        clearTable();

        // 3. 파일 읽기
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 콤마(,)로 데이터 분리
                // 파일형식: 333,학생,김씨,학과,실습실,911,2025-11-26,수,19:40,20:25,면담,예약대기
                // 인덱스:   0    1    2    3     4     5       6      7    8     9    10    11
                String[] data = line.split(",");

                //테스트용
                /*
                for (String s : data) {
                    System.out.println(s);
                }
                 */
                if (data.length < 12) {
                    continue; // 데이터 깨진 줄 건너뜀
                }
                String fileRoomType = data[4].trim();
                String fileRoomNum = data[5].trim();
                String fileDate = data[6].trim();
                String fileDay = data[7].trim();      // 요일 (월, 화...)
                String fileStartTime = data[8].trim(); // 시작 시간 (13:00)
                String fileStatus = data[11].trim();   // 상태 (예약대기, 거절, 취소 등)

                //테스트용
                System.out.println(fileRoomType + fileRoomNum + fileDate + fileDay + fileStartTime + fileStatus);

                DefaultTableModel model = (DefaultTableModel) view.getTimeTable().getModel();
                
                // 4. 필터링 로직
                // 방 타입, 방 번호, 그리고 '날짜'가 선택한 것과 똑같아야 함
                boolean isRoomMatch = fileRoomType.equals(selectedType) && fileRoomNum.equals(selectedNum);
                boolean isDateMatch = fileDate.equals(selectedDate);

                // "거절"이나 "취소"된 예약은 테이블에 안 보여줄 거라면 조건 추가
                boolean isValidStatus = !fileStatus.equals("거절") && !fileStatus.equals("취소");

                if (isRoomMatch && isDateMatch && isValidStatus) {
                    // 5. 테이블의 좌표(행, 열) 구하기
                    int rowIndex = getTimeRowIndex(fileStartTime); // 시간 -> 행
                    int colIndex = getDayColumnIndex(fileDay);     // 요일 -> 열

                    if (rowIndex != -1 && colIndex != -1) {
                        // 6. 테이블에 값 넣기 (예: "예약중(김씨)")
                        if (data[11].equals("예약확정") && data[11].equals("승인")) {
                            String cellText = "사용중"; // 목적 + 이름
                            view.getTimeTable().setValueAt(cellText, rowIndex, colIndex);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("파일 로드 중 오류: " + e.getMessage());
            // e.printStackTrace(); // 디버깅용
        }
    }

    // 테이블 내용 싹 비우기
    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) view.getTimeTable().getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 1; col < model.getColumnCount(); col++) { // 0열(시간) 제외하고 비움
                model.setValueAt(null, row, col);
            }
        }
    }

    // "13:00" 같은 시간을 테이블의 행(Row) 인덱스로 변환
    private int getTimeRowIndex(String time) {
        // 파일의 시간(예: 13:00) 앞 2글자만 떼서 비교
        String hour = time.split(":")[0];

        switch (hour) {
            case "09":
                return 0;
            case "10":
                return 1;
            case "11":
                return 2;
            case "12":
                return 3;
            case "13":
                return 4;
            case "14":
                return 5;
            case "15":
                return 6;
            case "16":
                return 7;
            case "17":
                return 8;
            case "18":
                return 9;
            case "19":
                return 10;
            case "20":
                return 11;
            case "21":
                return 12;
            default:
                return -1;
        }
    }

    // "월", "화" 같은 요일을 테이블의 열(Column) 인덱스로 변환
    private int getDayColumnIndex(String day) {
        switch (day) {
            case "월":
                return 1;
            case "화":
                return 2;
            case "수":
                return 3;
            case "목":
                return 4;
            case "금":
                return 5;
            case "토":
                return 6;
            case "일":
                return 7;
            default:
                return -1;
        }
    }

}
