package management;

// [Invoker] 명령을 받아서 실행하는 역할 (리모컨)
public class ReservationInvoker {
    private ReservationCommand command; // 명령을 저장할 변수

    // 명령 설정 (setCommand)
    public void setCommand(ReservationCommand command) {
        this.command = command;
    }

    // 버튼 눌림 (execute 호출)
    public void buttonPressed() {
        if (command != null) {
            command.execute();
        }
    }
}