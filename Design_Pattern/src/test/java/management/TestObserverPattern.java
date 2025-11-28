package management;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * [옵저버 패턴 통합 테스트]
 * 
 */
public class TestObserverPattern {

    // 1. Observer 인터페이스를 구현한 가짜 화면 클래스
    static class ConsoleView implements ReservationObserver {
        private String viewName;

        public ConsoleView(String viewName) {
            this.viewName = viewName;
        }

        @Override
        public void onReservationUpdated(List<Reservation> reservationList) {
            System.out.println("\n[" + viewName + "] 띠링! 데이터 변경 알림을 받았습니다.");
            System.out.println("   -> 현재 데이터 개수: " + reservationList.size());
            for (Reservation r : reservationList) {
                System.out.println("   -> 내용: " + r.getUserName() + " (" + r.getStatus() + ")");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("============== 옵저버 패턴 테스트 시작 ==============");

        // 0. 임시 파일 생성
        Path tempFile = Files.createTempFile("test_observer_pattern", ".txt");

        // 1. [Subject] 모델 생성
        AdminReservationModel model = new AdminReservationModel();

        // 1-1. 실제 파일을 건드리지 않도록 경로를 임시 파일로 교체
        try {
            Field pathField = AdminReservationModel.class.getDeclaredField("FILE_PATH");
            pathField.setAccessible(true);
            pathField.set(null, tempFile.toString()); // 경로 바꿔치기
            System.out.println(">> (안전장치) 모델 파일 경로가 임시 파일로 교체되었습니다.");
        } catch (Exception e) {
            System.err.println(">> ❌ 경로 교체 실패! AdminReservationModel의 FILE_PATH에서 final을 뺐는지 확인하세요.");
            Files.deleteIfExists(tempFile);
            return; // 테스트 중단
        }

        // 2. [Observer] 가짜 화면 2개 생성
        ConsoleView view1 = new ConsoleView("메인 화면");
        ConsoleView view2 = new ConsoleView("통계 화면");

        // 3. [Subscribe] 모델에 화면 등록
        System.out.println(">> 옵저버(화면) 2개를 모델에 등록합니다.");
        model.addObserver(view1);
        model.addObserver(view2);

        // 4. [Notify] 데이터 변경 발생 및 알림 전송 시뮬레이션
        System.out.println("\n>> 모델에서 데이터 변경 알림을 보냅니다...");
        
        List<Reservation> testData = new ArrayList<>();
        testData.add(new Reservation("20231234", "학생", "홍길동", "컴공", "실습실", 
                "911", "2025-05-05", "월", "10:00", "12:00", "공부", "예약대기"));
        
        model.notifyObservers(testData);

        // 5. 임시 파일 삭제
        Files.deleteIfExists(tempFile);
        System.out.println("\n============== 옵저버 패턴 테스트 종료 ==============");
    }
}