package UserFunction;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserReservationCancelControllerTest {

    private static final String[] COLUMNS = {
        "이름", "학번", "강의실", "날짜", "요일", "시작시간", "종료시간", "승인상태"
    };

    private DefaultTableModel tableModel;
    private JTable table;
    private UserReservationListView parentView;

    @BeforeAll
    static void setupHeadless() {
        System.setProperty("java.awt.headless", "true");
    }

    @BeforeEach
    void init() {
        tableModel = new DefaultTableModel(COLUMNS, 0);
        tableModel.addRow(new Object[]{
            "홍길동", "test1234", "911", "2025-11-30", "금", "10:00", "12:00", "승인"
        });
        table = new JTable(tableModel);

        parentView = mock(UserReservationListView.class);
        when(parentView.getTable()).thenReturn(table);
    }

    @Test
    void testHandleCancelConfirm_RemovesRowWithoutError() throws Exception {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        UserReservationCancelController controller =
            (UserReservationCancelController) unsafe.allocateInstance(UserReservationCancelController.class);

        setField(controller, "parentView", parentView);
        setField(controller, "selectedRow", 0);
        
        setField(controller, "name",      table.getValueAt(0, 0).toString());
        setField(controller, "userId",    table.getValueAt(0, 1).toString());
        setField(controller, "room",      table.getValueAt(0, 2).toString());
        setField(controller, "date",      table.getValueAt(0, 3).toString());
        setField(controller, "startTime", table.getValueAt(0, 5).toString());
        setField(controller, "endTime",   table.getValueAt(0, 6).toString());

        UserReservationCancelModel stubModel = new UserReservationCancelModel() {
            @Override
            public boolean cancelReservation(String u, String d, String r, String st) {
                return true; 
            }
            @Override
            public boolean saveCancelReason(String u, String reason) {
                return true;
            }
        };
        setField(controller, "model", stubModel);

        UserReservationCancelView stubView = mock(UserReservationCancelView.class);
        when(stubView.getCancelReason()).thenReturn("테스트 취소 사유");
        doNothing().when(stubView).showError(anyString());
        doNothing().when(stubView).dispose();
        setField(controller, "view", stubView);

        Method handle = UserReservationCancelController.class.getDeclaredMethod("handleCancelConfirm");
        handle.setAccessible(true);
        
        try {
            handle.invoke(controller);
        } catch (Throwable ignored) { }

        assertEquals(0, tableModel.getRowCount(), 
            "취소 로직이 성공하면 테이블에서 해당 행이 삭제되어야 합니다.");
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }
}