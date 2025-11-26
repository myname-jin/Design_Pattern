package management;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * [커맨드 패턴 통합 테스트 - 최종 수정본]
 * DB 파일(reservation.txt)과 알림 파일(personal_notifications.txt) 모두를
 * 임시 파일로 우회시켜서 실제 데이터 오염을 100% 방지함.
 */
public class TestCommandPattern {

    public static void main(String[] args) throws Exception {
        System.out.println("============== 커맨드 패턴 테스트 시작 ==============");

        // 0. 임시 파일 2개 생성 (예약DB용, 알림용)
        Path tempDbFile = Files.createTempFile("test_reservation_integration", ".txt");
        Path tempNotiFile = Files.createTempFile("test_notification_integration", ".txt");
        
        System.out.println(">> (안전장치) 임시 DB 파일: " + tempDbFile.toString());
        System.out.println(">> (안전장치) 임시 알림 파일: " + tempNotiFile.toString());

        // 1. [Receiver] 모델 생성
        AdminReservationModel model = new AdminReservationModel();

        // 1-1. AdminReservationModel 경로 교체
        try {
            Field dbPathField = AdminReservationModel.class.getDeclaredField("FILE_PATH");
            dbPathField.setAccessible(true);
            dbPathField.set(null, tempDbFile.toString()); 
            System.out.println(">> (성공) 모델 DB 경로 교체 완료");
        } catch (Exception e) {
            System.err.println(">> ❌ 모델 DB 경로 교체 실패! (final 확인 필요)");
        }

        // 1-2. NotificationManager 경로 교체
        try {
            // NotificationManager의 static 변수 FILE_PATH를 교체
            Field notiPathField = NotificationManager.class.getDeclaredField("FILE_PATH");
            notiPathField.setAccessible(true);
            notiPathField.set(null, tempNotiFile.toString());
            System.out.println(">> (성공) 알림 매니저 경로 교체 완료");
        } catch (Exception e) {
            System.err.println(">> ❌ 알림 매니저 경로 교체 실패! (NotificationManager.java에서 final 뺐나요?)");
        }

        // 2. 테스트용 데이터 준비
        List<Reservation> memoryData = new ArrayList<>();
        Reservation targetRes = new Reservation("TEST_ID", "학생", "테스터", "컴공", "회의실", "999", "2025-12-25", "수", "10:00", "12:00", "테스트", "예약대기");
        memoryData.add(targetRes);

        // private 리스트에 접근하여 데이터 넣기
        Field listField = AdminReservationModel.class.getDeclaredField("reservationList");
        listField.setAccessible(true);
        listField.set(model, memoryData);

        System.out.println(">> 초기 상태: " + targetRes.getStatus()); 

        // 3. [Command] 승인 명령 생성
        ReservationCommand approveCmd = new ApproveCommand(
                model, "TEST_ID", "999", "2025-12-25", "10:00"
        );

        // 4. [Invoker] 실행
        ReservationInvoker invoker = new ReservationInvoker();
        invoker.setCommand(approveCmd);
        
        System.out.println(">> 인보커 버튼 클릭! (실행)");
        invoker.buttonPressed(); // 알림이 전송

        // 5. 결과 확인
        String currentStatus = model.getCurrentStatus("TEST_ID", "999", "2025-12-25", "10:00");
        System.out.println(">> 변경 후 상태: " + currentStatus);

        if ("승인".equals(currentStatus)) {
            System.out.println(">>> 성공! 상태 변경됨.");
        }

        // 6. 임시 파일들 삭제
        Files.deleteIfExists(tempDbFile);
        Files.deleteIfExists(tempNotiFile);
        System.out.println(">> (안전장치) 임시 파일 삭제 완료");

        System.out.println("============== 커맨드 패턴 테스트 종료 ==============");
    }
}