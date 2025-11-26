package management;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationInvokerTest {

    static class MockCommand implements ReservationCommand {
        boolean isExecuted = false;

        @Override
        public void execute() {
            this.isExecuted = true;
        }
    }

    @Test
    public void testSetAndExecuteCommand() {
        System.out.println("Invoker setCommand & buttonPressed Test");
        
        ReservationInvoker invoker = new ReservationInvoker();
        MockCommand mockCommand = new MockCommand();

        invoker.setCommand(mockCommand);
        invoker.buttonPressed();

        assertTrue(mockCommand.isExecuted, "Invoker가 버튼을 누르면 Command가 실행되어야 합니다.");
    }
}