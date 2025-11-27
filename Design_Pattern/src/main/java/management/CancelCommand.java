package management;

public class CancelCommand implements ReservationCommand {
    
    // [Refactoring] 리시버는 오직 Model 하나입니다.
    private AdminReservationModel model;
    
    private String studentId;
    private String roomName;
    private String date;
    private String startTime;

    public CancelCommand(AdminReservationModel model, String studentId, String roomName, String date, String startTime) {
        this.model = model;
        this.studentId = studentId;
        this.roomName = roomName;
        this.date = date;
        this.startTime = startTime;
    }

    @Override
    public void execute() {
        // [Refactoring] 리시버에게 '취소해라'라고 명령만 내립니다.
        model.cancelReservation(studentId, roomName, date, startTime);
    }
}