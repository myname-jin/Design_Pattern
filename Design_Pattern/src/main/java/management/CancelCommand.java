package management;

public class CancelCommand implements ReservationCommand {
    
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
        // 리시버에게 '취소해라'라고 명령
        model.cancelReservation(studentId, roomName, date, startTime);
    }
}