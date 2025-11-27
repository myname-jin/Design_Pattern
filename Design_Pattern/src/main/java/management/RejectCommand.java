package management;

// 거절 명령 클래스
public class RejectCommand implements ReservationCommand {
    
    // [Refactoring] 리시버는 오직 Model 하나입니다.
    private AdminReservationModel model;
    
    private String studentId;
    private String roomName;
    private String date;
    private String startTime;

    public RejectCommand(AdminReservationModel model, String studentId, String roomName, String date, String startTime) {
        this.model = model;
        this.studentId = studentId;
        this.roomName = roomName;
        this.date = date;
        this.startTime = startTime;
    }

    @Override
    public void execute() {
        // [Refactoring] 리시버에게 '거절해라'라고 명령만 내립니다.
        model.rejectReservation(studentId, roomName, date, startTime);
    }
}