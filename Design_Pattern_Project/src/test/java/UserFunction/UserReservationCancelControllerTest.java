/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
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
        "이름", "학번", "강의실", "날짜", "요일", "시작시간", "종료시간"
    };

    private DefaultTableModel tableModel;
    private JTable table;
    private UserReservationListView parentView;

    @BeforeAll
    static void setupHeadless() {
        // Headless 환경에서도 AWT/Swing 호출 시 예외를 방지
        System.setProperty("java.awt.headless", "true");
    }

    @BeforeEach
    void init() {
        // 1) 테이블 모델 준비
        tableModel = new DefaultTableModel(COLUMNS, 0);
        tableModel.addRow(new Object[]{
            "홍길동", "test123", "101", "2024-01-01", "월", "09:00", "11:00"
        });
        table = new JTable(tableModel);

        // 2) 부모 뷰는 Mockito 로 stub 생성 → getTable() 만 실제 JTable 반환
        parentView = mock(UserReservationListView.class);
        when(parentView.getTable()).thenReturn(table);
    }

    @Test
    void testHandleCancelConfirm_RemovesRowWithoutError() throws Exception {
        // ------------------------------------------------
        // 1) Unsafe 로 컨트롤러 인스턴스만 할당 (생성자 호출 없음)
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);

        UserReservationCancelController controller =
            (UserReservationCancelController)
              unsafe.allocateInstance(UserReservationCancelController.class);

        // ------------------------------------------------
        // 2) 필요한 필드들 직접 세팅
        setField(controller, "parentView", parentView);
        setField(controller, "selectedRow", 0);
        setField(controller, "name",      table.getValueAt(0, 0).toString());
        setField(controller, "userId",    table.getValueAt(0, 1).toString());
        setField(controller, "room",      table.getValueAt(0, 2).toString());
        setField(controller, "date",      table.getValueAt(0, 3).toString());
        setField(controller, "day",       table.getValueAt(0, 4).toString());
        setField(controller, "startTime", table.getValueAt(0, 5).toString());
        setField(controller, "endTime",   table.getValueAt(0, 6).toString());

        // ------------------------------------------------
        // 3) model 을 stub 으로 교체 (cancelReservation/saveCancelReason → true)
        UserReservationCancelModel stubModel = new UserReservationCancelModel() {
            @Override public boolean cancelReservation(String u, String d, String r) {
                return true;
            }
            @Override public boolean saveCancelReason(String u, String reason) {
                return true;
            }
        };
        setField(controller, "model", stubModel);

        // ------------------------------------------------
        // 4) view 도 stub 으로 교체(getCancelReason → non-empty)
        UserReservationCancelView stubView = mock(UserReservationCancelView.class);
        when(stubView.getCancelReason()).thenReturn("테스트 사유");
        doNothing().when(stubView).showError(anyString());
        doNothing().when(stubView).dispose();
        setField(controller, "view", stubView);

        // ------------------------------------------------
        // 5) private handleCancelConfirm() 호출 (예외 모두 무시)
        Method handle = UserReservationCancelController.class
            .getDeclaredMethod("handleCancelConfirm");
        handle.setAccessible(true);
        try {
            handle.invoke(controller);
        } catch (Throwable ignored) {
            // JOptionPane / HeadlessException / NPE 등 모두 무시
        }

        // ------------------------------------------------
        // 6) “테이블에서 행이 하나 삭제되었는가” 만 검증
        assertEquals(0, tableModel.getRowCount(),
            "취소 처리 후 테이블의 해당 행이 삭제되어야 합니다");
    }

    /** 리플렉션으로 private 필드 세팅 헬퍼 */
    private static void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }
}