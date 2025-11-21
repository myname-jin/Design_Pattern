package management;

public class CancelCommand implements ReservationCommand {
    private AdminReservationModel model;
    private NotificationManager notiManager;
    private String studentId;
    private String roomName;
    private String date;
    private String startTime;

    public CancelCommand(AdminReservationModel model, String studentId, String roomName, String date, String startTime) {
        this.model = model;
        this.notiManager = new NotificationManager(); // 알림 매니저
        this.studentId = studentId;
        this.roomName = roomName;
        this.date = date;
        this.startTime = startTime;
    }

    @Override
    public void execute() {
        // 1. 상태를 '취소'로 변경 (파일에도 '취소'로 저장됨)
        model.updateStatus(studentId, roomName, date, startTime, "취소");
        
        // 2. 학생에게 알림 전송 (NotificationManager가 파일에 저장해둠 -> 로그인 시 팝업)
        String msg = String.format("관리자 사정으로 [%s %s] 예약이 '취소'되었습니다.", date, roomName);
        notiManager.sendNotification(studentId, msg);
    }
}