package management;

// 승인 명령 클래스 
public class ApproveCommand implements ReservationCommand {
    
    // [Refactoring] 리시버는 오직 Model 하나입니다.
    private AdminReservationModel model;
    
    private String studentId;
    private String roomName;
    private String date;
    private String startTime;

    public ApproveCommand(AdminReservationModel model, String studentId, String roomName, String date, String startTime) {
        this.model = model;
        this.studentId = studentId;
        this.roomName = roomName;
        this.date = date;
        this.startTime = startTime;
    }

    @Override
    public void execute() {
        // [Refactoring] 리시버에게 '승인해라'라고 명령만 내립니다. (알림 등 세부 사항은 리시버가 처리)
        model.approveReservation(studentId, roomName, date, startTime);
    }
}