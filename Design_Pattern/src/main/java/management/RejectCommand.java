package management;

// 거절 명령 클래스 (별도 파일)
public class RejectCommand implements ReservationCommand {
    private AdminReservationModel model;
    private NotificationManager notiManager;
    private String studentId;
    private String roomName;
    private String date;
    private String startTime;

    public RejectCommand(AdminReservationModel model, String studentId, String roomName, String date, String startTime) {
        this.model = model;
        this.notiManager = new NotificationManager(); // 알림 매니저 생성
        this.studentId = studentId;
        this.roomName = roomName;
        this.date = date;
        this.startTime = startTime;
    }

    @Override
    public void execute() {
        // 1. 상태를 '거절'로 변경
        model.updateStatus(studentId, roomName, date, startTime, "거절");
        
        // 2. 알림 전송
        String msg = String.format("[%s] %s 예약이 '거절'되었습니다.", date, roomName);
        notiManager.sendNotification(studentId, msg);
    }
}