package management;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * [커맨드 패턴 통합 테스트 - 최종 수정본]
 * * 목적: 
 * 1. Invoker -> Command -> Receiver(Model) 흐름 확인
 * 2. Model이 상태 변경과 알림 전송(NotificationManager)을 모두 수행하는지 확인
 * 3. Reflection을 사용하여 실제 파일(reservation.txt, personal_notifications.txt) 오염 방지
 */
public class TestCommandPattern {

    public static void main(String[] args) throws Exception {
        System.out.println("============== 커맨드 패턴 통합 테스트 시작 ==============");

        // 0. [안전장치] 임시 파일 2개 생성 (예약DB용, 알림용)
        Path tempDbFile = Files.createTempFile("test_reservation_integration", ".txt");
        Path tempNotiFile = Files.createTempFile("test_notification_integration", ".txt");
        
        System.out.println(">> (설정) 임시 DB 파일 생성: " + tempDbFile.toString());
        System.out.println(">> (설정) 임시 알림 파일 생성: " + tempNotiFile.toString());

        // 1. [Receiver] 모델 생성
        AdminReservationModel model = new AdminReservationModel();

        // 1-1. AdminReservationModel의 파일 경로를 임시 파일로 교체 (Reflection)
        try {
            Field dbPathField = AdminReservationModel.class.getDeclaredField("FILE_PATH");
            dbPathField.setAccessible(true);
            dbPathField.set(null, tempDbFile.toString()); 
            System.out.println(">> (성공) Model DB 경로 교체 완료");
        } catch (Exception e) {
            System.err.println(">> ❌ 모델 DB 경로 교체 실패! (변수명 FILE_PATH 확인 필요)");
            return;
        }

        try {
            Field notiPathField = NotificationManager.class.getDeclaredField("FILE_PATH");
            notiPathField.setAccessible(true);
            notiPathField.set(null, tempNotiFile.toString());
            System.out.println(">> (성공) NotificationManager 경로 교체 완료");
        } catch (Exception e) {
            System.err.println(">> ❌ 알림 매니저 경로 교체 실패! (변수명 FILE_PATH 확인 필요)");
            return;
        }

        // 2. 테스트용 더미 데이터 준비 
        List<Reservation> memoryData = new ArrayList<>();
        // 초기 상태: "예약대기"
        Reservation targetRes = new Reservation("TEST_ID", "학생", "테스터", 
                "컴공", "회의실", "999", "2025-12-25", "수", 
                "10:00", "12:00", "통합테스트", "예약대기");
        memoryData.add(targetRes);

        // private 리스트(reservationList)에 접근하여 데이터 넣기
        Field listField = AdminReservationModel.class.getDeclaredField("reservationList");
        listField.setAccessible(true);
        listField.set(model, memoryData);

        System.out.println(">> 초기 상태: " + targetRes.getStatus()); 

        // 3. [Command] 승인 명령 생성
        ReservationCommand approveCmd = new ApproveCommand(
                model, "TEST_ID", "999", "2025-12-25", "10:00"
        );

        // 4. [Invoker] 리모컨 설정 및 실행
        ReservationInvoker invoker = new ReservationInvoker();
        invoker.setCommand(approveCmd);
        
        System.out.println(">> [Action] 인보커 버튼 클릭! (execute 호출)");
        invoker.buttonPressed(); // 이 시점에 상태변경 + 알림파일 기록이 수행됨

        // 5. 결과 검증 (DB 상태 확인)
        String currentStatus = model.getCurrentStatus("TEST_ID", "999", "2025-12-25", "10:00");
        System.out.println(">> 변경 후 상태: " + currentStatus);

        if ("승인".equals(currentStatus)) {
            System.out.println(">>> [검증 1] 성공! 메모리 상의 상태가 '승인'으로 변경됨.");
        } else {
            System.err.println(">>> [검증 1] 실패! 상태가 변경되지 않음.");
        }

        // 6. 결과 검증 (알림 파일 기록 확인)
        List<String> notiLines = Files.readAllLines(tempNotiFile);
        if (!notiLines.isEmpty() && notiLines.get(0).contains("승인")) {
             System.out.println(">>> [검증 2] 성공! 알림 파일에 '승인' 메시지가 기록됨: " + notiLines.get(0));
        } else {
             System.err.println(">>> [검증 2] 실패! 알림 파일에 기록이 없거나 내용이 틀림.");
        }

        // 7. 뒷정리 (임시 파일 삭제)
        Files.deleteIfExists(tempDbFile);
        Files.deleteIfExists(tempNotiFile);
        System.out.println(">> (정리) 임시 파일 삭제 완료");

        System.out.println("============== 커맨드 패턴 통합 테스트 종료 ==============");
    }
}